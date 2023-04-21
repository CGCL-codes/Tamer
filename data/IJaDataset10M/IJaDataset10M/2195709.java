package co.fxl.gui.table.scroll.impl;

import java.util.LinkedList;
import java.util.List;
import co.fxl.gui.table.impl.sort.IComparableList;
import co.fxl.gui.table.impl.sort.QuickSort;
import co.fxl.gui.table.scroll.api.IRows;

class RowAdapter implements IRows<Object>, IComparableList {

    private QuickSort quickSort = new QuickSort();

    IRows<Object> rows;

    private int[] indices;

    private ScrollTableColumnImpl comparator;

    private int negator = 1;

    private boolean[] selected;

    RowAdapter(IRows<Object> rows) {
        this.rows = rows;
        indices = new int[rows.size()];
        selected = new boolean[rows.size()];
        for (int i = 0; i < indices.length; i++) indices[i] = i;
    }

    @Override
    public Object identifier(int i) {
        assert i != -1;
        return rows.identifier(indices[i]);
    }

    boolean selected(int i) {
        return selected[indices[i]];
    }

    void selected(int i, boolean b) {
        selected[indices[i]] = b;
    }

    void selected(Object o, boolean b) {
        for (int i = 0; i < rows.size(); i++) {
            if (o.equals(identifier(i))) {
                selected(i, b);
            }
        }
    }

    int selected(Object object) {
        for (int i = 0; i < rows.size(); i++) {
            Object identifier = identifier(i);
            if (object.equals(identifier)) {
                selected(i, true);
                return i;
            }
        }
        return -1;
    }

    void selected(List<Object> object) {
        for (Object o : object) selected(o);
    }

    void clearSelection() {
        selected = new boolean[rows.size()];
    }

    @Override
    public Object[] row(int i) {
        return rows.row(indices[i]);
    }

    @Override
    public int size() {
        return indices.length;
    }

    int sort(ScrollTableColumnImpl column) {
        if (comparator != null) {
            negator = comparator == column ? negator * -1 : 1;
        }
        comparator = column;
        quickSort.sort(this);
        return negator;
    }

    @Override
    public int compare(int firstIndex, int secondIndex) {
        return negator * comparator.compare(row(firstIndex), row(secondIndex));
    }

    @Override
    public void swap(int firstIndex, int secondIndex) {
        int tmp = indices[firstIndex];
        indices[firstIndex] = indices[secondIndex];
        indices[secondIndex] = tmp;
    }

    List<Object> selectedIdentifiers() {
        List<Object> ids = new LinkedList<Object>();
        for (int i = 0; i < size(); i++) {
            if (selected[i]) ids.add(rows.identifier(i));
        }
        return ids;
    }

    List<Integer> selectedIndices() {
        List<Integer> ids = new LinkedList<Integer>();
        for (int i = 0; i < size(); i++) {
            if (selected[i]) ids.add(i);
        }
        return ids;
    }

    int find(Object object) {
        for (int i = 0; i < rows.size(); i++) {
            if (identifier(i).equals(object)) return i;
        }
        return -1;
    }

    int find(List<Object> object) {
        for (int i = 0; i < rows.size(); i++) {
            if (object.contains(identifier(i))) return i;
        }
        return -1;
    }

    boolean selected(int preselectedIndex, Object preselected) {
        if (preselectedIndex < selected.length) {
            if (identifier(preselectedIndex).equals(preselected)) {
                selected[preselectedIndex] = true;
                return true;
            } else return false;
        } else return false;
    }

    @Override
    public boolean deletable(int i) {
        int index = indices[i];
        return rows.deletable(index);
    }
}
