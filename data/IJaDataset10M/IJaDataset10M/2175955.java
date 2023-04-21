package org.dwgsoftware.raistlin.logging.impl;

import org.apache.avalon.framework.logger.Logger;
import org.dwgsoftware.raistlin.util.criteria.CriteriaException;
import org.dwgsoftware.raistlin.util.criteria.Parameter;
import org.dwgsoftware.raistlin.util.i18n.ResourceManager;
import org.dwgsoftware.raistlin.util.i18n.Resources;

/**
 * A parameter descriptor that supports transformation of a 
 * a string to a Logger instance.
 * 
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $
 */
public class LoggerParameter extends Parameter {

    private static final Resources REZ = ResourceManager.getPackageResources(LoggerParameter.class);

    private static final int PRIORITY = ConsoleLogger.LEVEL_WARN;

    /**
    * Creation of a new logger parameter.  The parameter support
    * convertion of strings in the form "debug", "info", "warn", 
    * "error", "fatal" and "none" to an equivalent logger.
    *
    * @param key the parameter key
    * @param logger the default logger
    */
    public LoggerParameter(final String key, final Logger logger) {
        super(key, Logger.class, logger);
    }

    /**
    * Resolve a supplied string to a configuration
    * @param value the value to resolve
    * @exception CriteriaException if an error occurs
    */
    public Object resolve(Object value) throws CriteriaException {
        if (value == null) {
            return new ConsoleLogger(PRIORITY);
        }
        if (value instanceof Logger) {
            return value;
        }
        if (value instanceof String) {
            String priority = ((String) value).toLowerCase();
            if (priority.equals("debug")) {
                return new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG);
            } else if (priority.equals("info")) {
                return new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
            } else if (priority.equals("warn")) {
                return new ConsoleLogger(ConsoleLogger.LEVEL_WARN);
            } else if (priority.equals("error")) {
                return new ConsoleLogger(ConsoleLogger.LEVEL_ERROR);
            } else if (priority.equals("fatal")) {
                return new ConsoleLogger(ConsoleLogger.LEVEL_FATAL);
            } else if (priority.equals("none")) {
                return new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);
            }
        }
        final String error = REZ.getString("parameter.unknown", value.getClass().getName(), Logger.class.getName());
        throw new CriteriaException(error);
    }
}
