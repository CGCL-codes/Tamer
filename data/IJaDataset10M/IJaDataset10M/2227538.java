package org.june;

/**
 * 
 * @author Fabien DUMINY
 */
public class Assert {

    /**
	 * 
	 * @param b
	 * @param message
	 * @throws TestFailedException
	 */
    protected void verify(boolean b, String message) throws TestFailedException {
        if (!b) fail(message);
    }

    /**
	 * 
	 * @param message
	 * @throws TestFailedException
	 */
    protected void fail(String message) throws TestFailedException {
        throw new TestFailedException(message);
    }

    /**
	 * 
	 * @param string
	 * @param b
	 */
    protected void assertFalse(String message, boolean b) throws TestFailedException {
        verify(!b, message);
    }

    /**
	 * @param message
	 * @param b
	 */
    protected void assertTrue(String message, boolean b) throws TestFailedException {
        verify(b, message);
    }

    /**
	 * 
	 * @param string
	 * @param o
	 */
    protected void assertNotNull(String message, Object o) throws TestFailedException {
        verify(o != null, message);
    }

    /**
	 * 
	 * @param string
	 * @param o
	 */
    protected void assertNull(String message, Object o) throws TestFailedException {
        verify(o == null, message);
    }

    /**
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
    protected void assertEquals(String message, Object expected, Object actual) throws TestFailedException {
        boolean areSameRef = (expected == actual);
        verify(areSameRef || expected.equals(actual), message);
    }

    /**
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
    protected void assertEquals(String message, long expected, long actual) throws TestFailedException {
        verify(expected == actual, message);
    }
}
