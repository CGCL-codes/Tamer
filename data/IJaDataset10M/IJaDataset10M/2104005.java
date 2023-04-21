package net.sf.genethello.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * A logger to log error messages centrally through out all classes.
 *
 * @author praz
 * @since   0.2
 */
public abstract class Logger {

    private static final Logger defLogger = new DefaultLogger();

    /**
     * Gets logger
     *
     * @return  logger
     */
    public abstract java.util.logging.Logger getLogger();

    /**
     * Gets default logger.
     * 
     * @return  default logger
     */
    public static Logger getDefault() {
        return defLogger;
    }

    private static class DefaultLogger extends Logger {

        private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

        /**
         * Creates instance of default logger.
         */
        public DefaultLogger() {
            try {
                logger.addHandler(new FileHandler("error.log"));
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Gets logger
         *
         * @return  logger
         */
        public java.util.logging.Logger getLogger() {
            return logger;
        }
    }
}
