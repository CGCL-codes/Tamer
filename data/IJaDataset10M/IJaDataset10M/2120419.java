package org.racsor.jmeter.flex.messaging.swing;

import javax.swing.JTextArea;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.ListCellRenderer;

/**
 *
 * @author  rogan
 */
public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer, ListCellRenderer {

    public MultiLineCellRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setFont(table.getFont());
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));
                setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(new EmptyBorder(1, 2, 1, 2));
        }
        setText((value == null) ? "" : value.toString());
        int height_wanted = (int) getPreferredSize().getHeight();
        if (height_wanted > table.getRowHeight(row)) table.setRowHeight(row, height_wanted);
        return this;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setForeground(list.getSelectionForeground());
            setBackground(list.getSelectionBackground());
        } else {
            setForeground(list.getForeground());
            setBackground(list.getBackground());
        }
        setFont(list.getFont());
        if (cellHasFocus) {
            setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
        } else {
            setBorder(new javax.swing.border.LineBorder(java.awt.Color.LIGHT_GRAY));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}
