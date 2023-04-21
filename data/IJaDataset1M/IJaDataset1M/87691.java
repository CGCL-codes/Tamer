package org.xaware.shared.util.logging;

import java.util.ResourceBundle;

public interface XALogEvent {

    /**
     * Get the source Logger name's
     * 
     * @return source logger name (may be null)
     */
    String getLoggerName();

    /**
     * Get the localization resource bundle
     * <p>
     * This is the ResourceBundle that should be used to localize the message string before formatting it. The result
     * may be null if the message is not localizable, or if no suitable ResourceBundle is available.
     */
    ResourceBundle getResourceBundle();

    /**
     * Get the logging message level, for example Level.SEVERE.
     * 
     * @return the logging message level
     */
    String getLevel();

    /**
     * Get the "raw" log message, before localization or formatting.
     * <p>
     * May be null, which is equivalent to the empty string "".
     * <p>
     * This message may be either the final text or a localization key.
     * <p>
     * During formatting, if the source logger has a localization ResourceBundle and if that ResourceBundle has an entry
     * for this message string, then the message string is replaced with the localized value.
     * 
     * @return the raw message string
     */
    String getMessage();

    /**
     * Get event time in milliseconds since 1970.
     * 
     * @return event time in millis since 1970
     */
    long getMillis();

    /**
     * Get any throwable associated with the log record.
     * <p>
     * If the event involved an exception, this will be the exception object. Otherwise null.
     * 
     * @return a throwable
     */
    Throwable getThrown();

    /**
     * Get the localization resource bundle name
     * <p>
     * This is the name for the ResourceBundle that should be used to localize the message string before formatting it.
     * The result may be null if the message is not localizable.
     */
    String getResourceBundleName();

    /**
     * Get an identifier for the thread where the message originated.
     * <p>
     * This is a thread identifier within the Java VM and may or may not map to any operating system ID.
     * 
     * @return thread ID
     */
    String getThreadID();

    /**
     * Get the name of the class that (allegedly) issued the logging request.
     * <p>
     * Note that this sourceClassName is not verified and may be spoofed. This information may either have been provided
     * as part of the logging call, or it may have been inferred automatically by the logging framework. In the latter
     * case, the information may only be approximate and may in fact describe an earlier call on the stack frame.
     * <p>
     * May be null if no information could be obtained.
     * 
     * @return the source class name
     */
    String getSourceClassName();

    /**
     * Get the name of the method that (allegedly) issued the logging request.
     * <p>
     * Note that this sourceMethodName is not verified and may be spoofed. This information may either have been
     * provided as part of the logging call, or it may have been inferred automatically by the logging framework. In the
     * latter case, the information may only be approximate and may in fact describe an earlier call on the stack frame.
     * <p>
     * May be null if no information could be obtained.
     * 
     * @return the source method name
     */
    String getSourceMethodName();

    /**
     * Get the parameters to the log message.
     * 
     * @return the log message parameters. May be null if there are no parameters.
     */
    Object[] getParameters();
}
