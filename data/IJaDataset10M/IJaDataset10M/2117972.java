package fedora.server.errors;

/**
 *
 * <p><b>Title: </b>DisseminationException.java</p>
 * <p><b>Description: </b>Signals an error in processing a dissemination request.</p>
 *
 * @author rlw@virginia.edu
 * @version $Id: DisseminationException.java 5162 2006-10-25 00:49:06Z eddie $
 */
public class DisseminationException extends ServerException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a DisseminationException.
     *
     * @param message An informative message explaining what happened and
     *                (possibly) how to fix it.
     */
    public DisseminationException(String message) {
        super(null, message, null, null, null);
    }

    public DisseminationException(String bundleName, String code, String[] values, String[] details, Throwable cause) {
        super(bundleName, code, values, details, cause);
    }
}
