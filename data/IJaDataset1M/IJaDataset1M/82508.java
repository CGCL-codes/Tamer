package others.sergioo.AddressLocator;

@SuppressWarnings("serial")
public class BadAddressException extends Exception {

    /**
	 * Constructs an bad address exception
	 */
    public BadAddressException(String error) {
        super("The address is wrong: " + error + ".");
    }
}
