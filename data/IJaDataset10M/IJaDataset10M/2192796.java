package org.horen.task;

public class NoValidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoValidTokenException() {
    }

    public NoValidTokenException(String message) {
        super(message);
    }

    public NoValidTokenException(Throwable cause) {
        super(cause);
    }

    public NoValidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
