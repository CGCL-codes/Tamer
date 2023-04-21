package cbdp.server.contextreasoner.model.exceptions;

/**
 * Thrown by a <code>Instance</code> when a property can not be found in the ontology.
 * @author Xabier Laiseca (University of Deusto)
 *
 */
public class NotExistingPropertyInstanceException extends Exception {

    private static final long serialVersionUID = -1471994760546717645L;

    public NotExistingPropertyInstanceException(String message) {
        super(message);
    }

    public NotExistingPropertyInstanceException(String message, Throwable t) {
        super(message, t);
    }
}
