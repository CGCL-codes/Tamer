package hu.de.rft.bookstore.model.exception;

/**
 *
 * @author X3cut0r
 */
public class DefaultException extends Exception {

    public DefaultException() {
    }

    public DefaultException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DefaultException(String msg) {
        super(msg);
    }
}
