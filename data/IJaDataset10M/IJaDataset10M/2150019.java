package mobi.ilabs.cache.tests;

import java.util.Map;
import java.util.TreeMap;
import junit.framework.TestCase;
import mobi.ilabs.cache.Cache;
import mobi.ilabs.cache.CacheEntry;
import org.junit.Before;
import org.restlet.data.Request;

public class TestCache extends TestCase {

    public class BackingStoreMock {

        private Map<String, Integer> map = new TreeMap<String, Integer>();

        public final Integer get(final String id) {
            return map.get(id);
        }

        public BackingStoreMock() {
            super();
            map.put("one", new Integer(1));
            map.put("three", new Integer(3));
        }
    }

    private BackingStoreMock myBacking;

    private Cache<Integer> myCache;

    /**
     * A simple single-element cache, that holds a single element in cache.
     * test basic cache behavior on this one.
     *
     * @param <T> The type of the element being cached.
     */
    public abstract class SingleElementCache<T> extends Cache<T> {

        private CacheEntry<T> myItem;

        @Override
        protected CacheEntry<T> getItemToEvict() {
            return myItem;
        }

        @Override
        public CacheEntry<T> put(String key, T ob) {
            myItem = super.put(key, ob);
            return myItem;
        }

        @Override
        protected boolean evictionIsNecessary(T t) {
            return (myItem != null);
        }
    }

    private Request r;

    @Before
    public void setUp() throws Exception {
        myBacking = new BackingStoreMock();
        myCache = new SingleElementCache<Integer>() {

            @Override
            protected Integer getFromBacking(final Request r, final String key) {
                Integer result = myBacking.get(key);
                return result;
            }

            @Override
            protected boolean evictionIsNecessary(Integer t) {
                return noOfCachedItems() > 0;
            }

            @Override
            public CacheEntry<Integer> put(String key, Integer ob) {
                return super.put(key, ob);
            }
        };
        r = new Request();
    }

    public void testBasicCacheSetup() {
        assertTrue(!myCache.containsKey("two"));
        assertTrue(!myCache.containsKey("one"));
        assertTrue(myCache.get(r, "two") == null);
        assertTrue(myCache.get(r, "one") != null);
        assertTrue(!myCache.containsKey("two"));
        assertTrue(myCache.containsKey("one"));
    }

    public void testCacheFillup() {
        assertTrue(!myCache.containsKey("three"));
        assertTrue(!myCache.containsKey("two"));
        assertTrue(!myCache.containsKey("one"));
        assertTrue(myCache.get(r, "two") == null);
        assertTrue(!myCache.containsKey("one"));
        assertTrue(!myCache.containsKey("two"));
        assertTrue(!myCache.containsKey("three"));
        assertTrue(myCache.get(r, "one") != null);
        assertTrue(myCache.containsKey("one"));
        assertTrue(!myCache.containsKey("two"));
        assertTrue(!myCache.containsKey("three"));
        assertTrue(myCache.get(r, "two") == null);
        assertTrue(myCache.containsKey("one"));
        assertTrue(!myCache.containsKey("two"));
        assertTrue(!myCache.containsKey("three"));
        assertTrue(myCache.get(r, "three") != null);
        assertTrue(!myCache.containsKey("one"));
        assertTrue(!myCache.containsKey("two"));
        assertTrue(myCache.containsKey("three"));
    }
}
