package org.j2eebuilder.util;

import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)RowSetFactory.java	1.350 01/12/03
 * Suns' rowset implementation requires JDBC drivers to provide prepareStatement.isSecured(),
 * .isSigned(), isAutoIncrement(), and other column schema values. If driver throws exception
 * Sun's rowset instead of handling such triveal issues, propogates it.
 *
 */
public class RowSetFactory implements java.io.Serializable {

    private static transient LogManager log = new LogManager(RowSetFactory.class);

    public RowSet getRowSet(String selectStatement, PreparedStatementParameter[] parameters, String dataSourceName) throws RowSetFactoryException {
        if (selectStatement == null || selectStatement.trim().length() == 0 || dataSourceName == null) return null;
        Connection connection = null;
        CachedRowSet rset = null;
        try {
            connection = ConnectionFactory.getCurrentInstance().getConnection(dataSourceName);
            PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
            if (parameters != null) for (int i = 0; i < parameters.length; i++) {
                prepStmt.setObject(parameters[i].getIndex(), parameters[i].getValue());
            }
            ResultSet rs = prepStmt.executeQuery();
            rset = new CachedRowSet();
            rset.populate(rs);
            rs.close();
            prepStmt.close();
            return rset;
        } catch (SQLException e) {
            throw new RowSetFactoryException("getRowSet():" + e.getMessage());
        } catch (ConnectionFactoryException e) {
            throw new RowSetFactoryException("getRowSet():" + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                connection = null;
                throw new RowSetFactoryException("getRowSet().finally{}:" + e.getMessage());
            }
        }
    }
}
