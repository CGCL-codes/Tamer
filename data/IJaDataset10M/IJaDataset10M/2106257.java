package uk.ac.ncl.cs.instantsoap.t1;

/**
 * Exception to indicate that the InstantSoap installation is missing, or otherwise inaccessible.
 *
 * @author Matthew Pocock
 */
public class MissingInstantSoapException extends Exception {

    public MissingInstantSoapException() {
    }

    public MissingInstantSoapException(String message) {
        super(message);
    }

    public MissingInstantSoapException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingInstantSoapException(Throwable cause) {
        super(cause);
    }
}
