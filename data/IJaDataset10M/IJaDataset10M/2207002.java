package edu.udo.scaffoldhunter.view.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Michael Hesse
 *
 * note: this class is deprecated and will be removed in the near future
 * 
 */
public class DataCacheMatrix {

    private Map<Integer, Map<Integer, Object>> matrixRows;

    /**
     * 
     */
    public DataCacheMatrix() {
        matrixRows = new TreeMap<Integer, Map<Integer, Object>>();
    }

    /**
     * @param column
     * @param row
     * @return
     *  the value 
     */
    public synchronized Object getValue(int column, int row) {
        Object value = null;
        Map<Integer, Object> matrixColumns = matrixRows.get(row);
        if (matrixColumns != null) value = matrixColumns.get(column);
        return value;
    }

    /**
     * @param column
     * @param row
     * @return
     *  true if the specified value is currently stored in the matrix
     */
    public synchronized boolean contains(int column, int row) {
        boolean isPresent = false;
        Map<Integer, Object> matrixColumns = matrixRows.get(row);
        if (matrixColumns != null) isPresent = matrixColumns.containsKey(column);
        return isPresent;
    }

    /**
     * stores a value in the matrix
     * @param column
     * @param row
     * @param value
     */
    public synchronized void setValue(int column, int row, Object value) {
        Map<Integer, Object> matrixColumns = matrixRows.get(row);
        if (matrixColumns == null) {
            matrixColumns = new TreeMap<Integer, Object>();
            matrixRows.put(row, matrixColumns);
        }
        matrixColumns.put(column, value);
    }

    /**
     * removes matrix cells that are no longer needed and creates a list
     * of cells that should be loaded. the list will be sorted by 
     * loading priority
     * 
     * @param leftPreloadColumns
     * @param centerColumns
     * @param rightPreloadColumns
     * @param topPreloadRows
     * @param centerRows
     * @param bottomPreloadRows
     * @return 
     *  a list of cells that should be loaded, sorted by priority.
     *  the format of a list-entry:
     *     bit 62-32 : row
     *     bit    30 : = 1 if this is a center cell, = 0 otherwise.
     *                 this flag can be used to visualize that the currently
     *                 visible table cells may change their values
     *     bit  29-0 : column
     */
    public synchronized List<Long> synchronizeMatrix(List<Integer> leftPreloadColumns, List<Integer> centerColumns, List<Integer> rightPreloadColumns, List<Integer> topPreloadRows, List<Integer> centerRows, List<Integer> bottomPreloadRows) {
        List<Long> cellsToLoad;
        {
            List<Integer> allWantedColumns = new ArrayList<Integer>();
            allWantedColumns.addAll(leftPreloadColumns);
            allWantedColumns.addAll(centerColumns);
            allWantedColumns.addAll(rightPreloadColumns);
            List<Integer> allWantedRows = new ArrayList<Integer>();
            allWantedRows.addAll(topPreloadRows);
            allWantedRows.addAll(centerRows);
            allWantedRows.addAll(bottomPreloadRows);
            List<Integer> rowKeysToDelete = new ArrayList<Integer>();
            for (int r : matrixRows.keySet()) {
                if (allWantedRows.contains(r)) {
                    Map<Integer, Object> matrixColumns = matrixRows.get(r);
                    List<Integer> columnKeysToDelete = new ArrayList<Integer>();
                    for (int c : matrixColumns.keySet()) {
                        if (!allWantedColumns.contains(c)) {
                            columnKeysToDelete.add(c);
                        }
                    }
                    for (int c : columnKeysToDelete) matrixColumns.remove(c);
                } else {
                    rowKeysToDelete.add(r);
                }
            }
            for (int r : rowKeysToDelete) matrixRows.remove(r);
        }
        cellsToLoad = new ArrayList<Long>();
        cellsToLoad.addAll(processCellBlock(centerColumns, centerRows, true));
        cellsToLoad.addAll(processCellBlock(centerColumns, bottomPreloadRows, false));
        cellsToLoad.addAll(processCellBlock(centerColumns, topPreloadRows, false));
        cellsToLoad.addAll(processCellBlock(rightPreloadColumns, centerRows, false));
        cellsToLoad.addAll(processCellBlock(leftPreloadColumns, centerRows, false));
        cellsToLoad.addAll(processCellBlock(rightPreloadColumns, bottomPreloadRows, false));
        cellsToLoad.addAll(processCellBlock(rightPreloadColumns, topPreloadRows, false));
        cellsToLoad.addAll(processCellBlock(leftPreloadColumns, bottomPreloadRows, false));
        cellsToLoad.addAll(processCellBlock(leftPreloadColumns, topPreloadRows, false));
        return cellsToLoad;
    }

    /**
     * @param columns
     * @param rows
     * @param isCenterCell
     * @return
     *  and array specifying which cells should be loaded
     */
    private List<Long> processCellBlock(List<Integer> columns, List<Integer> rows, boolean isCenterCell) {
        List<Long> cellsToLoad = new ArrayList<Long>();
        long prio = (isCenterCell ? 0x40000000 : 0);
        for (long r : rows) {
            if (matrixRows.containsKey((int) r)) {
                Map<Integer, Object> matrixColumns = matrixRows.get((int) r);
                for (long c : columns) {
                    if (!matrixColumns.containsKey((int) c)) {
                        cellsToLoad.add((r << 32) | prio | c);
                    }
                }
            } else {
                for (long c : columns) {
                    cellsToLoad.add((r << 32) | prio | c);
                }
            }
        }
        return cellsToLoad;
    }
}
