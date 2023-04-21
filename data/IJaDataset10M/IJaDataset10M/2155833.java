package com.jcorporate.expresso.core.logging;

import com.jcorporate.expresso.core.db.DBConnectionPool;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.services.dbobj.LogEntry;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * JDBC type appender for Log4j
 * @author Michael Rimov
 * @version $Revision: 3 $ $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public class DBAppender extends AppenderSkeleton {

    private static final int ERROR = Priority.ERROR.toInt();

    private static final int WARN = Priority.WARN.toInt();

    private static final int INFO = Priority.INFO.toInt();

    private static final int DEBUG = Priority.DEBUG.toInt();

    private static final String thisClass = "com.jcorporate.expresso.core.logging.DBAppender.";

    private static int maxLevel = 9;

    private static DBConnectionPool myPool = null;

    /**
     *
     */
    public DBAppender() {
    }

    /**
     *
     *
     * @return true all the time
     */
    public boolean requiresLayout() {
        return true;
    }

    /**
     * Uses Expresso's Log Handler.
     *
     * @param   parm1 no longer used
     */
    protected void append(LoggingEvent parm1) {
        final String myName = "com.jcorporate.expresso.core.logging.DBAppender";
        String theMessage = layout.format(parm1);
        String theColor = "R";
        int priorityInt = parm1.getLevel().toInt();
        if (priorityInt <= DEBUG) {
            theColor = "G";
        } else if (priorityInt <= INFO) {
            theColor = "B";
        } else if (priorityInt <= WARN) {
            theColor = "Y";
        } else if (priorityInt <= ERROR) {
            theColor = "R";
        }
        priorityInt /= 10000;
        try {
            this.log(priorityInt, parm1.getLoggerName(), theMessage, theColor);
            if (layout.ignoresThrowable() && parm1.getThrowableInformation() != null) {
                this.log(parm1.getLoggerName(), parm1.getThrowableInformation().getThrowable());
            }
        } catch (LogException e) {
            LogLog.error(myName + ":Unable to log message '" + theMessage + "' from category '" + parm1.getLoggerName() + "':", e);
        }
    }

    /**
     * Log an exception from a particular object
     *
     * @param    objectName Calling object
     * @param    e Exception to log
     * @throws LogException upon error
     */
    protected static void log(String objectName, Throwable e) throws LogException {
        String myName = (thisClass + "log(String, Exception)");
        String message = e.getMessage();
        if (e instanceof DBException) {
            DBException de = (DBException) e;
            message = de.getMessage() + ":" + de.getDBMessage();
        }
        try {
            LogEntry myLog = new LogEntry(SecuredDBObject.SYSTEM_ACCOUNT);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(bos));
            myLog.setField("MessageText", bos.toString());
            myLog.setField("MessageColor", "R");
            myLog.setField("ObjectName", objectName);
            LogHandler.staticAddToQueue(myLog);
        } catch (DBException de) {
            throw new LogException(myName + ":" + de.getMessage());
        }
    }

    /**
     * Log a message from a particular object with a color at the given
     * level
     *
     * @param    messageLevel Message level to log
     * @param    objectName Calling object
     * @param    msg Message to log
     * @param    color Color to log the message with.  One character 'R' 'G' 'B' or 'Y'
     * @throws    LogException if the message cannot be logged
     */
    protected static void log(int messageLevel, String objectName, String msg, String color) throws LogException {
        String myName = (thisClass + "log(int, String, String, " + "String)");
        try {
            LogEntry myLog = new LogEntry(SecuredDBObject.SYSTEM_ACCOUNT);
            myLog.setField("ObjectName", objectName);
            myLog.setField("MessageText", msg);
            myLog.setField("MessageColor", color);
            myLog.setField("MessageLevel", "" + messageLevel);
            myLog.setTimeStamp();
            LogHandler.staticAddToQueue(myLog);
        } catch (DBException de) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            de.printStackTrace(new PrintStream(bos));
            LogLog.error(myName + ":Unable to log '" + msg + "'" + bos.toString());
        }
    }

    /**
     *
     */
    public void close() {
        final String myName = thisClass + "close()";
        try {
            LogHandler.flush();
        } catch (LogException e) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(bos));
            LogLog.error(myName + ":Unable to flush LogHandler '" + e.getMessage() + "'" + bos.toString());
        }
    }
}
