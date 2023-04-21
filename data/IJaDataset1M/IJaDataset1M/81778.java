package com.incendiaryblue.logging;

import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.database.ConnectionException;
import com.incendiaryblue.database.Database;
import java.sql.*;
import org.w3c.dom.*;

/**
 * The DatabaseLogWriter abstract class is the base class for all log writers
 * that need to access a database.
 */
public abstract class DatabaseLogWriter extends LogWriter {

    private Database database;

    /**
	 * The XML config file element for a DatabaseLogWriter may contain an
	 * attribute 'database'. This should contain the name of the Database
	 * element this class should use to obtain a connection. If not supplied the
	 * default (ie no name) Database element will be used.
	 */
    public void setup(Element e) {
        String databaseName = e.getAttribute("database");
        if (databaseName == null) {
            databaseName = "";
        }
        database = (Database) AppConfig.getComponent(Database.class, databaseName);
    }

    public void log(LogEvent event) {
        Connection conn = database.getConnection();
        try {
            log(event, conn);
        } catch (SQLException e) {
            throw new ConnectionException(e);
        } finally {
            database.releaseConnection(conn);
        }
    }

    /**
	 * This method should be implemented by every subclass. The Connection
	 * object passed to it will be a connection to the database specified by the
	 * database attribute in the XML config for this object. Subclasses should
	 * not close the connection when it is no longer required.
	 */
    public abstract void log(LogEvent event, Connection connection) throws SQLException;
}
