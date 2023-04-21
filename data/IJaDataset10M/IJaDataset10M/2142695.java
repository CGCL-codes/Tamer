package uk.ac.ebi.intact.util.biosource;

/**
 * Specific exception for this BioSourceService.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: BioSourceServiceException.java 7564 2007-02-20 17:27:16Z skerrien $
 * @since 1.0
 */
public class BioSourceServiceException extends Exception {

    public BioSourceServiceException() {
    }

    public BioSourceServiceException(Throwable cause) {
        super(cause);
    }

    public BioSourceServiceException(String message) {
        super(message);
    }

    public BioSourceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
