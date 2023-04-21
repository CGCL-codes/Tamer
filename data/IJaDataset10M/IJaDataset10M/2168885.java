package bd.com.escenic.flexilunch.util;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 * $Id: Logger.java 13 2009-05-22 03:05:02Z shihab.uddin@gmail.com $.
 *
 * @author <a href="mailto:shihab.uddin@gmail.com">Shihab Uddin</a>
 * @version $Revision: 13 $
 */
public class Logger extends org.apache.log4j.Logger {

    private static final LoggerFactory FACTORY = new LoggerFactory();

    protected Logger(final String pName) {
        super(pName);
    }

    public static Logger getLogger(final Object pObject) {
        return getLogger(pObject.getClass());
    }

    public static Logger getLogger(final Class pClass) {
        return getLogger(pClass.getName());
    }

    public static Logger getLogger(final String pName) {
        return (Logger) org.apache.log4j.Logger.getLogger(pName, FACTORY);
    }

    @Override
    protected void forcedLog(final String pFqcn, final Priority pLevel, final Object pMessage, final Throwable pThrowable) {
        if (Level.DEBUG.isGreaterOrEqual(pLevel)) {
            super.forcedLog(pFqcn, pLevel, pMessage, pThrowable);
        } else {
            super.forcedLog(pFqcn, pLevel, pMessage, null);
            super.forcedLog(pFqcn, Level.DEBUG, pMessage, pThrowable);
        }
    }

    private static class LoggerFactory implements org.apache.log4j.spi.LoggerFactory {

        @Override
        public org.apache.log4j.Logger makeNewLoggerInstance(final String pName) {
            return new Logger(pName);
        }
    }
}
