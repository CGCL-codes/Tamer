package org.ws4d.java.structures;

public class ConcurrentChangeException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1756654134328882210L;

    public ConcurrentChangeException() {
        super();
    }

    public ConcurrentChangeException(String message) {
        super(message);
    }
}
