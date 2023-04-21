package com.bluemarsh.jswat.core.watch;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the WatchEventMulticaster class.
 */
public class WatchEventMulticasterTest {

    @Test
    public void test_WatchEventMulticaster() {
        WatchListener sl = WatchEventMulticaster.add(null, null);
        assertEquals(sl, null);
        sl = WatchEventMulticaster.remove(null, null);
        assertEquals(sl, null);
        TestListener l1 = new TestListener();
        sl = WatchEventMulticaster.add(sl, l1);
        TestListener l2 = new TestListener();
        sl = WatchEventMulticaster.add(sl, l2);
        assertEquals(0, l1.added);
        assertEquals(0, l1.removed);
        assertEquals(0, l2.added);
        assertEquals(0, l2.removed);
        Watch watch = new DummyWatch();
        WatchEvent sevt = new WatchEvent(watch, WatchEventType.ADDED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.added);
        assertEquals(0, l1.removed);
        assertEquals(1, l2.added);
        assertEquals(0, l2.removed);
        sevt = new WatchEvent(watch, WatchEventType.REMOVED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.added);
        assertEquals(1, l1.removed);
        assertEquals(1, l2.added);
        assertEquals(1, l2.removed);
        sl = WatchEventMulticaster.remove(sl, l1);
        sevt = new WatchEvent(watch, WatchEventType.ADDED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.added);
        assertEquals(1, l1.removed);
        assertEquals(2, l2.added);
        assertEquals(1, l2.removed);
    }

    private static class TestListener implements WatchListener {

        public int added;

        public int removed;

        @Override
        public void watchAdded(WatchEvent event) {
            added++;
        }

        @Override
        public void watchRemoved(WatchEvent event) {
            removed++;
        }
    }
}
