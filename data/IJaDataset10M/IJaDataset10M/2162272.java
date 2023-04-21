package org.isurf.spmiddleware.reader;

/**
 * Exception to be thrown if the client is unable to connect to a Reader.
 */
public class ConnectException extends Exception {

    /**
	 * Constructs a ConnectionException.
	 *
	 * @param message The message.
	 */
    public ConnectException(String message) {
        super(message);
    }

    /**
	 * Constructs a ConnectionException.
	 *
	 * @param message The message.
	 * @param cause The cause.
	 */
    public ConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
