package org.jlf.logging;

import java.util.logging.*;

/**
 * This class provides a timer utility to instrument your application.
 * When you create a new instance of this class, it starts a timer
 * before returning.  Then do some work in your application and when
 * you want to stop the timer and output the result to the logger,
 * call one of the logTime() methods.
 *
 * @author Todd Lauinger
 *
 * @version $Revision: 1.2 $
 *
 * @see AppLogger
 * @see AppError
 */
public class AppInstrument {

    /**
     * Holds the <code>String</code> which will be output to
     * the logger as a prefix to the time it takes to perform
     * the actions the instrumented time.
     */
    protected String instrumentedEventName;

    /**
     * Holds the <code>String</code> which will be output to
     * the logger as an instrumentation message.
     */
    protected String instrumentedEventMessage;

    /**
     * Holds the <code>Logger</code> you will output the time to.
     */
    protected Logger outputLogger;

    /**
     * Holds a time out value in milliseconds.  If this value
     * is a positive number, the timer is checked to see if
     * it exceeds this value.  If so, instead of using
     * outputLoggingLevel as a logging level, the instrument
     * will use outputAlertLoggingLevel.
     */
    protected long alertTimeOutMilliseconds;

    /**
     * Holds the logging level for the Log when writing
     * the logger entry out.
     */
    protected Level outputLoggingLevel;

    /**
     * Holds the alert logging level for the Log when
     * an instrumented event goes beyond its time out
     * value.
     */
    protected Level outputAlertLoggingLevel;

    /**
	 * Holds the start time from when the instance was
	 * created.
	 */
    protected long startTime;

    /**
     * Constructor with now inputs, uses default settings
     * for all information.
     */
    public AppInstrument() {
        this("");
    }

    /**
     * Constructor which accepts the name of the event
     * you are going to instrument.  Uses defaults
     * for all other values.
     */
    public AppInstrument(String instrumentedEventName) {
        this(instrumentedEventName, getDefaultLoggingLevel());
    }

    /**
     * Constructor which accepts the name of the event
     * you are going to instrument and the logging level
     * you are going to logger to.  Uses defaults
     * for all other values.
     */
    public AppInstrument(String instrumentedEventName, Level loggingLevel) {
        this(instrumentedEventName, getDefaultLogger(), loggingLevel, getDefaultAlertLoggingLevel(), getDefaultAlertTimeOutMilliseconds());
    }

    /**
     * Constructor which accepts the name of the event
     * you are going to instrument and the logger
     * you are going to logger to.  Uses defaults
     * for all other values.
     */
    public AppInstrument(String instrumentedEventName, Logger logger) {
        this(instrumentedEventName, logger, getDefaultLoggingLevel());
    }

    /**
     * Constructor which accepts the name of the event
     * you are going to instrument and the logger
     * you are going to logger to, and the logging
     * level.  Uses defaults
     * for all other values.
     */
    public AppInstrument(String instrumentedEventName, Logger logger, Level loggingLevel) {
        this(instrumentedEventName, logger, loggingLevel, getDefaultAlertLoggingLevel(), getDefaultAlertTimeOutMilliseconds());
    }

    /**
     * Constructor which accepts the name of the event
     * you are going to instrument, the normal
     * logging level, and a timeout value
     * that if exceeded changes the logging level
     * to the alert status.
     */
    public AppInstrument(String instrumentedEventName, Level loggingLevel, long alertTimeOutMilliseconds) {
        this(instrumentedEventName, getDefaultLogger(), loggingLevel, getDefaultAlertLoggingLevel(), alertTimeOutMilliseconds);
    }

    /**
     * Constructor which accepts all parameters for creating
     * an instrument.  Starts the timer before returning.
     * Note to subclassers:  This method MUST eventually be
     * called or the class will not function properly!
     */
    public AppInstrument(String instrumentedEventName, Logger logger, Level loggingLevel, Level alertLoggingLevel, long alertTimeOutMilliseconds) {
        this.instrumentedEventName = instrumentedEventName;
        outputLogger = logger;
        outputLoggingLevel = loggingLevel;
        outputAlertLoggingLevel = alertLoggingLevel;
        this.alertTimeOutMilliseconds = alertTimeOutMilliseconds;
        startTime = System.currentTimeMillis();
    }

    /**
     * Returns the default <code>Log</code> to logger to when
     * writing the output of the instrument.  The default for
     * this class is <code>AppLog</code>.
     */
    protected static Logger getDefaultLogger() {
        return AppLogger.getLogger();
    }

    /**
     * Returns the default logging level to logger to when
     * writing the output of the instrument.  The default
     * for this class is detail level.
     */
    protected static Level getDefaultLoggingLevel() {
        return Level.FINE;
    }

