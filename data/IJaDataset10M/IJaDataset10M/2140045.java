package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class DBTableModel {

    protected ArrayList<String[]> vals = null;

    protected HashMap<String, Integer> colNames = null;

    public DBTableModel(DBhandler db, String table) {
        try {
            ResultSet temp = db.returnTable(table);
            vals = db.returnValues(temp);
        } catch (SQLException e) {
            System.out.println("DB read error. Cannot get data from " + table + " table");
            return;
        }
        int length = vals.get(0).length;
        colNames = new HashMap<String, Integer>();
        String[] columnNames = readRow(0);
        for (int i = 0; i < length; i++) colNames.put(columnNames[i], i);
    }

    public DBTableModel(DBTableModel src) {
        this.vals = new ArrayList<String[]>(src.vals);
        this.colNames = src.colNames;
    }

    public DBTableModel(ArrayList<String[]> vals) {
        this.vals = new ArrayList<String[]>(vals);
        int length = vals.get(0).length;
        colNames = new HashMap<String, Integer>();
        String[] columnNames = readRow(0);
        for (int i = 0; i < length; i++) colNames.put(columnNames[i], i);
    }

    public String[] readRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= vals.size()) return null;
        String[] row;
        row = vals.get(rowIndex);
        return row;
    }

    public String[] readColumn(String name) {
        return readColumn(colNames.get(name));
    }

    public String[] readColumn(int colIndex) {
        int rowNum = vals.size();
        TreeSet<String> set = new TreeSet<String>();
        String[] row;
        for (int i = 0; i < rowNum; i++) {
            row = vals.get(i);
            set.add(row[colIndex].trim());
        }
        rowNum = set.size() + 1;
        String[] colVals = new String[rowNum];
        Iterator itr = set.iterator();
        colVals[0] = "���";
        for (int i = 1; i < rowNum && itr.hasNext(); i++) colVals[i] = (String) itr.next();
        return colVals;
    }

    public String[] selectColumn(String name, String refColName, String ref) {
        return selectColumn(colNames.get(name), colNames.get(refColName), ref);
    }

    public String[] selectColumn(int colIndex, int refColIndex, String ref) {
        int rowNum = vals.size();
        TreeSet<String> set = new TreeSet<String>();
        String[] row;
        for (int i = 0; i < rowNum; i++) {
            row = vals.get(i);
            if ((row[refColIndex].trim().equals(ref.trim()))) set.add(row[colIndex].trim());
        }
        rowNum = set.size() + 1;
        Iterator itr = set.iterator();
        String[] colVals = new String[rowNum];
        colVals[0] = "���";
        for (int i = 1; i < rowNum && itr.hasNext(); i++) colVals[i] = (String) itr.next();
        return colVals;
    }

    public int getColIndex(String name) {
        return colNames.get(name);
    }
}
