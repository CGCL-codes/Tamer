package test.tuwien.auto.calimero.buffer.cache;

import junit.framework.TestCase;
import tuwien.auto.calimero.buffer.cache.CacheObject;
import tuwien.auto.calimero.buffer.cache.ExpiringCache;

/**
 * @author B. Malinowsky
 */
public class ExpiringCacheTest extends TestCase {

    private class ExpCacheImpl extends ExpiringCache {

        boolean notified;

        boolean remove;

        int count;

        /**
		 * @param timeToExpire
		 */
        public ExpCacheImpl(int timeToExpire) {
            super(timeToExpire);
            sweepInterval = 1;
        }

        public void clear() {
        }

        public CacheObject get(Object key) {
            return (CacheObject) map.get(key);
        }

        public void put(CacheObject obj) {
            map.put(obj.getKey(), obj);
        }

        public void remove(Object key) {
            map.remove(key);
        }

        void myStartSweeper() {
            startSweeper();
        }

        void myStopSweeper() {
            stopSweeper();
        }

        public void removeExpired() {
            remove = true;
            super.removeExpired();
            synchronized (this) {
                notifyAll();
            }
        }

        protected void notifyRemoved(CacheObject obj) {
            notified = true;
            count++;
            synchronized (this) {
                notifyAll();
            }
        }

        public Statistic statistic() {
            return null;
        }
    }

    private ExpCacheImpl c;

    /**
	 * @param name name of test case
	 */
    public ExpiringCacheTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        c = new ExpCacheImpl(1);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        c.myStopSweeper();
    }

    /**
	 * Test method for
	 * {@link tuwien.auto.calimero.buffer.cache.ExpiringCache#ExpiringCache(int)}.
	 */
    public void testExpiringCache() {
        c.myStartSweeper();
        try {
            synchronized (c) {
                c.wait(1300);
            }
        } catch (final InterruptedException e) {
        }
        assertTrue(c.remove);
        ExpCacheImpl c2 = new ExpCacheImpl(0);
        try {
            synchronized (c2) {
                c2.wait(1300);
            }
        } catch (final InterruptedException e) {
            fail("no remove should be called");
        }
        assertFalse(c2.remove);
        c2 = new ExpCacheImpl(-1);
        try {
            synchronized (c2) {
                c2.wait(1300);
            }
        } catch (final InterruptedException e) {
            fail("notifyRemoved shouldnt be called");
        }
        assertFalse(c2.remove);
    }

    /**
	 * Test method for
	 * {@link tuwien.auto.calimero.buffer.cache.ExpiringCache#removeExpired()}.
	 */
    public void testRemoveExpired() {
        c.myStartSweeper();
        c.put(new CacheObject("key1", "value"));
        c.put(new CacheObject("key2", "value"));
        c.put(new CacheObject("key3", "value"));
        try {
            synchronized (c) {
                c.wait(500);
            }
        } catch (final InterruptedException e) {
            fail("remove shouldnt be called yet");
        }
        assertFalse(c.notified);
        assertFalse(c.remove);
        assertNotNull(c.get("key1"));
        assertNotNull(c.get("key2"));
        assertNotNull(c.get("key3"));
        c.put(new CacheObject("key4", "value"));
        try {
            synchronized (c) {
                c.wait(300);
            }
        } catch (final InterruptedException e) {
            fail("remove shouldnt be called yet");
        }
        assertFalse(c.notified);
        assertFalse(c.remove);
        assertNotNull(c.get("key1"));
        assertNotNull(c.get("key2"));
        assertNotNull(c.get("key3"));
        c.remove("key1");
        c.put(new CacheObject("key1", "value"));
        try {
            synchronized (c) {
                c.wait(400);
            }
        } catch (final InterruptedException e) {
        }
        assertTrue(c.notified);
        assertTrue(c.remove);
        assertEquals(2, c.count);
        assertNull(c.get("key"));
        assertNotNull(c.get("key1"));
        assertNotNull(c.get("key4"));
        assertNull(c.get("key2"));
        assertNull(c.get("key3"));
    }

    /**
	 * Test method for
	 * tuwien.auto.calimero.cache.ExpiringCache#notifyRemoved
	 * (tuwien.auto.calimero.cache.CacheObject).
	 */
    public void testNotifyRemoved() {
        c.myStartSweeper();
        c.put(new CacheObject("key", "value"));
        try {
            synchronized (c) {
                c.wait(600);
            }
        } catch (final InterruptedException e) {
            fail("should not happen");
        }
        assertFalse(c.notified);
        try {
            synchronized (c) {
                c.wait(600);
            }
        } catch (final InterruptedException e) {
            fail("should not happen");
        }
        assertTrue(c.notified);
    }

    /**
	 * Test method for tuwien.auto.calimero.cache.ExpiringCache#startSweeper().
	 */
    public void testStartSweeper() {
        c.myStartSweeper();
        c.myStopSweeper();
        c.myStartSweeper();
        c.myStartSweeper();
        try {
            synchronized (c) {
                c.wait(1200);
            }
        } catch (final InterruptedException e) {
            fail("no remove was called while cache sweeping running");
        }
        assertTrue("no remove was called while cache sweeping running", c.remove);
    }

    /**
	 * Test method for tuwien.auto.calimero.cache.ExpiringCache#stopSweeper().
	 */
    public void testStopSweeper() {
        c.myStartSweeper();
        c.myStartSweeper();
        c.myStartSweeper();
        c.myStopSweeper();
        try {
            synchronized (c) {
                c.wait(1200);
            }
        } catch (final InterruptedException e) {
            fail("remove was called without cache sweeping");
        }
        assertFalse(c.remove);
        assertFalse(c.notified);
        assertEquals(0, c.count);
    }
}
