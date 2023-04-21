package org.engine.network;

/**
 * The Class SyncStringServerException.
 */
public class SyncStringServerException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The message. */
    private String message;

    /**
     * Instantiates a new sync string server exception.
     * 
     * @param message
     *            the message
     */
    public SyncStringServerException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "SyncString Server does not correspond correctly: " + message;
    }
}
