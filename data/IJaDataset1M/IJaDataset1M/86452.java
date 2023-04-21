package com.googlecode.vicovre.utils.nativeloader;

public class LoadException extends Exception {

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadException(Throwable cause) {
        super(cause);
    }
}
