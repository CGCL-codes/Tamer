package com.framedobjects.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * <p>Represents a single query. It can be used either as an atomic query to the
 * database using the <code>findObject()</code> and <code>execute()</code>
 * methods. These use a dedicated database connection.</p>
 * <p>The same query can be used within a multiple database request cycle. In 
 * this case the query is passed to the respective method of a 
 * <code>SQLMultipleRequest</code> instance.
 *  
 * @author Jens Richnow
 *
 */
public abstract class SQLQuery extends SQLAbstractQuery {

    public List execute(Object[] params) {
        List list = new Vector();
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            statement = conn.prepareStatement(this.sql);
            setParameters(params, statement);
            ResultSet rs = statement.executeQuery();
            Object object = null;
            while (rs.next()) {
                object = mapRow(rs);
                list.add(object);
            }
        } catch (SQLException sqlex) {
            logger.error("SQLException in execute(Object[]): " + sqlex.getMessage());
        } catch (SQLParameterException sqlpex) {
            logger.error("SQLParameterException in execute(Object[]): " + sqlpex.getMessage());
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(conn);
        }
        return list;
    }

    public List execute(Connection connection, Object[] params) throws SQLException, SQLParameterException {
        List list = new Vector();
        PreparedStatement statement = connection.prepareStatement(this.sql);
        setParameters(params, statement);
        ResultSet rs = statement.executeQuery();
        Object object = null;
        while (rs.next()) {
            object = mapRow(rs);
            list.add(object);
        }
        DBUtils.closeStatement(statement);
        return list;
    }

    /**
   * All other convenient <code>findObject()</code> methods fall back to this.
   * 
   * @param params  The query parameters.
   * @return        An object if the result set returns data, otherwise 
   *                <code>null</code>.
   */
    public Object findObject(Object[] params) {
        Object obj = null;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            statement = conn.prepareStatement(this.sql);
            setParameters(params, statement);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                obj = mapRow(rs);
            }
        } catch (SQLException sqlex) {
            logger.error("SQLException in findObject(Object[]): " + sqlex.getMessage());
        } catch (SQLParameterException sqlpex) {
            logger.error("SQLParameterException in findObject(Object[]): " + sqlpex.getMessage());
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(conn);
        }
        return obj;
    }

    /**
   * <p>This method is used implicitely when running the query in a multiple
   * database request cycle. There should be no need to call this method 
   * yourself as it is called by the respective methods of an instance of
   * <code>SQLMultipleRequest</code>.</p>
   * 
   * @param conn    The database connection.
   * @param params  The SQL statement parameters if present.
   * @return        An object if the result set returns data, otherwise 
   *                <code>null</code>.
   * @throws SQLException
   * @throws SQLParameterException
   */
    public Object findObject(Connection conn, Object[] params) throws SQLException, SQLParameterException {
        Object obj = null;
        PreparedStatement statement = conn.prepareStatement(this.sql);
        setParameters(params, statement);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            obj = mapRow(rs);
        }
        DBUtils.closeStatement(statement);
        return obj;
    }
}
