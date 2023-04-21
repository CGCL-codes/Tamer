package org.chon.core.common.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class PooledDatabase extends AbstactDB {

    private DataSource ds;

    public PooledDatabase(DataSource ds) {
        this.ds = ds;
    }

    public void close() {
    }

    public Connection connect() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
