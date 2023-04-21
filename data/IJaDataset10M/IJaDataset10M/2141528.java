package nuts.core.orm.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * AbstractSqlExecutor
 */
public abstract class AbstractSqlExecutor implements SqlExecutor {

    /**
	 * Constant to let us know not to skip anything
	 */
    public static final int NO_SKIPPED_RESULTS = 0;

    /**
	 * Constant to let us know to include all records
	 */
    public static final int NO_MAXIMUM_RESULTS = -999999;

    /**
	 * Connection
	 */
    protected Connection connection;

    /**
	 * resultSetType - one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 * ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 */
    protected int resultSetType;

    /**
	 * resultSetConcurrency - one of the following ResultSet constants: ResultSet.CONCUR_READ_ONLY
	 * or ResultSet.CONCUR_UPDATABLE
	 */
    protected int resultSetConcurrency;

    /**
	 * resultSetHoldability - one of the following ResultSet constants:
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
	 */
    protected int resultSetHoldability;

    /**
	 * Constructor
	 * @param connection connection
	 */
    public AbstractSqlExecutor(Connection connection) {
        this(connection, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
	 * Constructor
	 * 
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 */
    public AbstractSqlExecutor(Connection connection, int resultSetType) {
        this(connection, resultSetType, ResultSet.CONCUR_READ_ONLY);
    }

    /**
	 * Constructor
	 * 
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @param resultSetConcurrency one of the following ResultSet constants:
	 *            ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 */
    public AbstractSqlExecutor(Connection connection, int resultSetType, int resultSetConcurrency) {
        this(connection, resultSetType, resultSetConcurrency, 0);
    }

    /**
	 * Constructor
	 * 
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @param resultSetConcurrency one of the following ResultSet constants:
	 *            ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * @param resultSetHoldability one of the following ResultSet constants:
	 *            ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
	 */
    public AbstractSqlExecutor(Connection connection, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        this.connection = connection;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
    }

    /**
	 * @return the connection
	 */
    public Connection getConnection() {
        return connection;
    }

    /**
	 * @param connection the connection to set
	 */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
	 * @return the resultSetType
	 */
    public int getResultSetType() {
        return resultSetType;
    }

    /**
	 * @param resultSetType the resultSetType to set
	 */
    public void setResultSetType(int resultSetType) {
        this.resultSetType = resultSetType;
    }

    /**
	 * @return the resultSetConcurrency
	 */
    public int getResultSetConcurrency() {
        return resultSetConcurrency;
    }

    /**
	 * @param resultSetConcurrency the resultSetConcurrency to set
	 */
    public void setResultSetConcurrency(int resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
    }

    /**
	 * @return the resultSetHoldability
	 */
    public int getResultSetHoldability() {
        return resultSetHoldability;
    }

    /**
	 * @param resultSetHoldability the resultSetHoldability to set
	 */
    public void setResultSetHoldability(int resultSetHoldability) {
        this.resultSetHoldability = resultSetHoldability;
    }

    /**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public void execute(String sql) throws SQLException {
        execute(sql, null);
    }

    /**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public abstract void execute(String sql, Object parameterObject) throws SQLException;

    /**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @return The number of rows effected.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public int executeUpdate(String sql) throws SQLException {
        return executeUpdate(sql, null);
    }

    /**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @return The number of rows effected.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public abstract int executeUpdate(String sql, Object parameterObject) throws SQLException;

    /**
	 * Executes the given SQL statement, which returns a single <code>ResultSetEx</code> object.
	 * 
	 * @param sql an SQL statement to be sent to the database, typically a static SQL
	 *            <code>SELECT</code> statement
	 * @return a <code>ResultSet</code> object that contains the data produced by the given query;
	 *         never <code>null</code>
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
    public SqlResultSet executeQuery(String sql) throws SQLException {
        return executeQuery(sql, null);
    }

    /**
     * Executes the given SQL statement, which returns a single 
     * <code>ResultSetEx</code> object.
     *
     * @param sql an SQL statement to be sent to the database, typically a 
     *        static SQL <code>SELECT</code> statement
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
     * @return a <code>ResultSet</code> object that contains the data produced 
     *         by the given query; never <code>null</code> 
	 * @throws java.sql.SQLException If an SQL error occurs.
     */
    public abstract SqlResultSet executeQuery(String sql, Object parameterObject) throws SQLException;

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a single object instance.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @return The single result object populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
    public <T> T queryForObject(String sql, Class<T> resultClass) throws SQLException {
        return queryForObject(sql, null, resultClass);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a single object instance.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @return The single result object populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
    public <T> T queryForObject(String sql, Object parameterObject, Class<T> resultClass) throws SQLException {
        return queryForObject(sql, parameterObject, resultClass, null);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * the supplied result object.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultObject    The result object instance that should be populated with result data.
	 * @return The single result object as supplied by the resultObject parameter, populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
    @SuppressWarnings("unchecked")
    public <T> T queryForObject(String sql, Object parameterObject, T resultObject) throws SQLException {
        return queryForObject(sql, parameterObject, (Class<T>) resultObject.getClass(), resultObject);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a single object instance.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @param resultObject    The result object instance that should be populated with result data.
	 * @return The single result object populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
    protected abstract <T> T queryForObject(String sql, Object parameterObject, Class<T> resultClass, T resultObject) throws SQLException;

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public <T> List<T> queryForList(String sql, Object parameterObject, Class<T> resultClass) throws SQLException {
        return queryForList(sql, parameterObject, resultClass, NO_SKIPPED_RESULTS, NO_MAXIMUM_RESULTS);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public <T> List<T> queryForList(String sql, Class<T> resultClass) throws SQLException {
        return queryForList(sql, null, resultClass, NO_SKIPPED_RESULTS, NO_MAXIMUM_RESULTS);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects within a certain range.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @param skip            The number of results to ignore.
	 * @param max             The maximum number of results to return.
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public <T> List<T> queryForList(String sql, Class<T> resultClass, int skip, int max) throws SQLException {
        return queryForList(sql, null, resultClass, skip, max);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects within a certain range.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @param skip            The number of results to ignore.
	 * @param max             The maximum number of results to return.
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    public abstract <T> List<T> queryForList(String sql, Object parameterObject, Class<T> resultClass, int skip, int max) throws SQLException;

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects that will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @param keyPropertyName The property to be used as the key in the Map.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
    @SuppressWarnings("unchecked")
    public <T> Map<Object, T> queryForMap(String sql, Class<T> resultClass, String keyPropertyName) throws SQLException {
        return (Map<Object, T>) queryForMap(sql, null, resultClass, keyPropertyName, null);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects that will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param keyPropertyName         The property to be used as the key in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an error occurs.
	 */
    @SuppressWarnings("unchecked")
    public <T> Map<Object, T> queryForMap(String sql, Object parameterObject, Class<T> resultClass, String keyPropertyName) throws SQLException {
        return (Map<Object, T>) queryForMap(sql, parameterObject, resultClass, keyPropertyName, null);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects from which one property will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @param keyPropertyName The property to be used as the key in the Map.
	 * @param valuePropertyName The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
    @SuppressWarnings("unchecked")
    public <T> Map queryForMap(String sql, Class<T> resultClass, String keyPropertyName, String valuePropertyName) throws SQLException {
        return (Map<Object, T>) queryForMap(sql, null, resultClass, keyPropertyName, valuePropertyName);
    }

    /**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects from which one property will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameterObject The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @param keyPropertyName The property to be used as the key in the Map.
	 * @param valuePropertyName The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
    public abstract <T> Map queryForMap(String sql, Object parameterObject, Class<T> resultClass, String keyPropertyName, String valuePropertyName) throws SQLException;
}
