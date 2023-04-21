package uk.ac.cam.caret.minibix.qtibank.api;

/**
 * An exception for when the client has made a boo-boo
 * @author William Billingsley
 */
public class ClientArgumentException extends QTIBankException {

    /**
	 * (Set to the date this class was last updated in a serialization-breaking manner)
	 */
    private static final long serialVersionUID = 20071210L;

    public ClientArgumentException(String message) {
        super(message);
    }

    public ClientArgumentException(String message, Throwable t) {
        super(message, t);
    }
}
