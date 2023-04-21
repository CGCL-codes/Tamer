package pl.org.minions.stigma.databases.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing abstract request for
 * {@link SqlAsyncDB}. Children of this class can implement
 * their specific SQL operations.
 */
public abstract class SqlRequest {

    /**
     * Returns result set for given statement.
     * @param statement
     *            statement to acquire result from.
     * @return result set for given statement or {@code
     *         null} on error (or if statement was not
     *         "SELECT" type).
     */
    protected final ResultSet aquireResultSet(PreparedStatement statement) {
        try {
            return statement.getResultSet();
        } catch (SQLException e) {
            Log.logger.error("Couldn't get result set: " + e);
            return null;
        }
    }

    private void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            Log.logger.error("SQL error on COMMIT: " + e);
        }
    }

    /**
     * Executes class specific SQL requests. Should be build
     * using subsequent calls to
     * {@link #executeStatement(PreparedStatement, Object...)}
     * and {@link #aquireResultSet(PreparedStatement)}.
     * @return {@code true} effects in execution of {@code
     *         commit}, {@code false} will result in {@code
     *         rollback}.
     */
    protected abstract boolean execute();

    /**
     * Executes prepared statement with given parameters.
     * @param statement
     *            statement to execute
     * @param params
     *            parameters to substitute '?' in statement
     * @return {@code true} when statement execution
     *         succeed, {@code false} otherwise
     */
    protected final boolean executeStatement(PreparedStatement statement, Object... params) {
        int idx = 1;
        for (Object param : params) {
            try {
                statement.setObject(idx++, param);
            } catch (SQLException e) {
                Log.logger.error(MessageFormat.format("Statement: {0} with params: {1} failed, while setting {2} parameter.", statement, params, idx - 1));
                if (Log.isDebugEnabled()) {
                    Log.logger.debug(paramsToString(params));
                }
                return false;
            }
        }
        try {
            statement.execute();
        } catch (SQLException e) {
            Log.logger.error("Error while executing statement: " + e);
            if (Log.isDebugEnabled()) {
                Log.logger.debug(paramsToString(params));
            }
            return false;
        }
        return true;
    }

    private String paramsToString(Object... params) {
        StringBuilder builder = new StringBuilder();
        builder.append("Statement params: ");
        int i = 0;
        for (Object o : params) {
            builder.append("[").append(i).append("]=").append(o != null ? o.toString() : "null").append(" ");
            i++;
        }
        return builder.toString();
    }

    /**
     * Will be called by {@link SqlAsyncDB} to process this
     * request.
     * @param connection
     *            connection which should be used for
     *            processing request
     */
    final void process(Connection connection) {
        if (!execute()) rollback(connection); else commit(connection);
    }

    private boolean rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            Log.logger.error("ROLLBACK failed: " + e);
            return false;
        }
        return true;
    }

    /**
     * Returns whether or not this request should be flushed
     * to database. Flushing may be useful for example: on
     * shutting down server to enforce saving all players.
     * @return {@code true} if request should be flushed
     */
    protected abstract boolean shouldBeFlushed();
}
