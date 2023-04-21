package org.go.database;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Protects a <code>{@link java.sql.Connection}</code>'s attributes from being permanently modfied.
 * </p>
 * 
 * <p>
 * Wraps a provided <code>{@link java.sql.Connection}</code> such that its auto 
 * commit and transaction isolation attributes can be overwritten, but 
 * will automatically restored to their original values when the connection
 * is actually closed (and potentially returned to a pool for reuse).
 * </p>
 * 
 * @see org.go.database.delegate.quartz.impl.jdbcjobstore.JobStoreSupport#getConnection()
 * @see org.quartz.impl.jdbcjobstore.JobStoreCMT#getNonManagedTXConnection()
 */
public class AttributeRestoringConnectionInvocationHandler implements InvocationHandler {

    private Connection conn;

    private boolean originalAutoCommitValue;

    private int originalTxIsolationValue;

    private boolean overwroteOriginalAutoCommitValue;

    private boolean overwroteOriginalTxIsolationValue;

    public AttributeRestoringConnectionInvocationHandler(Connection conn) {
        this.conn = conn;
    }

    protected Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }

    /**
	 * Attempts to restore the auto commit and transaction isolation connection
	 * attributes of the wrapped connection to their original values (if they
	 * were overwritten), before finally actually closing the wrapped connection.
	 */
    public void close() throws SQLException {
        restoreOriginalAtributes();
        conn.close();
    }

    /**
	 * Gets the underlying connection to which all operations ultimately 
	 * defer.  This is provided in case a user ever needs to punch through 
	 * the wrapper to access vendor specific methods outside of the 
	 * standard <code>java.sql.Connection</code> interface.
	 * 
	 * @return The underlying connection to which all operations
	 * ultimately defer.
	 */
    public Connection getWrappedConnection() {
        return conn;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("setAutoCommit")) {
            setAutoCommit(((Boolean) args[0]).booleanValue());
        } else if (method.getName().equals("setTransactionIsolation")) {
            setTransactionIsolation(((Integer) args[0]).intValue());
        } else if (method.getName().equals("close")) {
            close();
        } else {
            return method.invoke(conn, args);
        }
        return null;
    }

    /**
	 * Attempts to restore the auto commit and transaction isolation connection
	 * attributes of the wrapped connection to their original values (if they
	 * were overwritten).
	 */
    public void restoreOriginalAtributes() {
        try {
            if (overwroteOriginalAutoCommitValue) {
                conn.setAutoCommit(originalAutoCommitValue);
            }
        } catch (Throwable t) {
            getLog().warn("Failed restore connection's original auto commit setting.", t);
        }
        try {
            if (overwroteOriginalTxIsolationValue) {
                conn.setTransactionIsolation(originalTxIsolationValue);
            }
        } catch (Throwable t) {
            getLog().warn("Failed restore connection's original transaction isolation setting.", t);
        }
    }

    /**
	 * Sets this connection's auto-commit mode to the given state, saving
	 * the original mode.  The connection's original auto commit mode is restored
	 * when the connection is closed.
	 */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        boolean currentAutoCommitValue = conn.getAutoCommit();
        if (autoCommit != currentAutoCommitValue) {
            if (overwroteOriginalAutoCommitValue == false) {
                overwroteOriginalAutoCommitValue = true;
                originalAutoCommitValue = currentAutoCommitValue;
            }
            conn.setAutoCommit(autoCommit);
        }
    }

    /**
	 * Attempts to change the transaction isolation level to the given level, saving
	 * the original level.  The connection's original transaction isolation level is 
	 * restored when the connection is closed.
	 */
    public void setTransactionIsolation(int level) throws SQLException {
        int currentLevel = conn.getTransactionIsolation();
        if (level != currentLevel) {
            if (overwroteOriginalTxIsolationValue == false) {
                overwroteOriginalTxIsolationValue = true;
                originalTxIsolationValue = currentLevel;
            }
            conn.setTransactionIsolation(level);
        }
    }
}
