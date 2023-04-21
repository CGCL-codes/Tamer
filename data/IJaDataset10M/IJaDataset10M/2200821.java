package seventhsense.gui.table;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Experimental renderer for icons in table headers
 * 
 * @author Parallan
 *
 */
public class TableHeaderIconCellRenderer implements TableCellRenderer {

    /**
	 * Class for a composite icon (two items in one)
	 * 
	 * @author Parallan
	 *
	 */
    private static class CompositeIcon implements Icon {

        private final Icon _icon1;

        private final Icon _icon2;

        /**
		 * Creates the composite icon
		 * 
		 * @param icon1 icon to show on left
		 * @param icon2 icon to show on right
		 */
        public CompositeIcon(final Icon icon1, final Icon icon2) {
            _icon1 = icon1;
            _icon2 = icon2;
        }

        @Override
        public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
            _icon1.paintIcon(c, g, x, y);
            _icon2.paintIcon(c, g, x + _icon1.getIconWidth(), y);
        }

        @Override
        public int getIconWidth() {
            return _icon1.getIconWidth() + _icon2.getIconWidth();
        }

        @Override
        public int getIconHeight() {
            return Math.max(_icon1.getIconHeight(), _icon2.getIconHeight());
        }
    }

    private final TableCellRenderer _defaultRenderer;

    private final Icon _icon;

    /**
	 * Creates the header renderer
	 * 
	 * @param defaultRenderer original renderer
	 * @param icon new icon used for rendering
	 */
    public TableHeaderIconCellRenderer(final TableCellRenderer defaultRenderer, final Icon icon) {
        _defaultRenderer = defaultRenderer;
        _icon = icon;
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        final Component defaultComponent = _defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (defaultComponent instanceof JLabel) {
            ((JLabel) defaultComponent).setHorizontalTextPosition(JLabel.RIGHT);
            final Icon icon = ((JLabel) defaultComponent).getIcon();
            if ((icon == null) || (icon == _icon)) {
                ((JLabel) defaultComponent).setIcon(_icon);
            } else {
                ((JLabel) defaultComponent).setIcon(new CompositeIcon(icon, _icon));
            }
            ((JLabel) defaultComponent).setToolTipText(((JLabel) defaultComponent).getText());
            ((JLabel) defaultComponent).setText("");
        }
        return defaultComponent;
    }
}
