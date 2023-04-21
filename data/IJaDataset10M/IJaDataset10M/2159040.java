package com.sri.common.dbobj;

/**
 * Runtime Exception for underlying repository errors.  Most errors such
 * as database connection lost errors are not really handleable and therefore
 * are set as runtime.
 *
 * @author Michael Rimov
 */
public class RepositoryException extends RuntimeException {

    /**
     * Default constructor.
     */
    public RepositoryException() {
        super();
    }

    /**
     * Constructor that takes a message.
     *
     * @param message String
     */
    public RepositoryException(final String message) {
        super(message);
    }

    /**
     * Constructor that takes a nested exception/error.
     *
     * @param cause Throwable the nested error.
     */
    public RepositoryException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor that takes a message and a nested exception.
     *
     * @param message String the message.
     * @param cause   Throwable the underlying exception.
     */
    public RepositoryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
