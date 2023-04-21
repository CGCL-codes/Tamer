package cgl.shindig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.h2.tools.Server;
import org.h2.util.StringUtils;

/**
 * This class can be used to start the H2 TCP server (or other H2 servers, for
 * example the PG server) inside a web application container such as Tomcat or
 * Jetty. It can also open a database connection.
 */
public class H2DbStarter implements ServletContextListener {

    private Connection conn;

    private Server server;

    private void createAdminUserTable() throws SQLException {
        String sqlstr = "" + " CREATE TABLE IF NOT EXISTS adminuser" + " (id 		INTEGER  NOT NULL AUTO_INCREMENT PRIMARY KEY," + " screenname VARCHAR(128) NOT NULL UNIQUE," + " password	VARCHAR(50) NOT NULL" + " );";
        if (conn != null) {
            Statement stmt = conn.createStatement();
            stmt.execute(sqlstr);
        }
    }

    private void createUserTable() throws SQLException {
        String sqlstr = "" + " CREATE TABLE IF NOT EXISTS user" + " (id 		INTEGER  NOT NULL AUTO_INCREMENT PRIMARY KEY," + " firstname 	VARCHAR(50) NOT NULL," + " lastname	VARCHAR(50) NOT NULL," + " screenname VARCHAR(32) NOT NULL UNIQUE," + " nickname   VARCHAR(32)," + " openid     VARCHAR(128)," + " password	VARCHAR(50) NOT NULL," + " dateofbirth DATE," + " language   VARCHAR(10)," + " timezone   VARCHAR(10)," + " email      VARCHAR(64)," + " gender     CHAR(1)," + " zipcode    VARCHAR(16)" + " );";
        if (conn != null) {
            Statement stmt = conn.createStatement();
            stmt.execute(sqlstr);
        }
    }

    private void createLayoutTable() throws SQLException {
        String sqlstr = "CREATE TABLE IF NOT EXISTS layout" + " (id 		INTEGER  NOT NULL AUTO_INCREMENT PRIMARY KEY," + " screenname CHAR(128) NOT NULL UNIQUE," + " layout     TEXT(1024000)" + ");";
        if (conn != null) {
            Statement stmt = conn.createStatement();
            stmt.execute(sqlstr);
        }
    }

    public void createUser() throws SQLException {
        String userName = "portal_admin", password = "admin_pass";
        String sqlstr = "create user if not exists " + userName + " password '" + password + "'";
        String sqlstr1 = "grant all on user to " + userName + ";";
        String sqlstr2 = "grant all on layout to " + userName + ";";
        String sqlstr3 = "grant all on adminuser to " + userName + ";";
        if (conn != null) {
            Statement stmt = conn.createStatement();
            stmt.execute(sqlstr);
            stmt = conn.createStatement();
            stmt.execute(sqlstr1);
            stmt = conn.createStatement();
            stmt.execute(sqlstr2);
            stmt = conn.createStatement();
            stmt.execute(sqlstr3);
        }
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            org.h2.Driver.load();
            ServletContext servletContext = servletContextEvent.getServletContext();
            String url = getParameter(servletContext, "db.url", "jdbc:h2:~/test");
            String user = getParameter(servletContext, "db.user", "sa");
            String password = getParameter(servletContext, "db.password", "sa");
            conn = DriverManager.getConnection(url, user, password);
            createUserTable();
            createAdminUserTable();
            createLayoutTable();
            createUser();
            conn.close();
            String serverParams = getParameter(servletContext, "db.tcpServer", null);
            if (serverParams != null) {
                String[] params = StringUtils.arraySplit(serverParams, ' ', true);
                server = Server.createTcpServer(params);
                server.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getParameter(ServletContext servletContext, String key, String defaultValue) {
        String value = servletContext.getInitParameter(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Get the connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return conn;
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (server != null) {
            server.stop();
            server = null;
        }
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
