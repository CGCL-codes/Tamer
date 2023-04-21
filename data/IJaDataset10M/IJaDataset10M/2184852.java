package KFrameWork.Widgets.DataBrowser;

import javax.swing.table.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import KFrameWork.Base.*;

public class tableHeaderRendererClass extends javax.swing.JTable implements TableCellRenderer {

    private Object[][] tableData;

    protected String column_name;

    /** Creates new tableHeaderRendererClass */
    public tableHeaderRendererClass(int row, String columnName) {
        super(row, 1);
        tableData = new Object[row][];
        for (int i = 0; i < row; i++) {
            tableData[i] = new Object[] { new javax.swing.JLabel(), null, null };
        }
        column_name = columnName;
        TableColumn column = getColumnModel().getColumn(0);
        TableCellRenderer render = getDefaultRenderer(column.getClass());
        if (render instanceof JLabel) {
            ((JLabel) render).setHorizontalAlignment(JLabel.CENTER);
            ((JLabel) render).setBackground(new Color(220, 220, 220));
        }
    }

    public String getColumnName() {
        return column_name;
    }

    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }

    public void setText(int row, String text) {
        getModel().setValueAt(text, row, 0);
        tableData[row][0] = new javax.swing.JLabel(text);
    }

    public Object[][] getOperations() {
        return tableData;
    }

    public void setOperationAt(String operation, int formatCode, int row) {
        tableData[row][1] = new String(operation);
        tableData[row][2] = new Integer(formatCode);
    }

    public void setLabelAt(String label, int row) {
        tableData[row][0] = new javax.swing.JLabel(label);
    }

    public void update(tableModelClass tableModel) throws KExceptionClass {
        for (int i = 0; i < tableData.length; i++) getModel().setValueAt(((JLabel) tableData[i][0]).getText(), i, 0);
    }
}
