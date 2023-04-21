package com.softserve.mproject.client.swing.ui;

import java.awt.Component;
import java.awt.Image;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class ImageRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        ImagePanel ip = new ImagePanel(20, false);
        ip.setOpaque(true);
        if (isSelected) {
            ip.setBackground(table.getSelectionBackground());
        } else {
            if (row % 2 == 0) {
                ip.setBackground(UIManager.getColor("Table.alternateRowColor"));
            } else {
                ip.setOpaque(false);
                ip.setBackground(table.getBackground());
            }
        }
        if (value != null) {
            ip.setImage((Image) value);
        }
        return ip;
    }
}
