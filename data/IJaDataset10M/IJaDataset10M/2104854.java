package tudresden.ocl20.logging.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import tudresden.ocl20.logging.LoggingPlugin;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class is a custom log4j appender that sends log4j events to the Eclipse Error Log.
 * By default, the {@link ILog} of the {@link LoggingPlugin} is used. Plugins may, however,
 * set their own log by calling {@link #setLog(ILog)}. If the {@link LoggingPlugin} has not been
 * activated (e.g. if Eclipse is not running), the <code>ErrorLogAppender</code> will issue
 * internal warning messages using the log4j error handling mechanism.
 * 
 * @author Manoel Marques, adapted by Matthias Braeuer
 */
public class ErrorLogAppender extends AppenderSkeleton {

    private ILog pluginLog;

    private String pluginName;

    /**
   * Creates a new <code>ErrorLogAppender</code>
   */
    public ErrorLogAppender() {
        super();
        Plugin loggingPlugin = LoggingPlugin.getDefault();
        if (loggingPlugin != null) {
            pluginLog = LoggingPlugin.getDefault().getLog();
            pluginName = LoggingPlugin.ID;
        }
    }

    /**
   * Sets the Eclipse log instance
   * 
   * @param log plug-in log
   */
    public void setLog(ILog pluginLog) {
        if (pluginLog == null) {
            throw new IllegalArgumentException("The parameter 'pluginLog' was null.");
        }
        this.pluginLog = pluginLog;
        pluginName = pluginLog.getBundle().getSymbolicName();
    }

    /**
   * Logs a logging event. The log4j log level is translated to the Eclipse {@link IStatus} levels
   * as follows:
   * 
   * <ul>
   * <li>{@link Level#FATAL} => {@link IStatus#CANCEL}
   * <li>{@link Level#ERROR} => {@link IStatus#ERROR}
   * <li>{@link Level#WARN} => {@link IStatus#WARNING}
   * <li>{@link Level#INFO} => {@link IStatus#INFO}
   * <li>default => {@link IStatus#OK}
   * 
   * @param event the <code>LoggingEvent</code> instance
   */
    @Override
    public void append(LoggingEvent event) {
        ThrowableInformation throwableInformation;
        Throwable throwable;
        int level, severity;
        if (!canAppend()) {
            return;
        }
        level = event.getLevel().toInt();
        if (level == Priority.OFF_INT) {
            return;
        }
        throwableInformation = event.getThrowableInformation();
        throwable = (throwableInformation != null) ? throwableInformation.getThrowable() : null;
        switch(level) {
            case Priority.FATAL_INT:
                severity = IStatus.CANCEL;
                break;
            case Priority.ERROR_INT:
                severity = IStatus.ERROR;
                break;
            case Priority.WARN_INT:
                severity = IStatus.WARNING;
                break;
            case Priority.INFO_INT:
                severity = IStatus.INFO;
                break;
            default:
                severity = IStatus.OK;
        }
        pluginLog.log(new Status(severity, pluginName, level, layout.format(event), throwable));
    }

    /**
   * This method determines if there is a sense in attempting to append. It checks whether a
   * 
   * <p>
   * It checks whether the appender has already been {@link #close() closed}and whether there is a
   * {@link #setLayout(org.apache.log4j.Layout) layout} set for the appender. If these checks fail,
   * <code>false</code> is returned. Subclasses may override to provide additional checks,
   * </p>
   * 
   * @return <code>true</code> if the conditions for appending a log event are met,
   *         <code>false</code> otherwise
   */
    protected boolean canAppend() {
        if (closed) {
            LogLog.warn("Tried to write to closed appender [" + name + "].");
            return false;
        }
        if (layout == null) {
            errorHandler.error("Missing layout for appender [" + name + "].", null, ErrorCode.MISSING_LAYOUT);
            return false;
        }
        if (pluginLog == null) {
            errorHandler.error("No plugin log set for appender [" + name + "].", null, ErrorCode.GENERIC_FAILURE);
            return false;
        }
        return true;
    }

    /**
   * Closes this appender
   */
    @Override
    public void close() {
        closed = true;
    }

    /**
   * Checks if this appender requires layout
   * 
   * @return true if layout is required.
   */
    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("pluginName", pluginName).toString();
    }
}
