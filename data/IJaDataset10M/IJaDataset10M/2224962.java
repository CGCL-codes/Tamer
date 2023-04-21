package huf.log;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

class LoggerUtil {

    /**
	 * Create new logger which will write messages to console.
	 */
    public LoggerUtil() {
    }

    /**
	 * Create new logger and specify if it should write to console.
	 *
	 * @param consoleLogging whether or not send log messages to console
	 */
    public LoggerUtil(boolean consoleLogging) {
        setConsoleLogging(consoleLogging);
    }

    /**
	 * Create new logger which will write to console and specified log file.
	 *
	 * <p>
	 * NOTE: file logging will be enabled only if <code>filename</code> is not
	 * <code>null</code> and file can be opened for writing.
	 * </p>
	 *
	 * @param filename łog file name
	 */
    public LoggerUtil(String filename) {
        setFile(filename);
        setFileLogging(true);
    }

    /**
	 * Create new logger which will write to console and specified log file;
	 * file logging can be disabled initially.
	 *
	 * <p>
	 * NOTE: file logging will be enabled only if <code>filename</code> is not
	 * <code>null</code> and file can be opened for writing.
	 * </p>
	 *
	 * @param filename log file name
	 * @param fileLogging whether or not send log messages to file
	 */
    public LoggerUtil(String filename, boolean fileLogging) {
        setFile(filename);
        setFileLogging(fileLogging);
    }

    /**
	 * Create new logger which will write to console and specified log file;
	 * both console and file logging can be disabled initially.
	 *
	 * <p>
	 * NOTE: file logging will be enabled only if <code>filename</code> is not
	 * <code>null</code> and file can be opened for writing.
	 * </p>
	 *
	 * @param filename log file name
	 * @param fileLogging whether or not send log messages to file
	 * @param consoleLogging whether or not send log messages to console
	 */
    public LoggerUtil(String filename, boolean fileLogging, boolean consoleLogging) {
        setFile(filename);
        setFileLogging(fileLogging);
        setConsoleLogging(consoleLogging);
    }

    /**
	 * Create new logger which will write to specified console stream and default log file.
	 *
	 * <p>
	 * NOTE: console logging will be enabled only if <code>out</code> is not
	 * <code>null</code>.
	 * </p>
	 *
	 * @param out console stream or <code>null</code> to turn off console logging
	 */
    public LoggerUtil(PrintStream out) {
        setConsole(out);
    }

    /**
	 * Create new logger which will write to console; console logging can be
	 * disabled initially.
	 *
	 * <p>
	 * NOTE: console logging will be enabled only if <code>out</code> is not
	 * <code>null</code>.
	 * </p>
	 *
	 * @param out console stream or <code>null</code> to turn off console logging
	 * @param consoleLogging whether or not send log messages to console
	 */
    public LoggerUtil(PrintStream out, boolean consoleLogging) {
        setConsole(out);
        setConsoleLogging(consoleLogging);
    }

    /**
	 * Create new logger which will write to specified console stream and log file.
	 *
	 * <p>
	 * NOTE: console logging will be enabled only if <code>out</code> is not
	 * <code>null</code>.
	 * Similarly, file logging will be enabled only if <code>filename</code> is not
	 * <code>null</code> and file can be opened for writing.
	 * </p>
	 *
	 * @param out console stream
	 * @param filename log file name
	 */
    public LoggerUtil(PrintStream out, String filename) {
        setConsole(out);
        setFile(filename);
        setFileLogging(true);
    }

    /**
	 * Create new logger which will write to specified console stream and log file;
	 * both console and file logging can be disabled initially.
	 *
	 * <p>
	 * NOTE: console logging will be enabled only if <code>out</code> is not
	 * <code>null</code>.
	 * Similarly, file logging will be enabled only if <code>filename</code> is not
	 * <code>null</code> and file can be opened for writing.
	 * </p>
	 *
	 * @param out console stream
	 * @param filename log file name
	 * @param fileLogging whether or not send log messages to file
	 * @param consoleLogging whether or not send log messages to console
	 */
    public LoggerUtil(PrintStream out, String filename, boolean fileLogging, boolean consoleLogging) {
        setConsole(out);
        setFile(filename);
        setFileLogging(fileLogging);
        setConsoleLogging(consoleLogging);
    }

