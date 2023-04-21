package org.pausequafe.misc.exceptions;

@SuppressWarnings("serial")
public class PQConnectionException extends PQException {

    public PQConnectionException() {
    }

    public PQConnectionException(String message) {
        super(message);
    }

    public PQConnectionException(Throwable cause) {
        super(cause);
    }

    public PQConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
