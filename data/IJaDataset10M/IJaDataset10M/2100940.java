package edu.rabbit.kernel.table;

import edu.rabbit.DbEncoding;
import edu.rabbit.DbErrorCode;
import edu.rabbit.DbException;
import edu.rabbit.TransactionMode;
import edu.rabbit.kernel.IDbHandle;
import edu.rabbit.kernel.ILimits;
import edu.rabbit.kernel.AutoVacuumMode;
import edu.rabbit.kernel.btree.IBtree;
import edu.rabbit.kernel.pager.PageCache;
import edu.rabbit.table.IOptions;

/**
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 * 
 */
public class Options implements IOptions {

    private static final int SCHEMA_COOKIE = 1;

    private static final int FILE_FORMAT = 2;

    private static final int PAGE_CACHE_SIZE = 3;

    private static final int AUTOVACUUM = 4;

    private static final int ENCODING = 5;

    private static final int USER_COOKIE = 6;

    private static final int INCREMENTAL_VACUUM = 7;

    private final IBtree btree;

    private final IDbHandle dbHandle;

    /**
     * Schema cookie. Changes with each schema change.
     */
    private int schemaCookie;

    /**
     * File format of schema layer.
     */
    private int fileFormat = DB_DEFAULT_FILE_FORMAT;

    /**
     * Size of the page cache.
     */
    private int pageCacheSize = PageCache.PAGE_CACHE_SIZE_DEFAULT;

    /**
     * Use freelist if false. Autovacuum if true.
     */
    private boolean autovacuum = IBtree.DB_DEFAULT_AUTOVACUUM != AutoVacuumMode.NONE;

    /**
     * Db text encoding.
     */
    private DbEncoding dbEncoding = DB_DEFAULT_ENCODING;

    /**
     * The user cookie. Used by the application.
     */
    private int userCookie;

    /**
     * Incremental-vacuum flag.
     */
    private boolean incrementalVacuum = IBtree.DB_DEFAULT_AUTOVACUUM == AutoVacuumMode.INCR;

    public Options(IBtree btree, IDbHandle dbHandle) throws DbException {
        this.btree = btree;
        this.dbHandle = dbHandle;
        if (readSchemaCookie() == 0) {
            try {
                initMeta();
            } catch (DbException e) {
                if (DbErrorCode.READONLY != e.getErrorCode()) {
                    throw e;
                }
            }
        } else {
            readMeta();
        }
    }

    /**
     * Get the database meta information.
     * 
     * Meta values are as follows:
     * 
     * <table border="1">
     * <tr>
     * <td>meta[1]</td>
     * <td>Schema cookie. Changes with each schema change.</td>
     * </tr>
     * <tr>
     * <td>meta[2]</td>
     * <td>File format of schema layer.</td>
     * </tr>
     * <tr>
     * <td>meta[3]</td>
     * <td>Size of the page cache.</td>
     * </tr>
     * <tr>
     * <td>meta[4]</td>
     * <td>Use freelist if 0. Autovacuum if greater than zero.</td>
     * </tr>
     * <tr>
     * <td>meta[5]</td>
     * <td>Db text encoding. 1:UTF-8 2:UTF-16LE 3:UTF-16BE</td>
     * </tr>
     * <tr>
     * <td>meta[6]</td>
     * <td>The user cookie. Used by the application.</td>
     * </tr>
     * <tr>
     * <td>meta[7]</td>
     * <td>Incremental-vacuum flag.</td>
     * </tr>
     * </table>
     * 
     * @throws DbException
     * 
     */
    private void readMeta() throws DbException {
        schemaCookie = readSchemaCookie();
        autovacuum = readAutoVacuum();
        fileFormat = readFileFormat();
        incrementalVacuum = readIncrementalVacuum();
        userCookie = readUserCookie();
        pageCacheSize = readPageCacheSize();
        dbEncoding = readEncoding();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ENCODING: " + dbEncoding + "\n");
        sb.append("SCHEMA VERSION: " + schemaCookie + "\n");
        sb.append("USER VERSION: " + userCookie + "\n");
        sb.append("FILE FORMAT: " + fileFormat + "\n");
        sb.append("AUTOVACUUM: " + autovacuum + "\n");
        sb.append("CACHE SIZE: " + pageCacheSize);
        return sb.toString();
    }

