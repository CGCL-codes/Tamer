package com.dcivision.framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO: Put the db connection params as separate properties file, no need to have db type specific codes
 * 
 * @author samlin
 *
 */
public class DatabaseManager {

    public static int DATABASE_TYPE_MYSQL = 0;

    public static int DATABASE_TYPE_SQLSERVER = 1;

    public static int DATABASE_TYPE_ORACLE = 2;

    private static final Log log = LogFactory.getLog(DatabaseManager.class);

    private static Connection getConnection(int dbType) throws Exception {
        Connection connection;
        String URL = null;
        String user = null;
        String password = null;
        java.util.Properties properties = new Properties();
        if (dbType == DATABASE_TYPE_MYSQL) {
            URL = "jdbc:mysql://localhost:3306/ParaDM?useUnicode=true&amp;characterEncoding=UTF-8&amp;autoReconnect=true";
            user = "root";
            password = "hihewo";
            Class.forName("com.mysql.jdbc.Driver");
            properties.setProperty("username", user);
            properties.setProperty("password", password);
            properties.setProperty("autocommit", "false");
            properties.setProperty("maxActive", "60");
            properties.setProperty("defaultAutoCommit", "false");
        } else if (dbType == DATABASE_TYPE_SQLSERVER) {
            URL = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=eip318Dbunit;SelectMethod=cursor;maxActive=60;maxIdle=3;defaultAutoCommit=false;defaultReadOnly=false";
            user = "sa";
            password = "";
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        } else if (dbType == DATABASE_TYPE_ORACLE) {
            URL = "jdbc:oracle:thin:@//203.189.168.35:1521/ParaDM";
            user = "sa";
            password = "";
            Class.forName("oracle.jdbc.OracleDriver");
        } else {
            throw new Exception("Unknow Database Type");
        }
        connection = DriverManager.getConnection(URL, user, password);
        connection.setAutoCommit(true);
        return connection;
    }

    public static Connection getCurrentConnection() {
        try {
            return getConnection(DATABASE_TYPE_MYSQL);
        } catch (Exception e) {
            log.error(e, e);
            return null;
        }
    }
}
