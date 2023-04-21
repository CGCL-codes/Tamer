package org.datanucleus.store.rdbms.scostore;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.mapped.exceptions.MappedDatastoreException;
import org.datanucleus.store.mapped.scostore.AbstractCollectionStore;
import org.datanucleus.store.mapped.scostore.AbstractSetStore;
import org.datanucleus.store.mapped.scostore.AbstractSetStoreSpecialization;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.SQLController;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;

/**
 * RDBMS-specific implementation of an {@link AbstractSetStoreSpecialization}.
 */
class RDBMSAbstractSetStoreSpecialization extends RDBMSAbstractCollectionStoreSpecialization implements AbstractSetStoreSpecialization {

    RDBMSAbstractSetStoreSpecialization(Localiser localiser, ClassLoaderResolver clr, RDBMSStoreManager storeMgr) {
        super(localiser, clr, storeMgr);
    }

    /**
     * Method to process an "add" statement.
     * @param ownerSM StateManager for the owner
     * @param conn The connection
     * @param batched Whether we are batching it
     * @param element The element
     * @return Number of datastore records changed (always 0 if batch since nothing yet changed)
     * @throws MappedDatastoreException Thrown if an error occurs
     */
    public int[] internalAdd(ObjectProvider ownerSM, ManagedConnection conn, boolean batched, Object element, boolean processNow, AbstractSetStore setStore) throws MappedDatastoreException {
        ExecutionContext ec = ownerSM.getExecutionContext();
        SQLController sqlControl = storeMgr.getSQLController();
        String addStmt = getAddStmt(setStore);
        try {
            PreparedStatement ps = sqlControl.getStatementForUpdate(conn, addStmt, batched);
            try {
                int jdbcPosition = 1;
                jdbcPosition = BackingStoreHelper.populateOwnerInStatement(ownerSM, ec, ps, jdbcPosition, setStore);
                jdbcPosition = BackingStoreHelper.populateElementInStatement(ec, ps, element, jdbcPosition, setStore.getElementMapping());
                if (setStore.getRelationDiscriminatorMapping() != null) {
                    jdbcPosition = BackingStoreHelper.populateRelationDiscriminatorInStatement(ec, ps, jdbcPosition, setStore);
                }
                return sqlControl.executeStatementUpdate(conn, addStmt, ps, processNow);
            } finally {
                sqlControl.closeStatement(conn, ps);
            }
        } catch (SQLException e) {
            throw new MappedDatastoreException(getAddStmt(setStore), e);
        }
    }

    public boolean remove(ObjectProvider sm, Object element, int size, AbstractSetStore setStore) {
        boolean modified = false;
        ExecutionContext ec = sm.getExecutionContext();
        String removeStmt = getRemoveStmt(setStore);
        try {
            ManagedConnection mconn = storeMgr.getConnection(ec);
            SQLController sqlControl = storeMgr.getSQLController();
            try {
                PreparedStatement ps = sqlControl.getStatementForUpdate(mconn, removeStmt, false);
                try {
                    int jdbcPosition = 1;
                    jdbcPosition = BackingStoreHelper.populateOwnerInStatement(sm, ec, ps, jdbcPosition, setStore);
                    jdbcPosition = BackingStoreHelper.populateElementInStatement(ec, ps, element, jdbcPosition, setStore.getElementMapping());
                    if (setStore.getRelationDiscriminatorMapping() != null) {
                        jdbcPosition = BackingStoreHelper.populateRelationDiscriminatorInStatement(ec, ps, jdbcPosition, setStore);
                    }
                    int[] rowsDeleted = sqlControl.executeStatementUpdate(mconn, removeStmt, ps, true);
                    modified = (rowsDeleted[0] == 1);
                } finally {
                    sqlControl.closeStatement(mconn, ps);
                }
            } finally {
                mconn.release();
            }
        } catch (SQLException e) {
            NucleusLogger.DATASTORE.error(e);
            String msg = localiser.msg("056012", removeStmt);
            NucleusLogger.DATASTORE.error(msg);
            throw new NucleusDataStoreException(msg, e);
        }
        return modified;
    }

    public int[] internalRemove(ObjectProvider ownerSM, ManagedConnection conn, boolean batched, Object element, boolean executeNow, AbstractCollectionStore acs) throws MappedDatastoreException {
        ExecutionContext ec = ownerSM.getExecutionContext();
        SQLController sqlControl = storeMgr.getSQLController();
        String removeStmt = getRemoveStmt(acs);
        try {
            PreparedStatement ps = sqlControl.getStatementForUpdate(conn, removeStmt, batched);
            try {
                int jdbcPosition = 1;
                jdbcPosition = BackingStoreHelper.populateOwnerInStatement(ownerSM, ec, ps, jdbcPosition, acs);
                jdbcPosition = BackingStoreHelper.populateElementInStatement(ec, ps, element, jdbcPosition, acs.getElementMapping());
                if (acs.getRelationDiscriminatorMapping() != null) {
                    jdbcPosition = BackingStoreHelper.populateRelationDiscriminatorInStatement(ec, ps, jdbcPosition, acs);
                }
                return sqlControl.executeStatementUpdate(conn, removeStmt, ps, executeNow);
            } finally {
                sqlControl.closeStatement(conn, ps);
            }
        } catch (SQLException e) {
            throw new MappedDatastoreException("SQLException", e);
        }
    }

    public void preInternalRemove(ManagedConnection mconn) throws MappedDatastoreException {
        SQLController sqlControl = storeMgr.getSQLController();
        try {
            sqlControl.processStatementsForConnection(mconn);
        } catch (SQLException e) {
            throw new MappedDatastoreException("SQLException", e);
        }
    }
}
