package org.ignoramus.application.store;

import org.ignoramus.application.IgnoramusException;

/**
 * Exception thrown by this package.
 */
public class StoreException extends IgnoramusException {

    /**
	 * Constructor for a root exception.
	 * 
	 * @param message Explanation of the exception for the developer.
	 */
    public StoreException(String message) {
        super(message);
    }

    /**
	 * Constructor for a chained exception.
	 * 
	 * @param message Explanation of the exception for the developer.
	 * @param cause the caught exception causing this one. 
	 */
    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
