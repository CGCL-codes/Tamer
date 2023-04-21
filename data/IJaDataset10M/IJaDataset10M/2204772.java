package org.horus.miniframewrk;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementa un dao básico de un objeto de dominio cuya llave primaria es un
 * BigInteger
 * @author AVEGA
 *
 * @param <T> El tipo de objeto de dominio.
 */
public abstract class BigDecimalDAO<T> extends JdbcDAO<T> implements DataAccessObject<T, BigDecimal> {

    protected BigDecimalDAO(String name) {
        super(name);
    }

    public T get(BigDecimal key) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            ps = prepareStatement("get");
            ps.setBigDecimal(1, key);
            rs = ps.executeQuery();
            if (rs.next()) return buildObject(rs); else throw new SQLException("Object not found with key: " + key);
        } finally {
            ps.close();
        }
    }

    public void delete(BigDecimal key) throws SQLException {
        PreparedStatement ps = null;
        int deleted;
        try {
            ps = prepareStatement("delete");
            ps.setBigDecimal(1, key);
            deleted = ps.executeUpdate();
            if (deleted != 1) throw new SQLException("Delete different than 1: " + deleted);
        } finally {
            ps.close();
        }
    }

    public BigDecimal nextId() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            ps = prepareStatement("nextId");
            rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal(1); else throw new SQLException("No new ID generated");
        } finally {
            ps.close();
        }
    }
}
