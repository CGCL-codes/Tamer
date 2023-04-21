package purej.dao.transaction;

import java.sql.Connection;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import purej.dao.datasource.JDBCDataSourceManager;
import purej.exception.DAOException;
import purej.logging.Logger;
import purej.logging.LoggerFactory;

/**
 * XA-JDBC Ʈ������
 * 
 * @author Administrator
 * 
 */
public class XAJDBCTransaction extends JDBCDataSourceManager implements JDBCTransaction {

    private static final Logger log = LoggerFactory.getLogger(XAJDBCTransaction.class, Logger.FRAMEWORK);

    private UserTransaction userTransaction;

    private boolean commmitted = false;

    private boolean newTransaction = false;

    public XAJDBCTransaction() {
    }

    public XAJDBCTransaction(UserTransaction utx) {
        try {
            userTransaction = utx;
            if (userTransaction == null) {
                throw new JDBCTransactionException("XA-JDBC Transaction initialization failed.  UserTransaction was null.");
            }
            newTransaction = userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION;
            if (newTransaction) {
                userTransaction.begin();
                log.info("XA-JDBC Transaction is started.");
            }
        } catch (Exception e) {
            throw new JDBCTransactionException("XA-JDBC Transaction could not start transaction.  Cause: ", e);
        }
    }

    public void commit() {
        if (commmitted) {
            throw new DAOException("XA-JDBC Transaction could not commit because this transaction has already been committed.");
        }
        try {
            try {
                if (newTransaction) {
                    userTransaction.commit();
                    log.info("XA-JDBC Transaction is commited.");
                }
            } finally {
            }
        } catch (Exception e) {
            throw new JDBCTransactionException("XA-JDBC Transaction could not commit.  Cause: ", e);
        }
        commmitted = true;
    }

    public void rollback() {
        if (!commmitted) {
            try {
                try {
                    if (userTransaction != null) {
                        if (newTransaction) {
                            userTransaction.rollback();
                            log.error("XA-JDBC Transaction is rollbacked.");
                        } else {
                            userTransaction.setRollbackOnly();
                            log.error("XA-JDBC Transaction is set rollback only.");
                        }
                    }
                } finally {
                }
            } catch (Exception e) {
                throw new JDBCTransactionException("XA-JDBC Transaction could not rollback.  Cause: ", e);
            }
        }
    }

    @Override
    public Connection getConnection() throws JDBCTransactionException {
        return super.getConnection();
    }

    @Override
    public Connection getConnection(String dataSourceName) throws JDBCTransactionException {
        return super.getConnection(dataSourceName);
    }

    @Override
    public void closeConnection(Connection connection) throws JDBCTransactionException {
        super.closeConnection(connection);
    }

    @Override
    public void commitConnection(Connection connection) throws JDBCTransactionException {
    }

    @Override
    public void rollbackConnection(Connection connection) throws JDBCTransactionException {
    }
}
