package pl.edu.pjwstk.mteam.jcsync.exception;

/**
 * Throws when the application is trying to create new shared object in the overlay
 * (or Topic) but the object with the same name is already defined in the overlay.
 * @author Piotr Bucior
 */
public class ObjectExistsException extends Exception {

    private static final ObjectExistsException instance = new ObjectExistsException();

    private ObjectExistsException() {
        super("Object already exists in the overlay");
    }

    public static ObjectExistsException instance() {
        return instance;
    }
}
