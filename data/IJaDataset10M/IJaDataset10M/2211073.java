package de.iritgo.aktera.persist.defaultpersist;

import de.iritgo.aktera.persist.PersistenceException;
import de.iritgo.aktera.persist.Transaction;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @avalon.component
 * @avalon.service type=de.iritgo.aktera.persist.Transaction
 * @x-avalon.info name=transaction
 * @x-avalon.lifestyle type=transient
 *
 * @author Michael Nash
 * @version $Revision: 1.3 $ $Date: 2003/07/13 15:51:24 $
 */
public class DefaultTransaction implements Transaction {

    private Connection myConnection = null;

    /**
	 * Data source service itself. Used for all connections from Persistent
	 * objects created from this factory
	 */
    private DataSourceComponent dataSource = null;

    private boolean supportsTransactions = true;

    void setDataSource(DataSourceComponent newDataSource) {
        dataSource = newDataSource;
    }

    /**
	 * DefaultTransaction understands if a database doesn't support transactions (such
	 * as MySQL) and simply doesn't do the transaction operations. This way, code can
	 * still call Transaction, and will work, but will silently ignore the calls
	 * for databases that can't handle them. If you *need* transactions, you can
	 * always check the DatabaseType in your code to ensure they are supported
	 * by the currently configured database.
	 */
    void setSupportsTransactions(boolean newSupports) {
        supportsTransactions = newSupports;
    }

    /**
	 * @see de.iritgo.aktera.persist.Transaction#begin()
	 */
    public void begin() throws PersistenceException {
        try {
            myConnection = dataSource.getConnection();
            if (supportsTransactions) {
                myConnection.setAutoCommit(false);
            }
        } catch (SQLException se) {
            throw new PersistenceException(se);
        }
    }

    /**
	 * @see de.iritgo.aktera.persist.Transaction#commit()
	 */
    public void commit() throws PersistenceException {
        try {
            if (supportsTransactions) {
                myConnection.commit();
            }
            myConnection.close();
        } catch (SQLException se) {
            throw new PersistenceException(se);
        }
    }

    /**
	 * @see de.iritgo.aktera.persist.Transaction#rollback()
	 */
    public void rollback() throws PersistenceException {
        try {
            if (supportsTransactions) {
                myConnection.rollback();
            }
            myConnection.close();
        } catch (SQLException se) {
            throw new PersistenceException(se);
        }
    }

    /**
	 * @see de.iritgo.aktera.persist.Transaction#getConnection()
	 */
    public Connection getConnection() throws PersistenceException {
        if (myConnection == null) {
            throw new PersistenceException("Transaction not begun");
        }
        return myConnection;
    }
}
