package com.googlecode.jrename.gui.list.model;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MatchingCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final ImageIcon tick = new ImageIcon(MatchingCellRenderer.class.getClassLoader().getResource("tick.gif"));

    private static final ImageIcon cross = new ImageIcon(MatchingCellRenderer.class.getClassLoader().getResource("cross.gif"));

    public MatchingCellRenderer() {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (((MatchingTableModel) table.getModel()).isMatchingRow(row)) {
            return new JLabel(tick);
        } else {
            return new JLabel(cross);
        }
    }
}
