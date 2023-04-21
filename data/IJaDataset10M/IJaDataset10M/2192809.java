package org.judo.database.orm.mappers;

import java.sql.Connection;
import java.sql.SQLException;
import org.judo.database.JudoDBConnection;
import org.judo.database.exceptions.DatabaseException;

public class SimpleJDBCConnection implements JudoDBConnection {

    Connection con = null;

    boolean hasFinalized = false;

    public SimpleJDBCConnection(Connection con) {
        this.con = con;
    }

    public void closeConnection() throws DatabaseException {
        try {
            con.close();
            hasFinalized = true;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            throw new DatabaseException("Error closing Simple JDBC databse connection: " + e);
        }
    }

    public void commitTransaction() throws DatabaseException {
        try {
            con.commit();
            hasFinalized = true;
        } catch (SQLException e) {
            throw new DatabaseException("Error commiting on Simple JDBC databse connection: " + e);
        }
    }

    public Object getConnectionService() {
        return con;
    }

    public boolean hasFinalized() {
        return hasFinalized;
    }

    public void rollbackTransaction() throws DatabaseException {
        try {
            con.rollback();
            hasFinalized = true;
        } catch (SQLException e) {
            throw new DatabaseException("Error commiting on Simple JDBC databse connection: " + e);
        }
    }

    public void startTransaction() throws DatabaseException {
    }
}
