package org.databene.gui.swing.table.render;

import org.databene.commons.NumberUtil;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.Component;
import java.awt.Color;

/**
 * Created: 06.03.2005 08:06:28
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class PercentageTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -4103463633854620900L;

    private Color GREEN = new Color(0, 192, 0);

    private int fractionDigits;

    public PercentageTableCellRenderer() {
        this(0);
    }

    public PercentageTableCellRenderer(int fractionDigits) {
        this.fractionDigits = fractionDigits;
        setHorizontalAlignment(RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String text = "0";
        double number = 0;
        if (value != null) {
            number = ((Number) value).doubleValue();
            text = NumberUtil.format(number * 100, fractionDigits) + "%";
        }
        Component component = super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
        if (number > 0) component.setForeground(GREEN); else if (number < 0) component.setForeground(Color.RED); else component.setForeground(Color.BLACK);
        return component;
    }
}
