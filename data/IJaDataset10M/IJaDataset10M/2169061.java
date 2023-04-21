package edu.stanford.ejalbert.exception;

/**
 * Exception thrown when the Operating System is not supported by
 * the browser launcher project.
 *
 * @author Markus Gebhard
 */
public class UnsupportedOperatingSystemException extends Exception {

    public UnsupportedOperatingSystemException(String message) {
        super(message);
    }

    public UnsupportedOperatingSystemException(Throwable cause) {
        super(cause);
    }
}
