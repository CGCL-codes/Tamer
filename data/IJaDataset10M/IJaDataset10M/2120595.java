package de.ibis.permoto.gui.textual.table;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A CellRenderer that renders the buttons.
 * @author Christian Markl
 * @author Oliver H�hn
 */
public class ButtonCellRenderer implements TableCellRenderer {

    Vector<Object> listeners = new Vector<Object>();

    private JButton button;

    public ButtonCellRenderer(final String title) {
        this.button = new JButton(title);
    }

    public ButtonCellRenderer(final JButton myButton) {
        this.button = myButton;
    }

    public final Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        return this.button;
    }
}
