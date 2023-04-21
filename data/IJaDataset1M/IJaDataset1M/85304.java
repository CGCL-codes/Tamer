package uk.ac.ebi.intact.sanity.check;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: SanityCheckerException.java 9538 2007-08-22 11:14:30Z baranda $
 */
public class SanityCheckerException extends Exception {

    public SanityCheckerException() {
        super();
    }

    public SanityCheckerException(Throwable cause) {
        super(cause);
    }

    public SanityCheckerException(String message) {
        super(message);
    }

    public SanityCheckerException(String message, Throwable cause) {
        super(message, cause);
    }
}
