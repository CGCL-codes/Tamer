package org.jcouchdb.exception;

public class CouchDBException extends RuntimeException {

    private static final long serialVersionUID = -2767482489254079131L;

    public CouchDBException(Throwable cause) {
        super(cause);
    }

    public CouchDBException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouchDBException(String message) {
        super(message);
    }
}
