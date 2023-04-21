package my.usm.cs.utmk.blexisma2.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This a convenience class for establishing UTF-8 encoded connections to SQL
 * databases. Currently only connections to MySQL and PostgreSQL can be
 * established.
 * 
 * @author LIM Lian Tze
 * @version 1.0
 * 
 */
public class DBUtils {

    /**
	 * Establishes a UTF-8 encoded connection to a MySQL database.
	 * 
	 * @param host
	 *            MySQL server host (IP or name) to connect to.
	 * @param port
	 *            MySQL port number (usually 3306) to connect to.
	 * @param db
	 *            Name of MySQL database to connect to.
	 * @param user
	 *            Username to establish the connection.
	 * @param password
	 *            Password to establish the connection.
	 * @return An SQL connection to the specified database and host. All data
	 *         sent through the connection will be encoded in UTF-8.
	 */
    public static Connection getUTF8MySQLConnection(String host, int port, String db, String user, String password) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionURL = "jdbc:mysql://" + host + ":" + port + "/" + db + "?" + "user=" + user + "&password=" + password + "&useUnicode=true&" + "characterEncoding=utf-8";
            conn = DriverManager.getConnection(connectionURL);
            System.err.println("Connected to " + getConnInfo(conn) + " database " + db);
            if (conn != null) {
                Statement setEncodingStmt = conn.createStatement();
                setEncodingStmt.execute("SET NAMES utf8");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
	 * Establishes a UTF-8 encoded connection to a MySQL database.
	 * 
	 * @param host
	 *            MySQL server host (IP or name) to connect to.
	 * @param port
	 *            MySQL port number (usually 3306) to connect to.
	 * @param db
	 *            Name of MySQL database to connect to.
	 * @param user
	 *            Username to establish the connection.
	 * @param password
	 *            Password to establish the connection.
	 * @return An SQL connection to the specified database and host. All data
	 *         sent through the connection will be encoded in UTF-8.
	 */
    public static Connection getUTF8PostgreSQLConnection(String host, int port, String db, String user, String password) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            String connectionURL = "jdbc:postgresql://" + host + ":" + port + "/" + db + "?" + "user=" + user + "&password=" + password + "&charSet=UTF8&protocolVersion=2";
            conn = DriverManager.getConnection(connectionURL);
            System.err.println("Connected to " + getConnInfo(conn) + " server at " + host + ":" + port + ", database " + db);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
	 * Returns brief information about a database connection.
	 * 
	 * @param conn
	 *            The JDBC database connectino.
	 * @return A string containing the DBMS name and version number, and also
	 *         the database name.
	 */
    public static String getConnInfo(Connection conn) {
        DatabaseMetaData md;
        try {
            md = conn.getMetaData();
            return md.getDatabaseProductName() + " v" + md.getDatabaseProductVersion();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
