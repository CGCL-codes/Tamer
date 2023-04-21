package uk.ac.ebi.rhea.webapp.curator.controller.curation;

/**
 * Thrown when something goes wrong while commiting changes.
 * @author rafalcan
 *
 */
public class CommitException extends Exception {

    private static final long serialVersionUID = 9166135201924571592L;

    public CommitException() {
    }

    public CommitException(String message) {
        super(message);
    }

    public CommitException(Throwable cause) {
        super(cause);
    }

    public CommitException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        return (getCause() == null) ? super.getMessage() : super.getMessage() + ": " + getCause().getMessage();
    }
}
