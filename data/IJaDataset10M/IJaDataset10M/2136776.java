package com.ma_la.myRunning;

import org.apache.log4j.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * TODO 
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public class RunningDBConnection implements Serializable {

    private static org.apache.log4j.Logger log = Logger.getLogger(RunningDBConnection.class);

    private DataSource datasource;

    public static final RunningDBConnection ONLY_ONE = new RunningDBConnection();

    private RunningDBConnection() {
        datasource = getDataSource();
    }

    public static RunningDBConnection getInstance() {
        return ONLY_ONE;
    }

    /**
	 * todo
	 * @param strSQL
	 * @return
	 */
    public ResultSet execute_connection(String strSQL) {
        Date before = new Date();
        try {
            Connection conn = datasource.getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                try {
                    java.util.Date after = new java.util.Date();
                    long duration = after.getTime() - before.getTime();
                    String logLine = strSQL;
                    if (duration > 0) {
                        logLine += "; Execution-Time: " + duration + " ms";
                    }
                    log.error(logLine);
                    return stmt.executeQuery(strSQL);
                } catch (SQLException e) {
                    log.error("Fehler in der RunningDB-ConnectionBean, Methode execute_connection! strSQL: " + strSQL, e);
                    try {
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException ex) {
                        log.fatal("Fehler in der RunningDB-ConnectionBean, Methode execute_connection! Close connections: " + strSQL, e);
                    }
                }
            } else {
                log.fatal("Fehler in der RunningDBConnection, Methode execute_connection: keine DB-Connection INNER. datasource: " + datasource);
            }
        } catch (SQLException e) {
            log.error("SQLException in der RunningDBConnection, Methode execute_connection: keine DB-Connection OUTER", e);
        }
        return null;
    }

    public int execute_update(String strSQL) {
        Statement stmt;
        Connection conn;
        int anz = 0;
        try {
            conn = datasource.getConnection();
            if (conn != null) {
                try {
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    log.error(strSQL);
                    anz = stmt.executeUpdate(strSQL);
                    stmt.close();
                } catch (SQLException e) {
                    log.error("Fehler in der RunningDB-ConnectionBean, Methode execute_update: " + e + "; strSQL: " + strSQL, e);
                }
                conn.close();
            } else {
                log.error("Fehler in der RunningDB-DBConnectionBean, Methode execute_update: keine DB-Connection INNER");
            }
        } catch (SQLException e) {
            log.error("Fehler in der RunningDB-DBConnectionBean, Methode execute_update: keine DB-Connection OUTER", e);
        }
        return anz;
    }

    public int getNewPK(String strTableName) {
        int newID = 1;
        String strSQL = "Select Max(id) as MAXID" + " From " + strTableName;
        ResultSet rsMaxID = execute_connection(strSQL);
        try {
            if (rsMaxID != null) {
                rsMaxID.next();
                if (rsMaxID.getString("MAXID") != null) {
                    newID = (rsMaxID.getInt("MAXID") + 1);
                }
            }
        } catch (SQLException e) {
            log.error("SQL exception in der RunningDBConnectionBean, Methode getNewPK", e);
        } finally {
            RunningSystemBean.closeResultSet(rsMaxID, "RunningDBConnection - getNewPK");
        }
        return newID;
    }

    public DataSource getDataSource() {
        Context ctx;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            log.fatal("Couldn't build an initial context", e);
            return null;
        }
        try {
            Object value = ctx.lookup("java:/comp/env/jdbc/myrunning");
            if (value instanceof DataSource) {
                return (DataSource) value;
            } else {
                log.fatal("JNDI lookup failed. Lookup result is not a DataSource!!");
                return null;
            }
        } catch (NamingException e) {
            log.fatal("JNDI lookup failed", e);
            return null;
        }
    }
}
