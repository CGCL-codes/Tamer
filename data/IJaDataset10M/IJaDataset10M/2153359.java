package com.ingenta.clownbike;

public class IndexException extends ClownbikeException {

    public IndexException(String message, Object[] parameters, Exception cause) {
        super(message, parameters, cause);
    }

    public IndexException(String message, Object[] parameters) {
        super(message);
        _parameters = parameters;
    }

    public IndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexException(String message) {
        super(message);
    }

    public IndexException(Throwable cause) {
        super(cause);
    }
}
