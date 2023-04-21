package be.cm.comps.cache;

import static junit.framework.Assert.fail;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import junit.framework.Assert;
import org.junit.Test;

/**
 * A test to ensure that it is not possible to create 2 different caches with
 * the same name
 * 
 * 
 * @author 7515005 Ivan Belis
 * 
 */
public class DuplicateCacheTestIT {

    private static CacheManager cacheManager = Caching.getCacheManager();

    private static final long MAX_TIMEOUT = 300;

    private Future<Cache<String, String>>[] futures;

    @Test(expected = CacheException.class)
    public void createTwoCachesWithSameName() throws Exception {
        runMultiThreads(2);
        waitForTermination();
        Cache<String, String> cache1 = (Cache<String, String>) futures[0].get();
        Cache<String, String> cache2 = (Cache<String, String>) futures[1].get();
        Assert.assertSame(cache1, cache2);
    }

    private Cache<String, String> createCache() {
        Cache<String, String> cache1 = cacheManager.<String, String>createCacheBuilder("local-cache1").build();
        return cache1;
    }

    @SuppressWarnings("unchecked")
    private void runMultiThreads(int numThreads) {
        ExecutorService group = new ThreadPoolExecutor(numThreads, numThreads, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {

            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
        futures = new Future[numThreads];
        for (int i = 0; i < numThreads; i++) {
            futures[i] = group.submit(new Callable<Cache<String, String>>() {

                public Cache<String, String> call() {
                    return createCache();
                }
            });
        }
    }

    private void waitForTermination() {
        try {
            for (Future<?> f : futures) try {
                f.get(MAX_TIMEOUT, TimeUnit.SECONDS);
            } catch (TimeoutException te) {
                fail("Failed " + te);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof CacheException) {
                    throw (CacheException) e.getCause();
                }
                fail("Failed " + e.getCause());
            }
        } catch (InterruptedException e) {
        }
    }
}