    /**
	 * Write actual log entry to console and/or file.
	 *
	 * @param entryHeader entry header ("Debug: ", "Log: " etc)
	 * @param msg message to log
	 */
    void writeEntry(String entryHeader, String msg) {
        writeEntry(entryHeader, msg, true, true);
    }

    /**
	 * Write actual log entry to console and/or file.
	 *
	 * @param entryHeader entry header ("Debug: ", "Log: " etc)
	 * @param msg message to log
	 */
    void writeEntryl(String entryHeader, String msg) {
        writeEntry(entryHeader, msg, true, false);
    }

    /**
	 * Write actual log entry to console and/or file.
	 *
	 * @param entryHeader entry header ("Debug: ", "Log: " etc)
	 * @param msg message to log
	 */
    void writeMsgEntryl(String entryHeader, String msg) {
        writeEntry(entryHeader, msg, false, false);
    }

    /**
	 * Write actual log entry to console and/or file.
	 *
	 * @param entryHeader entry header ("Debug: ", "Log: " etc)
	 * @param msg message to log
	 * @param doTrace add trace info or not
	 * @param lf should end of line character be printed?
	 */
    private void writeEntry(String entryHeader, String msg, boolean doTrace, boolean lf) {
        String tMsg = null;
        if (trace && doTrace) {
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            int level = 5 + traceLevel + bumpLevel;
            if (st.length <= level) {
                tMsg = "[no trace info]";
            } else {
                tMsg = st[level].getClassName() + ":" + st[level].getLineNumber() + ":: ";
            }
        } else {
            tMsg = "";
        }
        if (consoleLogging) {
            consoleOut.print(entryHeader);
            consoleOut.print(tMsg);
            consoleOut.print(msg);
            if (lf) {
                consoleOut.println();
            }
        }
        if (fileLogging) {
            fileOut.print(entryHeader);
            fileOut.print(tMsg);
            fileOut.print(msg);
            if (lf) {
                fileOut.println();
            }
            fileOut.flush();
            try {
                logFile.flush();
            } catch (IOException ioe) {
                setFileLogging(false);
            }
        }
        bumpLevel = 0;
    }

    /**
	 * Write actual log entry to console and/or file.
	 *
	 * @param entryHeader entry header ("Debug: ", "Log: " etc)
	 * @param throwable exception or error to log
	 */
    void writeEntry(String entryHeader, Throwable throwable) {
        if (consoleLogging) {
            System.out.print(entryHeader);
            System.out.print(" ");
            throwable.printStackTrace(consoleOut);
        }
        if (fileLogging) {
            fileOut.print(entryHeader);
            fileOut.print(" ");
            throwable.printStackTrace(fileOut);
            fileOut.flush();
            try {
                logFile.flush();
            } catch (IOException ioe) {
                setFileLogging(false);
            }
        }
        bumpLevel = 0;
    }

    /** Controls whether or not print class name and line number of log calls. */
    private boolean trace = true;

    /**
	 * Controls whether or not print class name and line number of log calls.
	 *
	 * @param trace <code>true</code> to enable additional trace info or <code>false</code> to disable
	 */
    public void setTraceCalls(boolean trace) {
        this.trace = trace;
    }

    /**
	 * Return current state of trace calls switch.
	 *
	 * @return current state of trace calls switch
	 */
    public boolean getTraceCalls() {
        return trace;
    }

    /** Trace level. */
    private int traceLevel = 0;

    /**
	 * Change level of stack trace this logger should use for debugging.
	 *
	 * <p>
	 * By default loggers use trace level 0, which means location of code calling logger
	 * method is included in the output. However it's possible that user may want to use
	 * former levels of stack trace. It's possible to change is using this method.
	 * </p>
	 *
	 * <p>
	 * For example, after <code>setTraceLevel(1)</code> logger would include location of code
	 * which called the code which called the logger method, not direcly location of code
	 * which called the logger method. 
	 * </p>
	 *
	 * <p>
	 * </p>
	 *
	 * @param traceLevel stack trace level to include in output messages; must be zero or positive value;
	 *        if stack trace isn't deep enough for requested level warning message is displayed in the output
	 */
    public void setTraceLevel(int traceLevel) {
        if (traceLevel < 0) {
            throw new IllegalArgumentException("Trace level must be zero or positive: " + traceLevel);
        }
        this.traceLevel = traceLevel;
    }

