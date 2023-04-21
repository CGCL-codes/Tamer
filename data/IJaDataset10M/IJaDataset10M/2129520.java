package com.hotdog.editors.resources;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author duo
 */
public class HotdogCurrencyRenderer extends DefaultTableCellRenderer {

    protected DecimalFormat formatter;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (formatter == null) {
            formatter = new DecimalFormat("###,##0.00");
        }
        if (value instanceof Double) {
            value = formatter.format((Double) value);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
