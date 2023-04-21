package com.androidcommons.webclient.json.rpc;

/**
 * @author Denis Migol
 * 
 */
public class JSONRPCException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2400364504641935708L;

    /**
	 * 
	 */
    public JSONRPCException() {
    }

    /**
	 * @param message
	 */
    public JSONRPCException(final String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public JSONRPCException(final Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public JSONRPCException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
