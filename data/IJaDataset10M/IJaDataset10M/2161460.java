package com.telstra.ess.logging.spi.log4j;

import org.apache.log4j.Logger;
import com.telstra.ess.*;
import com.telstra.ess.logging.*;

/**
 * Defined as a wrapper for custom-configured Log4j Logger instances.
 * 
 * @author Greg
 * @since v1.2 of Ess Services API
 * @see org.apache.log4j.Logger
 */
public class Log4jLogger extends Logger implements EssLogger {

    private static Log4jLoggerFactory factory = new Log4jLoggerFactory();

    /**
	 * Default constructor
	 * 
	 * @param arg0 the Logger's category name
	 */
    public Log4jLogger(String arg0) {
        super(arg0);
    }

    /**
	 * Specialized implementation of {@link org.apache.log4j.Logger#getLogger(java.lang.String).
	 * Uses an instance of {@link Log4jLoggerFactory} to create a Logger
	 * 
	 * @param 
	 */
    public static Logger getLogger(String name) {
        return Logger.getLogger(name, factory);
    }

    /**
	 * Implemented from {@link AphisLogger#getOwningComponent()}
	 * 
	 * Currently does nothing with the component. Detailed log message information derived via log4j itself
	 */
    public EssComponent getOwningComponent() {
        return null;
    }

    /**
	 * Implemented from {@link AphisLogger#setOwningComponent(AphisOriginator)}
	 * 
	 * Currently does nothing with the component. Detailed log message information derived via log4j itself
	 */
    public void setOwningComponent(EssComponent c) {
        ;
    }

    public boolean evictable() {
        return true;
    }

    public void debug(Object arg0, Throwable arg1) {
        super.debug(arg0, arg1);
    }

    public void debug(Object arg0) {
        super.debug(arg0);
    }

    public void error(Object arg0, Throwable arg1) {
        super.error(arg0, arg1);
    }

    public void error(Object arg0) {
        super.error(arg0);
    }

    public void fatal(Object arg0, Throwable arg1) {
        super.fatal(arg0, arg1);
    }

    public void fatal(Object arg0) {
        super.fatal(arg0);
    }

    public void info(Object arg0, Throwable arg1) {
        super.info(arg0, arg1);
    }

    public void info(Object arg0) {
        super.info(arg0);
    }
}
