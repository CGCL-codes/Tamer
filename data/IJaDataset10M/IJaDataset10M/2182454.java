package org.openliberty.igf.attributeService;

import org.openliberty.igf.attributeService.IGFException;

/**
 * Used for exceptions generated within IGF stack implementations.
 *
 */
public class InitializedException extends IGFException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5726078755210700392L;

    /**
	 * @see Exception#Exception()
	 */
    public InitializedException() {
    }

    /**
	 * @see java.lang.Exception#Exception(java.lang.String)
	 */
    public InitializedException(String arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.Throwable)
	 */
    public InitializedException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.String, java.lang.Throwable)
	 */
    public InitializedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
