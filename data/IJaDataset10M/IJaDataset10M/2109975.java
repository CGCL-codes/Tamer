package com.golemgame.properties;

public class UnhandledPropertyException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public UnhandledPropertyException() {
    }

    public UnhandledPropertyException(String message) {
        super(message);
    }

    public UnhandledPropertyException(Throwable cause) {
        super(cause);
    }

    public UnhandledPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
