package org.lnicholls.galleon.app;

public class AppException extends Exception {

    public AppException(String message) {
        this(message, null);
    }

    public AppException(String message, Throwable throwable) {
        super(message);
        this.mNestedException = throwable;
    }

    public Throwable getNestedException() {
        return mNestedException;
    }

    private Throwable mNestedException;
}
