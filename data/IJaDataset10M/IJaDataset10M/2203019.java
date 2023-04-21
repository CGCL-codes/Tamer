package com.amazon.carbonado.repo.sleepycat;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.JEVersion;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;
import com.amazon.carbonado.ConfigurationException;
import com.amazon.carbonado.FetchException;
import com.amazon.carbonado.IsolationLevel;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.txn.TransactionScope;
import static com.amazon.carbonado.repo.sleepycat.JE_SetConfigOption.setBooleanParam;

/**
 * Storage implementation for JERepository.
 *
 * @author Brian S O'Neill
 * @author Nicole Deflaux
 */
class JE_Storage<S extends Storable> extends BDBStorage<JE_Transaction, S> {

    private Database mDatabase;

    private String mName;

    /**
     *
     * @param repository repository reference
     * @param storableFactory factory for emitting storables
     * @param db database for Storables
     * @throws DatabaseException
     * @throws SupportException
     */
    JE_Storage(JE_Repository repository, Class<S> type) throws DatabaseException, RepositoryException {
        super(repository, type);
        open(repository.mEnv.getConfig().getReadOnly());
    }

    @Override
    public long countAll() throws FetchException {
        JEVersion v = JEVersion.CURRENT_VERSION;
        if (v.getMajor() == 3 && v.getMinor() < 1) {
            return super.countAll();
        }
        IsolationLevel level = getRepository().getTransactionIsolationLevel();
        if (level != null && level.isAtLeast(IsolationLevel.REPEATABLE_READ)) {
            return super.countAll();
        }
        try {
            return mDatabase.count();
        } catch (DatabaseException e) {
            throw mRepository.toFetchException(e);
        }
    }

    @Override
    protected boolean db_exists(JE_Transaction jetxn, byte[] key, boolean rmw) throws Exception {
        DatabaseEntry keyEntry = new DatabaseEntry(key);
        DatabaseEntry dataEntry = new DatabaseEntry();
        dataEntry.setPartial(0, 0, true);
        OperationStatus status = mDatabase.get(jetxn == null ? null : jetxn.mTxn, keyEntry, dataEntry, rmw ? LockMode.RMW : null);
        return status != OperationStatus.NOTFOUND;
    }

    @Override
    protected byte[] db_get(JE_Transaction jetxn, byte[] key, boolean rmw) throws Exception {
        DatabaseEntry keyEntry = new DatabaseEntry(key);
        DatabaseEntry dataEntry = new DatabaseEntry();
        OperationStatus status = mDatabase.get(jetxn == null ? null : jetxn.mTxn, keyEntry, dataEntry, rmw ? LockMode.RMW : null);
        if (status == OperationStatus.NOTFOUND) {
            return NOT_FOUND;
        }
        return dataEntry.getData();
    }

    @Override
    protected Object db_putNoOverwrite(JE_Transaction jetxn, byte[] key, byte[] value) throws Exception {
        Transaction txn = jetxn == null ? null : jetxn.mTxn;
        DatabaseEntry keyEntry = new DatabaseEntry(key);
        DatabaseEntry dataEntry = new DatabaseEntry(value);
        OperationStatus status = mDatabase.putNoOverwrite(txn, keyEntry, dataEntry);
        if (status == OperationStatus.SUCCESS) {
            if (jetxn != null) {
                jetxn.addUndo(new UndoByDelete(mDatabase, txn, key));
            }
            return SUCCESS;
        } else if (status == OperationStatus.KEYEXIST) {
            return KEY_EXIST;
        } else {
            return NOT_FOUND;
        }
    }

