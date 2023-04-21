package org.archive.crawler.byexample.datastructure.invertedindex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.archive.crawler.byexample.constants.OutputConstants;
import org.archive.crawler.byexample.utils.FileUtils;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

/**
 * InvertedIndex interface implementation using Berkeley DB.
 * Index data is written to DB during the crawl
 * 
 * @author Michael Bendersky
 *
 */
public class BdbIndex implements InvertedIndex {

    private Environment myEnv;

    private Database indexDb;

    private int numOfRecords;

    private Map<String, CachedIndexEntry> indexCache;

    public BdbIndex(String envHome) {
        openIndex(envHome);
        indexCache = new ConcurrentHashMap<String, CachedIndexEntry>();
        loadIndexCache();
        numOfRecords = indexCache.size();
    }

    public void openIndex(String envHome) {
        boolean readOnly = false;
        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        DatabaseConfig myDbConfig = new DatabaseConfig();
        myEnvConfig.setReadOnly(readOnly);
        myDbConfig.setReadOnly(readOnly);
        myEnvConfig.setAllowCreate(!readOnly);
        myDbConfig.setAllowCreate(!readOnly);
        try {
            myEnv = new Environment(new File(envHome), myEnvConfig);
            indexDb = myEnv.openDatabase(null, OutputConstants.TERMS_INDEX_FILENAME, myDbConfig);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to open Index", e);
        }
    }

