package thread.concurrencyInPractice.barriers.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Nearly every server application uses some form of
 * caching. Reusing the results of a previous computation
 * can reduce latency and increase throughput, at the cost
 * of some additional memory usage.
 * 
 * @author Sergiy Doroshenko webserg@gmail.com Feb 10, 2009
 *         4:41:41 PM
 */
public class Memoizer3<A, V> {

    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();

    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    /**
     * 
     * Memoizer3 redefines the backing Map for the value
     * cache as a ConcurrentHashMap<A,Future<V>> instead of
     * a ConcurrentHashMap<A,V>. Memoizer3 first checks to
     * see if the appropriate calculation has been started
     * (as opposed to finished, as in Memoizer2). If not, it
     * creates a FutureTask, registers it in the Map, and
     * starts the computation; otherwise it waits for the
     * result of the existing computation. The result might
     * be available immediately or might be in the process
     * of being computedbut this is transparent to the
     * caller of Future.get.
     * 
     * The Memoizer3 implementation is almost perfect: it
     * exhibits very good concurrency (mostly derived from
     * the excellent concurrency of ConcurrentHashMap), the
     * result is returned efficiently if it is already
     * known, and if the computation is in progress by
     * another thread, newly arriving threads wait patiently
     * for the result.
     * 
     * It has only one defectthere is still a small window
     * of vulnerability in which two threads might compute
     * the same value. This window is far smaller than in
     * Memoizer2, but because the if block in compute is
     * still a nonatomic check-thenact sequence, it is
     * possible for two threads to call compute with the
     * same value at roughly the same time, both see that
     * the cache does not contain the desired value, and
     * both start the computation
     * 
     * @param arg
     * @return
     * @throws InterruptedException
     */
    public V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = new Callable<V>() {

                public V call() throws InterruptedException {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run();
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