    /**
     * Returns the default logging level to logger to when
     * writing the output of the instrument.  The default
     * for this class is detail level.
     */
    protected static Level getDefaultAlertLoggingLevel() {
        return LevelAddition.ERROR;
    }

    /**
     * Returns the default alert time out value, set to
     * zero, so that an event does not time out and
     * raise its logging level to an error status.
     */
    protected static long getDefaultAlertTimeOutMilliseconds() {
        return 0;
    }

    /**
     * Logs the time since the timer was started to
     * the logger and logging level set when the instrument
     * was created.
     *
     * @returns the time, in milliseconds, it took to execute
     *   the event
     */
    public long logTime() {
        Level loggingLevel;
        long eventTime = System.currentTimeMillis() - startTime;
        if ((alertTimeOutMilliseconds > 0) && (eventTime > alertTimeOutMilliseconds)) {
            loggingLevel = outputAlertLoggingLevel;
        } else {
            loggingLevel = outputLoggingLevel;
        }
        StringBuffer sb = new StringBuffer();
        if (instrumentedEventName != null && !instrumentedEventName.equals("")) {
            sb.append("Event: '");
            sb.append(instrumentedEventName);
            sb.append("' ");
        } else {
            sb.append("Event ");
        }
        if (instrumentedEventMessage != null && !instrumentedEventMessage.equals("")) {
            sb.append("with message: '");
            sb.append(instrumentedEventMessage);
            sb.append("' ");
        }
        sb.append("took ");
        sb.append(eventTime);
        sb.append(" milliseconds to execute");
        outputLogger.log(loggingLevel, sb.toString());
        AppInstrumentVital.getInstance().registerInstrumentTiming(instrumentedEventName, instrumentedEventMessage, eventTime);
        return eventTime;
    }

    /**
     * Logs the time since the timer was started to
     * the logger and logging level set when the instrument
     * was created, sets/resets the instrumented
     * event string to the input
     * string.
     *
     * @returns the time, in milliseconds, it took to execute
     *   the event
     */
    public long logTime(String instrumentedEventMessage) {
        this.instrumentedEventMessage = instrumentedEventMessage;
        return logTime();
    }

    /**
     * Logs the time since the timer was started to
     * the logger and logging level set when the instrument
     * was created, sets/resets the instrumented
     * event string to the input
     * string.
     *
     * @returns the time, in milliseconds, it took to execute
     *   the event
     */
    public long logTime(String instrumentedEventName, String instrumentedEventMessage) {
        this.instrumentedEventName = instrumentedEventName;
        return logTime(instrumentedEventMessage);
    }

    /**
     * Resets the timer so that you can re-use the same
     * AppInstrument object to time different events.
     */
    public void resetTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
	 * Returns the instrumentedEventMessage.
	 * @return String
	 */
    public String getInstrumentedEventMessage() {
        return instrumentedEventMessage;
    }

    /**
	 * Returns the instrumentedEventName.
	 * @return String
	 */
    public String getInstrumentedEventName() {
        return instrumentedEventName;
    }

    /**
	 * Returns the outputAlertLoggingLevel.
	 * @return Level
	 */
    public Level getOutputAlertLoggingLevel() {
        return outputAlertLoggingLevel;
    }

    /**
	 * Returns the outputLoggingLevel.
	 * @return Level
	 */
    public Level getOutputLoggingLevel() {
        return outputLoggingLevel;
    }

    /**
	 * Sets the instrumentedEventMessage.
	 * @param instrumentedEventMessage The instrumentedEventMessage to set
	 */
    public void setInstrumentedEventMessage(String instrumentedEventMessage) {
        this.instrumentedEventMessage = instrumentedEventMessage;
    }

    /**
	 * Sets the instrumentedEventName.
	 * @param instrumentedEventName The instrumentedEventName to set
	 */
    public void setInstrumentedEventName(String instrumentedEventName) {
        this.instrumentedEventName = instrumentedEventName;
    }

    /**
	 * Sets the outputAlertLoggingLevel.
	 * @param outputAlertLoggingLevel The outputAlertLoggingLevel to set
	 */
    public void setOutputAlertLoggingLevel(Level outputAlertLoggingLevel) {
        this.outputAlertLoggingLevel = outputAlertLoggingLevel;
    }

    /**
	 * Sets the outputLoggingLevel.
	 * @param outputLoggingLevel The outputLoggingLevel to set
	 */
    public void setOutputLoggingLevel(Level outputLoggingLevel) {
        this.outputLoggingLevel = outputLoggingLevel;
    }
}
