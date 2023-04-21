package org.arch4j.logging.log4j;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.LogManager;
import org.arch4j.core.EnvironmentalException;
import org.arch4j.logging.LoggingCategory;
import org.arch4j.logging.LoggingConstants;
import org.arch4j.logging.LoggingFramework;
import org.arch4j.property.PropertyProvider;
import java.io.File;
import java.net.URL;

/**
 * <p> This class wraps the log4j logging facility.
 *
 * @author Ross E. Greinke
 * @version 1.0
 */
public class Log4JLoggingFramework implements LoggingFramework, LoggingConstants {

    public static final String LOG4J_FILE_NAME_PROPERTY = "framework.log4j.filename";

    private static final String LOG4J_FILE_NAME = "log4j.properties";

    /**
   * Creates a new <code>Log4JLoggingFramework</code> instance
   * and gives it a default configuration.
   */
    public Log4JLoggingFramework() {
        super();
        BasicConfigurator.configure();
    }

    /**
   * Retrieve a logging category with name <code>name</code>.
   * @param name The name of the logging category to retrieve.
   * @return The logging category to be used for logging.
   */
    public LoggingCategory getCategory(String name) {
        return new Log4JLoggingCategory(name);
    }

    /** Configure the logging framework. */
    public void configure() {
        File propertyDirectory = PropertyProvider.getProvider().getPropertyManager().getPropertyDirectory();
        String propertyFile = getFileName();
        File configFile = new File(propertyDirectory, propertyFile);
        if (configFile.exists()) {
            LogManager.resetConfiguration();
            PropertyConfigurator.configure(configFile.getAbsolutePath());
            return;
        }
        URL url = getClass().getClassLoader().getResource(propertyFile);
        if (url != null) {
            LogManager.resetConfiguration();
            PropertyConfigurator.configure(url);
            return;
        }
        throw new EnvironmentalException("Unable to find property file [" + propertyFile + "] in either Property dir [" + propertyDirectory.getPath() + "] or classapth");
    }

    private String getFileName() {
        return (PropertyProvider.getProvider().getPropertyManager().getProperty(LOGGING_DOMAIN, LOG4J_FILE_NAME_PROPERTY, LOG4J_FILE_NAME));
    }
}
