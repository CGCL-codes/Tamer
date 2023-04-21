package com.daffodilwoods.daffodildb.client;

import com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata._SelectColumnCharacteristics;
import com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata._RowReader;
import com.daffodilwoods.daffodildb.server.sql99.dql.listenerevents._SelectIterator;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.table.SelectIterator;
import java.sql.SQLException;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.server.datadictionarysystem._ColumnCharacteristics;
import java.util.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;

public class RecordSet implements _RecordSetBuffer {

    private int fetchSize = 50;

    private _RowReader rowReader;

    _SelectIterator selectIterator;

    private _ColumnCharacteristics columnCharacteristics;

    private _SelectColumnCharacteristics scc;

    private _RecordSetBufferIterator recordSetBufferIterator;

    ArrayList recordList;

    boolean topFetched;

    boolean bottomFetched;

    private int fetchDirection = 0;

    private int maxRows = 0;

    private int maxFieldSize = 0;

    private boolean lastFetchedDirection;

    private int noOfRecordMoved;

    private boolean afterLast;

    boolean isUpdateable;

    private String query = "";

    public RecordSet() {
        recordList = new ArrayList(fetchSize);
    }

    public void setSelectIterator(_SelectIterator selectIterator0) throws DException {
        this.selectIterator = selectIterator0;
        rowReader = selectIterator.getRowReader();
        columnCharacteristics = selectIterator.getColumnCharacteristics();
        scc = selectIterator.getSelectColumnCharacteristics();
    }

    public _RecordSetBufferIterator getIterator() throws SQLException {
        if (recordSetBufferIterator == null) {
            recordSetBufferIterator = new RecordSetBufferIterator();
            recordSetBufferIterator.setRecordSetBuffer(this);
        }
        return recordSetBufferIterator;
    }

    public void addDataOperationListener(DataOperationListener listener) {
        throw new java.lang.UnsupportedOperationException("Method addDataOperationListener() not yet implemented.");
    }

    public void removeDataOperationListener(DataOperationListener listener) {
        throw new java.lang.UnsupportedOperationException("Method removeDataOperationListener() not yet implemented.");
    }

    public void loadRecordForKey(_Record record, Object key) throws SQLException {
        if (key == null) throw new SQLException(new DException("DSE550", null).getMessage());
        int index = key.hashCode();
        if (index < 0 && index > recordList.size()) throw new SQLException(new DException("DSE552", new Object[] { key }).getMessage());
        Object row = recordList.get(index);
        JDBCRecord drecord = (JDBCRecord) record;
        drecord.setBuffer(this);
        drecord.setValues(row);
        drecord.setKey(key);
    }