    public void moveCacheToDb(String rowKey) {
        DatabaseEntry entryToAdd = new DatabaseEntry();
        DatabaseEntry dbKey = null;
        try {
            dbKey = new DatabaseEntry(rowKey.getBytes(OutputConstants.DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Invalid encoding", e);
        }
        DatabaseEntry dbEntry = new DatabaseEntry();
        IndexEntry ie = indexCache.get(rowKey).getCachedEntry();
        try {
            if (indexDb.get(null, dbKey, dbEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                IndexRow ir = new IndexRow();
                ir = (IndexRow) ir.entryToObject(dbEntry);
                ir.addRowEntry(ie);
                ir.setTotalValue(indexCache.get(rowKey).getCachedRowValue());
                ir.objectToEntry(ir, entryToAdd);
                indexDb.put(null, dbKey, entryToAdd);
            }
        } catch (DatabaseException e) {
            throw new RuntimeException("Could not access db", e);
        }
    }

    public void addEntry(String rowKey, IndexEntry rowEntry) {
        CachedIndexEntry cie = indexCache.get(rowKey);
        if (cie.getCachedEntry() != null) {
            if (cie.getCachedEntry().getEntryId() != rowEntry.getEntryId()) {
                moveCacheToDb(rowKey);
                indexCache.put(rowKey, new CachedIndexEntry(rowEntry, cie.getCachedRowValue() + 1));
            }
        } else indexCache.put(rowKey, new CachedIndexEntry(rowEntry, 1));
    }

    public void increaseRowLatestEntryValue(String rowKey) {
        indexCache.get(rowKey).getCachedEntry().increaseEntryValue();
        indexCache.get(rowKey).increaseCachedRowValue();
    }

    public String getRowLatestEntryId(String rowKey) {
        IndexEntry latestEntry = indexCache.get(rowKey).getCachedEntry();
        if (latestEntry == null) return String.valueOf(-1); else return latestEntry.getEntryId();
    }

    public void sortRow(String rowKey, Comparator<IndexEntry> comparator) {
        IndexRow rowToSort = getRow(rowKey);
        DatabaseEntry dbKey = null;
        try {
            dbKey = new DatabaseEntry(rowKey.getBytes(OutputConstants.DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Invalid encoding", e);
        }
        DatabaseEntry de = new DatabaseEntry();
        rowToSort.sortRow(comparator);
        rowToSort.objectToEntry(rowToSort, de);
        try {
            indexDb.put(null, dbKey, de);
        } catch (DatabaseException e) {
            throw new RuntimeException("Could not access db", e);
        }
    }

    public void normalizeRow(String rowKey) {
        IndexRow rowToNorm = getRow(rowKey);
        int normValue = indexCache.get(rowKey).getCachedRowValue();
        for (Iterator<IndexEntry> iter = rowToNorm.getRowIterator(); iter.hasNext(); ) {
            iter.next().scaleEntryValue(normValue);
        }
    }

    public void loadIndexCache() {
        try {
            Cursor cursor = indexDb.openCursor(null, null);
            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();
            IndexRow ir = new IndexRow();
            String keyString;
            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                keyString = new String(foundKey.getData(), OutputConstants.DEFAULT_ENCODING);
                ir = (IndexRow) ir.entryToObject(foundData);
                indexCache.put(keyString, new CachedIndexEntry(null, ir.getTotalValue()));
            }
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeIndex(String jobId, String envHome) {
        BufferedWriter out = FileUtils.createFileForJob(jobId, envHome, OutputConstants.TERMS_INDEX_FILENAME, true);
        StringBuffer dump = new StringBuffer();
        String currKey;
        for (Iterator<String> iter = indexCache.keySet().iterator(); iter.hasNext(); ) {
            currKey = iter.next();
            moveCacheToDb(currKey);
            dump.append(currKey);
            dump.append(OutputConstants.KEY_SEPARATOR);
            dump.append(getRow(currKey).toString());
            dump.append("\n");
        }
        try {
            if (myEnv != null) {
                indexDb.close();
                myEnv.close();
            }
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to close index", e);
        }
        FileUtils.dumpBufferToFile(out, dump);
        FileUtils.closeFile(out);
    }

    public Database getIndexDb() {
        return indexDb;
    }

    public Environment getMyEnv() {
        return myEnv;
    }

    public void addNewRow(String rowKey) {
        try {
            DatabaseEntry dbKey = new DatabaseEntry(rowKey.getBytes(OutputConstants.DEFAULT_ENCODING));
            DatabaseEntry dbEntry = new DatabaseEntry();
            IndexRow rowToPut = new IndexRow();
            rowToPut.objectToEntry(rowToPut, dbEntry);
            indexDb.put(null, dbKey, dbEntry);
            numOfRecords++;
            indexCache.put(rowKey, new CachedIndexEntry());
        } catch (Exception e) {
            throw new RuntimeException("Couldn't add new row", e);
        }
    }

    public void addRow(String rowKey, IndexRow row) {
        try {
            DatabaseEntry dbKey = new DatabaseEntry(rowKey.getBytes(OutputConstants.DEFAULT_ENCODING));
            DatabaseEntry dbEntry = new DatabaseEntry();
            row.objectToEntry(row, dbEntry);
            indexDb.put(null, dbKey, dbEntry);
            numOfRecords++;
            indexCache.put(rowKey, new CachedIndexEntry(row.getIndex(row.getRowSize()), row.getTotalValue()));
        } catch (Exception e) {
            throw new RuntimeException("Couldn't add index row", e);
        }
    }

    public IndexRow getRow(String rowKey) {
        try {
            DatabaseEntry dbKey = new DatabaseEntry(rowKey.getBytes(OutputConstants.DEFAULT_ENCODING));
            DatabaseEntry dbEntry = new DatabaseEntry();
            IndexRow rowToReturn = new IndexRow();
            if (indexDb.get(null, dbKey, dbEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                return (IndexRow) rowToReturn.entryToObject(dbEntry);
            }
        } catch (Exception e) {
            throw new RuntimeException("Couldn't get index row", e);
        }
        return null;
    }

    public void removeRow(String rowKey) {
        try {
            DatabaseEntry dbKey = new DatabaseEntry(rowKey.getBytes(OutputConstants.DEFAULT_ENCODING));
            indexDb.delete(null, dbKey);
            indexCache.remove(rowKey);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't delete index row", e);
        }
    }

    public int getSize() {
        return numOfRecords;
    }

    public boolean containsKey(String rowKey) {
        return indexCache.containsKey(rowKey);
    }

    public Iterator<String> getIndexKeysIterator() {
        return indexCache.keySet().iterator();
    }
}
