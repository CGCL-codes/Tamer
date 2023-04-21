package com.patientis.framework.utility;

/**
 * SerializeXMLException is thrown when XML is unable to be deserialized to an object
 *
 * <br/>Design Patterns: <a href="/functionality/rm/1000047.html">Exceptions</a>
 * <br/>  
 */
public class SerializeXMLException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public SerializeXMLException() {
        super();
    }

    /**
	 * @param cause
	 */
    public SerializeXMLException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public SerializeXMLException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public SerializeXMLException(String message) {
        super(message);
    }
}
