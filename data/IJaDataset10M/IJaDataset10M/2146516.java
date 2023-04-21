package org.vizzini.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Provides unit tests for the <code>Stack</code> class.
 *
 * <p>By default, all test methods (methods names beginning with <code>
 * test</code>) are run. Run individual tests from the command line using the
 * <code>main()</code> method. Specify individual test methods to run using the
 * <code>suite()</code> method.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @see      TestFinder
 * @since    v0.3
 */
public class StackTest extends TestCase {

    /** First stack. */
    private IStack<String> _stack0;

    /**
     * Construct this object with the given parameter.
     *
     * @param  method  Method to run.
     *
     * @since  v0.3
     */
    public StackTest(String method) {
        super(method);
    }

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.3
     */
    public static void main(String[] args) {
        TestFinder.getInstance().run(StackTest.class, args);
    }

    /**
     * @return  a suite of tests to run.
     *
     * @since   v0.3
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite(StackTest.class);
        return suite;
    }

    /**
     * Test the <code>me()</code> method.
     *
     * @since  v0.3
     */
    public void testMe() {
        assertTrue(true);
        String one = "one";
        String two = "two";
        String three = "three";
        _stack0.push(one);
        assertEquals(1, _stack0.size());
        assertEquals(one, _stack0.peek());
        _stack0.push(two);
        assertEquals(2, _stack0.size());
        assertEquals(two, _stack0.peek());
        _stack0.push(three);
        assertEquals(3, _stack0.size());
        assertEquals(three, _stack0.peek());
        Object object = _stack0.pop();
        assertEquals(2, _stack0.size());
        assertEquals(three, object);
        object = _stack0.pop();
        assertEquals(1, _stack0.size());
        assertEquals(two, object);
        object = _stack0.pop();
        assertTrue(_stack0.isEmpty());
        assertEquals(0, _stack0.size());
        assertEquals(one, object);
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _stack0 = new Stack<String>();
    }

    /**
     * @see  junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _stack0 = null;
    }
}
