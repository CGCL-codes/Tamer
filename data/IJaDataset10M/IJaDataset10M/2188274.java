package hambo.messaging.hambo_db;

import java.sql.*;
import hambo.svc.*;
import hambo.svc.log.Log;
import hambo.svc.log.LogServiceManager;
import hambo.svc.database.DBService;
import hambo.svc.database.DBConnection;
import hambo.util.HamboFatalException;

/**
 * A base class providing access to the messenger database.  Only intended for
 * use in the hambo_db messaging implementation package.
 */
class MaildbAccessor {

    private static DBService svc = null;

    /** Where to send log messages. */
    private static Log log = LogServiceManager.getLog("Hambo-Messaging");

    protected static DBConnection getConnection() {
        return getConnection(true);
    }

    protected static DBConnection getConnection(boolean autocommit) {
        if (svc == null) try {
            svc = (DBService) ServiceManager.lookupService("database", DBService.class, new ClientIdentity("Hambo-Messaging"));
        } catch (ServiceManagerException err) {
            throw new HamboFatalException("Failed to get database service", err);
        }
        DBConnection con = svc.allocateConnection("messenger");
        try {
            con.setAutoCommit(autocommit);
        } catch (SQLException err) {
            throw new HamboFatalException("Failed to setAutoCommit on connection", err);
        }
        return con;
    }

    protected static void releaseConnection(DBConnection con) {
        try {
            con.reset();
        } catch (Exception err) {
            logError("Failed to reset connection", err);
        }
        con.release();
    }

    /** Just for convenience */
    protected static void close(ResultSet rs) {
        if (rs != null) try {
            rs.close();
        } catch (Exception err) {
            logError("Failed to close resultset", err);
        }
    }

    /**
     * Conveniece - roll back a transaction and log a message on failure.
     * This is guaranteed not to throw an Exception.
     */
    protected static void rollback(DBConnection con) {
        try {
            con.rollback();
        } catch (Exception err) {
            logError("Failed to rollback transaction.", err);
        }
    }

    /**
     * Log an error message.
     * @param msg the message.
     * @param err what caused it.
     */
    static void logError(String msg, Exception err) {
        log.println(Log.ERROR, msg, err);
    }

    /**
     * Log an error message.
     * @param msg the message.
     */
    static void logError(String msg) {
        log.println(Log.ERROR, msg);
    }

    /**
     * Log an informative message.
     * @param msg the message.
     */
    static void logInfo(String msg) {
        log.println(Log.INFO, msg);
    }

    /**
     * Log a debug message.
     * @param msg the message.
     */
    static void logDebug(String msg) {
        log.println(Log.DEBUG3, msg);
    }
}
