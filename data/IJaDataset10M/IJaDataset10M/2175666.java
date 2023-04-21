package com.daffodilwoods.daffodildb.server.datasystem.utility;

import com.daffodilwoods.daffodildb.server.datasystem.utility._Record;
import com.daffodilwoods.database.resource.*;
import java.util.*;

public class RecordVersion implements java.io.Serializable {

    HashMap columnIndexesMap;

    RecordVersion rv;

    _Record currentRecord;

    public RecordVersion(_Record record0) throws DException {
        currentRecord = record0;
    }

    public _Record getPreviousRecord() throws DException {
        _Record record = (_Record) currentRecord.clone();
        ;
        if (columnIndexesMap == null) return record;
        Set keySet = columnIndexesMap.keySet();
        Iterator iter = keySet.iterator();
        for (int i = 0; i < columnIndexesMap.size(); i++) {
            Object columnIndex = iter.next();
            record.update(columnIndex.hashCode(), columnIndexesMap.get(columnIndex));
        }
        return (_Record) record;
    }

    public _Record getCurrentRecord() throws DException {
        return (_Record) currentRecord.clone();
    }

    public void update(String columnName, Object newValue) throws DException {
        throw new InternalError("  method not implemented ");
    }

    public void update(String[] columnName, Object[] newValue) throws DException {
        throw new InternalError("  method not implemented ");
    }

    public void update(int[] columnIndexes, Object[] newValues) throws DException {
        for (int i = 0; i < columnIndexes.length; i++) update(columnIndexes[i], newValues[i]);
    }

    public void update(int columnIndex, Object newValue) throws DException {
        Object oldValue = currentRecord.getObject(columnIndex);
        Integer column = new Integer(columnIndex);
        if (columnIndexesMap == null) columnIndexesMap = new HashMap();
        columnIndexesMap.put(column, oldValue);
        currentRecord.update(columnIndex, newValue);
    }

    public Object Clone() throws CloneNotSupportedException {
        try {
            rv = new RecordVersion((_Record) currentRecord.clone());
            if (columnIndexesMap == null) rv.setColumnIndexMap(null); else rv.setColumnIndexMap((HashMap) columnIndexesMap.clone());
        } catch (DException ex) {
        }
        return rv;
    }

    public void setColumnIndexMap(HashMap columnIndexesMap) throws DException {
        this.columnIndexesMap = columnIndexesMap;
    }
}
