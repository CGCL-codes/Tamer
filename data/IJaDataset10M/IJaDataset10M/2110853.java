package org.neuroph.util.io;

import org.neuroph.core.exceptions.NeurophException;

/**
 * This exception is thrown when some error occurs when writing neural network
 * output using some output adapter.
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see OutputAdapter
 */
public class NeurophOutputException extends NeurophException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an NeurophOutputException with no detail message.
     */
    public NeurophOutputException() {
        super();
    }

    /**
     * Constructs an NeurophOutputException with the specified detail message.
     * @param message the detail message.
     */
    public NeurophOutputException(String message) {
        super(message);
    }

    /**
     * Constructs a NeurophOutputException with the specified detail message and specified cause.
     * @param message the detail message.
     * @param cause the cause for exception
     */
    public NeurophOutputException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause
     * @param cause the cause for exception
     */
    public NeurophOutputException(Throwable cause) {
        super(cause);
    }
}