    private int bumpLevel = 0;

    /**
	 * Temporarily bump up level of stack trace this logger should use for debugging.
	 *
	 * <p>
	 * This change is effective for very next call only.
	 * This method is intended to be used in code like: myLogger.
	 * </p>
	 *
	 * @param bumpTraceLevel number to add to standard log trace level
	 */
    public void bumpTraceLevel(int bumpTraceLevel) {
        bumpLevel = bumpTraceLevel;
    }

    /**
	 * Return trace level.
	 *
	 * @return trace level
	 */
    public int getTraceLevel() {
        return traceLevel;
    }

    private boolean consoleLogging = true;

    void setConsoleLogging(boolean log) {
        consoleLogging = log && consoleOut != null;
    }

    boolean getConsoleLogging() {
        return consoleLogging;
    }

    /** Console output stream. */
    private PrintStream consoleOut = ILogger.DEFAULT_CONSOLE_STREAM;

    /**
	 * Set stream to which console logging will be written to.
	 *
	 * <p>
	 * NOTE: console logging will be enabled only if <code>out</code> is not
	 * <code>null</code>.
	 * </p>
	 *
	 * @param out console stream or <code>null</code> to turn off console logging
	 */
    void setConsole(PrintStream out) {
        setConsole(out, getConsoleLogging());
    }

    /**
	 * Set stream to which console logging will be written to;
	 * console logging can be disabled initially.
	 *
	 * <p>
	 * NOTE: console logging will be enabled only if <code>out</code> is not
	 * <code>null</code>.
	 * </p>
	 *
	 * @param out console stream or <code>null</code> to turn off console logging
	 * @param logToConsole whether or not send log messages to console
	 */
    void setConsole(PrintStream out, boolean logToConsole) {
        setConsoleLogging(false);
        consoleOut = out;
        setConsoleLogging(logToConsole);
    }

    /**
	 * Get console output stream.
	 *
	 * @return console output stream
	 */
    PrintStream getConsole() {
        return consoleOut;
    }

    /** Whether or not log to file. */
    private boolean fileLogging = false;

    void setFileLogging(boolean log) {
        if (fileLogging == log) {
            return;
        }
        if (log) {
            fileLogging = openLogFile();
        } else {
            fileLogging = false;
            closeLogFile();
        }
    }

    boolean getFileLogging() {
        return fileLogging;
    }

    /** Name of log file. */
    private String logFileName = null;

    void setFile(String filename) {
        setFile(filename, true);
    }

    void setFile(String filename, boolean logToFile) {
        setFileLogging(false);
        logFileName = filename;
        setFileLogging(logToFile);
    }

    String getFile() {
        return logFileName;
    }

    /** File stream. */
    private FileOutputStream logFile = null;

    /** Print stream. */
    private PrintStream fileOut = null;

    PrintStream getLogFilePrintStream() {
        return fileOut;
    }

    /**
	 * Open log file.
	 *
	 * @return true when file has been opened suffessfully
	 */
    private boolean openLogFile() {
        closeLogFile();
        if (logFileName == null) {
            return false;
        }
        try {
            logFile = new FileOutputStream(logFileName, true);
        } catch (IOException e) {
            logFile = null;
            fileOut = null;
            return false;
        }
        fileOut = new PrintStream(new BufferedOutputStream(logFile));
        return true;
    }

    /** Close log file. */
    private void closeLogFile() {
        if (fileOut != null) {
            fileOut.flush();
            fileOut.close();
            fileOut = null;
        }
        if (logFile != null) {
            try {
                logFile.flush();
            } catch (IOException ioe) {
                writeEntry("ERROR: ", "Error while finishing log file");
            }
            try {
                logFile.close();
            } catch (IOException ioe) {
                writeEntry("ERROR: ", "Error while closing log file");
            }
            logFile = null;
        }
    }
}
