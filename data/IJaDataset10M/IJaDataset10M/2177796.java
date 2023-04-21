package org.dbpt.performance;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class TransactionItem extends ContainerItem {

    private DataSource dataSource;

    private Connection connection;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void cleanup() {
        setConnection(null);
        super.cleanup();
    }

    @Override
    public void prepare() {
        try {
            connection = dataSource.getConnection();
            setConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.prepare();
    }

    @Override
    public void doIteration() {
        try {
            connection.setAutoCommit(false);
            super.doIteration();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
