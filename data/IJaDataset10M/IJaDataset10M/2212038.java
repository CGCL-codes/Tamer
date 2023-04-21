package org.tagbox.util;

public class PoolException extends RuntimeException {

    public PoolException(Throwable cause) {
        super(cause.toString());
    }

    public PoolException(String msg) {
        super(msg);
    }
}
