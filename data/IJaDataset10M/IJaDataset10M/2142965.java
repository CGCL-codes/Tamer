package org.nakedobjects.plugins.sql.objectstore.jdbc;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.SqlObjectStoreException;

public class JdbcResults implements Results {

    ResultSet set;

    public JdbcResults(final CallableStatement statement) {
    }

    public JdbcResults(final ResultSet set) {
        this.set = set;
    }

    public void close() {
        try {
            set.close();
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public int getInt(final String columnName) {
        try {
            return set.getInt(columnName);
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public long getLong(final String columnName) {
        try {
            return set.getLong(columnName);
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public String getString(final String columnName) {
        try {
            return set.getString(columnName);
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public boolean next() {
        try {
            return set.next();
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public Date getDate(final String columnName) {
        try {
            return set.getDate(columnName);
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }

    public Object getObject(final String columnName) {
        try {
            return set.getObject(columnName);
        } catch (SQLException e) {
            throw new SqlObjectStoreException(e);
        }
    }
}
