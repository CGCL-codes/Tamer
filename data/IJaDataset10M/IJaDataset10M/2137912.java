package junit.extensions.jfunc;

import junit.framework.*;

/**
 * Basically this is JUnit's Assert made into an instance class,
 * rather than a static one.  It also provides the necessary methods
 * to allow for multiple failures.
 *
 * @author Shane Celis <shane@terraspring.com>
 **/
public class JFuncAssert {

    /**
     * Needed for doing multiple failures in one test.
     **/
    protected TestResult result;

    /**
     * The test that will be calling the asserts (usually just
     * <code>this</code>, by virtue of extension a la JFuncTestCase)
     **/
    private Test test;

    /**
     * internal flag to denote whether assertions will be fatal or not
     **/
    private boolean fatal = true; // default behavior is fatal asserts

    /**
     * Public constructor for performing asserts on a test which
     * extends Assert (XXX can't do non-fatal asserts, unless extended from)
     **/
    protected JFuncAssert() {
        if (this instanceof Test) 
            this.test = (Test) this;
        else 
            this.test = null;
    }

    /**
     * Public constructor for performing asserts on tests which don't extend
     * Assert.
     **/
    public JFuncAssert(Test test, TestResult result) {
        if (test == null || result == null) {
            throw new NullPointerException();
        }
        this.test = test;
        this.result = result;
    }

    /**
     * @param fatal sets assert to fatal (one failure per test) or non fatal
     * (multiple failures per test)
     * @throws IllegalArgumentException If the test member isn't set
     * from either construtor or from an extending class.
     **/
    public void setFatal(boolean fatal) {
        if (test == null && !fatal) {
            throw new IllegalArgumentException("Must provide Assert with a " +
                  "Test during construction, if it's not being used from an " +
                  "extending class.");
        }
        this.fatal = fatal;
    }

    /**
     * @return whether asserts are currently fatal
     **/
    public boolean isFatal() {
        return fatal;
    }

    /**
     * Returns the test that's currently using the asserts.  This is
     * necessary in order to the logging of the assert, rather than
     * simply throwing an exception
     **/
    protected Test getTest() {
        return test;
    }

    // XXX these shouldn't be static any more.
    public TestResult getResult() {
        return result;
    }

    /**
     * Must be set prior to running test, if test is intended to do
     * non-fatal assertions.
     **/
    public void setResult(TestResult result) {
        this.result = result;
    }

    /**
     * Must be set prior to running test, if test is intended to do
     * non-fatal assertions.
     **/
    public void setTest(Test test) {
        this.test = test;
    }

    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError with the given message.
     * @deprecated use assertTrue
     */
    public void assert(String message, boolean condition) {
        if (!condition)
            fail(message);
    }
    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError.
     * @deprecated use assertTrue
     *
     */
    public void assert(boolean condition) {
        assert(null, condition);
    }

    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError with the given message.
     */
    public void assertTrue(String message, boolean condition) {
        if (!condition)
            fail(message);
    }
    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionFailedError.
     */
    public void assertTrue(boolean condition) {
        assertTrue(null, condition);
    }
    /**
     * Fails a test with the given message. 
     */
    public void fail(String message) {
        TestResult result = getResult();
        Test test = getTest();
        if (result != null && !isFatal() && test != null) {
            //New behavior (multiple failures per test)
            result.addFailure(test, 
                              new AssertionFailedError(message));
        } else {
            if (!isFatal()) {
                System.err.println("setResult on Assert is not being " +
                      "called to allow for NonFatalAsserts to work properly" +
                      " (result = " + result + ", test = " + test + ")");
            }
            //Old behavior (one failure per test)
            throw new AssertionFailedError(message);
        }
    }

