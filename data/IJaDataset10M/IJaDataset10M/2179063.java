package com.csam.dif;

/**
 * Superclass of all data-related exceptions.
 *
 * @author Nathan Crause
 * @since 1.0
 * @version 1.0
 */
public class DIFDataException extends DIFException {

    /**
     * Creates a new instance of <code>DIFDataException</code> without detail
     * message.
     *
     * @since 1.0
     */
    public DIFDataException() {
    }

    /**
     * Constructs an instance of <code>DIFDataException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @since 1.0
     */
    public DIFDataException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>DIFDataException</code> with the
     * specified root cause.
     *
     * @param cause the root cause of the exception.
     * @since 1.0
     */
    public DIFDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>DIFDataException</code> with the
     * specified detail message and root cause.
     *
     * @param msg the detail message.
     * @param cause the root cause of the exception.
     * @since 1.0
     */
    public DIFDataException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
