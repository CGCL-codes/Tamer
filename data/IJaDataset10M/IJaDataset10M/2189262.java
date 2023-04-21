package org.vizzini.util;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Provides unit tests for the <code>Queue</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class QueueTest extends TestCase {

    /** First integer. */
    private static final Integer INT0 = 0;

    /** Second integer. */
    private static final Integer INT1 = 1;

    /** Third integer. */
    private static final Integer INT2 = 2;

    /** Fourth integer. */
    private static final Integer INT3 = 3;

    /** Fifth integer. */
    private static final Integer INT4 = 4;

    /** First queue. */
    private Queue<Integer> _queue0;

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.1
     */
    public static void main(String[] args) {
        TestRunner.run(QueueTest.class);
    }

    /**
     * Test the <code>add()</code> method.
     *
     * @since  v0.1
     */
    public void testAdd() {
        assertEquals(5, _queue0.size());
        Integer newInt = new Integer(5);
        _queue0.add(newInt);
        assertEquals(6, _queue0.size());
        Exception exception = null;
        try {
            _queue0.add(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>clear()</code> method.
     *
     * @since  v0.1
     */
    public void testClear() {
        assertFalse(_queue0.isEmpty());
        _queue0.clear();
        assertTrue(_queue0.isEmpty());
    }

    /**
     * Test the <code>contains()</code> method.
     *
     * @since  v0.1
     */
    public void testContains() {
        assertTrue(_queue0.contains(INT0));
        assertFalse(_queue0.contains(new Integer(5)));
    }

    /**
     * Test the <code>isEmpty()</code> method.
     *
     * @since  v0.1
     */
    public void testIsEmpty() {
        assertFalse(_queue0.isEmpty());
        _queue0.clear();
        assertTrue(_queue0.isEmpty());
    }

    /**
     * Test the <code>remove()</code> method.
     *
     * @since  v0.1
     */
    public void testRemove() {
        assertEquals(INT0, _queue0.remove());
        assertEquals(INT1, _queue0.remove());
        assertEquals(INT2, _queue0.remove());
        assertEquals(INT3, _queue0.remove());
        assertEquals(INT4, _queue0.remove());
    }

    /**
     * Test the <code>size()</code> method.
     *
     * @since  v0.1
     */
    public void testSize() {
        assertEquals(5, _queue0.size());
        _queue0.remove();
        assertEquals(4, _queue0.size());
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _queue0 = new Queue<Integer>();
        _queue0.add(INT0);
        _queue0.add(INT1);
        _queue0.add(INT2);
        _queue0.add(INT3);
        _queue0.add(INT4);
    }

    /**
     * @see  junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _queue0 = null;
    }
}
