package com.gm.core.net;

/**
 * <p>
 * Exception when execute Request
 * </p>
 * 
 * 
 */
public class RequestException extends Exception {

    private static final long serialVersionUID = 3812105322628942359L;

    public RequestException() {
        super();
    }

    public RequestException(String msg) {
        super(msg);
    }

    public RequestException(Exception e) {
        super(e);
    }
}
