package org.dinopolis.gpstool.gui.util;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.dinopolis.gpstool.plugin.Plugin;

/**
 * Cell renderer for plugins (for choosing a plugin in comboboxes,
 * lists, etc.).
 *
 * @author Christof Dallermassl
 * @version $Revision: 418 $
 */
public class PluginCellRenderer extends JLabel implements ListCellRenderer, TableCellRenderer {

    public PluginCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(RIGHT);
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
        setText(((Plugin) value).getPluginName());
        return (this);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        setText(((Plugin) value).getPluginName());
        return (this);
    }
}
