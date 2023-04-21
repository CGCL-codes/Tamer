package com.mebigfatguy.smaxproto;

public class SmaxProtoTerminationException extends RuntimeException {

    private static final long serialVersionUID = -540889059302574814L;

    public SmaxProtoTerminationException() {
        super();
    }

    public SmaxProtoTerminationException(String message) {
        super(message);
    }

    public SmaxProtoTerminationException(String message, Throwable cause) {
        super(message, cause);
    }
}
