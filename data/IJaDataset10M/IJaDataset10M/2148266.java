package test.org.rollerjm.graph;

import junit.framework.*;

public class DistanceQueueTest extends TestCase {

    private DistanceQueue queue;

    private static final String A = "A";

    private static final String B = "B";

    private static final String C = "C";

    private static final String D = "D";

    private static final String E = "E";

    public DistanceQueueTest(String s) {
        super(s);
    }

    protected void setUp() {
        queue = new DistanceQueue();
        addData();
    }

    private void addData() {
        queue.insert(B, 5);
        queue.insert(C, 5);
        queue.insert(D, 5);
        queue.insert(E, 7);
    }

    protected void tearDown() {
    }

    public void testClear() {
        queue.clear();
        assertTrue(queue.isEmpty());
        addData();
    }

    public void testDequeueLowestPriorityElement() {
        assertEquals(D, queue.dequeueLowestPriorityElement());
        assertEquals(C, queue.dequeueLowestPriorityElement());
        assertEquals(B, queue.dequeueLowestPriorityElement());
        assertEquals(E, queue.dequeueLowestPriorityElement());
        assertNull(queue.dequeueLowestPriorityElement());
        assertTrue(queue.isEmpty());
    }

    public void testInsert() {
        try {
            queue.insert(null, 1);
            fail("Should raise an Exception");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            queue.insert(A, -1);
            fail("Should raise an Exception");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void testIsEmpty() {
        DistanceQueue q = new DistanceQueue();
        assertTrue(q.isEmpty());
    }
}