    @Override
    protected boolean db_put(JE_Transaction jetxn, byte[] key, byte[] value) throws Exception {
        DatabaseEntry keyEntry = new DatabaseEntry(key);
        DatabaseEntry dataEntry = new DatabaseEntry(value);
        if (jetxn == null) {
            return mDatabase.put(null, keyEntry, dataEntry) == OperationStatus.SUCCESS;
        }
        Transaction txn = jetxn.mTxn;
        DatabaseEntry oldEntry = new DatabaseEntry();
        OperationStatus getResult = mDatabase.get(txn, keyEntry, oldEntry, LockMode.RMW);
        if (mDatabase.put(txn, keyEntry, dataEntry) == OperationStatus.SUCCESS) {
            if (getResult == OperationStatus.NOTFOUND) {
                jetxn.addUndo(new UndoByDelete(mDatabase, txn, key));
            } else {
                jetxn.addUndo(new UndoByPut(mDatabase, txn, key, oldEntry));
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean db_delete(JE_Transaction jetxn, byte[] key) throws Exception {
        DatabaseEntry keyEntry = new DatabaseEntry(key);
        if (jetxn == null) {
            return mDatabase.delete(null, keyEntry) == OperationStatus.SUCCESS;
        }
        Transaction txn = jetxn.mTxn;
        DatabaseEntry oldEntry = new DatabaseEntry();
        if (mDatabase.get(txn, keyEntry, oldEntry, LockMode.RMW) == OperationStatus.NOTFOUND) {
            return false;
        }
        if (mDatabase.delete(txn, keyEntry) == OperationStatus.SUCCESS) {
            jetxn.addUndo(new UndoByPut(mDatabase, txn, key, oldEntry));
            return true;
        }
        return false;
    }

    @Override
    protected void db_truncate(JE_Transaction jetxn) throws Exception {
        close();
        JE_Repository repository = (JE_Repository) getRepository();
        repository.mEnv.truncateDatabase(jetxn == null ? null : jetxn.mTxn, mName, false);
        open(false, jetxn, false);
    }

    @Override
    protected boolean db_isEmpty(JE_Transaction jetxn, Object database, boolean rmw) throws Exception {
        Cursor cursor = ((Database) database).openCursor(jetxn == null ? null : jetxn.mTxn, null);
        OperationStatus status = cursor.getFirst(new DatabaseEntry(), new DatabaseEntry(), rmw ? LockMode.RMW : null);
        cursor.close();
        return status == OperationStatus.NOTFOUND;
    }

    @Override
    protected void db_close(Object database) throws Exception {
        ((Database) database).close();
    }

    @Override
    protected Object env_openPrimaryDatabase(JE_Transaction jetxn, String name) throws Exception {
        JE_Repository repository = (JE_Repository) getRepository();
        Environment env = repository.mEnv;
        boolean readOnly = env.getConfig().getReadOnly();
        DatabaseConfig config;
        try {
            config = (DatabaseConfig) repository.getInitialDatabaseConfig();
        } catch (ClassCastException e) {
            throw new ConfigurationException("Unsupported initial environment config. Must be instance of " + DatabaseConfig.class.getName(), e);
        }
        if (config == null) {
            config = new DatabaseConfig();
            setBooleanParam(config, "setSortedDuplicates", false);
        } else if (config.getSortedDuplicates()) {
            throw new IllegalArgumentException("DatabaseConfig: getSortedDuplicates is true");
        }
        setBooleanParam(config, "setTransactional", repository.mDatabasesTransactional);
        setBooleanParam(config, "setReadOnly", readOnly);
        setBooleanParam(config, "setAllowCreate", !readOnly);
        runDatabasePrepareForOpeningHook(config);
        mName = name;
        return mDatabase = env.openDatabase(jetxn == null ? null : jetxn.mTxn, name, config);
    }

    @Override
    protected void env_removeDatabase(JE_Transaction jetxn, String databaseName) throws Exception {
        mDatabase.getEnvironment().removeDatabase(jetxn == null ? null : jetxn.mTxn, databaseName);
    }

    @Override
    protected BDBCursor<JE_Transaction, S> openCursor(TransactionScope<JE_Transaction> scope, byte[] startBound, boolean inclusiveStart, byte[] endBound, boolean inclusiveEnd, int maxPrefix, boolean reverse, Object database) throws Exception {
        return new JE_Cursor<S>(scope, startBound, inclusiveStart, endBound, inclusiveEnd, maxPrefix, reverse, this, (Database) database);
    }

    private class UndoByDelete implements JE_Transaction.UndoAction {

        private final Database mDb;

        private final Transaction mTxn;

        private final byte[] mKey;

        UndoByDelete(Database db, Transaction txn, byte[] key) {
            mDb = db;
            mTxn = txn;
            mKey = key;
        }

        @Override
        public void apply() throws DatabaseException {
            if (mDb == mDatabase) {
                mDb.delete(mTxn, new DatabaseEntry(mKey));
            }
        }
    }

    private class UndoByPut implements JE_Transaction.UndoAction {

        private final Database mDb;

        private final Transaction mTxn;

        private final byte[] mKey;

        private final DatabaseEntry mEntry;

        UndoByPut(Database db, Transaction txn, byte[] key, DatabaseEntry entry) {
            mDb = db;
            mTxn = txn;
            mKey = key;
            mEntry = entry;
        }

        @Override
        public void apply() throws DatabaseException {
            if (mDb == mDatabase) {
                mDb.put(mTxn, new DatabaseEntry(mKey), mEntry);
            }
        }
    }
}
