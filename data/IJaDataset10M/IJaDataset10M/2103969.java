package org.apache.harmony.sql.tests.javax.sql;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

class Impl_PooledConnection implements PooledConnection {

    public void addConnectionEventListener(ConnectionEventListener theListener) {
    }

    public void close() throws SQLException {
    }

    public Connection getConnection() throws SQLException {
        return null;
    }

    public void removeConnectionEventListener(ConnectionEventListener theListener) {
    }

    public void addStatementEventListener(StatementEventListener listener) {
    }

    public void removeStatementEventListener(StatementEventListener listener) {
    }
}
