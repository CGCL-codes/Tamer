package de.ibis.permoto.loganalyzer.db;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.*;
import org.apache.log4j.Logger;

/**
 * @author nesapio
 *
 */
public class SqliteDataSource implements DataSource {

    private String URL;

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(SqliteDataSource.class);

    @Override
    public Connection getConnection() throws SQLException {
        Properties prop = new Properties();
        prop.setProperty("enable_load_extension", "true");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = DriverManager.getConnection(this.URL, prop);
        File test = resolveLibrary(System.getProperty("os.name"));
        String loadExtensionFunctions = "SELECT load_extension(\'" + test.getAbsolutePath() + "\');";
        logger.debug(loadExtensionFunctions);
        Statement stmt = con.createStatement();
        stmt.execute(loadExtensionFunctions);
        stmt.close();
        return con;
    }

    private File resolveLibrary(String osName) {
        String path = "";
        if (osName.contains("Windows")) {
            path = "native\\Windows\\x86\\extension-function.dll";
        } else if (osName.contains("Mac")) {
            path = "native\\Mac\\i386\\libsqlitefunctions.dylib";
        } else if (osName.contains("Linux")) {
            path = "native\\Linux\\i386\\libsqlitefunctions.so";
        } else {
            path = "native\\Windows\\x86\\extension-function.dll";
        }
        return new File(path);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Properties prop = new Properties();
        prop.setProperty("enable_load_extension", "true");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.debug(this.URL);
        Connection con = DriverManager.getConnection(this.URL, prop);
        File test = resolveLibrary(System.getProperty("os.name"));
        String loadExtensionFunctions = "SELECT load_extension(\'" + test.getAbsolutePath() + "\');";
        logger.debug(loadExtensionFunctions);
        Statement stmt = con.createStatement();
        stmt.execute(loadExtensionFunctions);
        stmt.close();
        return con;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    /**
	 * @return the uRL
	 */
    public String getURL() {
        return URL;
    }

    /**
	 * @param url the uRL to set
	 */
    public void setURL(String url) {
        URL = url;
    }
}
