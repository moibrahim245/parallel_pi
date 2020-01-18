/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi_parallel;


import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.LongVbl;
import edu.rit.pj2.LongLoop;
import edu.rit.util.Random;

/**
 *
 * @author Mohamed & Alaa
 */
public class Pi_Parallel extends Task {

    
     // Command line arguments.
    long seed;          
    long N;             // number of points of all the square.
    // Number of points within the unit circle.
    LongVbl count;
    // Main program.

    /**
     * @param args the command line arguments
     */
  

 @Override
    public void main(String[] args)
            throws Exception {
        // Validate command line arguments.
        if (args.length != 2) {
            usage();
        }
        long startTime=System.nanoTime();
        seed = Long.parseLong(args[0]);
        N = Long.parseLong(args[1]);
        // Generate n random points in the unit square, count
        // how many are in the unit circle.
        count = new LongVbl.Sum(0);
        parallelFor(0, N - 1).exec(new LongLoop() {
            Random prng;
            LongVbl thrCount;       // local thread for count.

            @Override
            public void start() {
                prng = new Random(seed + rank());
                thrCount = threadLocal(count);
            }

            @Override
            public void run(long i) {
                double x = prng.nextDouble();
                double y = prng.nextDouble();
                if (x * x + y * y <= 1.0) {
                    ++thrCount.item;
                }
            }
        });
        long finishTime=System.nanoTime();
            // Print results.
        System.out.printf("pi = 4*%d/%d = %.9f%n%n",
                count.item, N, 4.0 * count.item / N);
        System.out.println("dauration : " + ((finishTime-startTime)/1000000) + " ms");
        System.out.println("Number of Processors: " + Runtime.getRuntime().availableProcessors());
    }

    private void usage() {
        throw new IllegalArgumentException();
    }

}

