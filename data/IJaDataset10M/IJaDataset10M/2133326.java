package org.armedbear.j;

public final class Table {

    private int columnIndex;

    private int[] widths = new int[10];

    public Table() {
    }

    public final int getColumnIndex() {
        return columnIndex;
    }

    public final void nextRow() {
        columnIndex = -1;
    }

    public void nextColumn() {
        ++columnIndex;
        Debug.assertTrue(columnIndex >= 0);
        if (columnIndex >= widths.length) {
            int[] newArray = new int[widths.length * 2 + 2];
            System.arraycopy(widths, 0, newArray, 0, widths.length);
            widths = newArray;
        }
    }

    public void setColumnWidth(int width) {
        Debug.assertTrue(columnIndex >= 0);
        Debug.assertTrue(columnIndex < widths.length);
        if (width > widths[columnIndex]) widths[columnIndex] = width;
    }

    public int getColumnWidth() {
        Debug.assertTrue(columnIndex >= 0);
        Debug.assertTrue(columnIndex < widths.length);
        return widths[columnIndex];
    }

    public int getMinimumOffset() {
        Debug.assertTrue(columnIndex >= 0);
        Debug.assertTrue(columnIndex < widths.length);
        int offset = 0;
        for (int i = 0; i < columnIndex; i++) offset += widths[i];
        return offset;
    }
}
