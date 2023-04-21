package uk.ac.manchester.cs.snee.metadata.units;

public class UnrecognizedUnitException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6379394857758510123L;

    /**
	 * @param message
	 */
    public UnrecognizedUnitException(final String message) {
        super(message);
    }
}
