package br.org.databasetools.core.view.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MostrarCor implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        panel.setBackground((Color) value);
        return panel;
    }

    private JPanel panel = new JPanel();
}
