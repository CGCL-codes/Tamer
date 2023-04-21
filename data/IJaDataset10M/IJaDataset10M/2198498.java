package net.scharlie.lumberjack4logs.io;

import net.scharlie.lumberjack4logs.entry.LoggingEntry;
import net.scharlie.lumberjack4logs.time.ExtendedTimeStamp;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * Die Klasse <code>LogEntryCreator</code> binaere Log-Event vom Typ
 * <code>LoggingEntry</code> aus Zeitstempel in ms und textuellen Informationen.
 * 
 * @author Bernd Scharlemann
 */
public class LogEntryCreator {

    /**
     * Der voll qualifizierte Name dieser Klasse.
     */
    private static final String FQCN = LogEntryCreator.class.getName();

    private LogEntryCreator() {
    }

    /**
     * Erzeuge aus den Eingabedaten ein <code>LoggingEntry</code>.
     * 
     * @param pExtendedTimeStamp
     *            Zeitstempel
     * @param pLevelName
     *            Name des log4j-Levels
     * @param pLoggerName
     *            Name des log4j-Loggers
     * @param pThreadName
     *            Name des Threads
     * @param pContext
     *            Kontextinformation
     * @param pMessage
     *            Nachrichtentext
     * @param pThrowableTextArray
     *            Textdarstellung eines Throwables
     * @param pLocationTextArray
     *            Informationen ueber den Ursprungsort ([0]=Dateiname,
     *            [1]=Klassenname, [2]=Methodenname, [3]=Zeilennummer)
     * 
     * @return LoggingEntry
     */
    public static LoggingEntry createLogEntry(final ExtendedTimeStamp pExtendedTimeStamp, final String pLevelName, final String pLoggerName, final String pThreadName, final String pContext, final String pMessage, final String[] pThrowableTextArray, final String[] pLocationTextArray) {
        LoggingEntry aLoggingEntry = null;
        final Level aLevel = LogLevelHelper.string2Level(pLevelName);
        final Logger aLogger = Logger.getLogger(pLoggerName != null ? pLoggerName : "null");
        ThrowableInformation aThrowable = null;
        if (pThrowableTextArray != null && pThrowableTextArray.length > 0) {
            aThrowable = new ThrowableInformation(pThrowableTextArray);
        }
        LocationInfo anInfo = null;
        if (pLocationTextArray != null && pLocationTextArray.length > 0) {
            final int len = pLocationTextArray.length;
            anInfo = new LocationInfo(len > 0 ? pLocationTextArray[0] : null, len > 1 ? pLocationTextArray[1] : null, len > 2 ? pLocationTextArray[2] : null, len > 3 ? pLocationTextArray[3] : null);
        }
        aLoggingEntry = new LoggingEntry(FQCN, aLogger, pExtendedTimeStamp, aLevel, pMessage, pThreadName, aThrowable, pContext, anInfo, null);
        return aLoggingEntry;
    }

    /**
     * Erzeuge aus den Eingabedaten ein <code>LoggingEntry</code>.
     * 
     * @param pExtendedTimeStamp
     *            Zeitstempel
     * @param pLevelName
     *            Name des log4j-Levels
     * @param pLoggerName
     *            Name des log4j-Loggers
     * @param pThreadName
     *            Name des Threads
     * @param pContext
     *            Kontextinformation
     * @param pMessage
     *            Nachrichtentext
     * @param pThrowableTextArray
     *            Textdarstellung eines Throwables
     * 
     * @return LoggingEntry
     */
    public static LoggingEntry createLogEntry(final ExtendedTimeStamp pExtendedTimeStamp, final String pLevelName, final String pLoggerName, final String pThreadName, final String pContext, final String pMessage, final String[] pThrowableTextArray) {
        return createLogEntry(pExtendedTimeStamp, pLevelName, pLoggerName, pThreadName, pContext, pMessage, pThrowableTextArray, null);
    }

    /**
     * Don't copy a logging entry needlessly.
     * 
     * @param pEntry
     * @return
     */
    public static LoggingEntry createLogEntry(final LoggingEntry pEntry) {
        return pEntry;
    }

    /**
     * Erzeuge aus einem <code>LoggingEvent</code> ein <code>LoggingEntry</code>
     * .
     * 
     * @param pEvent
     *            log4j-Event
     * 
     * @return LoggingEntry
     */
    public static LoggingEntry createLogEntry(final LoggingEvent pEvent) {
        if (pEvent == null) {
            return null;
        }
        final LocationInfo locationInfo = pEvent.getLocationInformation();
        final String[] locationTextArray = { locationInfo.getFileName(), locationInfo.getClassName(), locationInfo.getMethodName(), locationInfo.getLineNumber() };
        return createLogEntry(new ExtendedTimeStamp(pEvent.getTimeStamp()), pEvent.getLevel().toString(), pEvent.getLoggerName(), pEvent.getThreadName(), pEvent.getNDC(), pEvent.getRenderedMessage(), pEvent.getThrowableStrRep(), locationTextArray);
    }
}
