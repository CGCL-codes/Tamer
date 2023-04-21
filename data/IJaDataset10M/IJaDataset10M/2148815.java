package org.book4j.components.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonHeaderRenderer extends JPanel implements TableCellRenderer {

    private JLabel label;

    private TableButton button;

    private int pushedColumn = -1;

    public ButtonHeaderRenderer() {
        initialize();
    }

    private void initialize() {
        this.setLayout(new FlowLayout());
        this.label = new JLabel();
        this.add(label);
        this.button = new TableButton();
        this.button.setPreferredSize(new Dimension(10, 10));
        this.add(button);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.label.setText((value == null) ? "" : value.toString());
        boolean isPressed = (column == pushedColumn);
        this.button.getModel().setPressed(isPressed);
        this.button.getModel().setArmed(isPressed);
        return this;
    }

    public void setPressedColumn(int col) {
        this.pushedColumn = col;
    }
}
