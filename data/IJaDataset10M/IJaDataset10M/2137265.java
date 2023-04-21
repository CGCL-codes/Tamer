package org.makagiga.commons.test;

import java.lang.reflect.Method;

/**
 * An unit test failure.
 *
 * @see org.makagiga.commons.test.Tester
 */
public final class TestException extends Exception {

    private Method method;

    /**
	 * Constructs a new exception.
	 * @param cause An exception that caused the test failure
	 * @param method A method that failed
	 */
    public TestException(final Throwable cause, final Method method) {
        super(cause);
        this.method = method;
    }

    /**
	 * Returns a method that failed.
	 */
    public Method getMethod() {
        return method;
    }
}
