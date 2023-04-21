package edu.purdue.traxplorer.data;

import java.util.ArrayList;
import java.util.Iterator;

public class BasicTable implements Table {

    private ArrayList<Column> columns = new ArrayList<Column>();

    public BasicTable() {
    }

    public Column getColumnAt(int column) {
        return columns.get(column);
    }

    public Column getColumn(String name) {
        for (Iterator<Column> i = columns.iterator(); i.hasNext(); ) {
            Column c = i.next();
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public String getColumnName(int column) {
        return columns.get(column).getName();
    }

    public Object[] getRow(int row) {
        Object[] rowArray = new Object[getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++) {
            rowArray[i] = columns.get(i).getValueAt(row);
        }
        return rowArray;
    }

    public void addColumn(Column column) {
        if (column == null) {
            System.err.println(column);
        }
        columns.add(column);
    }

    public void removeColumn(Column column) {
        columns.remove(column);
    }

    public Column.Type getColumnType(int column) {
        return columns.get(column).getType();
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int getRowCount() {
        if (columns.size() == 0) return 0;
        return columns.get(0).getRowCount();
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        for (Column c : columns) {
            sbuf.append(c + ", ");
        }
        return sbuf.toString();
    }

    public void sort(int column, boolean ascending) {
    }

    public void addRow(Object... objects) {
        int count = 0;
        for (Object o : objects) {
            Column c = columns.get(count++);
            c.addValue(o);
        }
    }

    public void clear() {
        columns.clear();
    }

    public void clearRows() {
        for (Column c : columns) {
            c.clear();
        }
    }
}
