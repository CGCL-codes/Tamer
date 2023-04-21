package com.xavax.xstore;

import java.sql.*;
import java.util.Map;
import com.xavax.xstore.util.CollectionFactory;

class StatementManager {

    StatementManager() {
        _sets = CollectionFactory.hashMap();
        _deleteStr = null;
        _insertStr = null;
        _selectStr = null;
        _updateStr = null;
    }

    PreparedStatement deleteStatement(Connection con) throws SQLException {
        StatementSet stset = findSet(con);
        PreparedStatement result = stset.deleteStatement();
        if (result == null && _deleteStr != null) {
            result = con.prepareStatement(_deleteStr);
            stset.deleteStatement(result);
        }
        return result;
    }

    void deleteStatement(String stmt) {
        _deleteStr = stmt;
    }

    PreparedStatement insertStatement(Connection con) throws SQLException {
        StatementSet stset = findSet(con);
        PreparedStatement result = stset.insertStatement();
        if (result == null && _insertStr != null) {
            result = con.prepareStatement(_insertStr);
            stset.insertStatement(result);
        }
        return result;
    }

    void insertStatement(String stmt) {
        _insertStr = stmt;
    }

    PreparedStatement selectStatement(Connection con) throws SQLException {
        StatementSet stset = findSet(con);
        PreparedStatement result = stset.selectStatement();
        if (result == null && _selectStr != null) {
            result = con.prepareStatement(_selectStr);
            stset.selectStatement(result);
        }
        return result;
    }

    void selectStatement(String stmt) {
        _selectStr = stmt;
    }

    PreparedStatement updateStatement(Connection con) throws SQLException {
        StatementSet stset = findSet(con);
        PreparedStatement result = stset.updateStatement();
        if (result == null && _updateStr != null) {
            result = con.prepareStatement(_updateStr);
            stset.updateStatement(result);
        }
        return result;
    }

    void updateStatement(String stmt) {
        _updateStr = stmt;
    }

    StatementSet findSet(Connection con) {
        StatementSet stset = (StatementSet) _sets.get(con);
        if (stset == null) {
            stset = new StatementSet(con);
            _sets.put(con, stset);
        }
        return stset;
    }

    void close() {
        for (StatementSet set : _sets.values()) {
            set.close();
        }
    }

    private String _deleteStr;

    private String _insertStr;

    private String _selectStr;

    private String _updateStr;

    private Map<Connection, StatementSet> _sets;
}
