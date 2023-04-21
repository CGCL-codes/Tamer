package org.qctools4j.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

/**
 * TODO Insert class description
 *
 * @author usf02000
 *
 */
public class LoggerFactory {

    private static Log logger;

    public static synchronized void setOtherLog(final Log log) {
        if (logger == null) {
            logger = log;
        }
    }

    public static Log getLog(final Class clazz) throws LogConfigurationException {
        if (logger == null) {
            return LogFactory.getLog(clazz);
        } else {
            return logger;
        }
    }
}
