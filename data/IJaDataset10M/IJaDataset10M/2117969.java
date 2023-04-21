package de.axxeed.jambox.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import de.axxeed.jambox.JaMBox;

/**
 * @author Markus J. Luzius <br>
 * created: 03.01.2011 18:26:41
 *
 */
public class DBConnection {

    private static final Logger log = Logger.getLogger(DBConnection.class);

    private static boolean newDB = false;

    private static Connection conn;

    private static final String DB_PATH = System.getProperty("user.home") + "/." + (JaMBox.isTest() ? "a" : "") + "JaMBox/JaMBox_db";

    private DBConnection() {
    }

    public static boolean dbExists() {
        return !newDB;
    }

    /**
	 * Checks the version of the database and initiates a database update, if necessary
	 */
    public static void checkDB() {
        String sql = "select VERSION from VERSION order by DBDATE desc";
        Statement stmt = null;
        try {
            stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                DBUpdate.dbUpdate(rs.getString(1));
            } else {
                DBUpdate.dbUpdate(null);
            }
        } catch (SQLException e) {
            log.error("error in executing statement <" + sql + ">");
            DBUpdate.dbUpdate(null);
            return;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    public static String getDBPath() {
        return DB_PATH;
    }

    static Connection getConnection() {
        if (conn == null) {
            log.debug("DB path=" + DB_PATH);
            try {
                Class.forName("org.hsqldb.jdbc.JDBCDriver");
            } catch (Exception e) {
                log.error("failed to load HSQLDB JDBC driver", e);
                return null;
            }
            try {
                conn = DriverManager.getConnection("jdbc:hsqldb:file:" + DB_PATH + ";ifexists=true", "SA", "");
            } catch (SQLException e) {
                if (e.getMessage().indexOf("does not exist") >= 0) {
                    newDB = true;
                    log.debug("database does not exist (yet)");
                    try {
                        conn = DriverManager.getConnection("jdbc:hsqldb:file:" + DB_PATH, "SA", "");
                    } catch (SQLException e1) {
                        log.error("failed to create hsql database", e);
                        return null;
                    }
                    DBUpdate.dbInit();
                    DBUpdate.dbUpdate(null);
                    return conn;
                }
                log.error("failed to obtain a connection", e);
            }
        }
        return conn;
    }

    static void doSQL(String sql) {
        Statement stmt = null;
        try {
            stmt = getConnection().createStatement();
            log.debug(sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            log.error("error in executing statement <" + sql + ">: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
            }
        }
    }
}
