package com.indragunawan.restobiz.app.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author igoens
 */
public class OperatorViewTM extends AbstractTableModel {

    private static final long serialVersionUID = 354576790266614817L;

    private int colnum = 3;

    private int rownum;

    private String[] colNames = { "Nama User", "Nama Asli", "Hak Akses" };

    private ArrayList<Object[]> ResultSets;

    @SuppressWarnings("unchecked")
    public OperatorViewTM(List ls) {
        ResultSets = new ArrayList<Object[]>();
        ResultSets.addAll(ls);
    }

    public int getRowCount() {
        rownum = ResultSets.size();
        return rownum;
    }

    public int getColumnCount() {
        return colnum;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] row = ResultSets.get(rowIndex);
        return row[columnIndex];
    }

    @Override
    public String getColumnName(int param) {
        return colNames[param];
    }
}
