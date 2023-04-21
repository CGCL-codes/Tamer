package de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions;

/**
 * DiffException Describes an exception that occurred while calculating the
 * diff.
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class DiffException extends Exception {

    /**
	 * (Constructor) Creates a new DiffException.
	 * 
	 * @param description
	 *            message
	 */
    public DiffException(final String description) {
        super(description);
    }

    /**
	 * (Constructor) Creates a new DiffException.
	 * 
	 * @param e
	 *            inner exception
	 */
    public DiffException(final Exception e) {
        super(e);
    }

    /**
	 * (Constructor) Creates a new DiffException.
	 * 
	 * @param description
	 *            message
	 * @param e
	 *            inner exception
	 */
    public DiffException(final String description, final Exception e) {
        super(description, e);
    }
}
