package com.jedox.etl.core.component;

/**
 * Exception class to be thrown in the {@link IComponent Component} runtime phase, when something unexpected and not recoverable has happened.
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class RuntimeException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1172715114665364053L;

    public RuntimeException() {
        super();
    }

    public RuntimeException(String message) {
        super(message);
    }

    public RuntimeException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }
}
