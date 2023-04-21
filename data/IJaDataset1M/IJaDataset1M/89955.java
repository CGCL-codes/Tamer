package org.subrecord.test.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.subrecord.model.Record;
import org.subrecord.repo.dao.AbstractDao;
import org.subrecord.repo.exception.RepoException;
import org.subrecord.repo.storage.Storage;
import org.subrecord.utils.Utils;

/**
 * This utility is meant to help keep track of data inserted during unit and
 * other tests, and allow an easy way of removing it all when tests are
 * finished.
 * 
 * @author bernie
 * 
 */
@Ignore
public class TestRecordHelper {

    /** An instance of the logger */
    protected final Log LOG = LogFactory.getLog(getClass());

    Storage storage;

    /**
	 * Keeps track of inserted rows by tablename & rowId. Map is: String
	 * tablename : List of String rowIds
	 */
    Map<String, List<String>> recordTracker = new HashMap<String, List<String>>();

    /**
	 * Can't create one without supplying the Store.
	 * 
	 * @param storage
	 */
    public TestRecordHelper(Storage storage) {
        this.storage = storage;
    }

    /**
	 * Gets the total number of records being tracked
	 * 
	 * @return the count of Records.
	 */
    public int size() {
        return (recordTracker == null ? 0 : getTotalRecords());
    }

    /**
	 * Calculate the total number of records tracked.
	 * 
	 * @return The integer total of all records stored
	 */
    private int getTotalRecords() {
        int total = 0;
        if (recordTracker != null) {
            for (String table : recordTracker.keySet()) {
                total += recordTracker.get(table) == null ? 0 : recordTracker.get(table).size();
            }
        }
        return total;
    }

    /**
	 * Remove the row.
	 * 
	 * @param tabelName
	 *            String: identifies the table
	 * @param rowId
	 *            String: identifies the row
	 * @return true if a row was removed.
	 */
    public boolean remove(String tableName, String rowId) throws RepoException {
        boolean removed = false;
        List<String> trackedRows = recordTracker.get(tableName);
        if (trackedRows != null) {
            storage.delete(tableName, rowId);
            trackedRows.remove(rowId);
            if (trackedRows.size() == 0) {
                recordTracker.remove(tableName);
            }
            removed = true;
        }
        return removed;
    }

    /**
	 * Store the supplied List of arrays of name value pairs as records and
	 * track them. Track and optionally store each entry in the supplied List of
	 * arrays of name value pairs.
	 * 
	 * @param tableName
	 *            The table to use.
	 * @param a
	 *            List of arrays of csvColValuePairs of columns and data.
	 * @return a list of row Ids.
	 * @throws RepoException
	 */
    public List<String> addTestRecords(String tableName, List<String[]> csvColValuePairs) throws RepoException {
        List<String> rowIds = new ArrayList<String>();
        for (String[] values : csvColValuePairs) {
            rowIds.add(addTestRecord(tableName, values));
        }
        return rowIds;
    }

    /**
	 * Store the supplied array of name value pairs and track it.
	 * 
	 * @param tableName
	 *            The table to use.
	 * @param csvColValuePairs
	 *            of columns and data
	 * @return the row Id of the new record.
	 * @throws RepoException
	 */
    public String addTestRecord(String tableName, String... csvColValuePairs) throws RepoException {
        Map<String, String> properties = Utils.toMap(csvColValuePairs);
        return addTestRecord(tableName, properties);
    }

    /**
	 * Store the supplied Map and track it.
	 * 
	 * @param tableName
	 *            The table to use.
	 * @param properties
	 *            The Map of columns and data.
	 * @return the row Id of the new record.
	 * @throws RepoException
	 */
    public String addTestRecord(String tableName, Map<String, String> properties) throws RepoException {
        try {
            String rowId = storage.put(tableName, properties);
            trackRow(tableName, rowId);
            return rowId;
        } catch (RepoException e) {
            LOG.error("Error tracking record", e);
            throw e;
        }
    }

    /**
	 * Store the supplied Record using the supplied DAO.
	 * 
	 * @param dao
	 *            The DAO to use.
	 * @param record
	 *            The new Record to Store.
	 * @return The Id of the new row.
	 * @throws RepoException
	 *             from underlying classes.
	 */
    public String addTestRecord(AbstractDao dao, Record record) throws RepoException {
        String rowId = dao.store(record);
        String tablename = dao.getTable();
        trackRow(dao.getTable(), rowId);
        return rowId;
    }

    /**
	 * Adds a single id to the table tracking, but doesn't store anything in the
	 * table. Use this to track data that is inserted by the test and not the
	 * helper.
	 * 
	 * @param tableName
	 * @param rowId
	 */
    public void trackRow(String tableName, String rowId) {
        List<String> rowIds = recordTracker.get(tableName);
        if (rowIds == null) {
            rowIds = new ArrayList<String>();
        }
        rowIds.add(rowId);
        recordTracker.put(tableName, rowIds);
    }

    /**
	 * Removes all tracked records.
	 */
    public void cleanupAll() {
        String id = null;
        String name = null;
        try {
            Set<String> tableNames = recordTracker.keySet();
            for (String tableName : tableNames) {
                name = tableName;
                List<String> rowIds = recordTracker.get(tableName);
                for (String rowId : rowIds) {
                    id = rowId;
                    storage.delete(tableName, rowId);
                }
            }
        } catch (RepoException e) {
            LOG.error("Error cleaning-up test record in table " + name + " for row id: " + id, e);
        }
    }
}
