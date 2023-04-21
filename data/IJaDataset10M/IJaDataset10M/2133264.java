package gui;

import javax.swing.table.*;
import java.util.*;
import javax.swing.event.*;
import core.SystemController;
import core.Equilibrium;

public class EquationTableModel extends AbstractTableModel implements TableModelListener, Observer {

    protected ResourceBundle resbundle;

    private String[] columns;

    private Object[][] data;

    private boolean editable;

    private UnicodeTranslator translator;

    private SystemController controller;

    public EquationTableModel(SystemController controller, boolean editable) {
        resbundle = ResourceBundle.getBundle("strings", Locale.getDefault());
        translator = new UnicodeTranslator();
        this.controller = controller;
        columns = new String[2];
        columns[0] = resbundle.getString("chemEquation");
        columns[1] = resbundle.getString("equilibriumConstant");
        if (editable) {
            data = new Object[1][2];
            data[0][0] = new String("H_2O - H^+ <=> OH^-");
            data[0][1] = new Double(1.0E-14);
            addRow(new Object[2]);
            addTableModelListener(this);
            fireTableRowsInserted(0, 0);
        } else {
            Object[] newRow;
            data = new Object[0][2];
            for (Equilibrium eq : controller.getEquilibria()) {
                newRow = new Object[2];
                newRow[0] = eq.getEquation();
                newRow[1] = eq.getK();
                addRow(newRow);
            }
        }
        this.editable = editable;
    }

    public int getColumnCount() {
        return columns.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columns[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return editable;
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public void addRow(Object[] row) {
        int newLength = data.length + 1;
        Object[][] newData = new Object[newLength][2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
        data[newLength - 1][0] = row[0];
        data[newLength - 1][1] = row[1];
        fireTableRowsInserted(data.length, data.length);
    }

    public void tableChanged(TableModelEvent e) {
        if (editable && e.getType() == TableModelEvent.UPDATE && e.getFirstRow() == (data.length - 1)) {
            addRow(new Object[2]);
        }
        String currentEq;
        for (int index = e.getFirstRow(); index < data.length; index++) {
            if (data[index][0] != null) {
                currentEq = data[index][0].toString();
                currentEq = translator.translate(currentEq);
                data[index][0] = currentEq;
            }
        }
        controller.clearEquilibria();
        for (int index = 0; index < data.length; index++) {
            if (data[index][0] != null && data[index][1] != null) {
                controller.addEquilibrium((String) data[index][0], (Double) data[index][1]);
            }
        }
    }

    public void update(Observable controller, Object arg) {
    }
}
