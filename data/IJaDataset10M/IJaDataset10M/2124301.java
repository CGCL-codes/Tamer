package be.xios.clearprint.main;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class EventTable extends JTable {

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);
        String status = getValueAt(row, 3).toString();
        if (status.equalsIgnoreCase("Completed")) {
            comp.setForeground(Color.GREEN);
        } else if (status.equalsIgnoreCase("In Progress")) {
            comp.setForeground(Color.BLUE);
        } else if (status.equalsIgnoreCase("Cancelled")) {
            comp.setForeground(Color.RED);
        } else {
            comp.setForeground(Color.BLACK);
        }
        return comp;
    }
}
