package controller;

import java.sql.Connection;

/**
 *
 * @author Mirza
 */
public interface DatabaseConnection {

    abstract void setDatabaseConnection(String databaseProvider, String hostName, String databaseName, String userName, String password);

    abstract Connection getConnection();
}
