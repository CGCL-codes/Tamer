package org.jfeedback;

import com.anthonyeden.lib.ChainedException;

/**
 * An exception which is thrown when there is an error during transport of FeedbackData.
 *
 * @author Anthony Eden
 */
public class TransportException extends ChainedException {

    /**
     * Construct a new TransportException with the given message.
     *
     * @param message The error message
     */
    public TransportException(String message) {
        super(message, null);
    }

    /**
     * Construct a new TransportException with the given nexted error.  The nested error's message is used as this
     * exception's error message.
     *
     * @param t The nested error
     */
    public TransportException(Throwable t) {
        super(t.getMessage(), t);
    }

    /**
     * Construct a new TransportException with the given message and nested error.
     *
     * @param message The error message
     * @param t The nexted error
     */
    public TransportException(String message, Throwable t) {
        super(message, t);
    }
}
