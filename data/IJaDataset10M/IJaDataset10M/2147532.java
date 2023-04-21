package fedora.server.errors;

/**
 *
 * <p><b>Title:</b> ObjectNotFoundException.java</p>
 * <p><b>Description:</b> Signals that an object could not be found.</p>
 *
 * @author cwilper@cs.cornell.edu
 * @version $Id: ObjectNotFoundException.java 5162 2006-10-25 00:49:06Z eddie $
 */
public class ObjectNotFoundException extends StorageException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an ObjectNotFoundException.
     *
     * @param message An informative message explaining what happened and
     *                (possibly) how to fix it.
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
