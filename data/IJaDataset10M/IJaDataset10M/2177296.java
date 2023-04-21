package org.jinion.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinion.database.connectionPool.ConnectionManager;
import org.jinion.database.model.AbstractResult;
import org.jinion.database.model.impl.DefaultResult;
import org.jinion.exception.JinionRuntimeException;

/**
 * @author zhlmmc
 *
 */
public abstract class AbstractDatabase {

    private static final Log log = LogFactory.getLog(AbstractDatabase.class);

    private ConnectionManager conManager;

    protected ThreadLocal<List<WhereClause>> whereClauses = new ThreadLocal<List<WhereClause>>();

    protected ThreadLocal<Connection> connections = new ThreadLocal<Connection>();

    public AbstractDatabase(ConnectionManager manager) throws ClassNotFoundException, SQLException {
        conManager = manager;
    }

    /**
	 * Get the connection for current thread. If there is no connection for current thread, a new connection will be allocated from ConnectionManager.
	 * @return
	 */
    protected Connection getConnection() {
        Connection con = connections.get();
        if (con != null) {
            return con;
        }
        try {
            con = conManager.getConnection();
            connections.set(con);
            return con;
        } catch (Exception e) {
            throw new JinionRuntimeException(e);
        }
    }

    /**
	 * Release the connection of current thread to ConnectionManager.
	 * If the Connection is on a transaction, it won't be released.
	 */
    protected void releaseConnection() {
        Connection con = connections.get();
        try {
            if (con != null && con.getAutoCommit()) {
                connections.set(null);
                conManager.releaseConnection(con);
            }
        } catch (SQLException e) {
            log.debug(e);
        }
    }

    protected List<WhereClause> getWhereClauses() {
        List<WhereClause> clauses = this.whereClauses.get();
        if (clauses == null) {
            clauses = new LinkedList<WhereClause>();
            this.whereClauses.set(clauses);
        }
        return clauses;
    }

    /**
	 * Class for where clause
	 * @author zhlmmc
	 *
	 */
    protected class WhereClause {

        private String fieldName;

        private Object value;

        private int compareType;

        private int clauseType;

        /**
		 * 
		 * @param fieldName
		 * @param value
		 * @param compareType
		 */
        public WhereClause(String fieldName, Object value, int compareType, int clauseType) {
            this.fieldName = fieldName;
            this.value = value;
            this.compareType = compareType;
            this.clauseType = clauseType;
        }

