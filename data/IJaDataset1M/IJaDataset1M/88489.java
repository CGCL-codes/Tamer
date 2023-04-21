package com.juliashine.db.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Juliashine	2011-4-29 
 * 
 */
public class DataSourceUtils {

    private static final Log logger = LogFactory.getLog(DataSourceUtils.class);

    public static Connection getConnection(DataSource dataSource) throws SQLException {
        try {
            return doGetConnection(dataSource);
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        logger.debug("Fetching JDBC Connection from DataSource");
        Connection con = dataSource.getConnection();
        return con;
    }

    public static void releaseConnection(Connection con, DataSource dataSource) {
        try {
            doReleaseConnection(con, dataSource);
        } catch (SQLException ex) {
            logger.debug("Could not close JDBC Connection", ex);
        } catch (Throwable ex) {
            logger.debug("Unexpected exception on closing JDBC Connection", ex);
        }
    }

    public static void doReleaseConnection(Connection con, DataSource dataSource) throws SQLException {
        if (con == null) {
            return;
        }
        con.close();
    }

    public static void applyTimeout(Statement stmt, DataSource dataSource, int timeout) throws SQLException {
    }
}
