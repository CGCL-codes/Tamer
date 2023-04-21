package de.fleckowarsky.yading.mainwindow;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class GrayWhiteTable extends JTable {

    public GrayWhiteTable() {
        super();
    }

    public GrayWhiteTable(TableModel tm) {
        super(tm);
    }

    public GrayWhiteTable(Object[][] data, Object[] columns) {
        super(data, columns);
    }

    public GrayWhiteTable(int rows, int columns) {
        super(rows, columns);
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            initToolTip(jc, row, column);
        }
        return c;
    }

    /**
	 * Sets the component's tool tip if the component is being rendered smaller
	 * than its preferred size. This means that all users automatically get tool
	 * tips on truncated text fields that show them the full value.
	 */
    private void initToolTip(JComponent c, int row, int column) {
        String toolTipText = null;
        if (c.getPreferredSize().width > getCellRect(row, column, false).width) {
            toolTipText = getValueAt(row, column).toString();
        }
        c.setToolTipText(toolTipText);
    }
}
