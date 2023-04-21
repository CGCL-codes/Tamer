package mipt.gui.choice;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import mipt.gui.Viewable;

/**
 * Renderer of JTable COLUMN storing Named and/or Viewable values
 //* Despite other renderers of this package this is not installed by default
 //*  in parent class (Table) because we don't know here what column has Named values
 * Installed by default fro Named and Viewable classes
 * Disabling is not supported (as in swing.*Table*)
 */
public class DefaultTableRenderer extends DefaultCellRenderer implements javax.swing.table.TableCellRenderer {

    protected static Border noFocusBorder = new EmptyBorder(1, 2, 1, 2);

    public DefaultTableRenderer() {
        this(LEFT);
    }

    /**
	 * @param horizontalAlignment int
	 */
    public DefaultTableRenderer(int horizontalAlignment) {
        super(horizontalAlignment);
        setBorder(noFocusBorder);
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                setForeground(javax.swing.UIManager.getColor("Table.focusCellForeground"));
                setBackground(javax.swing.UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(noFocusBorder);
        }
        setFont(table.getFont());
        showValue(value, Viewable.ICON);
        return this;
    }
}
