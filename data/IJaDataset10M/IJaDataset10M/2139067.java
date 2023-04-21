package com.jaccal;

/**
 * @author Chang Sau Sheong
 */
public class SessionFactoryException extends CardException {

    public SessionFactoryException() {
    }

    ;

    public SessionFactoryException(Throwable t) {
        super(t);
    }

    public SessionFactoryException(String msg) {
        super(msg);
    }
}
