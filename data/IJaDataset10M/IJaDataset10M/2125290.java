package com.wuala.loader2.loader.remote;

public class UpdateFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public UpdateFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
