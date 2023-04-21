package com.empower.client.tablemodels;

import java.util.Vector;
import com.empower.model.AccountTypeModel;

public class ChartOfAccountTypesModel extends javax.swing.table.DefaultTableModel {

    private static int COLUMN_COUNT = 3;

    public ChartOfAccountTypesModel() {
        super();
    }

    public ChartOfAccountTypesModel(java.lang.Object[][] data, java.lang.Object[] columnNames) {
        super(data, columnNames);
    }

    public ChartOfAccountTypesModel(java.lang.Object[] columnNames, int numRows) {
        super(columnNames, numRows);
    }

    public ChartOfAccountTypesModel(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public ChartOfAccountTypesModel(java.util.Vector columnNames, int numRows) {
        super(columnNames, numRows);
    }

    public ChartOfAccountTypesModel(java.util.Vector data, java.util.Vector columnNames) {
        super(data, columnNames);
    }

    public Class getColumnClass(int col) {
        return String.class;
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Vector getDataRow(int row) {
        Vector tmpRow = (Vector) dataVector.elementAt(row);
        return tmpRow;
    }

    public Object getValueAt(int row, int col) {
        Object obj = null;
        Vector tmpRow = (Vector) dataVector.elementAt(row);
        obj = tmpRow.elementAt(0);
        switch(col) {
            case -1:
                obj = tmpRow;
                break;
            case 0:
                obj = ((AccountTypeModel) obj).getAcctTypeCode();
                break;
            case 1:
                obj = ((AccountTypeModel) obj).getAcctTypeDesc();
                break;
            case 2:
                obj = ((AccountTypeModel) obj).getSubAcctTypeCode();
                break;
            default:
                System.out.println("Invalid Entry");
        }
        return obj;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setColumnNames() {
        Vector columnNames = new Vector();
        columnNames.add(0, "ACCOUNT TYPE CODE");
        columnNames.add(1, "DESCRIPTION");
        columnNames.add(2, "SUB-ACCOUNT TYPE");
        setColumnIdentifiers(columnNames);
    }

    public void setValueAt(Object aValue, int row, int col) {
        Vector tmpRow = (Vector) dataVector.elementAt(row);
        tmpRow.add(0, aValue);
        fireTableCellUpdated(row, col);
    }
}
