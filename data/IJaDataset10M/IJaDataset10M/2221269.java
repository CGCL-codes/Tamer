package com.turnengine.client.common.exception;

public class TurnEngineException extends RuntimeException {

    private static final long serialVersionUID = 7452294540820957981L;

    public TurnEngineException() {
    }

    public TurnEngineException(String message) {
        super(message);
    }

    public TurnEngineException(Throwable cause) {
        super(cause);
    }

    public TurnEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
