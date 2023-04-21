package org.openliberty.arisid.stack;

import org.openliberty.arisid.IGFException;

/**
 * The requested transaction was rejected due to a policy exception.
 *
 */
public class PolicyException extends IGFException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3221402197112319814L;

    /**
	 * @see Exception#Exception()
	 */
    public PolicyException() {
    }

    /**
	 * @see java.lang.Exception#Exception(java.lang.String)
	 */
    public PolicyException(String arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.Throwable)
	 */
    public PolicyException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.String, java.lang.Throwable)
	 */
    public PolicyException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
