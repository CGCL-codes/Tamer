package org.xito.dazzle.widget.table;

import org.xito.dazzle.widget.DefaultStyle;
import org.xito.dazzle.*;
import org.xito.dazzle.widget.table.TableHelper;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;

/**
 * A Table that uses alternating row colors like on the mac
 * @author deane
 */
public class ScrollTable extends JTable {

    protected TableCellRenderer myDefaultRenderer;

    protected boolean drawSelectedColumn = false;

    protected Color selectedColumnColor;

    protected Font rendererFont;

    public ScrollTable() {
        super();
        init();
    }

    public ScrollTable(Object[][] data, String[] colNames) {
        super(data, colNames);
        init();
    }

    public ScrollTable(TableModel model) {
        super(model);
        init();
    }

    protected void init() {
        myDefaultRenderer = new CellRenderer();
    }

    public void setRendererFont(Font f) {
        rendererFont = f;
    }

    /**
     * Get PreferredSize
     * @return Dimension
     */
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        if (getParent() instanceof JViewport) {
            int viewPortH = ((JViewport) getParent()).getHeight();
            if (viewPortH > dim.height) {
                dim.height = viewPortH;
            }
        }
        return dim;
    }

    /**
     * Paint the Component
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        TableHelper.drawExtendedBottomRows(this, g);
        if (drawSelectedColumn) {
            TableHelper.drawSelectedBottomCol(this, g, selectedColumnColor);
        }
        TableHelper.drawVerticalGrid(this, g);
    }

    public TableCellRenderer getDefaultRenderer(Class columnClass) {
        return myDefaultRenderer;
    }

    public class CellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (rendererFont != null) comp.setFont(rendererFont);
            if (row % 2 == 0) {
                comp.setBackground(DefaultStyle.TABLE_EVEN_ROW);
            } else {
                comp.setBackground(DefaultStyle.TABLE_ODD_ROW);
            }
            if (isSelected) {
                comp.setBackground(DefaultStyle.SELECTED_ROW_BACKGROUND);
                comp.setForeground(DefaultStyle.SELECTED_ROW_FOREGROUND);
            } else {
                comp.setForeground(Color.BLACK);
            }
            return comp;
        }
    }
}
