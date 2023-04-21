package net.sf.solarnetwork.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import net.sf.solarnetwork.util.ClassUtils;

/**
 * Base class for JDBC based DAO implementations.
 * 
 * <p>This emulates a small subset of Spring DAO patterns, without depending on Spring.
 * Extending classes must set up the {@link DataSource} by calling 
 * {@link #setDataSource(DataSource)} before using any of the methods in this class.</p>
 *
 * @author matt
 * @version $Revision: 41 $ $Date: 2008-07-25 20:31:17 -0400 (Fri, 25 Jul 2008) $
 */
public abstract class AbstractJdbcDao {

    /** A class-level Logger. */
    protected final Logger log = Logger.getLogger(getClass().getName());

    private DataSource dataSource;

    /**
	 * API for performing work within a SQL transaction.
	 */
    public static interface TransactionCallback {

        /**
		 * Perform some work with a Connection.
		 * 
		 * <p>The connection's autoCommit will be off when this method
		 * is called, and once it returns {@link Connection#commit()}
		 * will be called to commit the transaction. If any exception 
		 * is thrown by this method, the transaction will be rolled back.</p>
		 * 
		 * @param conn the connection
		 * @return some object
		 * @throws SQLException if any SQL error occurs
		 */
        public Object doInTransaction(Connection conn) throws SQLException;
    }

    /**
	 * API for extracting an object from a ResultSet.
	 */
    public static interface ResultSetExtractor {

        /**
		 * Extract a single object from a ResultSet.
		 * 
		 * @param rs the ResultSet to extract from
		 * @return some object
		 * @throws SQLException if any SQL error occurs
		 */
        public Object extractFromResultSet(ResultSet rs) throws SQLException;
    }

    /**
	 * API for setting values on a PreparedStatement.
	 */
    public static interface PreparedStatementSetter {

        /**
		 * Set a PreparedStatement's values.
		 * 
		 * @param ps the PreparedStatement
		 * @throws SQLException if any SQL error occurs
		 */
        public void setValues(PreparedStatement ps) throws SQLException;
    }

    /**
	 * Test if a table exists in the database.
	 * 
	 * @param conn the connection
	 * @param schemaName the schema name to look for (or <em>null</em> for any schema)
	 * @param tableName the table name to look for
	 * @return boolean if table is found
	 * @throws SQLException if any SQL error occurs
	 */
    protected boolean tableExists(Connection conn, String schemaName, String tableName) throws SQLException {
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet rs = null;
        try {
            rs = dbMeta.getTables(null, null, null, null);
            while (rs.next()) {
                String schema = rs.getString(2);
                String table = rs.getString(3);
                if ((schemaName == null || (schemaName != null && schemaName.equalsIgnoreCase(schema))) && tableName.equalsIgnoreCase(table)) {
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Found table " + schema + '.' + table);
                    }
                    return true;
                }
            }
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
	 * Execute a SQL transaction.
	 * 
	 * @param callback the callback to perform work within the transaction
	 * @return the object returned from the TransactionCallback
	 */
    protected Object executeTransaction(TransactionCallback callback) {
        Connection conn = null;
        boolean prevAutoCommit = false;
        try {
            conn = this.dataSource.getConnection();
            prevAutoCommit = conn.getAutoCommit();
            if (prevAutoCommit) {
                conn.setAutoCommit(false);
            }
            Object result = callback.doInTransaction(conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    log.log(Level.WARNING, "Unable to rollback SQL transaction", e);
                }
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                if (prevAutoCommit) {
                    try {
                        conn.setAutoCommit(prevAutoCommit);
                    } catch (SQLException e) {
                        throw new RuntimeException("Unable to restore autoCommit", e);
                    }
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Unable to commit SQL transaction", e);
                }
            }
        }
    }

