package net.sourceforge.processdash.ui.lib;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/** Wraps a TableCellRenderer, and adds column-based tool tips.
 */
public class ToolTipTableCellRendererProxy implements TableCellRenderer {

    TableCellRenderer renderer;

    String[] toolTips;

    public static void installHeaderToolTips(JTable table, String[] toolTips) {
        TableCellRenderer headerCellRenderer = table.getTableHeader().getDefaultRenderer();
        headerCellRenderer = new ToolTipTableCellRendererProxy(headerCellRenderer, toolTips);
        table.getTableHeader().setDefaultRenderer(headerCellRenderer);
    }

    public ToolTipTableCellRendererProxy(TableCellRenderer renderer, String[] toolTips) {
        this.renderer = renderer;
        this.toolTips = toolTips;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component result = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table != null && result instanceof JComponent) {
            String tip = toolTips[table.convertColumnIndexToModel(column)];
            ((JComponent) result).setToolTipText(tip);
        }
        return result;
    }
}