    /**
     * Fails a test with no message. 
     */
    public void fail() {
        fail(null);
    }

    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionFailedError is thrown.
     */
    public void assertEquals(String message, Object expected, Object actual) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        failNotEquals(message, expected, actual);
    }
    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionFailedError is thrown.
     */
    public void assertEquals(Object expected, Object actual) {
        assertEquals(null, expected, actual);
    }
    /**
     * Asserts that two doubles are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    public void assertEquals(String message, double expected, double actual, double delta) {
        // handle infinity specially since subtracting to infinite values gives NaN and the
        // the following test fails
        if (Double.isInfinite(expected)) {
            if (!(expected == actual))
                failNotEquals(message, new Double(expected), new Double(actual));
        } else if (!(Math.abs(expected-actual) <= delta)) // Because comparison with NaN always returns false
            failNotEquals(message, new Double(expected), new Double(actual));
    }
    /**
     * Asserts that two doubles are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    public void assertEquals(double expected, double actual, double delta) {
        assertEquals(null, expected, actual, delta);
    }
    /**
     * Asserts that two floats are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    public void assertEquals(String message, float expected, float actual, float delta) {
        // handle infinity specially since subtracting to infinite values gives NaN and the
        // the following test fails
        if (Float.isInfinite(expected)) {
            if (!(expected == actual))
                failNotEquals(message, new Float(expected), new Float(actual));
        } else if (!(Math.abs(expected-actual) <= delta))
            failNotEquals(message, new Float(expected), new Float(actual));
    }
    /**
     * Asserts that two floats are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    public void assertEquals(float expected, float actual, float delta) {
        assertEquals(null, expected, actual, delta);
    }
    /**
     * Asserts that two longs are equal.
     */
    public void assertEquals(String message, long expected, long actual) {
        assertEquals(message, new Long(expected), new Long(actual));
    }
    /**
     * Asserts that two longs are equal.
     */
    public void assertEquals(long expected, long actual) {
        assertEquals(null, expected, actual);
    }
    /**
     * Asserts that two booleans are equal.
     */
    public void assertEquals(String message, boolean expected, boolean actual) {
        assertEquals(message, new Boolean(expected), new Boolean(actual));
    }
    /**
     * Asserts that two booleans are equal.
     */
    public void assertEquals(boolean expected, boolean actual) {
        assertEquals(null, expected, actual);
    }
    /**
     * Asserts that two bytes are equal.
     */
    public void assertEquals(String message, byte expected, byte actual) {
        assertEquals(message, new Byte(expected), new Byte(actual));
    }
    /**
     * Asserts that two bytes are equal.
     */
    public void assertEquals(byte expected, byte actual) {
        assertEquals(null, expected, actual);
    }
    /**
     * Asserts that two chars are equal.
     */
    public void assertEquals(String message, char expected, char actual) {
        assertEquals(message, new Character(expected), new Character(actual));
    }
    /**
     * Asserts that two chars are equal.
     */
    public void assertEquals(char expected, char actual) {
        assertEquals(null, expected, actual);
    }
    /**
     * Asserts that two shorts are equal.
     */
    public void assertEquals(String message, short expected, short actual) {
        assertEquals(message, new Short(expected), new Short(actual));
    }
    /**
     * Asserts that two shorts are equal.
     */
    public void assertEquals(short expected, short actual) {
        assertEquals(null, expected, actual);
    }
    /**
     * Asserts that two ints are equal.
     */
    public void assertEquals(String message, int expected, int actual) {
        assertEquals(message, new Integer(expected), new Integer(actual));
    }
    /**
     * Asserts that two ints are equal.
     */
    public void assertEquals(int expected, int actual) {
        assertEquals(null, expected, actual);
    }
    /**
     * Asserts that an object isn't null.
     */
    public void assertNotNull(Object object) {
        assertNotNull(null, object);
    }
    /**
     * Asserts that an object isn't null.
     */
    public void assertNotNull(String message, Object object) {
        assertTrue(message, object != null); 
    }
    /**
     * Asserts that an object is null.
     */
    public void assertNull(Object object) {
        assertNull(null, object);
    }
    /**
     * Asserts that an object is null.
     */
    public void assertNull(String message, Object object) {
        assertTrue(message, object == null); 
    }
    /**
     * Asserts that two objects refer to the same object. If they are not
     * an AssertionFailedError is thrown.
     */
    public void assertSame(String message, Object expected, Object actual) {
        if (expected == actual)
            return;
        failNotSame(message, expected, actual);
    }
    /**
     * Asserts that two objects refer to the same object. If they are not
     * the same an AssertionFailedError is thrown.
     */
    public void assertSame(Object expected, Object actual) {
        assertSame(null, expected, actual);
    }
        
    private void failNotEquals(String message, Object expected, Object actual) {
        String formatted= "";
        if (message != null)
            formatted= message+" ";
        fail(formatted+"expected:<"+expected+"> but was:<"+actual+">");
    }
        
    private void failNotSame(String message, Object expected, Object actual) {
        String formatted= "";
        if (message != null)
            formatted= message+" ";
        fail(formatted+"expected same");
    }
}
