package fedora.server.errors;

/**
 *
 * <p><b>Title:</b> InconsistentTableSpecException.java</p>
 * <p><b>Description:</b> Thrown when a table specification is not internally
 * consistent.</p>
 *
 * @author cwilper@cs.cornell.edu
 * @version $Id: InconsistentTableSpecException.java 5162 2006-10-25 00:49:06Z eddie $
 */
public class InconsistentTableSpecException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an InconsistentTableSpecException
     *
     * @param message An informative message explaining what happened and
     *                (possibly) how to fix it.
     */
    public InconsistentTableSpecException(String message) {
        super(null, message, null, null, null);
    }

    public InconsistentTableSpecException(String bundleName, String code, String[] replacements, String[] details, Throwable cause) {
        super(bundleName, code, replacements, details, cause);
    }
}
