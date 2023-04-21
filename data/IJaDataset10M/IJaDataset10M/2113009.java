package com.daffodilwoods.daffodildb.server.datasystem.persistentsystem;

import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.utils.BufferRange;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import java.util.*;
import com.daffodilwoods.daffodildb.server.sql99.utils._Reference;

/**
 *
 * <p>Title: Your Product Name</p>
 * <p>Description: Your description</p>
 * <p>Copyright: Copyright (c) 1999</p>
 * <p>Company: Your Company</p>
 * @author Ashish Srivastava
 * @version
 */
public class RecordBytesTableHashMap implements _TableList {

    HashMap recordBytesHashMap;

    _TableList table;

    int CODE;

    public RecordBytesTableHashMap(HashMap recordBytesHashMap1, int CODE1) {
        recordBytesHashMap = recordBytesHashMap1;
        CODE = CODE1;
    }

    public Object insert(_DatabaseUser user, _RecordCluster recordCluster, Object values) throws DException {
        TableKey tableKey = (TableKey) table.insert(user, recordCluster, values);
        Object mapKey = new Long(tableKey.getStartAddress());
        Object obj = recordBytesHashMap.get(mapKey);
        ArrayList retrieveValues = obj == null ? initializeArrayList(recordCluster.getRecordCount()) : (ArrayList) obj;
        retrieveValues.set(tableKey.getRecordNumber() - 1, values);
        if (obj == null) recordBytesHashMap.put(mapKey, retrieveValues);
        return tableKey;
    }

    public _TableList getTable() {
        return table;
    }

    public _TableList getTable(int num) {
        return CODE == num ? table : table.getTable(num);
    }

    public Object update(_DatabaseUser user, _RecordCluster recordCluster, Object key, Object values) throws DException {
        TableKey tableKey = (TableKey) table.update(user, recordCluster, key, values);
        Object mapKey = new Long(tableKey.getStartAddress());
        boolean flag = recordBytesHashMap.containsKey(mapKey);
        ArrayList retrieveValues = !flag ? initializeArrayList(recordCluster.getRecordCount()) : (ArrayList) recordBytesHashMap.get(mapKey);
        retrieveValues.set(tableKey.getRecordNumber() - 1, values);
        if (!flag) recordBytesHashMap.put(mapKey, retrieveValues);
        return tableKey;
    }

    public Object delete(_DatabaseUser user, _RecordCluster recordCluster, Object key) throws DException {
        TableKey tableKey = (TableKey) table.delete(user, recordCluster, key);
        Object mapKey = new Long(tableKey.getStartAddress());
        boolean flag = recordBytesHashMap.containsKey(mapKey);
        if (!flag) return tableKey;
        ArrayList retrieveValues = (ArrayList) recordBytesHashMap.get(mapKey);
        retrieveValues.set(tableKey.getRecordNumber() - 1, null);
        return tableKey;
    }

    public void setTable(_TableList table1) {
        table = table1;
    }

    private ArrayList initializeArrayList(int size) {
        ArrayList retrievedObjects = new ArrayList(size);
        for (int i = 0; i < size; i++) retrievedObjects.add(null);
        return retrievedObjects;
    }

    public int getColumnCount() throws DException {
        return table.getColumnCount();
    }

    public TableProperties getTableProperties() throws DException {
        throw new java.lang.UnsupportedOperationException("Method update() not yet implemented.");
    }

    public void checkValidity(_RecordCluster recordCluster, Object key) throws DException {
        table.checkValidity(recordCluster, key);
    }

    public _TableCharacteristics getTableCharacteristics() throws DException {
        throw new java.lang.UnsupportedOperationException("Method getTableCharacteristics() not yet implemented.");
    }

    public void rollBack() throws DException {
        table.rollBack();
    }

    public Object getColumnObjects(_RecordCluster recordCluster, Object key) throws DException {
        return getColumnObjects(recordCluster, key);
    }

    public Object getColumnObjects(_RecordCluster recordCluster, Object key, int column) throws DException {
        return getColumnObjects(recordCluster, key, column);
    }

    public Object getColumnValues(TableKey key, _RecordCluster cluster) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public Object getColumnValues(TableKey key, _RecordCluster cluster, int column) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }

    public Object getColumnValues(TableKey key, _RecordCluster cluster, int[] columns) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnValues() not yet implemented.");
    }
}
