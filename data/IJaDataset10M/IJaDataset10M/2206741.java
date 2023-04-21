package net.sf.advjavacalendar.data.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import net.sf.advjavacalendar.data.DataException;

/**
 * Base class for all data access objects
 * 
 * @author probstl
 */
public abstract class BaseDAO<E> {

    /** Logger */
    private static Logger log = Logger.getLogger(BaseDAO.class);

    /** Connection to the database */
    private Connection m_Connection;

    /**
     * Returns the name of the table for this dao
     * 
     * @return
     */
    public abstract String getTableName();

    /**
     * Returns the primary columns of the table
     * 
     * @return
     */
    public abstract String[] getPKs();

    /**
     * Extracts an object from the current row of the ResultSet
     * 
     * @param result
     * @return
     * @throws SQLException
     */
    public abstract E extract(ResultSet result) throws SQLException;

    /**
     * Retrieves a new connection to the database with the given URL
     * 
     * @param url
     * @return
     * @throws DataException
     * @throws SQLException
     */
    public Connection getConnection() throws DataException {
        if (m_Connection == null) {
            try {
                m_Connection = DriverManager.getConnection("jdbc:derby:data;create=true");
            } catch (SQLException e) {
                throw new DataException("Error connecting to database", e);
            }
        }
        return m_Connection;
    }

    /**
     * @throws DataException
     * @throws SQLException
     * @see net.sf.advjavacalendar.data.jdbc.DAO#existsTable()
     */
    public boolean existsTable() throws DataException {
        boolean tableExists = false;
        DatabaseMetaData meta = null;
        ResultSet tables = null;
        try {
            meta = getConnection().getMetaData();
            tables = meta.getTables(null, null, "%", new String[] { "TABLE" });
            while (tables.next()) {
                if (tables.getString("TABLE_NAME").equalsIgnoreCase(getTableName())) {
                    tableExists = true;
                }
            }
        } catch (SQLException e) {
            throw new DataException("Error checking table " + getTableName(), e);
        } finally {
            close(tables);
        }
        return tableExists;
    }

    /**
     * Returns a prepared statement with the given columns
     * 
     * @return
     * @throws DataException
     */
    protected PreparedStatement getPreparedStatement(String sql) throws DataException {
        try {
            log.debug("Preparing statement: " + sql);
            return getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw new DataException("Error creating prepared statement " + sql, e);
        }
    }

    /**
     * Returns a prepared statement with the given columns
     * 
     * @return
     * @throws DataException
     */
    protected PreparedStatement getPreparedDeleteStatement(String where) throws DataException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(getTableName());
        buffer.append(" WHERE ");
        buffer.append(where);
        return getPreparedStatement(buffer.toString());
    }

    /**
     * Returns a prepared statement with the given columns
     * 
     * @return
     * @throws DataException
     */
    protected PreparedStatement getPreparedUpdateStatement(String... columns) throws DataException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE ");
        buffer.append(getTableName());
        buffer.append(" SET ");
        for (int i = 0; i < columns.length; i++) {
            buffer.append(columns[i]);
            buffer.append("=?, ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append(" WHERE ");
        for (int i = 0; i < getPKs().length; i++) {
            buffer.append(getPKs()[i]);
            buffer.append("=? AND ");
        }
        buffer.delete(buffer.length() - 4, buffer.length());
        return getPreparedStatement(buffer.toString());
    }

    /**
     * Returns a prepared statement with the given columns
     * 
     * @return
     * @throws DataException
     */
    protected PreparedStatement getPreparedInsertStatement(String... columns) throws DataException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO ");
        buffer.append(getTableName());
        buffer.append("( ");
        for (int i = 0; i < columns.length; i++) {
            buffer.append(columns[i]);
            buffer.append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append(") VALUES( ");
        for (int i = 0; i < columns.length; i++) {
            buffer.append("?, ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append(")");
        return getPreparedStatement(buffer.toString());
    }

    /**
     * Creates a new Statement on the connection and executes the given SQL statement
     * 
     * @param c
     * @param sql
     * @throws DataException
     */
    protected void executeStmt(Connection c, String sql) throws DataException {
        Statement stmt = null;
        try {
            log.debug("Executing statement: " + sql);
            stmt = c.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DataException("Error executing statement " + sql, e);
        } finally {
            close(stmt);
        }
    }

    /**
     * Creates a new Statement on the connection and executes the given SQL statement
     * 
     * @param c
     * @param sql
     * @throws DataException
     */
    protected boolean executeStatement(PreparedStatement stmt) throws DataException {
        try {
            return stmt.execute();
        } catch (SQLException e) {
            throw new DataException("Error executing statement", e);
        } finally {
            close(stmt);
        }
    }

    /**
     * Returns a list of the type queried by the given sql
     * 
     * @param sql
     * @return
     * @throws DataException
     */
    protected List<E> query(String sql) throws DataException {
        PreparedStatement stmt = getPreparedStatement(sql);
        return query(stmt, (Object[]) null);
    }

    /**
     * Queries the given prepared statement with the given parameters
     * 
     * @param stmt
     * @param params
     * @return
     * @throws DataException
     */
    protected List<E> query(PreparedStatement stmt, Object... params) throws DataException {
        List<E> toReturn = new ArrayList<E>();
        log.debug("Quering Prepared Statement");
        ResultSet result = null;
        try {
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    log.debug("Setting parameter " + (i + 1) + " to " + params[i].toString());
                    stmt.setObject(i + 1, params[i]);
                }
            }
            result = stmt.executeQuery();
            while (result.next()) {
                toReturn.add(extract(result));
            }
        } catch (SQLException e) {
            throw new DataException("Error selecting data from table", e);
        } finally {
            close(result);
            close(stmt);
        }
        log.debug("Found items: " + toReturn.size());
        return toReturn;
    }

    /**
     * Closes the given connection
     * 
     * @param c
     */
    protected void close(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                log.warn("Error closing Connection", e);
            }
        }
    }

    /**
     * Closes the given Statement
     * 
     * @param stmt
     */
    protected void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.warn("Error closing Statement", e);
            }
        }
    }

    /**
     * Closes the given ResultSet
     * 
     * @param resultSet
     */
    protected void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.warn("Error closing ResultSet", e);
            }
        }
    }
}
