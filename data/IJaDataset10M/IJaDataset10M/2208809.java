package net.ko.ksql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class KDbResultSet {

    private ResultSet resultSet;

    private KDataBase database;

    public HashMap<String, Object> toMap() throws SQLException {
        ResultSetMetaData rsmdt = resultSet.getMetaData();
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 1; i <= rsmdt.getColumnCount(); i++) {
            Object o = null;
            try {
                o = resultSet.getObject(i);
            } catch (Exception e) {
            }
            if (o != null) map.put(rsmdt.getColumnLabel(i), o);
        }
        return map;
    }

    public KDbResultSet(ResultSet resultSet) {
        super();
        this.resultSet = resultSet;
    }

    public KDbResultSet(ResultSet resultSet, KDataBase db) {
        this(resultSet);
        database = db;
    }

    public boolean isBeforeFirst() throws SQLException {
        return resultSet.isBeforeFirst();
    }

    public boolean isAfterLast() throws SQLException {
        return resultSet.isAfterLast();
    }

    public boolean next() throws SQLException {
        return resultSet.next();
    }

    public Connection getConnection() {
        try {
            return resultSet.getStatement().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        String ret = "";
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public KDataBase getDatabase() {
        return database;
    }
}