    /**
	 * Execute a SQL query and extract a single result from the ResultSet.
	 * 
	 * @param conn the connection
	 * @param sql the SQL to execute
	 * @param params parameters to set on the SQL statement, or null if none
	 * @param extractor the ResultSetExtractor to extract the results
	 * @return an Object
	 */
    protected Object executeQuery(Connection conn, String sql, Object[] params, ResultSetExtractor extractor) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            return extractor.extractFromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
	 * Execute a SQL query and extract a single result from the ResultSet.
	 * 
	 * @param conn the connection
	 * @param sql the SQL to execute
	 * @param setter the PreparedStatementSetter to use for setting 
	 * statement values
	 * @param extractor the ResultSetExtractor to extract the results
	 * @return an Object
	 */
    protected Object executeQuery(Connection conn, String sql, PreparedStatementSetter setter, ResultSetExtractor extractor) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            if (setter != null) {
                setter.setValues(ps);
            }
            rs = ps.executeQuery();
            return extractor.extractFromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
	 * Execute a SQL update and return the count of affected rows.
	 * 
	 * @param conn the connection
	 * @param sql the SQL to execute
	 * @param params parameters to set on the SQL statement, or null if none
	 * @return the number of rows affected
	 */
    protected int executeUpdate(Connection conn, String sql, Object[] params) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
	 * Execute a SQL update and return the count of affected rows.
	 * 
	 * @param conn the connection
	 * @param sql the SQL to execute
	 * @param setter the PreparedStatementSetter to use for setting 
	 * statement values
	 * @return the number of rows affected
	 */
    protected int executeUpdate(Connection conn, String sql, PreparedStatementSetter setter) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            setter.setValues(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
	 * Execute a SQL update and return the count of affected rows.
	 * 
	 * @param conn the connection
	 * @param sql the SQL to execute
	 * @param setter the PreparedStatementSetter to use for setting 
	 * statement values
	 * @return the number of rows affected
	 */
    protected List<?> executeUpdateWithAutogeneratedKeys(Connection conn, String sql, PreparedStatementSetter setter) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setter.setValues(ps);
            ps.executeUpdate();
            List<Object> results = new LinkedList<Object>();
            rs = ps.getGeneratedKeys();
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                if (rs.next()) {
                    for (int i = 1; i <= colCount; i++) {
                        results.add(rs.getObject(i));
                    }
                }
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
	 * Execute arbitrary SQL statements and return the total number of rows affected.
	 * 
	 * <p>Any number of updates or queries can be passed in {@code sql}. If 
	 * {@code setter} is provided, the query will have parameters set on it. For 
	 * each update result, an Integer will be added to the result list. If
	 * {@code extractor} is specified, for each ResultSet returned by the query, the
	 * result of invoking {@link ResultSetExtractor#extractFromResultSet(ResultSet)} 
	 * will be added to the result list.</p>
	 * 
	 * @param conn the connection
	 * @param sql the SQL to execute
	 * @param setter the PreparedStatementSetter to use for setting 
	 * statement values
	 * @param extractor the ResultSetExtractor to extract from the results.
	 * @return list of update and/or query results
	 */
    protected List<?> execute(Connection conn, String sql, PreparedStatementSetter setter, ResultSetExtractor extractor) {
        PreparedStatement ps = null;
        List<Object> results = new LinkedList<Object>();
        int updateCount = -1;
        try {
            ps = conn.prepareStatement(sql);
            if (setter != null) {
                setter.setValues(ps);
            }
            boolean haveResults = ps.execute();
            if (haveResults) {
                handleResultSet(ps, extractor, results);
            } else {
                updateCount = ps.getUpdateCount();
                results.add(updateCount);
            }
            while (true) {
                haveResults = ps.getMoreResults();
                updateCount = ps.getUpdateCount();
                if (!haveResults && updateCount == -1) {
                    break;
                }
                if (haveResults) {
                    handleResultSet(ps, extractor, results);
                } else if (updateCount > -1) {
                    results.add(updateCount);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return results;
    }

    /**
	 * Execute a batch set of commands.
	 * 
	 * @param conn the connection
	 * @param sql the SQL commands to execute
	 * @return the update counts
	 */
    protected int[] executeBatch(Connection conn, String[] sql) {
        Statement batch = null;
        try {
            batch = conn.createStatement();
            for (String oneSql : sql) {
                batch.addBatch(oneSql);
            }
            return batch.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (batch != null) {
                try {
                    batch.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
	 * Load a classpath SQL resource into a String.
	 * 
	 * @param resourceName the SQL resource to load
	 * @return the String
	 */
    protected String getSqlResource(String resourceName) {
        return ClassUtils.getResourceAsString(resourceName, getClass());
    }

    /**
	 * Get batch SQL statements, split into multiple statements on the
	 * {@literal ;} character.
	 * 
	 * @param sqlResource the SQL resource to load
	 * @return split SQL
	 */
    protected String[] getBatchSqlResource(String sqlResource) {
        String sql = getSqlResource(sqlResource);
        if (sql == null) {
            return null;
        }
        return sql.split(";\\s*");
    }

    private void handleResultSet(PreparedStatement ps, ResultSetExtractor extractor, List<Object> results) {
        if (extractor != null) {
            ResultSet rs = null;
            try {
                rs = ps.getResultSet();
                results.add(extractor.extractFromResultSet(ps.getResultSet()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
	 * @return the dataSource
	 */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
	 * @param dataSource the dataSource to set
	 */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
