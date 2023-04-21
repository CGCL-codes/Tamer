package net.jadoth.codegen;

/**
 * The Class CodeGenException.
 */
public class CodeGenException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4674035342961585253L;

    /**
	 * Instantiates a new code gen exception.
	 */
    public CodeGenException() {
        super();
    }

    /**
	 * Instantiates a new code gen exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
    public CodeGenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Instantiates a new code gen exception.
	 * 
	 * @param message the message
	 */
    public CodeGenException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new code gen exception.
	 * 
	 * @param cause the cause
	 */
    public CodeGenException(Throwable cause) {
        super(cause);
    }
}
