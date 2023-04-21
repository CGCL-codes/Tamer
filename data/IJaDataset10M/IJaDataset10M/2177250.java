package com.qbrowser.render;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ListCellRenderer2 extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final Color evenColor = new Color(248, 248, 255);

    public ListCellRenderer2() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
        setHorizontalAlignment(RIGHT);
        if (arg1 instanceof JComboBox) {
            return (JComboBox) arg1;
        } else if (arg1 instanceof JComponent) {
            return (JComponent) arg1;
        } else {
            if (arg2) {
                super.setForeground(arg0.getSelectionForeground());
                super.setBackground(arg0.getSelectionBackground());
            } else {
                super.setForeground(arg0.getForeground());
                super.setBackground((arg4 % 2 == 0) ? evenColor : arg0.getBackground());
            }
            return super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
        }
    }
}
