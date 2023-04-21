package com.colorado.core.diagnostics.reflection;

import java.io.Serializable;

/**
 * The exception that is thrown when a reflection error occurs.
 * @author Dilan Perera
 */
public class ReflectionException extends Exception implements Serializable {

    /**
     * Creates a new instance of
     * <code>ReflectionException</code> without detail message.
     */
    public ReflectionException() {
    }

    /**
     * Constructs an instance of
     * <code>ReflectionException</code> with the specified detail message.
     * @param message the detail message.
     */
    public ReflectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause. <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
