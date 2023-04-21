package org.sulweb.util.threads;

import java.util.Date;

/**  
 * This is a test class used for benchmarks. You don't want to use this class
 * in your programs. You may like to edit this class sources to play with it and
 * run your own benchmarks. You may want to take a look at this class sources to
 * understand how to use a ThreadPool.
 * @deprecated This class isn't of any use for your program. Please write your own main method instead. 
 */
public class TestThreadPool {

    /** 
   *  The number of times a thread gets called
   */
    private int ncalls;

    /**
   * The sum of the milliseconds between the call of execute and the
   * thread completion for all the threads 
   */
    private long totms;

    private void testExecute() throws Exception {
        RunnableWithParams rwp = new RunnableWithParams() {

            public void run(ThreadPool tp, Object params) {
                ncalls++;
                Date start = (Date) params;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
                Date end = new Date();
                long span = end.getTime() - start.getTime();
                totms += span;
            }
        };
        ThreadPool[] pools = new ThreadPool[2];
        pools[0] = new ThreadPool(rwp);
        pools[1] = new ThreadPool(rwp);
        pools[0].setLimits(100, 100, 0, 100, 0);
        pools[1].setLimits(2, 1000, 1, 3, 1);
        System.out.println("Warning: PASS#1 is almost always biased.");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < pools.length; j++) {
                int runs = 5000;
                ncalls = 0;
                totms = 0;
                timetest(pools[j], runs);
                while (ncalls < runs) Thread.yield();
                System.out.println("PASS#" + (i + 1) + ", pool#" + (j + 1) + " average=" + (totms / ncalls) + "ms");
            }
        }
        for (int i = 0; i < pools.length; i++) pools[i].shutdown();
    }

    private void timetest(ThreadPool tp, int runs) {
        for (int i = 0; i < runs; i++) tp.execute(new Date());
    }

    /**
   * The main method. Don't bother running this until you know what it does. Don't
   * bother understanding what it does until you are a newbie seeking for sample code.
   * Don't bother seeking for sample code until you haven't read all the javadocs.
   * Don't bother reading the javadocs until you haven't run this once.
   * And please, don't loop, you are human after all!
   * @param args Well, you should know what it is. No args required nor optional.
   */
    public static void main(String[] args) {
        try {
            new TestThreadPool().testExecute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }
}
