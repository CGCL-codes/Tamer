package org.openliberty.arisid;

import org.openliberty.arisid.IGFException;

/**
 * Used for exceptions generated within IGF stack implementations.
 *
 */
public class InvalidFilterException extends IGFException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2702284365834989322L;

    /**
	 * @see Exception#Exception()
	 */
    public InvalidFilterException() {
    }

    /**
	 * @see java.lang.Exception#Exception(java.lang.String)
	 */
    public InvalidFilterException(String arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.Throwable)
	 */
    public InvalidFilterException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.String, java.lang.Throwable)
	 */
    public InvalidFilterException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
