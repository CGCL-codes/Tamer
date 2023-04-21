package fr.gfi.gfinet.server;

import fr.gfi.gfinet.common.exception.EntityException;

/**
 * @author Jean DAT
 * @since  Nov. 09, 2007
 */
public class CVException extends EntityException {

    /**
	 * Constructor for the exception object.
	 * 
	 * @param message description of the error
	 */
    public CVException(String message) {
        super(message);
    }

    /**
	 * Constructor for the exception object.
	 * 
	 * @param message description of the error
	 * @param cause the nested exception
	 */
    public CVException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor for the exception object.
	 * 
	 * @param cause the nested exception
	 */
    public CVException(Throwable cause) {
        super(cause);
    }
}