    private DbEncoding readEncoding() throws DbException {
        switch(btree.getMeta(ENCODING)) {
            case 0:
                if (readSchemaCookie() != 0) throw new DbException(DbErrorCode.CORRUPT);
            case 1:
                return DbEncoding.UTF8;
            case 2:
                return DbEncoding.UTF16LE;
            case 3:
                return DbEncoding.UTF16BE;
            default:
                throw new DbException(DbErrorCode.CORRUPT);
        }
    }

    private boolean readIncrementalVacuum() throws DbException {
        return btree.getMeta(INCREMENTAL_VACUUM) != 0;
    }

    private int readUserCookie() throws DbException {
        return btree.getMeta(USER_COOKIE);
    }

    private boolean readAutoVacuum() throws DbException {
        return btree.getMeta(AUTOVACUUM) != 0;
    }

    private int readPageCacheSize() throws DbException {
        int meta = btree.getMeta(PAGE_CACHE_SIZE);
        return meta > 0 ? meta : ILimits.DB_DEFAULT_CACHE_SIZE;
    }

    private int readFileFormat() throws DbException {
        final int fileFormat = btree.getMeta(FILE_FORMAT);
        checkFileFormat(fileFormat);
        return fileFormat;
    }

    private void checkFileFormat(final int fileFormat) throws DbException {
        if (fileFormat < ILimits.DB_MIN_FILE_FORMAT || fileFormat > ILimits.DB_MAX_FILE_FORMAT) throw new DbException(DbErrorCode.CORRUPT);
    }

    private int readSchemaCookie() throws DbException {
        return btree.getMeta(SCHEMA_COOKIE);
    }

    public int getSchemaVersion() throws DbException {
        return schemaCookie;
    }

    public int getFileFormat() throws DbException {
        return fileFormat;
    }

    public int getCacheSize() throws DbException {
        return pageCacheSize;
    }

    public boolean isAutovacuum() throws DbException {
        return autovacuum;
    }

    public DbEncoding getEncoding() throws DbException {
        return dbEncoding;
    }

    public boolean isLegacyFileFormat() throws DbException {
        return fileFormat == ILimits.DB_MIN_FILE_FORMAT;
    }

    public void setLegacyFileFormat(boolean flag) throws DbException {
        fileFormat = flag ? ILimits.DB_MIN_FILE_FORMAT : ILimits.DB_MAX_FILE_FORMAT;
    }

    public int getUserVersion() throws DbException {
        return userCookie;
    }

    public boolean isIncrementalVacuum() throws DbException {
        return incrementalVacuum;
    }

