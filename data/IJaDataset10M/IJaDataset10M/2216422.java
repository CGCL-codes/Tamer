package com.jspx.sober.jdbc;

import java.sql.Savepoint;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2007-2-11
 * Time: 19:16:25
  */
public class TransactionInfo implements Transaction {

    public Savepoint getSavepoint() {
        return savepoint;
    }

    public void setSavepoint(Savepoint savepoint) {
        this.savepoint = savepoint;
    }

    private Savepoint savepoint = null;

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    private String pointName = null;

    private Connection connection = null;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void clear() {
        savepoint = null;
        pointName = null;
    }
}
