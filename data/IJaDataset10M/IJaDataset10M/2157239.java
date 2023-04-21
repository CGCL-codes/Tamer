package uk.org.ogsadai.performance.util;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import uk.org.ogsadai.performance.exception.ConfigurationException;

/**
 * Factor class that returns the logging object.
 * <p>
 * This class requires the following parameters to be defined.
 * </p>
 * <ul>
 * <li>
 * <code>log4j_config</code>: configuration file for logging, must be XML file.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class LoggerFactory {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009";

    /** Loggging configuration file property key. */
    public static final String LOGCONFIG_PROPERTY = "log4j_config";

    /** Logger object. */
    private static Logger mLogger;

    /**
     * Returns the logger object.
     * 
     * @return logger object
     */
    public static Logger getLogger() {
        if (mLogger == null) {
            throw new IllegalArgumentException("Logger object is null. " + "Call init() first to initialize the logger.");
        }
        return mLogger;
    }

    /**
     * Initializes the logger object.
     * 
     * @param name
     * @throws ConfigurationException
     */
    public static void init(String name) throws ConfigurationException {
        if (mLogger == null) {
            mLogger = Logger.getLogger(name);
            ConfigReader configReader = ConfigReaderFactory.getConfigReader();
            String configFile = configReader.getStringProperty(LOGCONFIG_PROPERTY);
            DOMConfigurator.configure(configFile);
        }
    }
}
