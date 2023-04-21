package ie.omk.smpp;

/** InvalidTONException
  * @author Oran Kelly
  * @version 1.0
  */
public class InvalidTONException extends ie.omk.smpp.SMPPException {

    public InvalidTONException() {
    }

    /** Construct a new InvalidTONException with specified message.
      */
    public InvalidTONException(String s) {
        super(s);
    }
}