    public void loadRecordForIdentity(_Record record, Object identity) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method loadRecordForIdentity() not yet implemented.");
    }

    public Object getKeyForIdentity(Object identity) {
        throw new java.lang.UnsupportedOperationException("Method getKeyForIdentity() not yet implemented.");
    }

    public Object getIdentityForKey(Object key) {
        throw new java.lang.UnsupportedOperationException("Method getIdentityForKey() not yet implemented.");
    }

    public String getTable() throws SQLException {
        return "";
    }

    public Object getTopKey() throws SQLException {
        try {
            int size = recordList.size();
            if (topFetched) return size == 0 ? null : new Integer(0);
            selectIterator.beforeFirst();
            int noOfRecordsToBeFetched = maxRows != 0 && fetchSize > maxRows ? maxRows : fetchSize;
            Object[] fetchedResult = selectIterator.fetchForward(noOfRecordsToBeFetched);
            topFetched = true;
            int numberOfRecordsFetched = fetchedResult == null ? 0 : fetchedResult.length;
            lastFetchedDirection = true;
            if (numberOfRecordsFetched == 0) {
                bottomFetched = true;
                afterLast = true;
                return null;
            }
            noOfRecordMoved = numberOfRecordsFetched;
            bottomFetched = maxRows != 0 && maxRows < fetchSize ? numberOfRecordsFetched <= maxRows : numberOfRecordsFetched < fetchSize;
            afterLast = bottomFetched && numberOfRecordsFetched < fetchSize;
            recordList.clear();
            recordList.addAll(Arrays.asList(fetchedResult));
            return new Integer(0);
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public Object getBottomKey() throws SQLException {
        try {
            int size = recordList.size();
            if (bottomFetched) return size == 0 ? null : new Integer(size - 1);
            int rowCount = selectIterator.getRowCount();
            if (rowCount < maxRows || maxRows == 0) {
                selectIterator.afterLast();
            } else {
                selectIterator.beforeFirst();
                selectIterator.move(maxRows + 1);
            }
            Object[] fetchedResult = selectIterator.fetchBackward(fetchSize);
            bottomFetched = true;
            afterLast = false;
            int numberOfRecordsFetched = fetchedResult == null ? 0 : fetchedResult.length;
            lastFetchedDirection = false;
            if (numberOfRecordsFetched == 0) {
                topFetched = true;
                return size == 0 ? null : new Integer(size - 1);
            }
            topFetched = numberOfRecordsFetched < fetchSize;
            noOfRecordMoved = maxRows;
            recordList.clear();
            for (int i = numberOfRecordsFetched - 1; i >= 0; i--) recordList.add(fetchedResult[i]);
            return new Integer(numberOfRecordsFetched - 1);
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public Object getNextKey(Object key) throws SQLException {
        try {
            if (key == null) throw new DException("DSE550", null);
            int index = key.hashCode();
            if (index == -1) throw new DException("DSE552", new Object[] { key });
            int size = recordList.size();
            if (bottomFetched || size - 1 > index) return size - 1 == index ? null : new Integer(index + 1);
            int noOfRecordsLeft = maxRows - noOfRecordMoved;
            if (maxRows != 0 && noOfRecordsLeft == 0) {
                bottomFetched = true;
                afterLast = true;
                return null;
            }
            if (!lastFetchedDirection) selectIterator.move(topFetched ? recordList.size() : recordList.size() - 1);
            lastFetchedDirection = true;
            int noOfRecordsToBeFetched = maxRows != 0 && fetchSize > noOfRecordsLeft ? noOfRecordsLeft : fetchSize;
            Object[] fetchedResult = selectIterator.fetchForward(noOfRecordsToBeFetched);
            int numberOfRecordsFetched = fetchedResult == null ? 0 : fetchedResult.length;
            if (numberOfRecordsFetched == 0) {
                bottomFetched = true;
                afterLast = true;
                return null;
            }
            topFetched = false;
            noOfRecordMoved += numberOfRecordsFetched;
            bottomFetched = maxRows != 0 && noOfRecordsLeft <= fetchSize ? numberOfRecordsFetched <= noOfRecordsToBeFetched : numberOfRecordsFetched < fetchSize;
            afterLast = bottomFetched && numberOfRecordsFetched < noOfRecordsLeft;
            recordList.clear();
            recordList.addAll(Arrays.asList(fetchedResult));
            return new Integer(0);
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public Object getPreviousKey(Object key) throws SQLException {
        try {
            if (key == null) throw new DException("DSE550", null);
            int index = key.hashCode();
            if (index == -1) throw new DException("DSE552", new Object[] { key });
            int size = recordList.size();
            if (topFetched || index != 0) return index == 0 ? null : new Integer(index - 1);
            if (lastFetchedDirection) selectIterator.move(bottomFetched ? (maxRows == 0 || afterLast ? -recordList.size() : -recordList.size() + 1) : -recordList.size() + 1);
            lastFetchedDirection = false;
            afterLast = false;
            Object[] fetchedResult = selectIterator.fetchBackward(fetchSize);
            int numberOfRecordsFetched = fetchedResult == null ? 0 : fetchedResult.length;
            if (numberOfRecordsFetched == 0) {
                topFetched = true;
                return null;
            }
            bottomFetched = false;
            topFetched = numberOfRecordsFetched < fetchSize;
            noOfRecordMoved -= size;
            recordList.clear();
            for (int i = numberOfRecordsFetched - 1; i >= 0; i--) recordList.add(fetchedResult[i]);
            return new Integer(numberOfRecordsFetched - 1);
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public int getDistanceBetweenKeys(Object key1, Object key2) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDistanceBetweenKeys() not yet implemented.");
    }

    public Object getKeyAtDistance(Object key, int distance) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getKeyAtDistance() not yet implemented.");
    }

    public Object insertInitiate() throws DException {
        throw new java.lang.UnsupportedOperationException("Method insertInitiate() not yet implemented.");
    }

    public Object updateInitiate(Object currentKey, String columnName, Object value) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateInitiate() not yet implemented.");
    }

    public Object deleteInitiate(Object deleteRecord) throws DException {
        throw new java.lang.UnsupportedOperationException("Method deleteInitiate() not yet implemented.");
    }

    public Object deleteRow(_Record deleteRecord) throws DException {
        throw new java.lang.UnsupportedOperationException("Method deleteInitiate() not yet implemented.");
    }

    public Object[] getParameters() {
        throw new java.lang.UnsupportedOperationException("Method getParameters() not yet implemented.");
    }

    public Object locateNearestKey(Object key) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method locateNearestKey() not yet implemented.");
    }

    public _SelectColumnCharacteristics getColumnCharacteristics() {
        return scc;
    }

    public Object seek(String clause) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method seek() not yet implemented.");
    }

    public void setFetchDirection(int direction) {
        fetchDirection = direction;
    }

    public void setFetchSize(int fetchSize0) {
        fetchSize = fetchSize0;
    }

    public void setMaxRows(int maxRows0) {
        maxRows = maxRows0;
    }

    public void setMaxFieldSize(int maxSize) {
        maxFieldSize = maxSize;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query0) {
        query = query0;
    }

    public void updateRow(_Record record) throws DException, SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateRow() not yet implemented.");
    }

    public _RowReader getRowReader() throws SQLException {
        return rowReader;
    }

    public Object insertInitiate(String[] columnNames, Object[] values) throws DException {
        throw new java.lang.UnsupportedOperationException("Method insertInitiate() not yet implemented.");
    }

    public Object updateInitiate(Object currentKey, String[] columnNames, Object[] values) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateInitiate() not yet implemented.");
    }

    public int getRowCount() throws SQLException {
        try {
            return selectIterator.getRowCount();
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public Object convertIntoParameter() throws SQLException, DException {
        throw new java.lang.UnsupportedOperationException("Method convertIntoParameter() not yet implemented.");
    }

    public boolean isBottom(Object key) throws SQLException {
        try {
            if (key == null) throw new DException("DSE550", null);
            int index = key.hashCode();
            if (index == -1) throw new DException("DSE552", new Object[] { key });
            int size = recordList.size();
            if (bottomFetched || size - 1 > index) return size - 1 == index;
            int noOfRecordsLeft = maxRows - noOfRecordMoved;
            if (maxRows != 0 && noOfRecordsLeft == 0) {
                bottomFetched = true;
                afterLast = true;
                return true;
            }
            if (!lastFetchedDirection) selectIterator.move(topFetched ? recordList.size() : recordList.size() - 1);
            int noOfRecordsToBeFetched = maxRows != 0 && fetchSize > noOfRecordsLeft ? noOfRecordsLeft : fetchSize;
            Object[] fetchedResult = selectIterator.fetchForward(noOfRecordsToBeFetched);
            int numberOfRecordsFetched = fetchedResult == null ? 0 : fetchedResult.length;
            if (numberOfRecordsFetched == 0) {
                bottomFetched = true;
                afterLast = true;
                return true;
            }
            topFetched = false;
            lastFetchedDirection = true;
            Object lastValue = recordList.get(index);
            noOfRecordMoved += numberOfRecordsFetched;
            bottomFetched = maxRows != 0 && noOfRecordsToBeFetched < fetchSize ? numberOfRecordsFetched <= noOfRecordsToBeFetched : numberOfRecordsFetched < fetchSize;
            afterLast = bottomFetched && numberOfRecordsFetched < noOfRecordsLeft;
            recordList.clear();
            recordList.add(lastValue);
            recordList.addAll(Arrays.asList(fetchedResult));
            recordSetBufferIterator.moveToKey(new Integer(0));
            return false;
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public boolean isTop(Object key) throws SQLException {
        try {
            if (key == null) throw new DException("DSE550", null);
            int index = key.hashCode();
            if (index == -1) throw new DException("DSE552", new Object[] { key });
            int size = recordList.size();
            if (topFetched || index != 0) return index == 0;
            if (lastFetchedDirection) selectIterator.move(bottomFetched ? (maxRows == 0 || afterLast ? -recordList.size() : -recordList.size() + 1) : -recordList.size() + 1);
            Object[] fetchedResult = selectIterator.fetchBackward(fetchSize);
            int numberOfRecordsFetched = fetchedResult == null ? 0 : fetchedResult.length;
            if (numberOfRecordsFetched == 0) {
                topFetched = true;
                return true;
            }
            bottomFetched = false;
            lastFetchedDirection = false;
            topFetched = numberOfRecordsFetched < fetchSize;
            noOfRecordMoved -= (size - 1);
            Object lastValue = recordList.get(0);
            recordList.clear();
            for (int i = numberOfRecordsFetched - 1; i >= 0; i--) recordList.add(fetchedResult[i]);
            recordList.add(lastValue);
            recordSetBufferIterator.moveToKey(new Integer(numberOfRecordsFetched));
            return false;
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public void setUpdateMode(boolean immediate) {
        throw new java.lang.UnsupportedOperationException("Method setUpdateMode() not yet implemented.");
    }

    public _Record getRecordInstance() {
        return new JDBCRecord(isUpdateable);
    }

    public _Record getInsertedRecord() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getInsertedRecord() not yet implemented.");
    }

    public Object insertRow() throws DException {
        throw new java.lang.UnsupportedOperationException("Method insertRow() not yet implemented.");
    }

    public _ExecutionPlan getExecutionPlan() throws DException {
        return selectIterator.getExecutionPlan();
    }

    public ExecutionPlanForBrowser getExecutionPlanForBrowser() throws DException {
        return selectIterator.getExecutionPlanForBrowser();
    }

    public _QueryPlan getQueryPlan() throws DException {
        return selectIterator.getQueryPlan();
    }

    public _Record getDummyRecord() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDummyRecord() not yet implemented.");
    }

    public void flushRecords(Object parm1, boolean parm2) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method flushRecords() not yet implemented.");
    }

    public void setAutoFlush(boolean flushData0) throws DException {
        throw new java.lang.UnsupportedOperationException("Method not yet implemented.");
    }

    public void setBufferSize(int bufferSize0) throws DException {
        throw new java.lang.UnsupportedOperationException("Method not yet implemented.");
    }

    void decrementNumberOfRecodsMoved() {
        if (maxRows > 0) {
            noOfRecordMoved--;
            bottomFetched = false;
        }
    }

    void updateBottomFetched() {
        if (maxRows == 0 || maxRows > noOfRecordMoved) {
            bottomFetched = false;
            afterLast = false;
        }
    }
}
