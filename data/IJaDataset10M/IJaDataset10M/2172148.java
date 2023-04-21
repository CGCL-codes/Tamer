package com.p6spy.engine.spy;

import java.sql.*;

public class P6Array extends P6Base implements java.sql.Array {

    protected Array passthru;

    protected P6Statement statement;

    protected String query;

    protected String preparedQuery;

    public P6Array(P6Factory factory, Array array, P6Statement statement, String preparedQuery, String query) {
        setP6Factory(factory);
        this.passthru = array;
        this.statement = statement;
        this.query = query;
        this.preparedQuery = preparedQuery;
    }

    public Object getArray() throws java.sql.SQLException {
        return passthru.getArray();
    }

    public Object getArray(java.util.Map p0) throws java.sql.SQLException {
        return passthru.getArray(p0);
    }

    public Object getArray(long p0, int p1) throws java.sql.SQLException {
        return passthru.getArray(p0, p1);
    }

    public Object getArray(long p0, int p1, java.util.Map map) throws java.sql.SQLException {
        return passthru.getArray(p0, p1, map);
    }

    public int getBaseType() throws java.sql.SQLException {
        return passthru.getBaseType();
    }

    public String getBaseTypeName() throws java.sql.SQLException {
        return passthru.getBaseTypeName();
    }

    public java.sql.ResultSet getResultSet() throws java.sql.SQLException {
        return getP6Factory().getResultSet(passthru.getResultSet(), statement, preparedQuery, query);
    }

    public java.sql.ResultSet getResultSet(java.util.Map p0) throws java.sql.SQLException {
        return getP6Factory().getResultSet(passthru.getResultSet(p0), statement, preparedQuery, query);
    }

    public java.sql.ResultSet getResultSet(long p0, int p1) throws java.sql.SQLException {
        return getP6Factory().getResultSet(passthru.getResultSet(p0, p1), statement, preparedQuery, query);
    }

    public java.sql.ResultSet getResultSet(long p0, int p1, java.util.Map p2) throws java.sql.SQLException {
        return getP6Factory().getResultSet(passthru.getResultSet(p0, p1, p2), statement, preparedQuery, query);
    }

    /**
     * Returns the underlying JDBC object (in this case, a
     * java.sql.Array)
     * @return the wrapped JDBC object 
     */
    public Array getJDBC() {
        Array wrapped = (passthru instanceof P6Array) ? ((P6Array) passthru).getJDBC() : passthru;
        return wrapped;
    }
}
