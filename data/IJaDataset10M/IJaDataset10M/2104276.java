package co.fxl.gui.table.impl;

import java.util.Comparator;

class RowComparator implements Comparator<RowImpl> {

    boolean sortUp = true;

    private ColumnImpl column;

    private int multiplicator;

    RowComparator(Comparator<RowImpl> comparator, ColumnImpl columnImpl) {
        column = columnImpl;
        if (comparator instanceof RowComparator) {
            RowComparator rowComparator = (RowComparator) comparator;
            if (rowComparator.column == column) {
                sortUp = !rowComparator.sortUp;
            }
        }
        multiplicator = sortUp ? 1 : -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(RowImpl o1, RowImpl o2) {
        Comparable<Object> c1 = (Comparable<Object>) o1.content.values[column.columnIndex];
        Comparable<Object> c2 = (Comparable<Object>) o2.content.values[column.columnIndex];
        if (c1 == null) return 1; else if (c2 == null) return -1;
        return multiplicator * c1.compareTo(c2);
    }
}
