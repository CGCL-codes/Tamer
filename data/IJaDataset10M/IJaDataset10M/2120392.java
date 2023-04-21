package org.jcvi.testUtil;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This is an abstract TestCase
 * which will test the constuctors
 * of an {@link Throwable} object.
 * @author dkatzel
 *
 *
 */
public abstract class AbstractThrowableTest<T extends Throwable> {

    private String message = "test message";

    private Throwable cause = new Exception("testCause");

    protected abstract T createThrowable(String message);

    protected abstract T createThrowable(String message, Throwable cause);

    @Test
    public void testConstructor() {
        Throwable ex = createThrowable(message);
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void testConstructorWithCause() {
        Throwable ex = createThrowable(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
