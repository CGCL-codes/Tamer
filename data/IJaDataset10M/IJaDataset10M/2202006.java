package com.lagerplan.basisdienste.util.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.log4j.Logger;
import util.logging.LogLoader;
import db.ConnectionTracker;
import db.MyDB;
import db.MySQL;
import util.config.XProperties;

/**
 * This class implements the methods necessary for reading from and writing
 * into a specific DB. 
 * 
 * If you need a new connection pool for a new DB, do the following:
 * (1) Follow instructions in SkexDB.java (or IlpDB.java)
 * (2) Search & Replace IlpDB by MyClassDB, HermesSQL by MyClassSQL in here
 *
 * @author Patrick Dockhorn
 * @version $Revision: 12188 $, $Date: 2006-04-27 14:50:46 +1000 (Thu, 27 Apr 2006) $
 */
public class ExcelSQL extends MySQL {

    protected static Logger log = LogLoader.getLogger(ExcelSQL.class);

    private static ExcelDB db = ExcelDB.getInstance();

    private static XProperties properties = ExcelDB.properties;

    /**
	 * class constructor gets an instance of IlpDB
	 */
    public ExcelSQL() {
        try {
            ExcelSQL.db = ExcelDB.getInstance();
        } catch (Exception e) {
            log.error("Error during initialization of database: " + e.getMessage());
        }
    }

    /**
	 * @return properties
	 */
    protected XProperties getProperties() {
        return properties;
    }

    /**
	 * @return SkexDB
	 */
    protected MyDB getDB() {
        return db;
    }

    /**
	 * @return poolname
	 */
    public String getPoolName() {
        return db.getPoolName();
    }

    /**
	 * @return log
	 */
    protected Logger getLogger() {
        return log;
    }

    public ResultSet executeReadSQL(String strSQLKey) throws Exception {
        return executeReadSQL(strSQLKey, (Vector) null);
    }

    public ResultSet executeReadSQL(String strSQLKey, Vector vParams) throws Exception {
        ResultSet oResultSet = null;
        String sqlStatement = this.getProperties().getProperty(strSQLKey);
        log.debug("SQL statement to be processed: " + sqlStatement);
        if (vParams != null) {
            log.debug("parameters were submitted, have to create valid statement...");
            for (int i = 0; i < vParams.size(); i++) {
                String para = (String) vParams.elementAt(i);
                sqlStatement = sqlStatement.replaceFirst("\\?", para);
            }
            log.debug("sql statement after inserting parameters: " + sqlStatement);
        } else {
            log.debug("no additional parameters submitted, take sql statement as it is");
        }
        try {
            this.connection = this.getDB().getConnection();
            this.statement = this.connection.createStatement();
            oResultSet = this.statement.executeQuery(sqlStatement);
        } catch (Exception e) {
            log.error("Error setting up a sql statement: " + e.getLocalizedMessage());
            throw e;
        }
        return oResultSet;
    }

    public int executeWriteSQL(String strSQLKey, Vector vParams) throws Exception {
        int result = 0;
        String sqlStatement = this.getProperties().getProperty(strSQLKey);
        log.debug("SQL statement to be processed: " + sqlStatement);
        if (vParams != null) {
            log.debug("parameters were submitted, have to create valid statement...");
            for (int i = 0; i < vParams.size(); i++) {
                String para = (String) vParams.elementAt(i);
                para = "'" + para + "'";
                sqlStatement = sqlStatement.replaceFirst("\\?", para);
            }
            log.debug("sql statement after inserting parameters: " + sqlStatement);
        } else {
            log.debug("no additional parameters submitted, take sql statement as it is");
        }
        try {
            this.connection = this.getDB().getConnection();
            this.statement = this.connection.createStatement();
            result = this.statement.executeUpdate(sqlStatement);
        } catch (Exception e) {
            log.error("Error setting up a sql statement: " + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Closes the opened connection (if still opened) and after that the opened
     * PreparedStatement (if still opened)
     */
    public void close() {
        try {
            if (connection != null) {
                if (isTraceConnections()) {
                    Throwable t = new Throwable();
                    ConnectionTracker ct = getTracker();
                    if (null != ct) {
                        ct.traceCloseConnection(connection, t);
                    } else {
                        log.error("Cannot trace closeing of connection as no connection tracker defined.", t);
                    }
                }
                if (!ExcelDB.isAutoCommit()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Committing transactions for connection [" + connection.toString() + "] as AutoCommit is off");
                    }
                    connection.commit();
                }
                ExcelDB.backToPool(connection);
                connection.close();
                connection = null;
                if (log.isDebugEnabled()) log.debug("successfully returned connection to pool and set to null afterwards");
            } else {
                log.warn("Failed to close connection as the connection object is NULL.", new Throwable());
            }
            closeStatement();
        } catch (SQLException e) {
            log.error("Error closing connection and PreparedStatement: " + e.getMessage(), e);
        }
    }
}
