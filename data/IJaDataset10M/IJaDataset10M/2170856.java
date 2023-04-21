package org.matsim.core.utils.collections;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * @author mrieser
 */
public class PseudoRemovePriorityQueueTest extends TestCase {

    private static final Logger log = Logger.getLogger(PseudoRemovePriorityQueueTest.class);

    public void testAdd() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        assertEquals(0, pq.size());
        pq.add(Integer.valueOf(1), 1.0);
        assertEquals(1, pq.size());
        pq.add(Integer.valueOf(2), 2.0);
        assertEquals(2, pq.size());
        pq.add(Integer.valueOf(3), 2.0);
        assertEquals(3, pq.size());
        pq.add(Integer.valueOf(3), 3.0);
        assertEquals(3, pq.size());
        assertEquals(3, iteratorElementCount(pq.iterator()));
    }

    public void testAdd_Null() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        try {
            pq.add(null, 1.0);
            fail("missing NullPointerException.");
        } catch (NullPointerException e) {
            log.info("catched expected exception. ", e);
        }
        assertEquals(0, pq.size());
        assertEquals(0, iteratorElementCount(pq.iterator()));
    }

    public void testPoll() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        assertEquals(3, pq.size());
        assertEquals(Integer.valueOf(3), pq.poll());
        assertEquals(2, pq.size());
        pq.add(Integer.valueOf(1), 1.0);
        pq.add(Integer.valueOf(4), 4.0);
        pq.add(Integer.valueOf(9), 9.0);
        assertEquals(5, pq.size());
        assertEquals(Integer.valueOf(1), pq.poll());
        assertEquals(Integer.valueOf(4), pq.poll());
        assertEquals(Integer.valueOf(5), pq.poll());
        assertEquals(Integer.valueOf(6), pq.poll());
        assertEquals(Integer.valueOf(9), pq.poll());
        assertEquals(0, pq.size());
        assertNull(pq.poll());
    }

    public void testIterator() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        Collection<Integer> coll = getIteratorCollection(pq.iterator());
        assertEquals(3, coll.size());
        assertTrue(coll.contains(Integer.valueOf(5)));
        assertTrue(coll.contains(Integer.valueOf(3)));
        assertTrue(coll.contains(Integer.valueOf(6)));
        assertFalse(coll.contains(Integer.valueOf(4)));
    }

    public void testIterator_ConcurrentModification_add() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        Iterator<Integer> iter = pq.iterator();
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
        pq.add(Integer.valueOf(4), 4.0);
        assertTrue(iter.hasNext());
        try {
            iter.next();
            fail("missing ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            log.info("catched expected exception.", e);
        }
        iter = pq.iterator();
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
    }

    public void testIterator_ConcurrentModification_poll() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        Iterator<Integer> iter = pq.iterator();
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
        pq.poll();
        assertTrue(iter.hasNext());
        try {
            iter.next();
            fail("missing ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            log.info("catched expected exception.", e);
        }
        iter = pq.iterator();
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
    }

    public void testIterator_ConcurrentModification_remove() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        Iterator<Integer> iter = pq.iterator();
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
        assertTrue(pq.remove(Integer.valueOf(5)));
        assertTrue(iter.hasNext());
        try {
            iter.next();
            fail("missing ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            log.info("catched expected exception.", e);
        }
        iter = pq.iterator();
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
        assertFalse(pq.remove(Integer.valueOf(5)));
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
    }

    public void testIterator_RemoveUnsupported() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        Iterator<Integer> iter = pq.iterator();
        assertTrue(iter.hasNext());
        assertNotNull(iter.next());
        try {
            iter.remove();
            fail("missing UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            log.info("catched expected exception.", e);
        }
    }

    public void testRemove() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        Collection<Integer> coll = getIteratorCollection(pq.iterator());
        assertEquals(3, coll.size());
        assertTrue(coll.contains(Integer.valueOf(5)));
        assertTrue(coll.contains(Integer.valueOf(3)));
        assertTrue(coll.contains(Integer.valueOf(6)));
        assertFalse(coll.contains(Integer.valueOf(4)));
        assertTrue(pq.remove(Integer.valueOf(5)));
        assertEquals(2, pq.size());
        coll = getIteratorCollection(pq.iterator());
        assertEquals(2, coll.size());
        assertFalse(coll.contains(Integer.valueOf(5)));
        assertTrue(coll.contains(Integer.valueOf(3)));
        assertTrue(coll.contains(Integer.valueOf(6)));
        assertFalse(pq.remove(Integer.valueOf(5)));
        assertEquals(2, pq.size());
        coll = getIteratorCollection(pq.iterator());
        assertEquals(2, coll.size());
        assertFalse(pq.remove(null));
        assertEquals(2, pq.size());
        coll = getIteratorCollection(pq.iterator());
        assertEquals(2, coll.size());
        assertTrue(coll.contains(Integer.valueOf(3)));
        assertTrue(coll.contains(Integer.valueOf(6)));
        assertEquals(Integer.valueOf(3), pq.poll());
        assertEquals(Integer.valueOf(6), pq.poll());
        assertNull(pq.poll());
    }

    public void testRemoveAndAdd_LowerPriority() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        assertEquals(3, pq.size());
        pq.remove(Integer.valueOf(5));
        assertEquals(2, pq.size());
        pq.add(Integer.valueOf(5), 7.0);
        assertEquals(3, pq.size());
        assertEquals(Integer.valueOf(3), pq.poll());
        assertEquals(Integer.valueOf(6), pq.poll());
        assertEquals(Integer.valueOf(5), pq.poll());
        assertNull(pq.poll());
    }

    public void testRemoveAndAdd_HigherPriority() {
        PseudoRemovePriorityQueue<Integer> pq = new PseudoRemovePriorityQueue<Integer>(10);
        pq.add(Integer.valueOf(5), 5.0);
        pq.add(Integer.valueOf(3), 3.0);
        pq.add(Integer.valueOf(6), 6.0);
        assertEquals(3, pq.size());
        pq.remove(Integer.valueOf(5));
        assertEquals(2, pq.size());
        pq.add(Integer.valueOf(5), 2.5);
        assertEquals(3, pq.size());
        assertEquals(Integer.valueOf(5), pq.poll());
        assertEquals(Integer.valueOf(3), pq.poll());
        assertEquals(Integer.valueOf(6), pq.poll());
        assertNull(pq.poll());
    }

    private int iteratorElementCount(final Iterator<?> iterator) {
        int cnt = 0;
        while (iterator.hasNext()) {
            cnt++;
            iterator.next();
        }
        return cnt;
    }

    private <T> Collection<T> getIteratorCollection(final Iterator<T> iterator) {
        LinkedList<T> list = new LinkedList<T>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
