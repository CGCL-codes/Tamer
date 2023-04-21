package org.apache.ibatis.transaction.managed;

import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

/**
 * Creates {@link ManagedTransaction} instances.
 *
 * @see ManagedTransaction
 */
public class ManagedTransactionFactory implements TransactionFactory {

    private boolean closeConnection = true;

    public void setProperties(Properties props) {
        if (props != null) {
            String closeConnectionProperty = props.getProperty("closeConnection");
            if (closeConnectionProperty != null) {
                closeConnection = Boolean.valueOf(closeConnectionProperty);
            }
        }
    }

    public Transaction newTransaction(Connection conn) {
        return new ManagedTransaction(conn, closeConnection);
    }

    public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
        return new ManagedTransaction(ds, level, closeConnection);
    }
}
