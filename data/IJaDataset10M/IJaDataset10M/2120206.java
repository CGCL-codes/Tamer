package uk.ac.ebi.intact.irefindex.seguid;

/**
 * Custom Exception class to handle exceptions from this module
 *
 * @author Prem Anand (prem@ebi.ac.uk)
 * @version $Id$
 * @since TODO specify the maven artifact version
 */
public class SeguidException extends Exception {

    public SeguidException() {
        super();
    }

    public SeguidException(String message) {
        super(message);
    }

    public SeguidException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeguidException(Throwable cause) {
        super(cause);
    }
}
