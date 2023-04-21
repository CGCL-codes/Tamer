package be.yildiz.client.util.exeption;

/**
 *
 * @author Van Den Borre Grégory
 *
 */
public abstract class YildizException extends RuntimeException {

    /***/
    private static final long serialVersionUID = -1941868065660831876L;

    /**
     * Simple constructor.
     *
     * @param message
     *            Message to print to the user.
     */
    protected YildizException(final String message) {
        super(message);
    }

    /**
     * Full constructor.
     *
     * @param message
     *            Message to print to the user.
     * @param exception
     *            Original exception stack.
     */
    protected YildizException(final String message, final Throwable exception) {
        super(message, exception);
    }
}
