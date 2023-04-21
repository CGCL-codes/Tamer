package com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata;

import java.text.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.server.datadictionarysystem._ColumnCharacteristics;

/**
 * It represents the column characteristics of 'From Subquery'
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class ColumnCharacteristicsFromSubQuery implements _SelectColumnCharacteristics {

    /**
    * Represents the characteristics of select query contained in 'from subquery'
    */
    _SelectColumnCharacteristics cc;

    public ColumnCharacteristicsFromSubQuery(_SelectColumnCharacteristics cc0) {
        cc = cc0;
    }

    /**
    * It delegates the call to column characteristics of select query involved in
    * 'from subquery'. For documentation, refer the documentation of
    * ColumnCharacteristics interface.
    */
    public int getColumnType(int index) throws DException {
        return cc.getColumnType(index);
    }

    public int getColumnIndex(String columnName) throws DException {
        return cc.getColumnIndex(columnName);
    }

    public String getColumnName(int parm1) throws DException {
        return cc.getColumnName(parm1);
    }

    public String getRelatedTable(int parm1) throws DatabaseException, DException {
        return cc.getRelatedTable(parm1);
    }

    public int getColumnCount() throws DException {
        return cc.getColumnCount();
    }

    public String[] getColumnNames() throws DException {
        return cc.getColumnNames();
    }

    public int getSize(int parm1) throws DException {
        return cc.getSize(parm1);
    }

    public String[] getPrimaryKeys() throws DatabaseException, DException {
        return cc.getPrimaryKeys();
    }

    public String getRelation(int parm1) throws DatabaseException, DException {
        return cc.getRelation(parm1);
    }

    public int[] getColumnIndexes(String[] parm1) throws DException {
        return cc.getColumnIndexes(parm1);
    }

    public int[] getPrimaryConditionColumns() throws DException {
        return cc.getPrimaryConditionColumns();
    }

    public String getTableName(int index) throws DatabaseException, DException {
        return cc.getTableName(index);
    }

    public short getTableType() throws DException {
        return TypeConstants.VIEW;
    }

    public int getPrecision(int index) throws DException {
        return cc.getPrecision(index);
    }

    public int getScale(int index) throws DException {
        return cc.getScale(index);
    }

    public String getSchemaName(int index) throws DException {
        return cc.getSchemaName(index);
    }

    public String getCatalogName(int index) throws DException {
        return cc.getCatalogName(index);
    }

    public int isNullable(int index) throws DException {
        return cc.isNullable(index);
    }

    public boolean isAutoIncrement(int index) throws DException {
        return cc.isAutoIncrement(index + 1);
    }

    public String getColumnLabel(int index) throws DException {
        return cc.getColumnLabel(index);
    }

    public String getQualifiedTableName(int index) throws DException {
        return cc.getQualifiedTableName(index);
    }

    public boolean isColumnUpdatable(int columnIndex) throws DException {
        return cc.isColumnUpdatable(columnIndex);
    }

    public boolean isUpdatable() throws DException {
        return cc.isUpdatable();
    }

    public boolean isColumnSelectable(int columnIndex) throws DException {
        return cc.isColumnSelectable(columnIndex);
    }

    public _SelectColumnCharacteristics getColumnCharacteristics(int parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getColumnCharacteristics() not yet implemented.");
    }

    public boolean isForeignTableRecordFetched(int parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method isForeignTableReferenced() not yet implemented.");
    }

    public int getSelectedIndex(int parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getSelectedIndex() not yet implemented.");
    }

    public int getSelectedIndex(String parm1) throws com.daffodilwoods.database.resource.DException {
        throw new java.lang.UnsupportedOperationException("Method getSelectedIndex() not yet implemented.");
    }

    public String getExpression(int index) throws DException {
        throw new java.lang.UnsupportedOperationException("Method getExpression() not yet implemented.");
    }

    public String[] getRelatedColumns(int parm1) throws DatabaseException, DException {
        throw new java.lang.UnsupportedOperationException("Method getExpression() not yet implemented.");
    }

    public Collator getCollator() throws DException {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    public Object[] getCorresspondingColumnsForFromSubQueryColumn(Object column) throws DException {
        return cc.getCorresspondingColumnsForFromSubQueryColumn(column);
    }

    public _ColumnCharacteristics getCCFromIndexes(_ColumnCharacteristics cc, int offset, int[] columnIndexes) throws DException {
        throw new UnsupportedOperationException("getCCFromIndexes(cc, offset, columnIndexes) method not implemented yet");
    }
}
