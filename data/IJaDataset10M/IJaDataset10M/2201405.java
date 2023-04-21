package thread.concurrencyInPractice.cancellation;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BrokenPrimeProducer can be easily fixed (and simplified) by using
 * interruption instead of a boolean flag to request cancellation,
 * 
 * There are two points in each loop iteration where interruption may
 * be detected: in the blocking put call, and by explicitly polling
 * the interrupted status in the loop header. The explicit test is not
 * strictly necessary here because of the blocking put call, but it
 * makes PrimeProducer more responsive to interruption because it
 * checks for interruption before starting the lengthy task of
 * searching for a prime, rather than after.
 * 
 * @author Sergiy Doroshenko webserg@gmail.com Feb 24, 2009 12:35:09
 *         PM
 */
public class InteruptedPrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    InteruptedPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()) queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException consumed) {
        }
    }

    public void cancel() {
        System.out.println("cancel");
        interrupt();
    }

    static void consumePrimes() throws InterruptedException {
        BlockingQueue<BigInteger> primes = new LinkedBlockingQueue<BigInteger>(11);
        InteruptedPrimeProducer producer = new InteruptedPrimeProducer(primes);
        producer.start();
        try {
            for (int i = 0; i < 12; i++) {
                System.out.println(primes.take());
                SECONDS.sleep(1);
            }
        } finally {
            producer.cancel();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        consumePrimes();
    }
}
