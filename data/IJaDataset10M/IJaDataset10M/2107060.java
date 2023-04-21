package org.openliberty.arisid.stack;

import org.openliberty.arisid.IGFException;

/**
 * The requested subject could not be located or does not exist.
 *
 */
public class NoSuchSubjectException extends IGFException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 559699072956945553L;

    /**
	 * @see Exception#Exception()
	 */
    public NoSuchSubjectException() {
    }

    /**
	 * @see java.lang.Exception#Exception(java.lang.String)
	 */
    public NoSuchSubjectException(String arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.Throwable)
	 */
    public NoSuchSubjectException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @see Exception#Exception(java.lang.String, java.lang.Throwable)
	 */
    public NoSuchSubjectException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
