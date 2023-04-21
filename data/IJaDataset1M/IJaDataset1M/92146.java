package iwallet.common.account.util;

/**
 * @author 黄源河
 *
 */
public class NoInitialSeedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public NoInitialSeedException() {
        super();
    }

    /**
     * @param message
     */
    public NoInitialSeedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NoInitialSeedException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NoInitialSeedException(String message, Throwable cause) {
        super(message, cause);
    }
}
