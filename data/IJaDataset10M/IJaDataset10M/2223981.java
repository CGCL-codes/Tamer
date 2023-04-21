package fr.gfi.foundation.core.interceptor;

/**
 * MyEmailValidatorException.
 * 
 * @author Tiago Fernandez
 * @since 0.4
 */
public class MyEmailValidatorException extends Exception {

    private static final long serialVersionUID = -7477347805565969952L;

    /**
	 * Constructor.
	 */
    public MyEmailValidatorException() {
        super();
    }

    /**
	 * Constructor.
	 * 
	 * @param message describing the exception
	 */
    public MyEmailValidatorException(String message) {
        super(message);
    }

    /**
	 * Constructor.
	 * 
	 * @param message describing the exception
	 * @param ex the wrapped exception
	 */
    public MyEmailValidatorException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
	 * Constructor.
	 * 
	 * @param ex the wrapped exception
	 */
    public MyEmailValidatorException(Throwable ex) {
        super(ex);
    }
}
