package org.datascooter.exception;

public class SnipCasheException extends RuntimeException {

    private static final long serialVersionUID = -3286181426599104017L;

    public SnipCasheException() {
        super();
    }

    public SnipCasheException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnipCasheException(String message) {
        super(message);
    }

    public SnipCasheException(Throwable cause) {
        super(cause);
    }
}
