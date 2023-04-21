package muse.external;

/**
 * Represents error occured in external environment.
 * For example, this exception occured when external module returns -1. 
 *
 * @author Korchak Anton
 */
public class ExternalException extends RuntimeException {

    /**
	 * Use this constant when, for example, Process throws
	 * Exception.
	 */
    public static int UNKNOWN_ERROR = 0;

    /**
	 * Code of external error. Often this code is equal to code 
	 * returned by external module. By default it is equal to UNKNOWN_ERROR. 
	 */
    protected int code = UNKNOWN_ERROR;

    /**
	 * Class constructor.
	 * 
	 * @param code - code of error.
	 */
    public ExternalException(int code) {
        super();
        this.code = code;
    }

    /**
	 * Class constructor.
	 * 
	 * @param message - error message.
	 */
    public ExternalException(String message) {
        super(message);
    }

    /**
	 * Class constructor.
	 * 
	 * @param message - error message.
	 * @param code - code or error.
	 */
    public ExternalException(String message, int code) {
        super(message);
        this.code = code;
    }

    /**
	 * Geting code of error.
	 * @return code of error.
	 */
    public int getCode() {
        return code;
    }
}
