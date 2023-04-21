package net.rptools.initiativetool.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.rptools.initiativetool.Encounter;

public class InitiativeTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 4865451675770436098L;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Component) return (Component) value;
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Encounter encounter = MainFrame.getMainFrame().getEncounter();
        if (row == encounter.getCurrentIndex()) {
            super.setForeground(Color.WHITE);
            super.setBackground(Color.BLACK);
        } else {
            super.setForeground(Color.BLACK);
            super.setBackground(Color.WHITE);
        }
        return this;
    }
}
