package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.operation.DatabaseOperation;
import fit.Fixture;

public class DatabaseContext extends Fixture {

    private IDatabaseConnection databaseConnection;

    private static DefaultDataSet dataSet;

    public DatabaseContext() {
        dataSet = new DefaultDataSet();
    }

    public static DefaultDataSet getDataSet() {
        return dataSet;
    }

    private static String driverName;

    private static String url;

    private static String user;

    private static String password;

    public void driverName(String name) {
        this.driverName = name;
    }

    public void url(String url) {
        this.url = url;
    }

    public void user(String user) {
        this.user = user;
    }

    public void password(String password) {
        this.password = password;
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static DatabaseConnection createDatabaseConnection() throws ClassNotFoundException, SQLException {
        return new DatabaseConnection(getConnection());
    }

    protected void connect() throws ClassNotFoundException, SQLException {
        databaseConnection = createDatabaseConnection();
    }

    public void truncate() throws Exception {
        connect();
        DatabaseOperation.TRUNCATE_TABLE.execute(databaseConnection, dataSet);
        disconnect();
    }

    protected void disconnect() throws SQLException {
        databaseConnection.close();
    }

    public void insert() throws DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(databaseConnection, dataSet);
    }

    public void cleanInsert() throws DatabaseUnitException, SQLException, ClassNotFoundException {
        connect();
        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        disconnect();
    }

    public void resetDataSet() {
        dataSet = new DefaultDataSet();
    }
}
