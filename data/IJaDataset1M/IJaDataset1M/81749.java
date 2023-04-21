package com.gwtaf.core.shared.util;

public class InvalidSessionException extends AuthorisationFailure {

    private static final long serialVersionUID = 1L;

    public InvalidSessionException() {
    }

    public InvalidSessionException(String msg) {
        super(msg);
    }

    public InvalidSessionException(String msg, Throwable t) {
        super(msg, t);
    }
}
