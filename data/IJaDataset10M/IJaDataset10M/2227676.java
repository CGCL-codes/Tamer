package uk.ac.ebi.intact.core.persistence.svc;

/**
 * TODO comment that class header
 *
 * @author Prem Anand (prem@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.1-SNAPSHOT
 */
public class DbInfoServiceException extends Exception {

    public DbInfoServiceException() {
        super();
    }

    public DbInfoServiceException(String message) {
        super(message);
    }

    public DbInfoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbInfoServiceException(Throwable cause) {
        super(cause);
    }
}
