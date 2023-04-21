package br.ufmg.lcc.pcollecta.model.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import oracle.jdbc.driver.OracleResultSetCache;

public class OracleResultSetCacheLightImpl implements OracleResultSetCache {

    private int recordsToSkip = -1;

    private int currentRowPosition = 0;

    private int currentColumnPosition = 0;

    private final Map<Integer, Map<Integer, Object>> rowsMap = new HashMap();

    @Override
    public void clear() throws IOException {
        rowsMap.clear();
    }

    @Override
    public void close() throws IOException {
        rowsMap.clear();
    }

    @Override
    public Object get(int row, int column) throws IOException {
        Object obj = null;
        Map rowMap = rowsMap.get(row);
        if (rowMap != null) {
            if (rowMap.containsKey(column)) {
                obj = rowMap.get(column);
                rowMap.remove(column);
            }
            if (rowMap.isEmpty()) {
                rowsMap.remove(row);
            }
        }
        return obj;
    }

    @Override
    public void put(int row, int column, Object object) throws IOException {
        if (row <= recordsToSkip || (row <= currentRowPosition && column <= currentColumnPosition)) {
            return;
        }
        currentRowPosition = row;
        currentColumnPosition = column;
        Map rowMap = rowsMap.get(row);
        if (rowMap == null) {
            rowMap = new HashMap();
            rowsMap.put(row, rowMap);
        }
        rowMap.put(column, object);
    }

    @Override
    public void remove(int row) throws IOException {
        if (rowsMap.containsKey(row)) {
            rowsMap.remove(row);
        }
    }

    @Override
    public void remove(int row, int column) throws IOException {
        Map rowMap = rowsMap.get(row);
        if (rowMap != null) {
            if (rowMap.containsKey(column)) {
                rowMap.remove(column);
            }
        }
    }

    public int getRecordsToSkip() {
        return recordsToSkip;
    }

    public void setRecordsToSkip(int recordsToSkip) {
        this.recordsToSkip = recordsToSkip;
    }
}
