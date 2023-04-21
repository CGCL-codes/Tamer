package org.piuframework.service.aspects.test;

/**
 * TODO
 * 
 * @author Dirk Mascher
 */
public class UndeclaredTestException extends Exception {

    private static final long serialVersionUID = 1L;

    public UndeclaredTestException() {
        super();
    }

    public UndeclaredTestException(String message) {
        super(message);
    }

    public UndeclaredTestException(Throwable cause) {
        super(cause);
    }

    public UndeclaredTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
