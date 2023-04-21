package com.googlecode.jazure.sdk.core;

public class JAzureException extends RuntimeException {

    private static final long serialVersionUID = -8822944615324859027L;

    public JAzureException() {
        super();
    }

    public JAzureException(String message) {
        super(message);
    }

    public JAzureException(Throwable cause) {
        super(cause);
    }

    public JAzureException(String message, Throwable cause) {
        super(message, cause);
    }
}
