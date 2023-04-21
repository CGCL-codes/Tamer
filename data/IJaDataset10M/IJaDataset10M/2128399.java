package Log;

import java.io.ObjectOutputStream;
import java.io.IOException;

/**
  * Represents a log file.
  *
  * <p>There is one global writable log file in the whole system. An instance of LogCreator 
  * is used to create the global log file as well as any local log file.</p>
  * <p>Anything loggable must suit the Loggable interface and must be able to generate an
  * instance of LogEntry</p>
  *
  * @see Loggable
  * @see LogEntry
  * @see LogDisplay
  *
  * @author Steffen Zschaler
  * @version 0.5
  */
public class Log {

    private static Log theGlobalLog = null;

    private static LogCreator theLogCreator = null;

    static {
        theLogCreator = new LogCreator() {

            public Log createLog(ObjectOutputStream oo) {
                return new Log(oo);
            }
        };
    }

    /**
    * Reference to the global output stream.
    *
    * <p><STRONG>Read Only</STRONG></p>
    *
    * @see Log#setGlobalOutputStream
    */
    protected static ObjectOutputStream theGlobalOutputStream = null;

    /**
    * The log files output stream. <I>(Instance variable)</I>
    *
    * @see Log#changeOutputStream
    * @see Log#log
    */
    protected ObjectOutputStream ooOutput = null;

    /**
    * Construct a new log file.
    *
    * @param oo the outputstream to write to.
    */
    public Log(ObjectOutputStream oo) {
        super();
        changeOutputStream(oo);
    }

    /**
    * Returns the current global log file.
    *
    * <p>If no log file exists, creates it using the Outputstream as specified by
    * setGlobalOutputStream()</p>
    *
    * @see Log#setGlobalOutputStream
    * @see Log#closeGlobalLog
    *
    * @exception LogNoOutputStreamException if the method setGlobalOutputStream has not been called yet.
    */
    public static synchronized Log getGlobalLog() throws LogNoOutputStreamException {
        if (theGlobalOutputStream == null) throw new LogNoOutputStreamException("On Log.getGlobalLog()");
        if (theGlobalLog == null) theGlobalLog = createLog(theGlobalOutputStream);
        return theGlobalLog;
    }

    /** 
    * Create a new Log file using the current Log creator. 
    *
    * <p>You should prefer calling this method to directly creating a new Log
    * file as this method will provide an easy interface for adapting to new
    * log classes.</p>
    *
    * @param oo the OutputStream to be used.
    */
    public static Log createLog(ObjectOutputStream oo) {
        return theLogCreator.createLog(oo);
    }

    /**
    * Change the Log creator.
    *
    * <p>Call to provide support for descended Log classes.</p>
    *
    * @param lc the log creator to be used when creating log files.
    *
    * @see Log#getGlobalLog
    */
    public static void setLogCreator(LogCreator lc) {
        theLogCreator = lc;
    }

    /**
    * Closes the global log file if any log file was open.
    *
    * <p>If no log file exists no exception is thrown. 
    * Closes the log file <B>and</B> it's OutputStream.</p>
    *
    * @see Log#getGlobalLog
    * @see Log#setGlobalOutputStream
    */
    public static synchronized void closeGlobalLog() {
        if (theGlobalLog != null) {
            theGlobalLog.closeLog();
            theGlobalLog = null;
            theGlobalOutputStream = null;
        }
    }

    /**
    * Closes this Log file.
    *
    * @see Log#closeGlobalLog
    */
    public synchronized void closeLog() {
        changeOutputStream(null);
    }

    /**
    * Called by the garbage collector on an object when garbage collection determines that
    * there are no more references to the object.
    *
    * <p>Disposes of this log file. If this is the global log file, calls closeGlobalLog(),
    * else calls closeLog().</p>
    *
    * @see Log#closeLog
    * @see Log#closeGlobalLog
    */
    protected void finalize() {
        if (this == theGlobalLog) closeGlobalLog(); else closeLog();
    }

    /**
    * Changes the current OutputStream of the global log file.
    * <p>This method <STRONG>must</STRONG> be called before any global log operation
    * takes place.</p>
    * <p>If an OutputStream exists it will be closed automatically. To close the entire
    * global log file use closeGlobalLog().</p>
    *
    * @param newOutputStream the new global output stream
    *
    * @see Log#getGlobalLog
    * @see Log#closeGlobalLog
    * @see Log#changeOutputStream
    */
    public static synchronized void setGlobalOutputStream(ObjectOutputStream newOutputStream) {
        theGlobalOutputStream = newOutputStream;
        if (theGlobalLog != null) theGlobalLog.changeOutputStream(newOutputStream);
    }

    /**
    * Change this log's outputstream.
    *
    * <p>If an outputstream exists it is closed.</p>
    *
    * @param oo the new output stream.
    * @see Log#setGlobalOutputStream
    */
    public synchronized void changeOutputStream(ObjectOutputStream oo) {
        if (ooOutput != null) logCloseLog();
        ooOutput = oo;
        if (ooOutput != null) logOpenLog();
    }

    /**
    * Add a log entry when closing the log file.
    *
    * <p>Currently does nothing. You can override it to write a log entry when the log 
    * file is being closed.</p>
    *
    * @see Log#closeLog
    */
    protected void logCloseLog() {
    }

    /**
    * Add a log entry when opening the log file.
    *
    * <p>Currently does nothing. You can override it to write a log entry when the log
    * file is being opened.</p>
    *
    * @see Log#changeOutputStream
    */
    protected void logOpenLog() {
    }

    /** Adds one entry to the log file. Calls l.getLogData().
    * 
    * @param l the event to be logged.
    * @see Loggable 
    *
    * @exception LogNoOutputStreamException if no OutputStream has been specified.
    * @exception IOException if an IOException occurs when writing to the stream.
    *
    * @see LogDisplay
    */
    public synchronized void log(Loggable l) throws LogNoOutputStreamException, IOException {
        if (ooOutput == null) throw new LogNoOutputStreamException("on Log.log ( " + l + " )");
        LogEntry le = l.getLogData();
        ooOutput.writeObject(le);
    }
}
