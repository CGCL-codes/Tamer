package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Matrix backed by HashMap, i.e. sparse matrix with unlimited size
 */
public class Matrix<R extends Comparable, C extends Comparable, V> {

    private final Map<R, Map<C, V>> matrix = new HashMap<R, Map<C, V>>();

    private final R minRow;

    private final C minColumn;

    public Matrix(R minRow, C minColumn) {
        this.minRow = minRow;
        this.minColumn = minColumn;
    }

    public V get(R row, C column) {
        if (matrix.containsKey(row)) {
            Map<C, V> rowData = matrix.get(row);
            if (rowData.containsKey(column)) {
                return rowData.get(column);
            }
        }
        return null;
    }

    public void set(V value, R row, C column) {
        if (!matrix.containsKey(row)) {
            Map<C, V> rowData = new HashMap<C, V>();
            matrix.put(row, rowData);
        }
        Map<C, V> rowData = matrix.get(row);
        rowData.put(column, value);
    }

    public R getMaxRow() {
        Set<R> rowSet = matrix.keySet();
        R max = minRow;
        for (R r : rowSet) {
            if (r.compareTo(max) > 0) {
                max = r;
            }
        }
        return max;
    }

    public C getMaxColumn() {
        C max = minColumn;
        for (Map<C, V> rowData : matrix.values()) {
            for (C c : rowData.keySet()) {
                if (c.compareTo(max) > 0) {
                    max = c;
                }
            }
        }
        return max;
    }

    public void clear() {
        matrix.clear();
    }
}
