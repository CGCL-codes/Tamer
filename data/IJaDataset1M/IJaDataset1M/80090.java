package org.pagger.view.picture;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.apache.sanselan.common.RationalNumber;
import org.pagger.view.CellProcessor;

public class RationalNumberCellProcessor implements CellProcessor {

    private class RationalNumberRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = -544956826285790456L;

        public RationalNumberRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof RationalNumber) {
                final RationalNumber number = (RationalNumber) value;
                setText(number.toDisplayString());
            }
            return component;
        }
    }

    @Override
    public TableCellRenderer getCellRenderer() {
        return new RationalNumberRenderer();
    }

    @Override
    public TableCellEditor getCellEditor() {
        return null;
    }

    @Override
    public Class<?> getType() {
        return RationalNumber.class;
    }
}
