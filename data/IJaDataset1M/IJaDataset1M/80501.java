package net.sf.jdpa;

/**
 * @author Andreas Nilsson
 */
public class RealizationException extends Exception {

    public RealizationException() {
    }

    public RealizationException(String message) {
        super(message);
    }

    public RealizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RealizationException(Throwable cause) {
        super(cause);
    }
}
