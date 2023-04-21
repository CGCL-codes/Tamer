package first;

import java.util.ArrayList;
import java.util.Iterator;

public class reportTable {

    private int cols;

    private String[] columnNames;

    private ArrayList<String[]> rows;

    public reportTable(int c) {
        cols = c;
        columnNames = new String[c];
        rows = new ArrayList<String[]>();
    }

    public int getCols() {
        return cols;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public boolean setColumnName(String colName, int index) {
        if (index >= cols) return false;
        columnNames[index] = colName;
        return true;
    }

    public boolean addRow(String[] row) {
        if (row.length > cols) return false;
        rows.add(row);
        return true;
    }

    public String[] getRow(int index) {
        if (index > rows.size()) return null;
        return rows.get(index);
    }

    public boolean removeRow(int index) {
        if (index > rows.size()) return false;
        rows.remove(index);
        return true;
    }

    public ArrayList<String[]> getRows() {
        return rows;
    }

    public void setRows(ArrayList<String[]> rows) {
        this.rows = rows;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < cols; i++) result += columnNames[i] + ";";
        result += "\n";
        Iterator<String[]> it = rows.iterator();
        String[] sArray;
        while (it.hasNext()) {
            sArray = it.next();
            for (int i = 0; i < sArray.length; i++) {
                result += sArray[i] + ";";
            }
            result += "\n";
        }
        return result;
    }
}
