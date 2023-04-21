package org.masukomi.tools.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import org.masukomi.tools.utils.SizedStack;
import org.masukomi.tools.utils.StackTraceFormatter;

/**
 * A standardized error reporting Tool with various output options.
 * 
 * <p>
 * Log levels are: <br />
 * DEBUG <br />
 * INFO <br />
 * WARN <br />
 * ERROR <br />
 * </p>
 * 
 * <p>
 * This software is copyright 2002 Kate Rhodes. <br>
 * It is distributed under the MIT License which can be found at <a
 * href="http://www.opensource.org/licenses/mit-license.php">http://www.opensource.org/licenses/mit-license.php
 * </a> <br>
 * Details on this package can be found at <a
 * href="http://tools.masukomi.org/">http://tools.masukomi.org/ </a> <br>
 * e-mail <tt>masukomi at masukomi dot org</tt> for questions or details
 * </p>
 */
public class Logs extends java.lang.Object {

    /** debug status level */
    public static final int DEBUG = 0;

    /** string representation of debug status level */
    public static final String DEBUG_STRING = "DEBUG";

    /** info status level */
    public static final int INFO = 1;

    /** string representatin of info status level */
    public static final String INFO_STRING = "INFO";

    /** warn status level */
    public static final int WARN = 2;

    /** string representation of warn status level */
    public static final String WARN_STRING = "WARN";

    /** error status level */
    public static final int ERROR = 3;

    /** string representation of status warning level */
    public static final String ERROR_STRING = "ERROR";

    /** fatal status level */
    public static final int FATAL = 4;

    /** string representation of fatal status level */
    public static final String FATAL_STRING = "FATAL";

    /** The singleton instance */
    public static Logs instance = null;

    /** the name of the log file we should write to */
    protected static File logFile = null;

    /** indicates if we should write to a log file */
    protected static boolean useLogFile = false;

    /** the total number of errors that have been reported */
    protected static int totalEntries;

    protected static int maxStoredEntries = 10;

    /** A vector of LogEntry objects */
    protected static SizedStack logsStack;

    /** indicates if we should write errors to System.out */
    protected static boolean useSystemOut = true;

    /** the minim severity level we should bother logging */
    protected static int logThreshold = 0;

    /** the minimum level required to send a warning originalMessage to an administrator. */
    protected static int warningLevel;

    /** a listing of session IDs that have had errors reported in them. */
    protected static Vector sessionsWithEntries;

    /** for converting epoch time into a more readable format */
    protected static SimpleDateFormat dateFormatter;

    /** e-mail address of the person who should receive warning e-mails */
    protected static String logmaster;

    protected static String logEmailSubject = "automatted log originalMessage";

    protected static String smtpServer = "Aspirin";

    protected static boolean shortClassNames = true;

