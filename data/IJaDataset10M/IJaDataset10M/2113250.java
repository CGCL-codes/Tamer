package org.agilar.gwt.sortabletable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.google.gwt.user.client.ui.Widget;

public class SimpleTableModel extends TableModel {

    private List<Comparator> comparators = new ArrayList<Comparator>();

    private List<List<Object>> data = new ArrayList<List<Object>>();

    private int rows = 0;

    public SimpleTableModel(int rows, int columns) {
        super(columns);
        this.rows = rows;
        for (int col = 0; col < columns; col++) {
            comparators.add(null);
            data.add(new ArrayList<Object>());
            for (int row = 0; row < rows; row++) {
                data.get(col).add(null);
            }
        }
    }

    public void setColumnAsSortable(int column, Comparator comparator) {
        comparators.set(column, comparator);
    }

    public void setColumnAsNonSortable(int column) {
        comparators.set(column, null);
    }

    public int getRowCount() {
        return rows;
    }

    public boolean isColumnSortable(int index) {
        return comparators.get(index) != null || (data.get(index).size() > 0 && data.get(index).get(0) instanceof Comparable);
    }

    public void set(int row, int column, Object value) {
        data.get(column).set(row, value);
    }

    public void set(int row, int column, Widget widget) {
        data.get(column).set(row, widget);
    }

    public void sort(int column, boolean ascending) {
        if (rows > 0) {
            Comparator comparator = comparators.get(column);
            if (comparator != null || data.get(column).get(0) instanceof Comparable) {
                qsort(column, comparator, 0, rows - 1);
                if (!ascending) {
                    for (List<Object> list : data) {
                        Collections.reverse(list);
                    }
                }
            }
        }
    }

    private void qsort(int column, Comparator comparator, int left, int right) {
        if (left < right) {
            int i = 0;
            if (comparator != null) {
                i = partition(column, comparator, left, right);
            } else {
                i = partition(column, left, right);
            }
            qsort(column, comparator, left, i - 1);
            qsort(column, comparator, i + 1, right);
        }
    }

    private int partition(int column, Comparator comparator, int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (comparator.compare(data.get(column).get(++i), data.get(column).get(right)) < 0) ;
            while (comparator.compare(data.get(column).get(right), data.get(column).get(--j)) < 0) {
                if (j == left) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            swapRows(i, j);
        }
        swapRows(i, right);
        return i;
    }

    private int partition(int column, int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (((Comparable) data.get(column).get(++i)).compareTo(data.get(column).get(right)) < 0) ;
            while (((Comparable) data.get(column).get(right)).compareTo(data.get(column).get(--j)) < 0) {
                if (j == left) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            swapRows(i, j);
        }
        swapRows(i, right);
        return i;
    }

    private void swapRows(int i, int j) {
        Object aux = null;
        for (List<Object> list : data) {
            aux = list.get(i);
            list.set(i, list.get(j));
            list.set(j, aux);
        }
    }

    public Object get(int row, int column) {
        return data.get(column).get(row);
    }
}
