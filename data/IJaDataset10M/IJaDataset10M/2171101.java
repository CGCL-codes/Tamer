package com.jiexplorer.rcp.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface PooledConnection {

    public void close();

    public void init();

    public boolean validate();

    public Connection getConnection();

    public void createPrepStatements() throws SQLException;
}
