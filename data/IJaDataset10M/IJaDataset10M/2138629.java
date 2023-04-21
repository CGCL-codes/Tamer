package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.logging.Log;

/**
 * <p>Implementation of the <code>org.apache.commons.logging.Log</code>
 * interface that wraps the standard JDK logging mechanisms that are
 * available in SourceForge's Lumberjack for JDKs prior to 1.4.</p>
 *
 * @author <a href="mailto:sanders@apache.org">Scott Sanders</a>
 * @author <a href="mailto:bloritsch@apache.org">Berin Loritsch</a>
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 * @author <a href="mailto:vince256@comcast.net">Vince Eagen</a>
 * @version $Revision: 1.6 $ $Date: 2004/06/06 21:13:43 $
 */
public class Jdk13LumberjackLogger implements Log, Serializable {

    /**
     * The underlying Logger implementation we are using.
     */
    protected transient Logger logger = null;

    protected String name = null;

    private String sourceClassName = "unknown";

    private String sourceMethodName = "unknown";

    private boolean classAndMethodFound = false;

    /**
     * Construct a named instance of this Logger.
     *
     * @param name Name of the logger to be constructed
     */
    public Jdk13LumberjackLogger(String name) {
        this.name = name;
        logger = getLogger();
    }

    private void log(Level level, String msg, Throwable ex) {
        if (getLogger().isLoggable(level)) {
            LogRecord record = new LogRecord(level, msg);
            if (!classAndMethodFound) {
                getClassAndMethod();
            }
            record.setSourceClassName(sourceClassName);
            record.setSourceMethodName(sourceMethodName);
            if (ex != null) {
                record.setThrown(ex);
            }
            getLogger().log(record);
        }
    }

    /**
     * <p>Gets the class and method by looking at the stack trace for the
     * first entry that is not this class.</p>
     */
    private void getClassAndMethod() {
        try {
            Throwable throwable = new Throwable();
            throwable.fillInStackTrace();
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            String traceString = stringWriter.getBuffer().toString();
            StringTokenizer tokenizer = new StringTokenizer(traceString, "\n");
            tokenizer.nextToken();
            String line = tokenizer.nextToken();
            while (line.indexOf(this.getClass().getName()) == -1) {
                line = tokenizer.nextToken();
            }
            while (line.indexOf(this.getClass().getName()) >= 0) {
                line = tokenizer.nextToken();
            }
            int start = line.indexOf("at ") + 3;
            int end = line.indexOf('(');
            String temp = line.substring(start, end);
            int lastPeriod = temp.lastIndexOf('.');
            sourceClassName = temp.substring(0, lastPeriod);
            sourceMethodName = temp.substring(lastPeriod + 1);
        } catch (Exception ex) {
        }
        classAndMethodFound = true;
    }

    /**
     * Log a message with debug log level.
     */
    public void debug(Object message) {
        log(Level.FINE, String.valueOf(message), null);
    }

    /**
     * Log a message and exception with debug log level.
     */
    public void debug(Object message, Throwable exception) {
        log(Level.FINE, String.valueOf(message), exception);
    }

    /**
     * Log a message with error log level.
     */
    public void error(Object message) {
        log(Level.SEVERE, String.valueOf(message), null);
    }

    /**
     * Log a message and exception with error log level.
     */
    public void error(Object message, Throwable exception) {
        log(Level.SEVERE, String.valueOf(message), exception);
    }

    /**
     * Log a message with fatal log level.
     */
    public void fatal(Object message) {
        log(Level.SEVERE, String.valueOf(message), null);
    }

    /**
     * Log a message and exception with fatal log level.
     */
    public void fatal(Object message, Throwable exception) {
        log(Level.SEVERE, String.valueOf(message), exception);
    }

    /**
     * Return the native Logger instance we are using.
     */
    public Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(name);
        }
        return (logger);
    }

    /**
     * Log a message with info log level.
     */
    public void info(Object message) {
        log(Level.INFO, String.valueOf(message), null);
    }

    /**
     * Log a message and exception with info log level.
     */
    public void info(Object message, Throwable exception) {
        log(Level.INFO, String.valueOf(message), exception);
    }

    /**
     * Is debug logging currently enabled?
     */
    public boolean isDebugEnabled() {
        return (getLogger().isLoggable(Level.FINE));
    }

    /**
     * Is error logging currently enabled?
     */
    public boolean isErrorEnabled() {
        return (getLogger().isLoggable(Level.SEVERE));
    }

    /**
     * Is fatal logging currently enabled?
     */
    public boolean isFatalEnabled() {
        return (getLogger().isLoggable(Level.SEVERE));
    }

    /**
     * Is info logging currently enabled?
     */
    public boolean isInfoEnabled() {
        return (getLogger().isLoggable(Level.INFO));
    }

    /**
     * Is trace logging currently enabled?
     */
    public boolean isTraceEnabled() {
        return (getLogger().isLoggable(Level.FINEST));
    }

    /**
     * Is warn logging currently enabled?
     */
    public boolean isWarnEnabled() {
        return (getLogger().isLoggable(Level.WARNING));
    }

    /**
     * Log a message with trace log level.
     */
    public void trace(Object message) {
        log(Level.FINEST, String.valueOf(message), null);
    }

    /**
     * Log a message and exception with trace log level.
     */
    public void trace(Object message, Throwable exception) {
        log(Level.FINEST, String.valueOf(message), exception);
    }

    /**
     * Log a message with warn log level.
     */
    public void warn(Object message) {
        log(Level.WARNING, String.valueOf(message), null);
    }

    /**
     * Log a message and exception with warn log level.
     */
    public void warn(Object message, Throwable exception) {
        log(Level.WARNING, String.valueOf(message), exception);
    }
}
