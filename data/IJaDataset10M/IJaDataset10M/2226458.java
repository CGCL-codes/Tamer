package com.sybrix.easygsp.exception;

public class IndexFileNotFoundException extends Exception {

    public IndexFileNotFoundException() {
    }

    public IndexFileNotFoundException(String message) {
        super(message);
    }

    public IndexFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexFileNotFoundException(Throwable cause) {
        super(cause);
    }
}
