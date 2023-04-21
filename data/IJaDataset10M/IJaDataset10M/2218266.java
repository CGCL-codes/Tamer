package org.monet.backmobile.exception;

public class NetworkException extends Exception {

    public NetworkException() {
        super();
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable e) {
        super(message, e);
    }

    private static final long serialVersionUID = 4460297026363304682L;
}
