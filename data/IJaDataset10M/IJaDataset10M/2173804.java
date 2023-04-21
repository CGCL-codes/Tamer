package com.sptci.rwt;

/**
 * A custom exception that is used to wrap any exceptions raised when
 * fetching {@link java.sql.Connection}s.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-09-24
 * @version $Id: ConnectionException.java 2 2007-10-19 21:06:36Z rakesh.vidyadharan $
 */
public class ConnectionException extends RuntimeException {

    /**
   * Default constructor.  Cannot be instantiated.
   */
    protected ConnectionException() {
    }

    /**
   * Create a new exception with the specified message.
   *
   * @param message The message that describes the problem.
   */
    public ConnectionException(final String message) {
        super(message);
    }

    /**
   * Create a new exception with the instance of {@link Throwable} that
   * caused the problem.
   *
   * @param cause The exception that caused this instance of the
   *   exception to be thrown.
   */
    public ConnectionException(final Throwable cause) {
        super(cause);
    }

    /**
   * Create a new exception with the specified message and instance of
   * {@link Throwable} caused the problem.
   *
   * @param message The message that describes the problem.
   * @param cause The exception that caused this instance of the
   *   exception to be thrown.
   */
    public ConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
