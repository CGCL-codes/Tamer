package org.lightcommons.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

/**
 * Provides basic formatting for log messages.
 * <p>
 * This class extends the <code>java.util.logging.Formatter</code> class,
 * allowing it to be specified as a formatter for the
 * <code>java.util.logging</code> system.
 * <p>
 * The static {@link #format(String level, String message, String loggerName)}
 * method provides a means of using the same formatting outside of the
 * <code>java.util.logging</code> framework. See the documentation of this
 * method for more details.
 */
public class BasicLogFormatter extends Formatter {

    /**
     * Determines whether the <a href="Logger.html#LoggingLevel">logging level</a>
     * is included in the output.
     * <p>
     * The default value is <code>true</code>.
     * <p>
     * As this is a static property, changing the value will affect all
     * <code>BasicLogFormatter</code> instances, as well as the behaviour of
     * the static
     * {@link #format(String level, String message, String loggerName)} method.
     */
    public static boolean OutputLevel = true;

    /**
     * Determines whether the logger name is included in the output.
     * <p>
     * The default value is <code>false</code>.
     * <p>
     * The logger name used for all automatically created {@link Log}
     * instances is "<code>net.htmlparser.jericho</code>".
     * <p>
     * As this is a static property, changing the value will affect all
     * <code>BasicLogFormatter</code> instances, as well as the behaviour of
     * the static
     * {@link #format(String level, String message, String loggerName)} method.
     */
    public static boolean OutputName = false;

    static final Formatter INSTANCE = new BasicLogFormatter();

    /**
     * Returns a formatted string representing the log entry information
     * contained in the specified <code>java.util.logging.LogRecord</code>.
     * <p>
     * This method is not called directly, but is used by the
     * <code>java.util.logging</code> framework when this class is specified
     * as a formatter in the <code>logging.properties</code> file.
     * <p>
     * See the documentation of the parent
     * <code>java.util.logging.Formatter</code> class in the Java SDK for more
     * details.
     *
     * @param logRecord
     *            a <code>java.util.logging.LogRecord</code> object containing
     *            all of the log entry information.
     * @return a formatted string representing the log entry information
     *         contained in the specified
     *         <code>java.util.logging.LogRecord</code>.
     */
    @Override
    public String format(final LogRecord logRecord) {
        return format(logRecord.getLevel().getName(), logRecord.getMessage(), logRecord.getLoggerName());
    }

    /**
     * Returns a formatted string representing the specified log entry
     * information.
     * <p>
     * This method is used by the {@link WriterLogger} class to format its
     * output.
     * <p>
     * The static properties {@link #OutputLevel} and {@link #OutputName} affect
     * what information is included in the output.
     * <p>
     * The {@link Config#NewLine} static property determines the character
     * sequence used for line breaks.
     * <p>
     * A line of output typically looks like this: <blockquote
     * class="SmallVerticalMargin"><code>INFO: this is the log message</code></blockquote>
     * or if the {@link #OutputName} property is set to <code>true</code>,
     * the output would look similar to this: <blockquote
     * class="SmallVerticalMargin"><code>INFO: [net.htmlparser.jericho] this is the log message</code></blockquote>
     *
     * @param level
     *            a string representing the <a
     *            href="Logger.html#LoggingLevel">logging level</a> of the log
     *            entry.
     * @param message
     *            the log message.
     * @param loggerName
     *            the name of the logger.
     * @return a formatted string representing the specified log entry
     *         information.
     */
    public static String format(final String level, final String message, final String loggerName) {
        final StringBuffer sb = new StringBuffer(message == null ? 0 : message.length() + 40);
        if (OutputLevel) {
            sb.append(level).append(": ");
        }
        if (OutputName && (loggerName != null)) {
            sb.append('[').append(loggerName).append("] ");
        }
        sb.append(message);
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Returns a String object to be log.
     * @param message any type object to log.
     * @return String message to log
     */
    public static String toMessage(Object message) {
        if (message instanceof String) return (String) message;
        if (message instanceof Throwable) return ((Throwable) message).getMessage();
        return message.toString();
    }

    public static String toTraceMessage(Object message) {
        if (message instanceof String) return (String) message;
        if (message instanceof Throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ((Throwable) message).printStackTrace(pw);
            pw.flush();
            String ret = sw.toString();
            pw.close();
            return ret;
        }
        return message.toString();
    }

    public static String toMessage(Object message, Throwable throwable) {
        return toMessage(message) + "\nCause by:\n" + toMessage(throwable);
    }

    public static String toTraceMessage(Object message, Throwable throwable) {
        return toTraceMessage(message) + "\nCause by:\n" + toTraceMessage(throwable);
    }
}
