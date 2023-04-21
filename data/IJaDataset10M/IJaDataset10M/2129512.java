package com.commander4j.autolabel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class myPropertyTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1;

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    Color foreground, background;

    private myDragIcon icon;

    public myPropertyTableCellRenderer(myDragIcon icon) {
        super();
        this.icon = icon;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        System.out.println("Row=" + String.valueOf(row));
        System.out.println("Col=" + String.valueOf(column));
        System.out.println("icon.getKeyList() size =" + String.valueOf(icon.getKeyList().size()));
        if (row < icon.getKeyList().size()) {
            myPropertyItem temp = icon.getPropertyList().properties.get(icon.getKeyList().get(row));
            foreground = Color.black;
            background = new Color(238, 230, 230);
            setForeground(foreground);
            setBackground(background);
            setHorizontalAlignment(JLabel.LEFT);
            if (column == 1) {
                if (temp.getPropertyEditor().equals("JCheckBox")) {
                    JCheckBox checkBox = new JCheckBox();
                    checkBox.setSelected(((Boolean) value).booleanValue());
                    checkBox.setBackground(background);
                    return checkBox;
                }
                if (temp.getPropertyEditor().equals("JComboBox")) {
                    JComboBox comboBox = new JComboBox(temp.getSelectValues());
                    comboBox.setBackground(background);
                    comboBox.setForeground(foreground);
                    comboBox.setSelectedItem(value);
                    return comboBox;
                }
            }
        } else {
            return null;
        }
        return this;
    }
}
