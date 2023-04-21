package org.plazmaforge.framework.platform;

import org.apache.log4j.Logger;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.core.exception.ServiceNotFoundException;
import org.plazmaforge.framework.platform.service.ServiceFactory;
import org.plazmaforge.framework.util.ClassUtils;

/**
 * The Environment
 * 
 * @author Oleh Hapon
 */
public class Environment {

    public static final String LOG_LINE = "******************************************************************************";

    protected static void logInfo(Logger logger, Object message, boolean isSystemOut) {
        if (logger == null || message == null) {
            return;
        }
        logger.info(message);
        if (isSystemOut) {
            System.out.println(message);
        }
    }

    protected static void logWarn(Logger logger, Object message) {
        if (logger == null || message == null) {
            return;
        }
        logger.warn(message);
    }

    protected static void logError(Logger logger, Object message) {
        if (logger == null || message == null) {
            return;
        }
        logger.error(message);
    }

    protected static void logError(Logger logger, Object message, Throwable t) {
        if (logger == null || message == null) {
            return;
        }
        logger.error(message, t);
    }

    protected static void logInfo(Logger logger, Object message) {
        logInfo(logger, message, true);
    }

    /**
     * Return service by class
     * @param serviceClass
     * @return
     */
    public static Object getService(Class serviceClass) {
        try {
            return ServiceFactory.getService(serviceClass);
        } catch (ServiceNotFoundException e) {
            return null;
        }
    }

    /**
     * Return service by class name
     * @param serviceClassName
     * @return
     */
    public static Object getServiceByClass(String serviceClassName) {
        try {
            return ServiceFactory.getServiceByClass(serviceClassName);
        } catch (ServiceNotFoundException e) {
            return null;
        }
    }

    /**
     * Return service by system property
     * @param propertyName
     * @return
     */
    public static Object getServiceByProperty(String propertyName) {
        String serviceClassName = PlatformEnvironment.getProperty(propertyName);
        return getServiceByClass(serviceClassName);
    }

    public static Class getClassByProperty(String propertyName) throws ApplicationException {
        String className = PlatformEnvironment.getProperty(propertyName);
        return ClassUtils.getClass(className);
    }

    public static Object getInstanceByProperty(String propertyName) throws ApplicationException {
        Class klass = getClassByProperty(propertyName);
        return ClassUtils.getClassInstance(klass);
    }
}
