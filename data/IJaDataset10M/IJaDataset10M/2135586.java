package de.vsis.coordination.coordinator.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Logger factory. Manages the logging, initiates the logger.
 * 
 * @author Michael von Riegen, Stefan Fink
 */
public class LoggerFactory {

    private static final LoggerFactory _instance;

    static {
        try {
            _instance = new LoggerFactory();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Logger logger;

    /**
	 * Constructor. Set to private in order to implement a singleton
	 */
    private LoggerFactory() {
        this.logger = Logger.getLogger("de.vsis.coordination");
        String level = PropertiesFactory.getInstance().getProperty("log.level", "INFO");
        if (level.equals("INFO")) {
            this.logger.setLevel((Level) Level.INFO);
        } else if (level.equals("DEBUG")) {
            this.logger.setLevel((Level) Level.DEBUG);
        } else if (level.equals("WARN")) {
            this.logger.setLevel((Level) Level.WARN);
        } else if (level.equals("ERROR")) {
            this.logger.setLevel((Level) Level.ERROR);
        } else if (level.equals("FATAL")) {
            this.logger.setLevel((Level) Level.FATAL);
        } else {
            this.logger.setLevel((Level) Level.INFO);
        }
    }

    /**
	 * Returns an instance of the logger
	 * 
	 * @return LoggerFactory
	 */
    public static LoggerFactory getInstance() {
        return _instance;
    }

    /**
	 * Returns a logger
	 * 
	 * @return logger
	 */
    public Logger getLogger() {
        return this.logger;
    }
}