        public int getCompareType() {
            return compareType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Object getValue() {
            return value;
        }

        public int getClauseType() {
            return clauseType;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    /**
	 * 
	 * @throws SQLException If something is wrong when start transaction by <code>Connection.setAutoCommit(false)</code>.
	 */
    public void beginTransaction() throws SQLException {
        Connection conn = this.getConnection();
        if (!conn.getAutoCommit()) {
            throw new SQLException("Thansaction for this thread already has already begun.");
        }
        conn.setAutoCommit(false);
        log.debug("Transaction started for thread: " + Thread.currentThread().getName());
    }

    /**
	 * If transaction has not begun, nothing happens.
	 * @throws SQLException 
	 */
    public void commitTransaction() throws SQLException {
        Connection conn = this.getConnection();
        if (conn.getAutoCommit()) {
            return;
        }
        conn.commit();
        conn.setAutoCommit(true);
        releaseConnection();
        log.debug("Transaction committed for thread: " + Thread.currentThread().getName());
    }

    /**
	 * If transaction has not begun, nothing happens.
	 * @throws SQLException
	 */
    public void rollback() throws SQLException {
        Connection conn = this.getConnection();
        if (conn.getAutoCommit()) {
            return;
        }
        conn.rollback();
        conn.setAutoCommit(true);
        releaseConnection();
        log.debug("Transaction rolled back for thread: " + Thread.currentThread().getName());
    }

    /**
	 * Execute the given sql. The sql may be any type of sql. Only three values returned <br> 
	 * (updateCount, generatedKeys, resultSet) from <code>statement.execute(sql)</code> will be saved in <br>
	 * the returned <code>AbstractResult</code>
	 * 
	 * @param sql standard sql
	 * @return The execution result.
	 * @throws SQLException
	 */
    public AbstractResult executeSql(String sql) throws SQLException {
        log.debug("Execute sql: " + sql);
        Statement statement = getConnection().createStatement();
        statement.execute(sql);
        AbstractResult result = new DefaultResult(statement.getUpdateCount(), statement.getGeneratedKeys(), statement.getResultSet());
        releaseConnection();
        return result;
    }

    /**
	 * Execute the given sql with the given parameters.Only three values returned <br> 
	 * (updateCount, generatedKeys, resultSet) from <code>statement.execute(sql)</code> will be saved in <br>
	 * the returned <code>AbstractResult</code>
	 * 
	 * @param sql sql with preparaed paremeters eg. "UPDATE user SET name=?". 
	 * @param parameters - parameters for sql.
	 * @return The execution result.
	 * @throws SQLException
	 */
    public AbstractResult executeSql(String sql, String[] parameters) throws SQLException {
        log.debug("Execute sql: " + sql);
        log.debug("Execute parameters: " + parameters);
        PreparedStatement ps = getConnection().prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            ps.setString(i + 1, parameters[i]);
        }
        ps.execute();
        AbstractResult result = new DefaultResult(ps.getUpdateCount(), ps.getGeneratedKeys(), ps.getResultSet());
        releaseConnection();
        return result;
    }

    /**
	 * Do a query to the specified table. The effect of this method is same with <br>
	 * <i>SELECT * FROM tableName...</i><br>
	 * <b>NOTE: This method will clear where clauses</b>
	 * 
	 * @param tableName the name of the table to be queried. 
	 * @return the returned data
	 * @throws SQLException
	 */
    public AbstractResult query(String tableName) throws SQLException {
        return this.query(tableName, null);
    }

    /**
	 * Do a query to the specified table. The effect of this method is same with <br>
	 * <i>SELECT * FROM tableName...ORDER BY orderBy</i><br>
	 * <b>NOTE: This method will clear where clauses</b>
	 * 
	 * @param tableName the name of the table
	 * @param orderBy the order column name
	 * @return the returned data
	 */
    public AbstractResult query(String tableName, String orderBy) throws SQLException {
        return this.query(tableName, orderBy, SqlConstants.ASC);
    }

    /**
	 * Do a query to the specified table. The effect of this method is same with <br>
	 * <i>SELECT * FROM tableName...ORDER BY orderBy DESC(ASC)</i><br>
	 * <b>NOTE: This method will clear where clauses</b>
	 * 
	 * @param tableName the name of the table
	 * @param orderBy the order column name
	 * @param ascOrDesc descend or ascend
	 * @return the returned data
	 */
    public AbstractResult query(String tableName, String orderBy, int ascOrDesc) throws SQLException {
        String sql = this.buildQuerySql(tableName, orderBy, ascOrDesc);
        return this.executeSql(sql);
    }

    /**
	 * Do a update to the specified table. All items with column name key in map will be updated. <br>
	 * <b>NOTE: This method will clear where clauses</b>
	 * 
	 * @param tableName the name of table
	 * @param data data to updated
	 * @return the returned data.
	 * @throws SQLException
	 */
    public AbstractResult update(String tableName, Map<Object, Object> data) throws SQLException {
        String sql = this.buildUpdateSql(tableName, data);
        return this.executeSql(sql);
    }

    /**
	 * Do an insert to the specified table. All items with column name key in map will be inserted. <br>
	 * <b>NOTE: This method will clear where clauses</b>
	 * 
	 * @param tableName the name of table
	 * @param data data to be inserted
	 * @return	The returned data.
	 * @throws SQLException
	 */
    public AbstractResult insert(String tableName, Map<Object, Object> data) throws SQLException {
        String sql = this.buildInsertSql(tableName, data);
        return this.executeSql(sql);
    }

    /**
	 * Delete records from the given table. 
	 * 
	 * @param tableName the name of the table
	 * @return	the returned data.
	 * @throws SQLException
	 */
    public AbstractResult delete(String tableName) throws SQLException {
        String sql = this.buildDeleteSql(tableName);
        if (sql != null) {
            return this.executeSql(sql);
        } else {
            return null;
        }
    }

    /**
	 * Add a where clause. This clause is an "and" clause if it is not at the head of the list. <br>
	 * The compare type of this method is IDatabase.COMPARE_TYPE_EQUAL. <br>
	 * This medhod assumes that if the string of <i>value</i> is the same with any column name, <br> 
	 * the <i>value</i> denotes the column not the string. If you want to use the string of the <br> 
	 * <i>value</i> instead of the column, add single quotes around <i>value</i>.
	 * 
	 * @param fieldName the column name
	 * @param value value of field
	 * @return current database for where chain
	 */
    public AbstractDatabase where(String fieldName, Object value) {
        return this.where(fieldName, value, SqlConstants.EQUEAL);
    }

    /**
	 * Add a where clause. This clause is an "and" clause if it is not at the head of the list.
	 * This medhod assumes that if the string of <i>value</i> is the same with any column name, <br> 
	 * the <i>value</i> denotes the column not the string. If you want to use the string of the <br> 
	 * <i>value</i> instead of the column, add single quotes around <i>value</i>.
	 * 
	 * @param fieldName the column name
	 * @param value value of field
	 * @param compareType	IDatabase.COMPARE_TYPE_EQUAL, IDatabase.COMPARE_TYPE_UNEQUAL
	 * 						, IDatabase.COMPARE_TYPE_GREATER_THAN, IDatabase.COMPARE_TYPE_LESS_THAN,
	 * 						IDatabase.COMPARE_TYPE_LIKE
	 * @return current database for where chain
	 */
    public AbstractDatabase where(String fieldName, Object value, int compareType) {
        WhereClause wc = new WhereClause(fieldName, value, compareType, SqlConstants.AND);
        this.getWhereClauses().add(wc);
        return this;
    }

    /**
	 * Add a where clause. This clause is an "or" clause.
	 * The compare type of this method is IDatabase.COMPARE_TYPE_EQUAL. <br>
	 * This medhod assumes that if the string of <i>value</i> is the same with any column name, <br> 
	 * the <i>value</i> denotes the column not the string. If you want to use the string of the <br> 
	 * <i>value</i> instead of the column, add single quotes around <i>value</i>.
	 * 
	 * @param fieldName the column name
	 * @param value value of field
	 * @return current database for where chain
	 */
    public AbstractDatabase orWhere(String fieldName, Object value) {
        return this.orWhere(fieldName, value, SqlConstants.EQUEAL);
    }

    /**
	 * Add a where clause. This clause is an "or" clause. 
	 * This medhod assumes that if the string of <i>value</i> is the same with any column name, <br> 
	 * the <i>value</i> denotes the column not the string. If you want to use the string of the <br> 
	 * <i>value</i> instead of the column, add single quotes around <i>value</i>.
	 * 
	 * @param fieldName the column name
	 * @param value value of field
	 * @param compareType	IDatabase.COMPARE_TYPE_EQUAL, IDatabase.COMPARE_TYPE_UNEQUAL
	 * 						, IDatabase.COMPARE_TYPE_GREATER_THAN, IDatabase.COMPARE_TYPE_LESS_THAN,
	 * 						IDatabase.COMPARE_TYPE_LIKE
	 * @return current database for where chain
	 */
    public AbstractDatabase orWhere(String fieldName, Object value, int compareType) {
        if (this.getWhereClauses().isEmpty()) {
            throw new UnsupportedOperationException("Missing where caluse! You should call \"where()\" first!");
        }
        WhereClause wc = new WhereClause(fieldName, value, compareType, SqlConstants.OR);
        this.getWhereClauses().add(wc);
        return this;
    }

    /**
	 * Clear where clauses of this AbstractDatabase object and this thread.
	 */
    public void clearWhereClauses() {
        this.getWhereClauses().clear();
    }

    /**
	 * Query sql is a little complicated, so separate it with others. 
	 * 
	 * @param sqlType
	 * @param tableNames
	 * @param filedNames
	 * @param Data
	 * @param orderBy
	 * @param ascOrDesc
	 * @return
	 * @throws SQLException
	 */
    protected abstract String buildQuerySql(String tableName, String orderBy, int ascOrDesc) throws SQLException;

    /**
	 * 
	 * @param tableNames
	 * @param filedNames
	 * @param Data
	 * @return
	 * @throws SQLException
	 */
    protected abstract String buildUpdateSql(String tableName, Map<Object, Object> data) throws SQLException;

    /**
	 * 
	 * @param tableNames
	 * @param filedNames
	 * @param Data
	 * @return
	 * @throws SQLException
	 */
    protected abstract String buildInsertSql(String tableName, Map<Object, Object> data) throws SQLException;

    /**
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
    protected abstract String buildDeleteSql(String tableName) throws SQLException;
}
