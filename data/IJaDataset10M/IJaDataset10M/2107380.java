package org.dinopolis.gpstool.gui.layer.location;

import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Cell renderer for categories (includes display of icon, etc.).
 *
 * @author Christof Dallermassl
 * @version $Revision: 5 $
 */
public class CategoryCellRenderer extends JLabel implements ListCellRenderer, TableCellRenderer {

    LocationMarkerCategory category_;

    public CategoryCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        LocationMarkerCategory category = (LocationMarkerCategory) value;
        setText(category.getName());
        setIcon(category.getIcon());
        return this;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        LocationMarkerCategory category = (LocationMarkerCategory) value;
        setText(category.getName());
        setIcon(category.getIcon());
        return this;
    }
}
