package uk.ac.warwick.dcs.cokefolk.util.datastructures;

/**
 * This exception is thrown by the system when an object is required to exist in a
 * {@link java.util.Collection} for successful completion of an operation but was not found. It
 * serves a similar purpose to {@link java.util.NoSuchElementException} but unlike that class
 * does not extend {@link RuntimeException}, so therefore must be caught by the calling method
 * wherever the possibility of an unsuccessful search occurs and this class is used.
 * @author Adrian
 * @designer Adrian
 */
public class ElementNotFoundException extends Exception {

    /** Constructs an {@code ElementNotFoundException} with no detail message. */
    public ElementNotFoundException() {
        super();
    }

    /**
   * Constructs an {@code ElementNotFoundException} with the specified detail message.
   * @param message
   * the detail message.
   */
    public ElementNotFoundException(String message) {
        super(message);
    }
}
