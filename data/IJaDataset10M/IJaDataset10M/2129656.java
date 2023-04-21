package org.archive.util;

import java.util.NoSuchElementException;

/**
 * JUnit test suite for Queue.  It's an abstract class which is implemented by
 * each queue implementation
 *
 * @author <a href="mailto:me@jamesc.net">James Casey</a>
 * @version $Id: QueueTestBase.java 1388 2004-02-18 00:11:36Z stack-sf $
 */
public abstract class QueueTestBase extends TmpDirTestCase {

    /**
     * Create a new PaddingStringBufferTest object
     *
     * @param testName the name of the test
     */
    public QueueTestBase(final String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        queue = makeQueue();
    }

    public void tearDown() {
        if (queue != null) {
            queue.release();
        }
    }

    /**
     * The abstract subclass constructor.  The subclass should create an
     * instance of the object it wishes to have tested
     *
     * @return the Queue object to be tested
     */
    protected abstract Queue makeQueue();

    /** test that queue puts things on, and they stay there :) */
    public void testQueue() {
        assertEquals("no items in new queue", 0, queue.length());
        assertTrue("queue is empty", queue.isEmpty());
        queue.enqueue("foo");
        assertEquals("now one item in queue", 1, queue.length());
        assertFalse("queue not empty", queue.isEmpty());
    }

    /** test that dequeue works */
    public void testDequeue() {
        assertEquals("no items in new queue", 0, queue.length());
        assertTrue("queue is empty", queue.isEmpty());
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");
        assertEquals("now three items in queue", 3, queue.length());
        assertEquals("foo dequeued", "foo", queue.dequeue());
        assertEquals("bar dequeued", "bar", queue.dequeue());
        assertEquals("baz dequeued", "baz", queue.dequeue());
        assertEquals("no items in new queue", 0, queue.length());
        assertTrue("queue is empty", queue.isEmpty());
    }

    /** check what happens we dequeue on empty */
    public void testDequeueEmptyQueue() {
        assertTrue("queue is empty", queue.isEmpty());
        try {
            queue.dequeue();
        } catch (NoSuchElementException e) {
            return;
        }
        fail("Expected a NoSuchElementException on dequeue of empty queue");
    }

    /** the queue object to be tested */
    private Queue queue;
}
