package samples.vats;

import java.util.concurrent.Semaphore;
import net.sf.asyncobjects.vats.SingleThreadRunner;
import net.sf.asyncobjects.vats.Vat;

/**
 * This class demonstrate how to use single thread vat. It creates vat
 * explicitly and starts it in the new thread.
 * 
 * @author const
 */
public class SingleThreadRunnerSample2 {

    /**
   * Application entry point
   * 
   * @param args
   *          application arguments
   */
    public static void main(String[] args) {
        final SingleThreadRunner runner = new SingleThreadRunner("other runner", true);
        Vat vat = runner.newVat("my vat");
        final Semaphore s = new Semaphore(0);
        Runnable r = new Runnable() {

            public void run() {
                System.out.println("Inside the vat: " + Thread.currentThread().getName());
                runner.stop();
                s.release();
            }
        };
        vat.enqueue(r);
        System.out.println("Before runner vat: " + Thread.currentThread().getName());
        runner.startInNewThread();
        try {
            s.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Runner finished: " + Thread.currentThread().getName());
    }
}
