package com.hestia.b2b.data;

import com.hestia.b2b.conversionmanager.DataConversionException;

/**
*/
public class B2bDataException extends DataConversionException {

    private static final long serialVersionUID = 6908956037969600385L;

    /**
	 * @param message
	 */
    public B2bDataException(String message) {
        super(message);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public B2bDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
