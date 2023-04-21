package com.csam.stubs;

/**
 *
 * @author Nathan Crause
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StubbingException extends RuntimeException {

    /**
     * Creates a new instance of <code>StubbingException</code> without
     * detail message.
     *
     * @since 1.0.0
     */
    public StubbingException() {
    }

    /**
     * Constructs an instance of <code>StubbingException</code> with the 
     * specified detail message.
     *
     * @param msg the detail message.
     * @since 1.0.0
     */
    public StubbingException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>StubbingException</code> with the
     * specified cause.
     * 
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public StubbingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>StubbingException</code> with the
     * specified message text and cause.
     *
     * @param message the detail message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public StubbingException(String message, Throwable cause) {
        super(message, cause);
    }
}
