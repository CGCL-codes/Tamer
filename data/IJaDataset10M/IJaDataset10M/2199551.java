package com.hp.hpl.jena.shared;

public class CannotCreateException extends JenaException {

    public CannotCreateException(String message) {
        super(message);
    }

    public CannotCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
