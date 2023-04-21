package com.nonesole.persistence.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.nonesole.persistence.exception.OperationsException;

/**
 * Deal with object with blob.
 * @author JACK LEE
 * @version 1.0 - build in 2009-07-26
 */
public class PreparedStatementOperations implements ISQLOperations {

    private static final PreparedStatementOperations BO;

    private static final PreparedStatementDML PSDML;

    private static final SQLOperations SQLOP;

    static {
        BO = new PreparedStatementOperations();
        PSDML = PreparedStatementDML.getInstance();
        SQLOP = SQLOperations.getInstance();
    }

    private PreparedStatementOperations() {
    }

    public static PreparedStatementOperations getInstance() {
        return BO;
    }

    public void batchDelete(List<Object> obj, Connection conn) throws OperationsException {
        SQLOP.batchDelete(obj, conn);
    }

    public List<Object> batchInsert(List<Object> obj, Connection conn) throws OperationsException {
        try {
            conn.setAutoCommit(false);
            for (Object o : obj) PSDML.getInsertConnection(conn, o);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            throw new OperationsException("Batch insert is failed.");
        }
        return obj;
    }

    public List<Object> batchUpdate(List<Object> obj, Connection conn) throws OperationsException {
        try {
            conn.setAutoCommit(false);
            for (Object o : obj) PSDML.getUpdateConnection(conn, o);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            throw new OperationsException("Batch insert is failed.");
        }
        return obj;
    }

    public void delete(Object obj, Connection conn) throws OperationsException {
        SQLOP.delete(obj, conn);
    }

    public void execute(String sql, Connection conn) throws OperationsException {
        SQLOP.execute(sql, conn);
    }

    public Object insert(Object obj, Connection conn) throws OperationsException {
        try {
            conn.setAutoCommit(false);
            PSDML.getInsertConnection(conn, obj);
            conn.commit();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            throw new OperationsException("Insert is failed.");
        }
        return null;
    }

    public List<Object> query(String sql, Class<?> clazz, Connection conn) throws OperationsException {
        return SQLOP.query(sql, clazz, conn);
    }

    public Object update(Object obj, Connection conn) throws OperationsException {
        try {
            conn.setAutoCommit(false);
            PSDML.getUpdateConnection(conn, obj);
            conn.commit();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            throw new OperationsException("Batch insert is failed.");
        }
        return obj;
    }
}
