package com.google.code.gronono.jarseeker.ui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ToolTipTableCellRenderer extends DefaultTableCellRenderer {

    /** serialVersionUID */
    private static final long serialVersionUID = -4744434692328737079L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String tip = table.getModel().getValueAt(row, column).toString();
        setToolTipText(tip);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
