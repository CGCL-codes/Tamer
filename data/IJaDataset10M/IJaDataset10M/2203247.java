package dovetaildb.util;

import java.util.HashMap;
import junit.framework.TestCase;
import org.junit.Test;

public class PriorityQueueTest extends TestCase {

    @Override
    public void setUp() {
    }

    class SimplePQ extends PriorityQueue {

        SimplePQ(int maxSize) {
            initialize(maxSize);
        }

        @Override
        public int compare(Object a, Object b) {
            return ((Comparable) a).compareTo(b);
        }
    }

    @Test
    public void testFixedPQ() {
        SimplePQ pq = new SimplePQ(2);
        pq.insert(5);
        assertEquals(1, pq.size());
        assertEquals(5, pq.top());
        pq.insert(3);
        assertEquals(2, pq.size());
        assertEquals(3, pq.top());
        pq.insert(8);
        assertEquals(2, pq.size());
        assertEquals(5, pq.top());
        pq.insert(4);
        assertEquals(2, pq.size());
        assertEquals(5, pq.top());
        assertEquals(5, pq.pop());
        assertEquals(8, pq.pop());
        assertEquals(0, pq.size());
    }
}
