package net.toften.jlips2.db.data.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 * @author thomaslarsen
 * @version 1.0
 * @created 03-Oct-2005 20:29:19
 */
public class DbConnectionManager implements ConnectionManager {

    /**
	 * The ConnectionFactory that created this ConnectionManager
	 */
    private ConnectionFactory cf;

    private String expression = null;

    private Connection connection = null;

    private Statement lastStatement = null;

    public DbConnectionManager(ConnectionFactory connectionFactory) {
        if (connectionFactory == null) throw new NullPointerException("ConnectionFactory not supplied");
        this.cf = connectionFactory;
    }

    public void setStatement(String expression) {
        if (expression != null) if (expression.equals("")) expression = null;
        this.expression = expression;
    }

    public void release() throws SQLException {
        if (connection != null) {
            cf.releaseConnection(connection);
            lastStatement = null;
            connection = null;
        }
    }

    public Statement execute() throws SQLException {
        if (expression == null) throw new IllegalStateException("Statement not set");
        connection = cf.getConnection();
        lastStatement = connection.createStatement();
        lastStatement.execute(expression);
        Logger.getLogger("net.toften.jlips.db").info("Executed SQL expression: " + expression);
        return lastStatement;
    }

    /**
	 * Returns the ResultSet from the last executed expression
	 * 
	 * @return ResultSet
	 * @throws SQLException
	 * @throws IllegalStateException if the expression hasn'pti been executed yet
	 */
    public ResultSet getResultSet() throws SQLException {
        if (lastStatement == null) throw new IllegalStateException("Statement not executed yet");
        return lastStatement.getResultSet();
    }
}
