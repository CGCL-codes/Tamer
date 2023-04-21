package com.peterhi.client;

import com.peterhi.PeterHiException;

/**
 * @author YUN TAO
 *
 */
public class ServerMissingException extends PeterHiException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8673027307836763269L;

    /**
	 * 
	 */
    public ServerMissingException() {
    }

    /**
	 * @param message
	 */
    public ServerMissingException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public ServerMissingException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public ServerMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
