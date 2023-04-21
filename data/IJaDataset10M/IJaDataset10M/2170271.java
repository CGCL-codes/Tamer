package de.eversync.localFileSystem;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class FileUpdateCacher {

    private SqlJetDb mDb;

    private ReentrantLock mDbLock;

    public FileUpdateCacher() throws SqlJetException {
        this.setDbLock(new ReentrantLock(true));
        this.openDbConnection();
    }

    @Override
    protected void finalize() throws Throwable {
        this.getDb().close();
        this.setDb(null);
        super.finalize();
    }

    public synchronized long getLastUpdate(String aFullPath) throws SqlJetException {
        long lastUpdate = 0;
        this.getDbLock().lock();
        try {
            if (aFullPath.equals("")) aFullPath = "rootDir";
            this.getDb().beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetCursor cursor = this.getDb().getTable("cache").lookup("path_index", aFullPath);
            if (cursor.first()) {
                lastUpdate = cursor.getInteger("last_update");
            }
            this.getDb().commit();
        } finally {
            this.getDbLock().unlock();
        }
        return lastUpdate;
    }

    public synchronized void setLastUpdate(String aFullPath, long aLastUpdate) throws SqlJetException {
        if (aFullPath.equals("")) {
            aFullPath = "rootDir";
        }
        this.getDbLock().lock();
        try {
            if (this.entryExists(aFullPath)) {
                this.updateEntry(aFullPath, aLastUpdate);
            } else {
                this.insertEntry(aFullPath, aLastUpdate);
            }
        } finally {
            this.getDbLock().unlock();
        }
    }

    public synchronized void clearCache() throws SqlJetException {
        this.getDbLock().lock();
        try {
            this.getDb().beginTransaction(SqlJetTransactionMode.WRITE);
            this.getDb().getTable("cache").clear();
            this.getDb().commit();
        } finally {
            this.getDbLock().unlock();
        }
    }

    public synchronized void deleteCacheEntry(String aFullPath) throws SqlJetException {
        if (aFullPath.equals("")) {
            aFullPath = "rootDir";
        }
        this.getDbLock().lock();
        try {
            if (this.entryExists(aFullPath)) {
                this.removeEntry(aFullPath);
            }
        } finally {
            this.getDbLock().unlock();
        }
    }

    public long getRecentUpdateTime() throws SqlJetException {
        long maxTime = 0;
        this.getDbLock().lock();
        try {
            this.getDb().beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetCursor cursor = this.getDb().getTable("cache").open();
            while (!cursor.eof()) {
                if (cursor.getInteger("last_update") > maxTime) {
                    maxTime = cursor.getInteger("last_update");
                }
                cursor.next();
            }
            this.getDb().commit();
        } finally {
            this.getDbLock().unlock();
        }
        return maxTime;
    }

    private void openDbConnection() throws SqlJetException {
        File dbFile = new File("fsCache.sq3");
        boolean newDb = !dbFile.exists();
        SqlJetDb db = SqlJetDb.open(dbFile, true);
        this.setDb(db);
        if (newDb) this.createDbStructure();
    }

    private void createDbStructure() throws SqlJetException {
        this.getDb().beginTransaction(SqlJetTransactionMode.WRITE);
        this.getDb().createTable("CREATE TABLE IF NOT EXISTS cache(path VARCHAR(512),last_update LONG)");
        this.getDb().createIndex("CREATE INDEX IF NOT EXISTS path_index on cache(path)");
        this.getDb().commit();
    }

    private boolean entryExists(String aFullPath) throws SqlJetException {
        this.getDb().beginTransaction(SqlJetTransactionMode.READ_ONLY);
        ISqlJetCursor cursor = this.getDb().getTable("cache").lookup("path_index", aFullPath);
        boolean stat = cursor.first();
        this.getDb().commit();
        return stat;
    }

    private void updateEntry(String aFullPath, long aLastUpdate) throws SqlJetException {
        this.getDb().beginTransaction(SqlJetTransactionMode.WRITE);
        ISqlJetCursor cursor = this.getDb().getTable("cache").lookup("path_index", aFullPath);
        if (cursor.first()) {
            cursor.update(aFullPath, aLastUpdate);
        }
        this.getDb().commit();
    }

    private void insertEntry(String aFullPath, long aLastUpdate) throws SqlJetException {
        if (aFullPath.equals("")) {
            aFullPath = "rootDir";
        }
        this.getDb().beginTransaction(SqlJetTransactionMode.WRITE);
        this.getDb().getTable("cache").insert(aFullPath, aLastUpdate);
        this.getDb().commit();
    }

    private void removeEntry(String aFullPath) throws SqlJetException {
        if (aFullPath.equals("")) {
            aFullPath = "rootDir";
        }
        this.getDb().beginTransaction(SqlJetTransactionMode.WRITE);
        ISqlJetCursor cursor = this.getDb().getTable("cache").lookup("path_index", aFullPath);
        if (cursor.first()) {
            cursor.delete();
        }
        this.getDb().commit();
    }

    private void setDb(SqlJetDb mDb) {
        this.mDb = mDb;
    }

    private SqlJetDb getDb() {
        return mDb;
    }

    public void setDbLock(ReentrantLock mDbLock) {
        this.mDbLock = mDbLock;
    }

    public ReentrantLock getDbLock() {
        return mDbLock;
    }
}
