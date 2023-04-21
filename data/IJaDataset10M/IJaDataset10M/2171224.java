package de.ueppste.misc;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stellt Funktionen bereit um einfach auf eine MySql Datenbank zugreifen
 * zu können.
 * @author bernhard
 */
public class MySqlCon {

    /** Logger */
    private static Logger logger = Logger.getLogger(MySqlCon.class.getName());

    /** Verbindung zu Datenbank */
    private Connection connection;

    /** JDBC-Treiber-Name. Muss im Klassenpfad sein. */
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    /** Url */
    private String url;

    /** Database */
    private String database;

    /** User */
    private String user;

    /** Passwort */
    private String pass;

    /**
     * Konstruktor fuellt die entsprechenden Attribute
     * @param url		Url
     * @param database	Database
     * @param user		User
     * @param pass		Passwort
     */
    public MySqlCon(String url, String database, String user, String pass) {
        this.url = url;
        this.database = database;
        this.user = user;
        this.pass = pass;
    }

    /**
	 * Verbindung zur Datenbank herstellen. 
	 * @throws Exception 
	 */
    public void connect() throws Exception {
        try {
            Class.forName(MySqlCon.DRIVER).newInstance();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Der JDBC-Treiber konnte nicht geladen werden.");
            throw ex;
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + this.url + "/" + this.database + "?user=" + this.user + "&password=" + this.pass);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Die Verbindung zur Datenbank konnte nicht hergestellt werden. " + "Die Fehlermeldung lautet: " + ex.getMessage());
            throw ex;
        }
        logger.log(Level.FINE, "Verbindung zu Datenbank hergestellt");
    }

    /**
	 * Verbindung zur Datenbank trennen
	 * @throws Exception 
	 */
    public void disconnect() throws Exception {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                this.connection = null;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Die Verbindung zur Datenbank konnte nicht geschlossen werden. " + "Die Fehlermeldung lautet: " + ex.getMessage());
            throw ex;
        }
    }

    /**
	 * Gibt ein Statment
	 * @return Statement
	 * @throws SQLException
	 */
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    /**
	 * Gibt ein PreparedStatement
	 * @param query	SQL-Query
	 * @return PreparedStatement
	 * @throws SQLException
	 */
    public PreparedStatement preparedStatement(String query) throws SQLException {
        return this.connection.prepareStatement(query);
    }

    /**
	 * Gibt ein PreparedStatement
	 * @param query	Sql-Query
	 * @param flag	GeneratedKeys-Flag
	 * @return PreparedStatment
	 * @throws SQLException
	 */
    public PreparedStatement preparedStatement(String query, int flag) throws SQLException {
        return this.connection.prepareStatement(query, flag);
    }

    protected void finalize() {
        try {
            this.disconnect();
        } catch (Exception e) {
        }
    }
}
