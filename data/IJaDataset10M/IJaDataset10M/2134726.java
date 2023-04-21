package uk.ac.ebi.intact.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This error will be thrown if an error occurs during Intact initialization
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: IntactInitializationError.java 7540 2007-02-19 10:30:57Z skerrien $
 * @since <pre>04-Sep-2006</pre>
 */
public class IntactInitializationError extends Error {

    private static final Log log = LogFactory.getLog(IntactInitializationError.class);

    public IntactInitializationError() {
        super();
    }

    public IntactInitializationError(String message) {
        super(message);
    }

    public IntactInitializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public IntactInitializationError(Throwable cause) {
        super(cause);
    }
}
