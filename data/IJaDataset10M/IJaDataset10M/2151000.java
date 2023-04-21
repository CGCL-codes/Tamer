package rkr_gst;

import java.util.Iterator;
import junit.framework.*;

/**
 *
 * @author Ondrej Guth
 */
public class ListOfQueuesOfMaxMatchesTest extends TestCase {

    Match match4, match5, match6, match7, match8;

    LstOfQueuesOfMaxMatches listEmpty, listW5, listW5and7;

    public ListOfQueuesOfMaxMatchesTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        match4 = new Match(29, 31, 4);
        match5 = new Match(3, 5, 5);
        match6 = new Match(13, 17, 6);
        match7 = new Match(7, 11, 7);
        match8 = new Match(19, 23, 8);
        listEmpty = new LstOfQueuesOfMaxMatches();
        listW5 = new LstOfQueuesOfMaxMatches();
        listW5.add(new QueueOfMaximalMatches(match5));
        listW5and7 = new LstOfQueuesOfMaxMatches();
        listW5and7.add(new QueueOfMaximalMatches(match7));
        listW5and7.add(new QueueOfMaximalMatches(match5));
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ListOfQueuesOfMaxMatchesTest.class);
        return suite;
    }

    /**
	 * Test of recordMatch method, of class rkr_gst.LstOfQueuesOfMaxMatches.
	 */
    public void testRecordMatch() {
        System.out.println("recordMatch");
        LstOfQueuesOfMaxMatches instance = new LstOfQueuesOfMaxMatches();
        instance.recordMatch(match5.p, match5.t, match5.L);
        assertEquals(1, instance.size());
        QueueOfMaximalMatches queue = instance.getFirst();
        assertEquals(1, queue.size());
        Match match = queue.getFirst();
        assertEquals(match5.L, match.L);
        assertEquals(match5.p, match.p);
        assertEquals(match5.t, match.t);
        instance.recordMatch(match5.p, match5.t, match5.L);
        assertEquals(1, instance.size());
        queue = instance.getFirst();
        assertEquals(2, queue.size());
        match = queue.getLast();
        assertEquals(match5.L, match.L);
        assertEquals(match5.p, match.p);
        assertEquals(match5.t, match.t);
        instance.recordMatch(match7.p, match7.t, match7.L);
        assertEquals(2, instance.size());
        queue = instance.getFirst();
        assertEquals(1, queue.size());
        match = queue.getLast();
        assertEquals(match7.L, match.L);
        assertEquals(match7.p, match.p);
        assertEquals(match7.t, match.t);
        queue = instance.getLast();
        assertEquals(2, queue.size());
        match = queue.getLast();
        assertEquals(match5.L, match.L);
        assertEquals(match5.p, match.p);
        assertEquals(match5.t, match.t);
        instance.recordMatch(match5.p, match5.t, match5.L);
        assertEquals(2, instance.size());
        queue = instance.getLast();
        assertEquals(3, queue.size());
        match = queue.getLast();
        assertEquals(match5.L, match.L);
        assertEquals(match5.p, match.p);
        assertEquals(match5.t, match.t);
        instance.recordMatch(match6.p, match6.t, match6.L);
        assertEquals(3, instance.size());
        queue = instance.get(1);
        assertEquals(1, queue.size());
        match = queue.getLast();
        assertEquals(match6.L, match.L);
        assertEquals(match6.p, match.p);
        assertEquals(match6.t, match.t);
        instance.recordMatch(match8.p, match8.t, match8.L);
        assertEquals(4, instance.size());
        queue = instance.getFirst();
        assertEquals(1, queue.size());
        match = queue.getLast();
        assertEquals(match8.L, match.L);
        assertEquals(match8.p, match.p);
        assertEquals(match8.t, match.t);
        instance.recordMatch(match4.p, match4.t, match4.L);
        assertEquals(5, instance.size());
        queue = instance.getLast();
        assertEquals(1, queue.size());
        match = queue.getLast();
        assertEquals(match4.L, match.L);
        assertEquals(match4.p, match.p);
        assertEquals(match4.t, match.t);
    }

    /**
	 * Test of lengthOfLongestMatch method, of class rkr_gst.LstOfQueuesOfMaxMatches.
	 */
    public void testLengthOfLongestMatch() {
        System.out.println("lengthOfLongestMatch");
        assertEquals(0, listEmpty.lengthOfLongestMatch());
        assertEquals(5, listW5.lengthOfLongestMatch());
        assertEquals(7, listW5and7.lengthOfLongestMatch());
    }

    /**
	 ** Tests order of queues
	 */
    public void testIterating() {
        System.out.println("test iterate queues - order");
        Iterator<QueueOfMaximalMatches> it = listW5and7.iterator();
        int prevL, currL = Integer.MAX_VALUE;
        while (it.hasNext()) {
            prevL = currL;
            currL = it.next().getLengthOfMaxMatch();
            assertTrue(currL < prevL);
        }
    }

    public void testGetItBfQ() {
        System.out.println("getItBfQ");
        Iterator<QueueOfMaximalMatches> it = listEmpty.getItBfQ(0);
        assertFalse(it.hasNext());
        it = listEmpty.getItBfQ(-10);
        assertFalse(it.hasNext());
        it = listEmpty.getItBfQ(115);
        assertFalse(it.hasNext());
        it = listW5.getItBfQ(4);
        assertFalse(it.hasNext());
        it = listW5.getItBfQ(5);
        assertEquals(new QueueOfMaximalMatches(match5), it.next());
        assertFalse(it.hasNext());
        it = listW5.getItBfQ(6);
        assertEquals(new QueueOfMaximalMatches(match5), it.next());
        assertFalse(it.hasNext());
        it = listW5and7.getItBfQ(4);
        assertFalse(it.hasNext());
        it = listW5and7.getItBfQ(5);
        assertEquals(new QueueOfMaximalMatches(match5), it.next());
        assertFalse(it.hasNext());
        it = listW5and7.getItBfQ(6);
        assertEquals(new QueueOfMaximalMatches(match5), it.next());
        assertFalse(it.hasNext());
        it = listW5and7.getItBfQ(7);
        assertEquals(new QueueOfMaximalMatches(match7), it.next());
        assertEquals(new QueueOfMaximalMatches(match5), it.next());
        assertFalse(it.hasNext());
        it = listW5and7.getItBfQ(8);
        assertEquals(new QueueOfMaximalMatches(match7), it.next());
        assertEquals(new QueueOfMaximalMatches(match5), it.next());
        assertFalse(it.hasNext());
    }

    public void testSetLastUsedQueue() {
        System.out.println("setLastUsedQueue");
        listEmpty.lastUsedQueue = null;
        listEmpty.setLastUsedQueue(0);
        assertNull(listEmpty.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5.setLastUsedQueue(0);
        assertNull(listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5.setLastUsedQueue(7);
        assertNull(listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5.setLastUsedQueue(8);
        assertNull(listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5.setLastUsedQueue(4);
        assertNull(listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5.setLastUsedQueue(5);
        assertEquals(new QueueOfMaximalMatches(match5), listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5.setLastUsedQueue(0);
        assertNull(listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5.setLastUsedQueue(8);
        assertNull(listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5.setLastUsedQueue(4);
        assertNull(listW5.lastUsedQueue);
        listW5.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5.setLastUsedQueue(5);
        assertEquals(new QueueOfMaximalMatches(match5), listW5.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5and7.setLastUsedQueue(0);
        assertNull(listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5and7.setLastUsedQueue(7);
        assertEquals(new QueueOfMaximalMatches(match7), listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5and7.setLastUsedQueue(8);
        assertNull(listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5and7.setLastUsedQueue(4);
        assertNull(listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match5);
        listW5and7.setLastUsedQueue(5);
        assertEquals(new QueueOfMaximalMatches(match5), listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5and7.setLastUsedQueue(0);
        assertNull(listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5and7.setLastUsedQueue(7);
        assertEquals(new QueueOfMaximalMatches(match7), listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5and7.setLastUsedQueue(8);
        assertNull(listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5and7.setLastUsedQueue(4);
        assertNull(listW5and7.lastUsedQueue);
        listW5and7.lastUsedQueue = new QueueOfMaximalMatches(match7);
        listW5and7.setLastUsedQueue(5);
        assertEquals(new QueueOfMaximalMatches(match5), listW5and7.lastUsedQueue);
    }

    public void testGetIndexWhereToPlaceQueue() {
        System.out.println("getIndexWhereToPlaceQueue");
        assertEquals(0, listEmpty.getIndexWhereToPlaceQueue(0));
        assertEquals(0, listEmpty.getIndexWhereToPlaceQueue(5));
        assertEquals(0, listEmpty.getIndexWhereToPlaceQueue(6));
        assertEquals(0, listEmpty.getIndexWhereToPlaceQueue(7));
        assertEquals(0, listEmpty.getIndexWhereToPlaceQueue(8));
        assertEquals(listW5.size(), listW5.getIndexWhereToPlaceQueue(0));
        assertEquals(0, listW5.getIndexWhereToPlaceQueue(5));
        assertEquals(0, listW5.getIndexWhereToPlaceQueue(6));
        assertEquals(0, listW5.getIndexWhereToPlaceQueue(7));
        assertEquals(0, listW5.getIndexWhereToPlaceQueue(8));
        assertEquals(listW5and7.size(), listW5and7.getIndexWhereToPlaceQueue(0));
        assertEquals(1, listW5and7.getIndexWhereToPlaceQueue(5));
        assertEquals(1, listW5and7.getIndexWhereToPlaceQueue(6));
        assertEquals(0, listW5and7.getIndexWhereToPlaceQueue(7));
        assertEquals(0, listW5and7.getIndexWhereToPlaceQueue(8));
    }
}
