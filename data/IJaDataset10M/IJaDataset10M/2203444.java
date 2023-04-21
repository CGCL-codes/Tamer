package org.springframework.richclient.util;

import java.util.Iterator;
import junit.framework.TestCase;

/**
 * Test cases for {@link EventListenerList}
 * 
 * @author Oliver Hutchison
 */
public class EventListenerListTests extends TestCase {

    private EventListenerList llh;

    private TestListener l1;

    private TestListener l2;

    private TestListener l3;

    public void setUp() {
        llh = new EventListenerList(TestListener.class);
        l1 = new TestListener();
        l2 = new TestListener();
        l3 = new TestListener();
    }

    public void testConstructor() {
        try {
            new EventListenerList(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testAddRemove() {
        assertHasListeners(new TestListener[] {});
        llh.add(l1);
        assertHasListeners(new TestListener[] { l1 });
        llh.add(l2);
        assertHasListeners(new TestListener[] { l1, l2 });
        llh.remove(l3);
        assertHasListeners(new TestListener[] { l1, l2 });
        llh.add(l2);
        assertHasListeners(new TestListener[] { l1, l2 });
        llh.add(l3);
        assertHasListeners(new TestListener[] { l1, l2, l3 });
        llh.remove(l1);
        assertHasListeners(new TestListener[] { l2, l3 });
        llh.remove(l2);
        assertHasListeners(new TestListener[] { l3 });
        llh.remove(l3);
        assertHasListeners(new TestListener[] {});
    }

    public void testAddRemoveInvalidType() {
        try {
            llh.add(new Object());
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            llh.remove(new Object());
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testIterator() {
        Iterator i = llh.iterator();
        assertTrue(!i.hasNext());
        try {
            i.remove();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        try {
            i.next();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        llh.add(l3);
        llh.add(l1);
        i = llh.iterator();
        assertTrue(i.hasNext());
        try {
            i.remove();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        assertEquals(i.next(), l3);
        assertTrue(i.hasNext());
        assertEquals(i.next(), l1);
        assertTrue(!i.hasNext());
        try {
            i.next();
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public void testFire() {
        llh.fire("event");
        llh.fire("someOtherEvent");
        llh.add(l1);
        try {
            llh.fire("someOtherEvent");
            fail();
        } catch (IllegalArgumentException e) {
        }
        llh.fire("event");
        assertEquals(l1.e0ArgCount, 1);
        llh.fire("event", new Object());
        assertEquals(l1.e1ArgCount, 1);
        llh.fire("event", new Object[] { new Object(), new Object() });
        assertEquals(l1.e2ArgCount, 1);
    }

    public void assertHasListeners(final TestListener[] listeners) {
        assertEquals(llh.getListenerCount(), listeners.length);
        if (listeners.length > 0) {
            assertTrue(llh.hasListeners());
        } else {
            assertTrue(!llh.hasListeners());
        }
        for (Iterator i = llh.iterator(); i.hasNext(); ) {
            TestListener listener = (TestListener) i.next();
            int j = 0;
            for (; j < listeners.length; j++) {
                if (listeners[j] == listener) {
                    listeners[j] = null;
                    break;
                }
            }
            if (j == listeners.length) {
                fail("listener [" + listener + "] not in ListenerListHelper");
            }
        }
    }

    public static class TestListener {

        public int e0ArgCount = 0;

        public int e1ArgCount = 0;

        public int e2ArgCount = 0;

        public void event() {
            e0ArgCount++;
        }

        public void event(Object p1) {
            e1ArgCount++;
        }

        public void event(Object p1, Object p2) {
            e2ArgCount++;
        }
    }
}
