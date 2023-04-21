package org.peertrust.demo.servlet.jsptags;

/**
 * @author pat_dev
 *
 */
public class BadObjectStateException extends Exception {

    public BadObjectStateException() {
        super();
    }

    public BadObjectStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadObjectStateException(String message) {
        super(message);
    }

    public BadObjectStateException(Throwable cause) {
        super(cause);
    }
}
