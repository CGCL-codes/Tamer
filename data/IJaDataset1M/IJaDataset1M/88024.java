package addressbook;

/**
 * @author dmitriy
 *
 * This class is used for representing own AddressBook application specific exceptions
 */
public class AddressException extends Exception {

    public AddressException(String message) {
        super(message);
    }

    public AddressException(String message, Throwable t) {
        super(message, t);
    }
}
