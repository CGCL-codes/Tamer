package ie.omk.smpp;

/** InvalidListNameException
  * @author Oran Kelly
  * @version 1.0
  */
public class InvalidListNameException extends ie.omk.smpp.SMPPException {

    private String name = null;

    public InvalidListNameException() {
    }

    /** Construct a new InvalidListNameException with specified message.
      */
    public InvalidListNameException(String s) {
        super(s);
    }

    /** Construct a new InvalidListNameException with specified message.
      * @param s A detail message.
      * @param name The list name that caused this exception.
      */
    public InvalidListNameException(String s, String name) {
        super(s);
        this.name = name;
    }

    /** Get the list name that caused this exception.
      */
    public String getName() {
        return (this.name);
    }
}
