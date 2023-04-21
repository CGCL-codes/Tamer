package org.t2framework.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;
import org.t2framework.commons.cache.Cache;
import org.t2framework.commons.cache.CacheFactory;
import org.t2framework.commons.cache.CacheType;
import junit.framework.TestCase;

public class LazyLoadingReferenceTest extends TestCase {

    public void test_loadSimple() throws Exception {
        LazyLoadingReference<String> target = new LazyLoadingReference<String>(new LazyLoadingReference.Factory<String>() {

            public String create() {
                return "hoge";
            }
        });
        assertEquals("hoge", target.get());
    }

    public void test_loadSimple2() throws Exception {
        LazyLoadingReference<Cache<String, String>> t2 = new LazyLoadingReference<Cache<String, String>>(new LazyLoadingReference.Factory<Cache<String, String>>() {

            @Override
            public Cache<String, String> create() throws CancellationException {
                Cache<String, String> c = CacheFactory.createCache(CacheType.DEFAULT);
                c.put("aaa", "bbb");
                return c;
            }
        });
        Cache<String, String> cache = t2.get();
        assertNotNull(cache);
        assertNotNull(cache.get("aaa"));
    }

    public void test_exception() throws Exception {
        final Exception e = new Exception();
        LazyLoadingReference<String> target = new LazyLoadingReference<String>(new LazyLoadingReference.Factory<String>() {

            public String create() throws Exception {
                throw e;
            }
        });
        try {
            target.get();
            fail();
        } catch (IllegalStateException t) {
            assertNotNull(t.getCause());
            assertTrue(t.getCause().getClass() == Exception.class);
            assertEquals(e, t.getCause());
        }
    }

    public void test_loadHeavy() throws Exception {
        final LazyLoadingReference<String> target = new LazyLoadingReference<String>(new LazyLoadingReference.Factory<String>() {

            public String create() {
                return "hoge";
            }
        });
        final Random random = new Random(System.currentTimeMillis());
        List<Thread> list = new ArrayList<Thread>();
        for (int i = 0; i < 20; i++) {
            Runnable r = new Runnable() {

                public void run() {
                    try {
                        long l = random.nextLong() % 100;
                        Thread.sleep(l < 0 ? l * -1 : l);
                        assertEquals("hoge", target.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t = new Thread(r);
            list.add(t);
            t.start();
        }
        for (Thread t : list) {
            t.join();
        }
    }
}
