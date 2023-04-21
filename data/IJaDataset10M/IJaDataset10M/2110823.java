package org.netxilia.server.service.event;

public class EventConversionException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2291103350197308352L;

    public EventConversionException() {
        super();
    }

    public EventConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventConversionException(String message) {
        super(message);
    }

    public EventConversionException(Throwable cause) {
        super(cause);
    }
}
