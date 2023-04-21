package it.simplerecords.connection;

import java.sql.Connection;

public interface ConnectionProvider {

    public Connection getConnection() throws Exception;
}
