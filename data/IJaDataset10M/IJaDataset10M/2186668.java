package com.atolsystems.atolutilities;

/**
 * Exception thrown when a function want to exit the application
 *
 * @version 1.0, 10/10/09
 * @author Sebastien Riou
 */
public class ExitException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ExitException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of <code>(cause==null ? null : cause.toString())</code>.
     *
     * @param cause the cause of this exception or null
     */
    public ExitException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception or null
     */
    public ExitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExitException() {
    }
}
