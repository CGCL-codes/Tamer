package net.sourceforge.ondex.wsapi.exceptions;

import org.apache.log4j.Logger;

/**
 *
 * @author Christian Brenninkmeijer
 */
public class PluginNotFoundException extends WebserviceException {

    /**
     * Constructs an instance of <code>MappingNotFoundException</code>
     * with the specified detail message.
     * @param msg the detail message.
     * @param logger
     */
    public PluginNotFoundException(String msg, Logger logger) {
        super(msg, logger);
    }
}
