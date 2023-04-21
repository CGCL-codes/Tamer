package com.htdsoft.ihm;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class CompteTreeCellRenderer extends JTree implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        System.out.println(value);
        return null;
    }
}
