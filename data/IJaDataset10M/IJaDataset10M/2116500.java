package samples.vats;

import java.util.concurrent.Semaphore;
import net.sf.asyncobjects.vats.ExecutorRunner;
import net.sf.asyncobjects.vats.Vat;

/**
 * This is an example that demonstrate how to use thread pool vat group.
 * 
 * @author const
 */
public class ExecutorRunnerSample {

    /**
   * Application entry point
   * 
   * @param args
   *          program arugments
   */
    public static void main(String[] args) {
        final ExecutorRunner runner = ExecutorRunner.newFixedThreadPoolRunner(2, "pool runner thread ", true);
        final Semaphore s = new Semaphore(0);
        Runnable r = new Runnable() {

            public void run() {
                System.out.println("Inside the vat '" + Vat.current().getName() + "' in the thread '" + Thread.currentThread().getName() + "'");
                s.release();
            }
        };
        System.out.println("Before starting vats: " + Thread.currentThread().getName());
        new Vat(runner, "vat 1").enqueue(r);
        new Vat(runner, "vat 2").enqueue(r);
        try {
            s.acquire(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Vats finished: " + Thread.currentThread().getName());
    }
}