    public void setSchemaVersion(int version) throws DbException {
        dbHandle.getMutex().enter();
        try {
            if (!btree.isInTrans()) throw new DbException("It can be performed only in active transaction");
            verifySchemaVersion(true);
            writeSchemaCookie(this.schemaCookie = version);
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    public boolean verifySchemaVersion(boolean throwIfStale) throws DbException {
        dbHandle.getMutex().enter();
        try {
            final boolean stale = (schemaCookie != btree.getMeta(1));
            if (stale && throwIfStale) {
                throw new DbException(DbErrorCode.SCHEMA);
            }
            return !stale;
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    public void changeSchemaVersion() throws DbException {
        dbHandle.getMutex().enter();
        try {
            if (!btree.isInTrans()) throw new DbException("It can be performed only in active transaction");
            verifySchemaVersion(true);
            schemaCookie++;
            writeSchemaCookie(schemaCookie);
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    private void initMeta() throws DbException {
        btree.beginTrans(TransactionMode.EXCLUSIVE);
        try {
            schemaCookie = 1;
            writeSchemaCookie(schemaCookie);
            writeFileFormat(fileFormat);
            writePageCacheSize(pageCacheSize);
            writeEncoding(dbEncoding);
            final AutoVacuumMode btreeAutoVacuum = btree.getAutoVacuum();
            autovacuum = AutoVacuumMode.NONE != btreeAutoVacuum;
            incrementalVacuum = AutoVacuumMode.INCR == btreeAutoVacuum;
            writeAutoVacuum(autovacuum);
            writeIncrementalVacuum(incrementalVacuum);
            btree.commit();
        } catch (DbException e) {
            btree.rollback();
            throw e;
        }
    }

    private void writeSchemaCookie(int schemaCookie) throws DbException {
        btree.updateMeta(SCHEMA_COOKIE, schemaCookie);
    }

    private void writeEncoding(DbEncoding dbEncoding) throws DbException {
        switch(dbEncoding) {
            case UTF8:
                btree.updateMeta(ENCODING, 1);
                break;
            case UTF16LE:
                btree.updateMeta(ENCODING, 2);
                break;
            case UTF16BE:
                btree.updateMeta(ENCODING, 3);
                break;
            default:
                throw new DbException(DbErrorCode.CORRUPT);
        }
    }

    private void writeIncrementalVacuum(boolean incrementalVacuum) throws DbException {
        btree.updateMeta(INCREMENTAL_VACUUM, incrementalVacuum ? 1 : 0);
    }

    private void writeAutoVacuum(boolean autovacuum) throws DbException {
        btree.updateMeta(AUTOVACUUM, autovacuum ? 1 : 0);
    }

    private void writePageCacheSize(int pageCacheSize) throws DbException {
        checkPageCacheSize();
        btree.updateMeta(PAGE_CACHE_SIZE, pageCacheSize);
    }

    private void checkPageCacheSize() throws DbException {
        if (pageCacheSize < PageCache.PAGE_CACHE_SIZE_MINIMUM) pageCacheSize = PageCache.PAGE_CACHE_SIZE_DEFAULT;
    }

    private void writeFileFormat(int fileFormat) throws DbException {
        checkFileFormat(fileFormat);
        btree.updateMeta(FILE_FORMAT, fileFormat);
    }

    public void setUserVersion(int userCookie) throws DbException {
        dbHandle.getMutex().enter();
        try {
            if (!btree.isInTrans()) throw new DbException("It can be performed only in active transaction");
            writeUserCookie(this.userCookie = userCookie);
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    private void writeUserCookie(int userCookie) throws DbException {
        btree.updateMeta(USER_COOKIE, userCookie);
    }

    private void checkSchema() throws DbException {
        if (readSchemaCookie() != 1) {
            throw new DbException(DbErrorCode.MISUSE);
        }
    }

    public void setFileFormat(int fileFormat) throws DbException {
        dbHandle.getMutex().enter();
        try {
            checkSchema();
            if (btree.isInTrans()) throw new DbException("It can't be performed in active transaction");
            btree.beginTrans(TransactionMode.EXCLUSIVE);
            try {
                writeFileFormat(this.fileFormat = fileFormat);
                btree.commit();
            } catch (DbException e) {
                btree.rollback();
                throw e;
            }
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    public void setCacheSize(int pageCacheSize) throws DbException {
        dbHandle.getMutex().enter();
        try {
            if (!btree.isInTrans()) throw new DbException("It can be performed only in active transaction");
            writePageCacheSize(this.pageCacheSize = pageCacheSize);
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    public void setAutovacuum(boolean autovacuum) throws DbException {
        dbHandle.getMutex().enter();
        try {
            checkSchema();
            if (btree.isInTrans()) throw new DbException("It can't be performed in active transaction");
            btree.beginTrans(TransactionMode.EXCLUSIVE);
            try {
                writeAutoVacuum(this.autovacuum = autovacuum);
                btree.commit();
            } catch (DbException e) {
                btree.rollback();
                throw e;
            }
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    public void setEncoding(DbEncoding dbEncoding) throws DbException {
        dbHandle.getMutex().enter();
        try {
            checkSchema();
            if (btree.isInTrans()) throw new DbException("It can't be performed in active transaction");
            btree.beginTrans(TransactionMode.EXCLUSIVE);
            try {
                writeEncoding(this.dbEncoding = dbEncoding);
                btree.commit();
            } catch (DbException e) {
                btree.rollback();
                throw e;
            }
        } finally {
            dbHandle.getMutex().leave();
        }
    }

    public void setIncrementalVacuum(boolean incrementalVacuum) throws DbException {
        dbHandle.getMutex().enter();
        try {
            checkSchema();
            if (btree.isInTrans()) throw new DbException("It can't be performed in active transaction");
            btree.beginTrans(TransactionMode.EXCLUSIVE);
            try {
                writeIncrementalVacuum(this.incrementalVacuum = incrementalVacuum);
                btree.commit();
            } catch (DbException e) {
                btree.rollback();
                throw e;
            }
        } finally {
            dbHandle.getMutex().leave();
        }
    }
}
