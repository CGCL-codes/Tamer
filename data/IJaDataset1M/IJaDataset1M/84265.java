package com.ohua.checkpoint.framework.operatorcheckpoints;

public class DatabaseOperatorCheckPoint extends AbstractCheckPoint {

    private String _tableName = null;

    private int _batchCount = 0;

    private ColumnTypePair[] _colTypeArray = null;

    public int getBatchCount() {
        return _batchCount;
    }

    public void setBatchCount(int batchCount) {
        _batchCount = batchCount;
    }

    public ColumnTypePair[] getColTypeArray() {
        return _colTypeArray;
    }

    public void setColTypeArray(ColumnTypePair[] colTypeArray) {
        _colTypeArray = colTypeArray;
    }

    public String getTableName() {
        return _tableName;
    }

    public void setTableName(String tableName) {
        _tableName = tableName;
    }
}
