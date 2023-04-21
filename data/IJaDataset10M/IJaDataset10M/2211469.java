package org.modelibra.exception;

/**
 * Property type runtime exception.
 * 
 * @version 2008-10-22
 * @author Dzenan Ridjanovic
 * @author Vedad Kirlic
 */
public class TypeRuntimeException extends ModelibraRuntimeException {

    private static final long serialVersionUID = 3080L;

    /**
	 * @see ModelibraRuntimeException#ModelibraRuntimeException()
	 */
    public TypeRuntimeException() {
        super();
    }

    /**
	 * Constructs a property type exception with a given error message.
	 * 
	 * @see ModelibraRuntimeException#ModelibraRuntimeException(java.lang.String)
	 * 
	 * @param error
	 *            error message
	 */
    public TypeRuntimeException(String error) {
        super(error);
    }

    /**
	 * @see ModelibraRuntimeException#ModelibraRuntimeException(java.lang.Throwable)
	 */
    public TypeRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
	 * @see ModelibraRuntimeException#ModelibraRuntimeException(java.lang.String,
	 *      java.lang.Throwable)
	 */
    public TypeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
