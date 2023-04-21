package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
* @author Mattias Gees
*/
public class Database {

    private static final String DB_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";

    private static final String DB_URL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=snookerPoolBowling";

    private Connection connection;

    private static Database db;

    public static Database getInstance() {
        return db == null ? new Database() : db;
    }

    private Database() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException sqlException) {
        } catch (ClassNotFoundException classNotFound) {
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
            db = null;
        } catch (SQLException sqlException) {
        }
    }
}