    /**
	 * Creates a new Logs object after first loading the appropriate system preferences
	 * 
	 */
    protected Logs() {
        dateFormatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss:SSS");
        try {
            if (System.getProperty("entriesToSystemOut") != null) {
                useSystemOut = Boolean.parseBoolean(System.getProperty("entriesToSystemOut"));
            }
            if (System.getProperty("logFileRequired") != null && System.getProperty("logFile") != null) {
                useLogFile = Boolean.parseBoolean(System.getProperty("logFileRequired"));
                setLogFile(System.getProperty("logFile"));
            }
            if (System.getProperty("useLogFile") != null && System.getProperty("logFile") != null) {
                useLogFile = Boolean.parseBoolean(System.getProperty("useLogFile"));
                setLogFile(System.getProperty("logFile"));
            }
            if (System.getProperty("maxStoredEntries") != null) {
                maxStoredEntries = Integer.parseInt(System.getProperty("maxStoredEntries", "20"));
            }
            if (System.getProperty("entryLogThreshold") != null) {
                try {
                    logThreshold = getSeverityIntFromString(System.getProperty("entryLogThreshold", Logs.DEBUG_STRING));
                } catch (Exception e) {
                    logThreshold = Logs.DEBUG;
                }
            }
            if (System.getProperty("logmaster") != null) {
                logmaster = System.getProperty("logmaster");
            } else {
                logmaster = "root@localhost";
            }
            if (System.getProperty("logEmailSubject") != null) {
                logEmailSubject = System.getProperty("logEmailSubject");
            }
            if (System.getProperty("smtpServer") != null) {
                smtpServer = System.getProperty("smtpServer");
            }
            if (System.getProperty("shortClassNames") != null) {
                shortClassNames = Boolean.parseBoolean(System.getProperty("shortClassNames"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logsStack = new SizedStack(maxStoredEntries);
        sessionsWithEntries = new Vector();
        totalEntries = 0;
    }

    /**
	 * Gets a listing of all the entries as a String
	 * 
	 * @return a listing of all the entries as a String
	 */
    public static String getEntriesString() {
        return getEntriesString(Logs.DEBUG);
    }

    /**
	 * Returns a listing of all the entries at the specified severity or higher
	 * as a string
	 * 
	 * @param minimumLevel
	 *            the minimum severity level to report
	 * 
	 * @return a listing of all the entries at the specified severity or higher
	 *         as a string
	 */
    public static String getEntriesString(int minimumLevel) {
        StringBuffer errorsString = new StringBuffer();
        try {
            if (logsStack.size() > 0) {
                Iterator it = logsStack.iterator();
                while (it.hasNext()) {
                    LogEntry error = (LogEntry) it.next();
                    if (error.severity >= minimumLevel) {
                        errorsString.append(error.toString());
                        errorsString.append("\n");
                    }
                }
            }
        } catch (Exception e) {
            addInternalError("getErrorsString(int)", e.toString(), 2, false);
        }
        return errorsString.toString();
    }

    /**
	 * retreives the entry at the specified index in the Vector of entries
	 * 
	 * @param index
	 *            DOCUMENT ME!
	 * 
	 * @return the entry at the specified index in the Vector of entries
	 */
    public static LogEntry getEntryAt(int index) {
        LogEntry lastMessage = (LogEntry) logsStack.get(index);
        return lastMessage;
    }

    /**
	 * retreives the last error originalMessage reported or null if none have been
	 * reported.
	 * 
	 * @return the last error originalMessage reported or null if none have been
	 *         reported
	 */
    public static LogEntry getLastEntry() {
        LogEntry lastMessage = (LogEntry) logsStack.lastElement();
        return lastMessage;
    }

    /**
	 * Sets the logFile.
	 * 
	 * @param logFile
	 *            The logFile to set
	 */
    public static void setLogFile(String logFile) {
        Logs.logFile = new File(logFile);
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return String
	 */
    public static File getLogFile() {
        return logFile;
    }

    /**
	 * modifies the current log threshold.
	 * 
	 * @param newThreshold
	 *            the new Threshold level.
	 */
    public static void setLogThreshold(int newThreshold) {
        logThreshold = newThreshold;
        if (Logs.isLoggable(Logs.DEBUG)) {
            Logs.debug("changed log threshold to " + Logs.getSeverityString(logThreshold));
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param newThreshold
	 *            DOCUMENT ME!
	 */
    public static void setLogThreshold(String newThreshold) {
        setLogThreshold(getSeverityIntFromString(newThreshold));
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return int describing indicating the minimum severity that will be
	 *         recorded
	 */
    public static int getLogThreshold() {
        return logThreshold;
    }

    /**
	 * returns true if the threshold you are testing is >= the current
	 * logThreshold.
	 * 
	 * @param levelToTest
	 *            the logging threshold you wish to test the viability of.
	 * 
	 * @return true if the threshold you are testing is >= the current
	 *         logThreshold.
	 */
    public static boolean isLoggable(int levelToTest) {
        if (isInitialized()) {
            if (levelToTest >= getLogThreshold()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDebugLoggable() {
        return Logs.isLoggable(Logs.DEBUG);
    }

    public static boolean isInfoLoggable() {
        return Logs.isLoggable(Logs.INFO);
    }

    public static boolean isWarnLoggable() {
        return Logs.isLoggable(Logs.WARN);
    }

    public static boolean isErrorLoggable() {
        return Logs.isLoggable(Logs.ERROR);
    }

    /**
	 * creates an HTML table with all the errors and human readable time stamps.
	 * 
	 * @return an HTML table listing all the errors with human readable time
	 *         stamps.
	 */
    public static String getLogsHTML() {
        return getLogsHTML(Logs.DEBUG);
    }

    /**
	 * creates an HTML table with all the errors with a severity >= the
	 * minimumLevel and human readable time stamps.
	 * 
	 * @param minimumLevel
	 *            the minimum serverity level an error can have to be included
	 *            in the generated table.
	 * 
	 * @return an HTML table of errors at or above the requested limit.
	 */
    public static String getLogsHTML(int minimumLevel) {
        StringBuffer logHTML = new StringBuffer();
        logHTML.append("<table border='1'>\n");
        try {
            logHTML.append("<tr><td colspan='");
            logHTML.append(String.valueOf(LogEntry.COLUMN_ROWS));
            logHTML.append("'>total entries: ");
            logHTML.append(String.valueOf(totalEntries));
            logHTML.append("</td></tr>\n");
        } catch (Exception e) {
            System.out.print("encountered: " + e + " \nwhile testing length of errors array\n");
        }
        try {
            logHTML.append("<tr><th>severity</th><th>reporter.method</th><th>originalMessage</th><th>time</th><th>session</th></tr>\n");
            if (logsStack.size() > 0) {
                for (int i = logsStack.size() - 1; i > -1; i--) {
                    LogEntry entry = (LogEntry) logsStack.elementAt(i);
                    if (entry.severity >= minimumLevel) {
                        logHTML.append("\n<tr valign='top' bgcolor=\"");
                        try {
                            logHTML.append(getSeverityHTMLColor(entry.severity));
                        } catch (Exception e) {
                            logHTML.append("#ffffff");
                        }
                        logHTML.append("\">");
                        logHTML.append("<td>&nbsp;");
                        logHTML.append(Logs.getSeverityString(entry.severity) + "");
                        logHTML.append("</td>");
                        logHTML.append("<td>&nbsp;");
                        logHTML.append(entry.getReporter());
                        logHTML.append(".");
                        logHTML.append(entry.getCallingMethod());
                        logHTML.append("</td>");
                        logHTML.append("<td>&nbsp;<pre>");
                        logHTML.append(entry.getMessage().replaceAll("<", "&lt;"));
                        logHTML.append("</pre></td>");
                        logHTML.append("<td>&nbsp;");
                        try {
                            logHTML.append(dateFormatter.format(new Date(entry.getTime())));
                        } catch (Exception e) {
                            Logs.error(e);
                        }
                        logHTML.append("</td>");
                        logHTML.append("<td>&nbsp;");
                        logHTML.append(entry.getSessionID());
                        logHTML.append("</td>");
                        logHTML.append("</tr>");
                    }
                }
            }
        } catch (Exception e) {
            addInternalError("getErrorsHTML(int)", e.toString(), 2, false);
        }
        logHTML.append("</table>\n");
        return logHTML.toString();
    }

    /**
	 * @param severity
	 * @return
	 */
    protected static Object getSeverityHTMLColor(int severity) {
        if (severity == Logs.WARN) {
            return "#ffce0b";
        } else if (severity == Logs.ERROR) {
            return "#ffa018";
        }
        return "#ffffff";
    }

    /**
	 * Returns the vector of Logs.
	 * 
	 * @return the vector of Logs
	 */
    public static Vector getLogsStack() {
        return logsStack;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return Vector
	 */
    public static Vector getSessionsWithEntries() {
        return sessionsWithEntries;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param severityString
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws UnsupportedOperationException
	 *             DOCUMENT ME!
	 */
    public static int getSeverityIntFromString(String severityString) {
        if (severityString.equalsIgnoreCase(Logs.DEBUG_STRING)) {
            return Logs.DEBUG;
        } else if (severityString.equalsIgnoreCase(Logs.INFO_STRING)) {
            return Logs.INFO;
        } else if (severityString.equalsIgnoreCase(Logs.WARN_STRING)) {
            return Logs.WARN;
        } else if (severityString.equalsIgnoreCase(Logs.ERROR_STRING)) {
            return Logs.ERROR;
        } else if (severityString.equalsIgnoreCase(Logs.FATAL_STRING)) {
            return Logs.FATAL;
        } else if (severityString.equalsIgnoreCase(String.valueOf(Logs.DEBUG))) {
            return Logs.DEBUG;
        } else if (severityString.equalsIgnoreCase(String.valueOf(Logs.INFO))) {
            return Logs.INFO;
        } else if (severityString.equalsIgnoreCase(String.valueOf(Logs.WARN))) {
            return Logs.WARN;
        } else if (severityString.equalsIgnoreCase(String.valueOf(Logs.ERROR))) {
            return Logs.ERROR;
        } else if (severityString.equalsIgnoreCase(String.valueOf(Logs.FATAL))) {
            return Logs.FATAL;
        } else {
            try {
                int i = Integer.parseInt(severityString);
                if (i <= Logs.FATAL) {
                    return i;
                } else {
                    throw new UnsupportedOperationException(severityString + " is not an acceptable log level or warning threshold.");
                }
            } catch (Exception e) {
                throw new UnsupportedOperationException(severityString + " is not a supported log level or warning threshold");
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param severity
	 *            DOCUMENT ME!
	 * 
	 * @return String a more human parseable version of the severity
	 */
    public static String getSeverityString(int severity) {
        if (severity == Logs.DEBUG) {
            return Logs.DEBUG_STRING;
        } else if (severity == Logs.INFO) {
            return Logs.INFO_STRING;
        } else if (severity == Logs.WARN) {
            return Logs.WARN_STRING;
        } else if (severity == Logs.ERROR) {
            return Logs.ERROR_STRING;
        } else if (severity == Logs.FATAL) {
            return Logs.FATAL_STRING;
        }
        return String.valueOf(severity);
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return int
	 */
    public static int getTotalEntries() {
        return totalEntries;
    }

    /**
	 * Sets the useLogFile.
	 * 
	 * @param useLogFile
	 *            The useLogFile to set
	 */
    public static void setUseLogFile(boolean useLogFile) {
        Logs.useLogFile = useLogFile;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return boolean
	 */
    public static boolean isUseLogFile() {
        return useLogFile;
    }

    /**
	 * Sets the useSystemOut.
	 * 
	 * @param useSystemOut
	 *            The useSystemOut to set
	 */
    public static void setUseSystemOut(boolean useSystemOut) {
        Logs.useSystemOut = useSystemOut;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return Boolean
	 */
    public static boolean getUseSystemOut() {
        return useSystemOut;
    }

    /**
	 * Sets the level at which the warn(ErrorMethod) method is called. By
	 * default the warnign level is -1, which results in No warnings being sent.
	 * 
	 * @param newWarningLevel
	 *            the new level at which to generate e-mail alerts.
	 */
    public static void setWarningLevel(int newWarningLevel) {
        warningLevel = newWarningLevel;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return int
	 */
    public static int getWarningLevel() {
        return warningLevel;
    }

    /**
	 * adds and Error to the Vector of errors
	 * 
	 * @param reporter
	 *            the name of the thing reporting the error. Generally this is
	 *            the class name
	 * @param originalMessage
	 *            The actual error originalMessage
	 * @param severity
	 *            a numeric indication of how severe the error was.
	 * 
	 * @return true if the error was successfully added
	 */
    protected static boolean addEntry(String message, int severity) {
        return addEntry(getReporterString(null), message, severity, null);
    }

    static String cleanReporterString(String reporterString) {
        if (!shortClassNames || reporterString == null) {
            return reporterString;
        } else {
            return reporterString.replaceAll(".*\\.(\\w+\\.\\w+\\(.*?\\).*$)", "$1");
        }
    }

    /**
	 * Figures out where the error came from
	 * 
	 * @param reporter
	 * 
	 * @return
	 */
    static String getReporterString(Object reporter) {
        if (reporter instanceof Throwable || reporter instanceof Exception) {
            StackTraceElement[] elements = ((Throwable) reporter).getStackTrace();
            for (int i = 0; i < elements.length; i++) {
                if (elements[i].toString().indexOf("org.masukomi.tools.logging.Logs.") == -1) {
                    return cleanReporterString(elements[i].toString());
                }
            }
        } else if (reporter instanceof String) {
            return cleanReporterString((String) reporter);
        } else {
            try {
                throw new Exception();
            } catch (Exception e) {
                StackTraceElement[] elements = e.getStackTrace();
                for (int i = 1; i < elements.length; i++) {
                    if (elements[i].toString().indexOf("org.masukomi.tools.logging.Logs.") == -1) {
                        return cleanReporterString(elements[i].toString());
                    }
                }
            }
        }
        return null;
    }

    /**
	 * adds and Error to the Vector of errors
	 * 
	 * @param exception
	 *            the name of the thing reporting the error. Generally this is
	 *            the class name
	 * @param severity
	 *            a numeric indication of how severe the error was.
	 * 
	 * @return true if the error was successfully added
	 */
    protected static boolean addEntry(Throwable exception, int severity) {
        StringBuffer message = new StringBuffer();
        message.append(exception.toString());
        message.append("\n");
        message.append(StackTraceFormatter.stackTraceElementsToString(exception.getStackTrace()));
        return addEntry(getReporterString(exception), message.toString(), severity, null);
    }

    /**
	 * adds and Error to the Vector of errors.
	 * 
	 * @param reporter
	 *            the name of the thing reporting the error. Generally this is
	 *            the class name
	 * @param originalMessage
	 *            The actual error originalMessage
	 * @param severity
	 *            a numeric indication of how severe the error was.
	 * @param sessionID
	 *            If this error was generated as part of some tracked session
	 *            indicating the sessionID here will allow you to later
	 *            determine if there were any errors related to that session.
	 * 
	 * @return true if the error was successfully reported
	 */
    protected static boolean addEntry(String reporter, String message, int severity, String sessionID) {
        if (isInitialized()) {
            try {
                if (isLoggable(severity)) {
                    totalEntries++;
                    try {
                        LogEntry thisMessage = new LogEntry(reporter, severity, message, sessionID);
                        storeEntry(thisMessage);
                        if ((warningLevel > -1) && (warningLevel <= severity)) {
                            warn(thisMessage);
                        }
                        if ((sessionID != null) && (!sessionID.equals(""))) {
                            if (sessionsWithEntries.indexOf(sessionID) == -1) {
                                sessionsWithEntries.add(sessionID);
                            }
                        }
                        if (useSystemOut) {
                            System.out.println(thisMessage.toString());
                        }
                        if (useLogFile && logFile != null) {
                            appendStringToFile(thisMessage.toString() + "\n", logFile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            addInternalError("encountered " + e + " -while adding '" + message + "' from " + reporter + "\n", "addError", 1, true);
                        } catch (Exception f) {
                            try {
                                addInternalError("encountered " + e + " -while adding a originalMessage from " + reporter + "\n", "addError", 1, true);
                            } catch (Exception g) {
                                try {
                                    addInternalError("encountered " + e + " -while adding a originalMessage from " + reporter + " ( unknown method )\n", "addError", 1, true);
                                } catch (Exception h) {
                                    addInternalError("encountered " + e + " -while adding a originalMessage from unknown class ( unknown method )\n", "addError", 1, true);
                                }
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Stores a log entry for easy retreival (no log parsing) while maintaining
	 * the maximum allowed number of entries
	 * 
	 * @param entry
	 *            the LogEntry to store
	 */
    protected static void storeEntry(LogEntry entry) {
        logsStack.push(entry);
    }

    /**
	 * Analogous to Log4Js debug method this will generate a new log entry with
	 * a "Debug" severity level.
	 * 
	 * @param theMessage
	 *            the originalMessage you wish to log
	 * @param caller
	 *            the class that called this
	 */
    public static void debug(Object theMessage) {
        try {
            addEntry(theMessage.toString(), Logs.DEBUG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Analogous to Log4Js debug method this will generate a new log entry with
	 * a "Debug" severity level.
	 * 
	 * @param exception
	 *            the exception that was caught and whose stack trace you wish
	 *            to log.
	 * @param caller
	 *            the class that called this
	 */
    public static void debug(Throwable exception) {
        try {
            addEntry(exception, Logs.DEBUG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Deletes the notice of an error in this session so that future errors in
	 * this session won't be confused with past errors. This does NOT remove the
	 * LogEntry from the errors stored in memory
	 * 
	 * @param sessionID
	 *            the unique ID of the session that you wish to delete
	 *            references to
	 */
    public static void deleteSessionLogEntries(String sessionID) {
        if (sessionsWithEntries.indexOf(sessionID) != -1) {
            sessionsWithEntries.removeElementAt(sessionsWithEntries.indexOf(sessionID));
        }
    }

    /**
	 * Analogous to Log4Js error method this will generate a new log entry with
	 * a "Error" severity level.
	 * 
	 * @param theMessage
	 *            the originalMessage you wish to log
	 * @param caller
	 *            the class that called this
	 */
    public static void error(Object theMessage) {
        try {
            addEntry(String.valueOf(theMessage), Logs.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Analogous to Log4Js error method this will generate a new log entry with
	 * a "Error" severity level.
	 * 
	 * @param exception
	 *            the exception that was caught and whose stack trace you wish
	 *            to log.
	 */
    public static void error(Throwable exception) {
        try {
            addEntry(exception, Logs.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Indicates that an error of Fatal severity has occourred
	 * 
	 * @param theMessage
	 *            the originalMessage you wish to log
	 * @param caller
	 *            the class that called this
	 */
    public static void fatal(Object theMessage) {
        try {
            addEntry(String.valueOf(theMessage), Logs.FATAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Indicates that an error of Fatal severity has occourred
	 * 
	 * @param exception
	 *            the exception that was caught and whose stack trace you wish
	 *            to log.
	 * @param caller
	 *            the class that called this
	 */
    public static void fatal(Exception exception) {
        try {
            addEntry(exception, Logs.FATAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Deletes all reported errors from the logsStack.
	 */
    public static void flushEntries() {
        logsStack.empty();
        totalEntries = 0;
    }

    /**
	 * tests if any errors have been reported.
	 * 
	 * @return true if errors have been reported.
	 */
    public static boolean gotEntries() {
        if (totalEntries == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
	 * tests if any errors have been reported at or above the specified severity
	 * level.
	 * 
	 * @param minimumLevel
	 *            the minimum severity level of errors to check for.
	 * 
	 * @return true if there were any errors reported with a severity equal to
	 *         or greater than the severity specified in minimumLevel.
	 */
    public static boolean gotEntries(int minimumLevel) {
        int myErrorCount = 0;
        if (totalEntries > 0) {
            Iterator it = logsStack.iterator();
            LogEntry currentError;
            while (it.hasNext()) {
                currentError = (LogEntry) it.next();
                if (currentError.severity >= minimumLevel) {
                    myErrorCount++;
                }
            }
        }
        if (myErrorCount == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
	 * tests if there have been any errors reported in the given session.
	 * 
	 * @param sessionID
	 *            the sessionID to test for errors in.
	 * 
	 * @return true if one or more errors has been recorded for this.
	 */
    public static boolean gotEntriesInSession(String sessionID) {
        if (sessionsWithEntries.indexOf(sessionID) != -1) {
            return true;
        }
        return false;
    }

    /**
	 * tests if there have been any errors reported in the given session at or
	 * above the minimumLevel.
	 * 
	 * @param sessionID
	 *            the sessionID to test for errors in.
	 * @param minimumLevel
	 *            the minimum severity level
	 * 
	 * @return true if one or more errors has been recorded for this
	 */
    public static boolean gotEntriesInSession(String sessionID, int minimumLevel) {
        if (sessionsWithEntries.indexOf(sessionID) != -1) {
            Iterator it = logsStack.iterator();
            while (it.hasNext()) {
                LogEntry error = (LogEntry) it.next();
                if (error.severity >= minimumLevel) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param theMessage
	 *            DOCUMENT ME!
	 * @param caller
	 *            DOCUMENT ME!
	 */
    public static void info(Object theMessage) {
        try {
            addEntry(String.valueOf(theMessage), Logs.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Analogous to Log4Js info method this will generate a new log entry with a
	 * "Info" severity level.
	 * 
	 * @param exception
	 *            the exception that was caught and whose stack trace you wish
	 *            to log.
	 * @param caller
	 *            the class that called this
	 */
    public static void info(Throwable exception) {
        try {
            addEntry(exception, Logs.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param theMessage
	 *            DOCUMENT ME!
	 * @param caller
	 *            DOCUMENT ME!
	 */
    public static void warn(Object theMessage) {
        try {
            addEntry(String.valueOf(theMessage), Logs.WARN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Analogous to Log4Js error method this will generate a new log entry with
	 * a "Error" severity level.
	 * 
	 * @param exception
	 *            the exception that was caught and whose stack trace you wish
	 *            to log.
	 * @param caller
	 *            the class that called this
	 */
    public static void warn(Throwable exception) {
        try {
            addEntry(exception, Logs.WARN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * adds an Error from Logs to the Vector of errors.
	 * 
	 * @param originalMessage
	 *            The actual error originalMessage
	 * @param callingMethod
	 *            the method that is actually calling addError
	 * @param severity
	 *            a numeric indication of how severe the error was.
	 * @param warn
	 *            set to true if a warning should be sent for this error,
	 *            assuming it meets or exceedes the warningLevel
	 */
    protected static void addInternalError(String message, String callingMethod, int severity, boolean warn) {
        totalEntries++;
        try {
            LogEntry thisMessage = new LogEntry("Logs", severity, message, null);
            if (warn && (warningLevel > -1) && (warningLevel <= severity)) {
                warn(thisMessage);
            }
            if (severity >= logThreshold) {
                logsStack.add(thisMessage);
                if (useSystemOut) {
                    System.out.println(thisMessage.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Appends the given String to a file. If the specified file is not found a
	 * new one will be created and filled with the specified string.
	 * 
	 * @param stringToAppend
	 *            The string to append to the File.
	 * @param theFile
	 *            The file to append the string to
	 * 
	 * @return true if the operation was successful;
	 */
    protected static boolean appendStringToFile(String stringToAppend, File theFile) {
        boolean success = false;
        try {
            FileOutputStream out = new FileOutputStream(theFile, true);
            out.write(stringToAppend.getBytes());
            out.close();
            success = true;
        } catch (FileNotFoundException e) {
            try {
                stringToFile(stringToAppend, theFile);
            } catch (Exception f) {
                addInternalError(f.toString(), "appendStringToFile(String, File)", Logs.ERROR, true);
                addInternalError("log file is " + theFile.getAbsolutePath(), "appendStringToFile(String, File)", Logs.ERROR, true);
            }
        } catch (Exception e) {
            addInternalError(e.toString(), "appendStringToFile(String, File)", Logs.ERROR, true);
        }
        return success;
    }

    /**
	 * Generates a file from the data passed in and writes it to the location
	 * specified by the File object
	 * 
	 * @param fileContents
	 *            a string representation of the new files contents.
	 * @param theFile
	 *            a File object indicating where to create the new file
	 * 
	 * @return DOCUMENT ME!
	 */
    protected static boolean stringToFile(String fileContents, File theFile) {
        boolean success = false;
        if (!theFile.exists()) {
            if (!theFile.getParentFile().exists()) {
                theFile.getParentFile().mkdirs();
            }
        }
        if (theFile.canWrite()) {
            try {
                FileOutputStream fos = new FileOutputStream(theFile);
                fos.write(fileContents.getBytes());
                fos.close();
                success = true;
            } catch (FileNotFoundException e) {
                addInternalError("unable to find path to new file: " + e, "stringToFile(String, File)", Logs.ERROR, true);
            } catch (IOException e) {
                addInternalError("problem writing new file" + e, "stringToFile(String, File)", Logs.ERROR, true);
            } catch (Exception e) {
                addInternalError(e.toString(), "stringToFile(String, File)", Logs.ERROR, true);
            }
        } else {
            addInternalError("can't write to " + theFile.toString(), "stringToFile(String, File)", Logs.ERROR, true);
            return false;
        }
        return success;
    }

    /**
	 * sends out an e-mail warning to the webmaster
	 * 
	 * @param theMessage
	 *            the error originalMessage being used to generate a warning e- mail.
	 */
    protected static void warn(LogEntry theMessage) {
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public static boolean isInitialized() {
        if (instance == null) {
            instance = new Logs();
        }
        return instance != null;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param logs
	 *            DOCUMENT ME!
	 */
    protected static void setInstance(Logs logs) {
        instance = logs;
    }
}
