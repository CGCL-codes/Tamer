package net.sourceforge.ondex.webservice2.Exceptions;

import org.apache.log4j.Logger;

/**
 *
 * @author Christian Brenninkmeijer
 */
public class RelationNotFoundException extends WebserviceException {

    /**
     * Constructs an instance of <code>GraphNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     * @param logger The logger of the calling class.
     */
    public RelationNotFoundException(String msg, Logger logger) {
        super(msg, logger);
    }
}
