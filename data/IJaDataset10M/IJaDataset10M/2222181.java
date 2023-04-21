package com.daffodilwoods.daffodildb.client;

import com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata._SelectColumnCharacteristics;
import com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata._RowReader;
import com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata.SelectColumnCharacteristics;
import com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata.ForeignTableColumnCharacteristics;
import java.sql.SQLException;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.queryexpression.*;

public class ForeignTableRecord implements _Record {

    Object row;

    _RowReader rowReader;

    _SelectColumnCharacteristics queryScc;

    _SelectColumnCharacteristics newScc;

    public ForeignTableRecord(Object row0, _RowReader rowReader0, _SelectColumnCharacteristics queryScc0, _SelectColumnCharacteristics newScc0) {
        row = row0;
        rowReader = rowReader0;
        queryScc = queryScc0;
        newScc = newScc0;
    }

    public _SelectColumnCharacteristics getColumnCharacteristics() {
        return newScc;
    }

    public Object getColumnValue(int parm1) throws java.sql.SQLException {
        try {
            int selectIndex = ((ForeignTableColumnCharacteristics) newScc).getSelectedIndex(parm1);
            return rowReader.getObject(selectIndex, row);
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public Object getColumnValue(String parm1) throws java.sql.SQLException {
        try {
            int selectIndex = ((ForeignTableColumnCharacteristics) newScc).getSelectedIndex(parm1);
            return rowReader.getObject(selectIndex, row);
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    public void updateInitiate(int parm1, Object parm2) throws java.sql.SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateInitiate() not yet implemented.");
    }

    public void loadRecord(_Record parm1) throws java.sql.SQLException {
        throw new java.lang.UnsupportedOperationException("Method loadRecord() not yet implemented.");
    }

    public _Record getRecord(int parm1) throws java.sql.SQLException {
        try {
            int selectIndex = ((ForeignTableColumnCharacteristics) newScc).getSelectedIndex(parm1);
            if (queryScc.isForeignTableRecordFetched(selectIndex)) {
                _SelectColumnCharacteristics fScc = ((SelectColumnCharacteristics) queryScc).getColumnCharacteristics(selectIndex);
                return new ForeignTableRecord(row, rowReader, queryScc, fScc);
            }
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
        return null;
    }

    public _Record getRecord(String parm1) throws SQLException {
        try {
            int selectIndex = ((ForeignTableColumnCharacteristics) newScc).getSelectedIndex(parm1);
            if (queryScc.isForeignTableRecordFetched(selectIndex)) {
                _SelectColumnCharacteristics fScc = ((SelectColumnCharacteristics) queryScc).getColumnCharacteristics(selectIndex);
                return new ForeignTableRecord(row, rowReader, queryScc, fScc);
            }
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
        return null;
    }

    public Object getIdentity() throws java.sql.SQLException {
        throw new java.lang.UnsupportedOperationException("Method getIdentity() not yet implemented.");
    }

    public void cancelUpdate() {
        throw new java.lang.UnsupportedOperationException("Method cancelUpdate() not yet implemented.");
    }

    public _RecordSetBuffer getRecordSetBuffer() {
        throw new java.lang.UnsupportedOperationException("Method getRecordSetBuffer() not yet implemented.");
    }

    public boolean isLoaded() {
        return true;
    }

    public boolean wasUpdated() {
        throw new java.lang.UnsupportedOperationException("Method wasUpdated() not yet implemented.");
    }

    public void unLoad() {
        throw new java.lang.UnsupportedOperationException("Method unLoad() not yet implemented.");
    }

    public void setBuffer(_RecordSetBuffer parm1) {
        throw new java.lang.UnsupportedOperationException("Method setBuffer() not yet implemented.");
    }

    public boolean isInserted() {
        throw new java.lang.UnsupportedOperationException("Method isInserted() not yet implemented.");
    }
}
