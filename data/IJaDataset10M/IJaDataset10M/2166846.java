package org.artags.site.service;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierre
 */
public class LogService {

    private static final Logger logger = Logger.getLogger("ARTags Server ");

    private static final LogService logService = new LogService();

    public static LogService getLogger() {
        return logService;
    }

    public void log(String message) {
        System.out.println(message);
        logger.log(Level.INFO, message);
    }

    public void log(String message, Object... params) {
        System.out.println(MessageFormat.format(message, params));
        logger.log(Level.INFO, message, params);
    }
}
