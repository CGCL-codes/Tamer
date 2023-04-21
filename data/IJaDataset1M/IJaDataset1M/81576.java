package iwallet.common.currency;

/**
 * @author 汤洋
 *
 */
public class CurrencyException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6821671283357249987L;

    /**
	 * 
	 */
    public CurrencyException() {
        super();
    }

    /**
	 * @param message
	 */
    public CurrencyException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public CurrencyException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public CurrencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
