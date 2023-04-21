package cardgames.control.maumau.exceptions;

/**
 * Thrown to indicate that a token provided is illegal.
 * <p>
 * This is a deadly exception. It indicates that the table considers the token
 * provided to be illegal. This makes the table shut down.
 */
public class IllegalToken extends MauMauTableException {

    /**
     * ID used to make instances of this class transferable over the net.
     * <p>
     * @see <a href="http://dev.root1.de/wiki/simon/Sample_helloworld" target="_BLANK">SIMON-sample</a>
     */
    @SuppressWarnings("deprecation")
    static final long serialVersionUID = (new java.util.Date(2010, 6, 12)).getTime();

    public IllegalToken(String tableName, Long token) {
        super("Token " + token + " is illegal at " + tableName);
    }
}
