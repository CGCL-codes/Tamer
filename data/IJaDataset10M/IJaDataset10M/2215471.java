package org.jaqlib.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.jaqlib.core.DataSourceQueryException;
import org.jaqlib.core.bean.FieldMapping;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;
import org.jaqlib.db.util.DbUtil;
import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.LogUtil;

/**
 * Abstract base class for database data sources.
 * 
 * @author Werner Fragner
 */
public abstract class AbstractDbDataSource {

    protected final Logger log = LogUtil.getLogger(this);

    private final DataSource dataSource;

    private SqlTypeHandlerRegistry sqlTypeHandlerRegistry = DbDefaults.INSTANCE.getSqlTypeHandlerRegistry();

    private Connection connection;

    private Statement statement;

    private final Map<String, PreparedStatement> prepStatements = CollectionUtil.newDefaultMap();

    private boolean autoCloseConnection = DbDefaults.INSTANCE.isAutoCloseConnection();

    private boolean autoClosePreparedStatement = DbDefaults.INSTANCE.isAutoClosePreparedStatement();

    public AbstractDbDataSource(DataSource dataSource) {
        this.dataSource = Assert.notNull(dataSource);
    }

    /**
   * <p>
   * If <tt>true</tt> the database {@link Connection} is automatically closed
   * after executing the SQL statement. By default the database
   * {@link Connection} is <b>not</b> closed.
   * </p>
   * <p>
   * <b>NOTE: use this method with care. Creating and closing database
   * connections can be costly operations.</b>
   * </p>
   * 
   * @param autoCloseConnection
   */
    public void setAutoCloseConnection(boolean autoCloseConnection) {
        this.autoCloseConnection = autoCloseConnection;
    }

    /**
   * Returns <tt>true</tt> the database {@link Connection} is automatically
   * closed after executing the SQL statement. By default the database
   * {@link Connection} is <b>not</b> closed.
   * 
   * @return see description.
   */
    public boolean isAutoCloseConnection() {
        return autoCloseConnection;
    }

    /**
   * <p>
   * If <tt>true</tt> the {@link PreparedStatement} is automatically closed
   * after executing the SQL statement. By default the statement is closed. If a
   * prepared statement pool is implemented 'around' JaQLib this value can be
   * set to false. In that case it is important to close the statements yourself
   * after you don't need them anymore (note: this can be done using the
   * {@link #close()} method).
   * </p>
   * <p>
   * <b>NOTE: use this method with care. Not closing prepared statements can
   * stress the database (because it has to cache the prepared statement). So
   * make sure that prepared statement are always closed.</b>
   * </p>
   * 
   * @param value
   */
    public void setAutoClosePreparedStatement(boolean value) {
        this.autoClosePreparedStatement = value;
    }

    /**
   * Returns <tt>true</tt> the {@link PreparedStatement} is automatically closed
   * after executing the SQL statement. By default the statement is closed.
   * 
   * @return see description.
   */
    public boolean isAutoClosePreparedStatement() {
        return autoClosePreparedStatement;
    }

    /**
   * Registers the given custom SQL type handler with the given SQL data type.
   * 
   * @param sqlDataType a SQL data type as defined at {@link java.sql.Types}.
   * @param typeHandler a not null type handler.
   */
    public void registerSqlTypeHandler(int sqlDataType, SqlTypeHandler typeHandler) {
        sqlTypeHandlerRegistry.registerTypeHandler(sqlDataType, typeHandler);
    }

    /**
   * Changes the SQL type handler registry to a custom implementation. By
   * default the standard SQL types are supported.
   * 
   * @param registry a custom SQL type handler registry.
   */
    public void setSqlTypeHandlerRegistry(SqlTypeHandlerRegistry registry) {
        this.sqlTypeHandlerRegistry = Assert.notNull(registry);
    }

    protected SqlTypeHandlerRegistry getSqlTypeHandlerRegistry() {
        return sqlTypeHandlerRegistry;
    }

    protected void commit() throws SQLException {
        if (connection != null) {
            connection.commit();
        }
    }

    /**
   * Closes all used {@link Statement} and {@link PreparedStatement} instances.
   * If the property <tt>autoCloseConnection</tt> is set to true then the
   * {@link Connection} is also closed.
   */
    public void close() {
        close(true, autoCloseConnection);
    }

    void closeAfterQuery() {
        close(autoClosePreparedStatement, autoCloseConnection);
    }

    private void close(boolean autoClosePreparedStatement, boolean autoCloseConnection) {
        DbUtil.close(statement);
        statement = null;
        if (autoClosePreparedStatement) {
            closePreparedStatements();
            prepStatements.clear();
        }
        if (autoCloseConnection) {
            DbUtil.close(connection);
            connection = null;
        }
    }

    private void closePreparedStatements() {
        for (PreparedStatement stmt : prepStatements.values()) {
            DbUtil.close(stmt);
        }
    }

    protected Statement getStatement() throws SQLException {
        if (statement == null) {
            log.fine("Creating SQL statement");
            statement = getConnection().createStatement();
        }
        return statement;
    }

    protected PreparedStatement getPreparedStatement(String sql) throws SQLException {
        PreparedStatement stmt = prepStatements.get(sql);
        if (stmt == null) {
            stmt = getConnection().prepareStatement(sql);
            prepStatements.put(sql, stmt);
        }
        return stmt;
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || isClosed(connection)) {
            log.fine("Getting JDBC connection");
            connection = dataSource.getConnection();
        }
        return connection;
    }

    private boolean isClosed(Connection connection) throws SQLException {
        return connection.isClosed() && !connection.isValid(2);
    }

    protected void close(DbResultSet rs) {
        if (rs != null) {
            rs.close();
        }
    }

    protected DataSourceQueryException handleSqlException(SQLException sqle) {
        DataSourceQueryException e = new DataSourceQueryException(sqle);
        e.setStackTrace(sqle.getStackTrace());
        return e;
    }

    protected void appendWhereClause(StringBuilder sql, String whereClause) {
        if (shouldAppendWhereClause(whereClause)) {
            sql.append(" WHERE ");
            sql.append(whereClause);
        }
    }

    private boolean shouldAppendWhereClause(String whereClause) {
        return (whereClause != null && whereClause.trim().length() > 0);
    }

    public static ColumnMapping<?> cast(FieldMapping<?> mapping) {
        return ColumnMapping.cast(mapping);
    }
}
