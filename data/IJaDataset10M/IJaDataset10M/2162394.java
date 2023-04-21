package uk.ac.ebi.intact.bridges.blast;

/**
 * TODO comment this ... someday
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version
 * @since <pre>18 Sep 2007</pre>
 */
public class BlastStrategyException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public BlastStrategyException() {
    }

    public BlastStrategyException(Throwable cause) {
        super(cause);
    }

    public BlastStrategyException(String message) {
        super(message);
    }

    public BlastStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
}
