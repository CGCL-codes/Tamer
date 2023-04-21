package de.uni_bremen.informatik.sopra.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Die Grundlagenklasse für die Anbindung an die Datenbank. Hier wird der
 * Treiber geladen, die Verbindung hergestellt, Statements an die Datenbank
 * gegeben und die Verbindung geschlossen.
 * 
 * @author Sopra Growie
 * @version schaut ins CVS
 */
public abstract class DB {

    /**
	 * Die Connection mit der später die Verbindung zum Datenbankserver
	 * hergestellt wird.
	 */
    private static Connection connection;

    /** Mit Statements werden die Befehle für die Datenbank vorbereitet. */
    private static Statement statement;

    protected static boolean initialized = false;

    protected boolean fromDB = false;

    /**
	 * Initalisierung der Datenbank über Informationen aus der Server.xml von
	 * Tomcat.
	 * 
	 * 
	 * @throws SQLException
	 * @throws NamingException
	 */
    private static void init() throws NamingException, SQLException {
        final Context ic = new InitialContext();
        final Context envContext = (Context) ic.lookup("java:/comp/env");
        final DataSource ds = (DataSource) envContext.lookup("jdbc/sopra-datasource");
        System.out.println(ds.toString());
        connection = ds.getConnection();
        statement = connection.createStatement();
        initialized = true;
    }

    /**
	 * Um Anfragen an die Datenbank zu stellen.
	 * 
	 * @param sql
	 *            Die SQL-Datenbankanfrage als String.
	 * @return ResultSet Die Ergebnistabelle auf die Anfrage.
	 * @throws Exception
	 */
    public static synchronized ResultSet exeQuery(final String sql) throws Exception {
        if (!initialized) {
            init();
        }
        return statement.executeQuery(sql);
    }

    /**
	 * Um die etwas in die Datenbank einzutragen bzw. zu aktualisieren.
	 * 
	 * @param sql
	 *            Das SQL-Statment als String.
	 * @return int Die Anzahl der veränderten Stellen in der Tabelle.
	 * @throws Exception
	 */
    public static synchronized int update(final String sql) throws Exception {
        if (!initialized) {
            init();
        }
        return statement.executeUpdate(sql);
    }

    /**
	 * close() beendet die Verbindung mit der Datenbank und schliesst so auch
	 * alle Statements.
	 * 
	 * @throws SQLException
	 */
    public static synchronized void close() throws SQLException {
        connection.close();
        initialized = false;
    }

    /**
	 * Abstracte Methode die in den Basisklassen das synchronizieren mit der
	 * Datenbank übernimmt.
	 * 
	 * @throws Exception
	 */
    public abstract void sync() throws Exception;
}
