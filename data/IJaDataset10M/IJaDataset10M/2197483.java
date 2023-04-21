package com.daffodilwoods.daffodildb.server.datasystem.indexsystem;

import com.daffodilwoods.daffodildb.server.sql99.dql.iterator._Iterator;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.TableCharacteristics;
import java.util.*;
import com.daffodilwoods.daffodildb.server.datasystem.utility.*;
import com.daffodilwoods.daffodildb.server.datasystem.btree.*;
import com.daffodilwoods.fulltext.common._FullTextDML;
import com.daffodilwoods.daffodildb.server.sql99.fulltext.dml._FullTextIndexInformation;

/**
 *
 * <p>Title: Your Product Name</p>
 * @version
 */
public class ColumnObjectsTableHashMap implements _IndexTableList, _IndexTable {

    private _IndexTableList table;

    private TableCharacteristics tableCharacteristics;

    public ColumnObjectsTableHashMap(HashMap columnObjectsHashMap1) {
    }

    public _TableCharacteristics getTableCharacteristics() throws DException {
        return table.getTableCharacteristics();
    }

    public void setTable(_IndexTableList table0) throws DException {
        table = table0;
        tableCharacteristics = (TableCharacteristics) table.getTableCharacteristics();
    }

    public _IndexTableList getTable() {
        throw new java.lang.UnsupportedOperationException("Method getTable() not yet implemented.");
    }

    public void setFullTextDML(_FullTextDML fullTextDML[]) throws DException {
        table.setFullTextDML(fullTextDML);
    }

    public _FullTextDML[] getFullTextDML() throws DException {
        return table.getFullTextDML();
    }

    public Object insert(_TableIterator iterator, _DatabaseUser user, Object[] values, int index) throws DException {
        return null;
    }

    public Object update(_TableIterator iterator, _DatabaseUser user, Object[] values, int index) throws DException {
        return null;
    }

    public Object delete(_TableIterator iterator, _DatabaseUser user, int index) throws DException {
        return null;
    }

    public Object insert(_TableIterator iterator, Object[] values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method insert() not yet implemented.");
    }

    public Object update(_TableIterator iterator, Object[] values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method update() not yet implemented.");
    }

    public Object delete(_TableIterator iterator, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method delete() not yet implemented.");
    }

    public Object deleteBlobClobRecord(_TableIterator iterator, _DatabaseUser user, int index) throws DException {
        return null;
    }

    public Object getColumnObjects(_TableIterator iterator, int[] columns) throws DException {
        return null;
    }

    public _Index[] getIndexes() throws DException {
        return table.getIndexes();
    }

    public Object getColumnObjects(_TableIterator iterator, int columns) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnObjects() not yet implemented.");
    }

    public Object getColumnObjects(_TableIterator iterator) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnObjects() not yet implemented.");
    }

    public _IndexInformation[] getIndexInformations() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getIndexInformations() not yet implemented.");
    }

    public _Iterator getIterator(int parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getIterator() not yet implemented.");
    }

    public _Record getBlankRecord() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getBlankRecord() not yet implemented.");
    }

    public _Index getIndex(int position) {
        return table.getIndex(position);
    }

    public Object insert(_TableIterator iterator, _DatabaseUser user, Object values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method insert() not yet implemented.");
    }

    public Object update(_TableIterator iterator, _DatabaseUser user, Object values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method update() not yet implemented.");
    }

    public Object insert(_TableIterator iterator, Object values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method insert() not yet implemented.");
    }

    public Object update(_TableIterator iterator, Object values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method update() not yet implemented.");
    }

    public void rollback() throws DException {
        throw new java.lang.UnsupportedOperationException("Method update() not yet implemented.");
    }

    public Object seek(_Index btree, Object indexKey) throws DException {
        throw new java.lang.UnsupportedOperationException("Method seek() not yet implemented.");
    }

    public Object seekFromTopRelative(_Index btree, Object currentKey, Object indexKey) throws DException {
        throw new java.lang.UnsupportedOperationException("Method seekFromTopRelative() not yet implemented.");
    }

    public Object seekFromBottomRelative(_Index btree, Object currentKey, Object indexKey) throws DException {
        throw new java.lang.UnsupportedOperationException("Method seekFromBottomRelative() not yet implemented.");
    }

    public Object locateKey(_Index btree, Object indexKey, boolean flag) throws DException {
        throw new java.lang.UnsupportedOperationException("Method locateKey() not yet implemented.");
    }

    public void setIndexes(_Index btrees[]) throws DException {
        table.setIndexes(btrees);
    }

    public _Iterator getDefaultIterator() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getDefaultIterator() not yet implemented.");
    }

    public int getEstimatedRowCount() throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getEstimatedRowCount() not yet implemented.");
    }

    public Object update(_TableIterator iterator, _DatabaseUser user, int[] columns, Object[] values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method update() not yet implemented.");
    }

    public Object update(_TableIterator iterator, int[] columns, Object[] values, int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method update() not yet implemented.");
    }

    public ReadWriteLock getLock() {
        return table.getLock();
    }

    public boolean isAnyIndexUpdated(int[] columns) throws DException {
        throw new java.lang.UnsupportedOperationException("Method isAnyIndexUpdated() not yet implemented.");
    }

    public _FullTextIndexInformation[] getFullTextIndexInformation() throws DException {
        return table.getFullTextIndexInformation();
    }

    public _Database getDatabase() throws DException {
        return table.getDatabase();
    }

    public void setDuplicateKeysAllowedInBtrees() throws DException {
        throw new java.lang.UnsupportedOperationException("Method setDuplicateKeysAllowedInBtrees() not yet implemented.");
    }
}
