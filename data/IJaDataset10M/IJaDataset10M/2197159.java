package org.openliberty.igf.attributeService.stack;

import org.openliberty.igf.attributeService.IGFException;

/**
 * Used when an unrecoverable connection error has occured connecting to an attribute authority.
 *
 */
public class ConnectionException extends IGFException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5981179117186753260L;

    /**
	 * @see Exception#Exception()
	 */
    public ConnectionException() {
    }

    /**
	 * @see java.lang.Exception#Exception(java.lang.String)
	 */
    public ConnectionException(String arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.Throwable)
	 */
    public ConnectionException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.String, java.lang.Throwable)
	 */
    public ConnectionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
