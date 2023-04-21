package org.dicom4j.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Manage loggers
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class Logging {

    /**
	 * the root logger's name
	 */
    public static final String ROOT_LOGGER = "org.dicom4j";

    /**
	 * the network logger's name
	 */
    public static final String NETWORK_LOGGER = ROOT_LOGGER + ".network";

    Logging() {
        super();
    }

    public Logger getRootLogger() {
        return LogManager.getLogger("org.dicom4j");
    }

    @Override
    public String toString() {
        return "Logging";
    }
}
