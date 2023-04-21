package net.sealisland.swing.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.AbstractSpinnerModel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class SpinnerTableCellRenderer extends JSpinner implements TableCellRenderer {

    public SpinnerTableCellRenderer() {
        super(new AbstractSpinnerModel() {

            private Object value;

            public Object getValue() {
                return value;
            }

            public void setValue(Object value) {
                this.value = value;
                fireStateChanged();
            }

            public Object getNextValue() {
                return null;
            }

            public Object getPreviousValue() {
                return null;
            }
        });
        setBorder(null);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
        setOpaque(true);
        setBackground(Color.RED);
        getEditor().setOpaque(true);
        getEditor().setBackground(Color.RED);
        setFont(table.getFont());
        setValue(value);
        return this;
    }
}
