package net.sourceforge.ondex.wsapi.exceptions;

import org.apache.log4j.Logger;

/**
 *
 * @author Christian Brenninkmeijer
 */
public class IllegalClassException extends WebserviceException {

    /**
     * Constructs an instance of <code>GraphNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     * @param logger The logger of the calling class.
     */
    public IllegalClassException(String msg, Logger logger) {
        super(msg, logger);
    }
}
