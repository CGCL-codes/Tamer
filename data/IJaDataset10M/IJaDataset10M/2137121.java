package com.jaccal;

/**
 * Generic smart card exception
 * @author Chang Sau Sheong
 */
public class CardException extends Exception {

    public CardException() {
    }

    ;

    public CardException(Throwable t) {
        super(t);
    }

    public CardException(String message) {
        super(message);
    }
}
