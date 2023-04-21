package java.awt.color;

/**
  * Thrown when there is an error in the native CMM.
  *
  * @author Eric Blake (ebb9@email.byu.edu)
  * @status updated to 1.4
  */
public class CMMException extends RuntimeException {

    /**
   * Compatible with JDK 1.2+.
   */
    private static final long serialVersionUID = 5775558044142994965L;

    /**
   * Create a new instance with a specified detailed error message.
   *
   * @param message the message
   */
    public CMMException(String message) {
        super(message);
    }
}
