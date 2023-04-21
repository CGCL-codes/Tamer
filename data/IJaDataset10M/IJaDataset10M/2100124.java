package org.tripcom.triplespace.exception;

public class TSException extends Exception {

    private static final long serialVersionUID = 1L;

    public TSException(String message) {
        super(message);
    }

    public TSException(String message, Throwable t) {
        super(message, t);
    }

    public TSException(Throwable t) {
        super(t);
    }
}
