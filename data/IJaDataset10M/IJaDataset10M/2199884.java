package de.fhg.igd.earth.view.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class ColoredTableCellRenderer implements TableCellRenderer {

    private Color lightBlue = new Color(160, 160, 255);

    private Color darkBlue = new Color(64, 64, 128);

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel((String) value);
        label.setOpaque(true);
        Border b = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        label.setBorder(b);
        label.setFont(table.getFont());
        label.setForeground(table.getForeground());
        label.setBackground(table.getBackground());
        if (hasFocus) {
            label.setBackground(darkBlue);
            label.setForeground(Color.white);
        } else {
            if (isSelected) {
                label.setBackground(lightBlue);
            } else {
                column = table.convertColumnIndexToModel(column);
                if (column == 0) {
                    label.setBackground(Color.orange);
                }
            }
        }
        return label;
    }
}
