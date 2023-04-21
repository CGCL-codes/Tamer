package org.xfc.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import ca.odell.glazedlists.EventList;

/**
 * 
 * 
 * @author Devon Carew
 */
public class XRowHeader extends JComponent {

    private XTable table;

    private TableCellRenderer defaultRenderer;

    private static TableCellRenderer DEFAULT_RENDERER = new XRowHeaderRenderer();

    public XRowHeader() {
        setBorder(new MatteBorder(0, 0, 0, 1, XTable.BORDER_COLOR));
    }

    public XRowHeader(XTable table) {
        this();
        setTable(table);
    }

    public XTable getTable() {
        return table;
    }

    public void setTable(XTable table) {
        this.table = table;
    }

    public TableCellRenderer getDefaultRenderer() {
        return defaultRenderer;
    }

    public void setDefaultRenderer(TableCellRenderer defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }

    private TableCellRenderer getRenderer() {
        if (getDefaultRenderer() == null) return DEFAULT_RENDERER;
        return getDefaultRenderer();
    }

    public Dimension getMinimumSize() {
        Dimension min = new Dimension();
        TableCellRenderer renderer = getRenderer();
        EventList list = getTable().getVisibleRowObjects();
        JTable table = getTable().getTable();
        try {
            list.getReadWriteLock().readLock().lock();
            for (int i = 0; i < list.size(); i++) {
                Object rowObject = list.get(i);
                Component component = renderer.getTableCellRendererComponent(table, rowObject, false, false, i, -1);
                Dimension dim = component.getMinimumSize();
                min.width = Math.max(dim.width, min.width);
            }
        } finally {
            list.getReadWriteLock().readLock().unlock();
        }
        Insets insets = getInsets();
        min.height = table.getRowCount() * table.getRowHeight() + insets.top + insets.bottom;
        min.width += insets.left + insets.right;
        return min;
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        JTable table = getTable().getTable();
        TableCellRenderer renderer = getRenderer();
        EventList list = getTable().getVisibleRowObjects();
        int rowHeight = table.getRowHeight();
        Dimension rowHeaderSize = getSize();
        Rectangle clipBounds = g.getClipBounds();
        int startRow = clipBounds.y / rowHeight;
        int endRow = ((clipBounds.y + clipBounds.height) / rowHeight) + 1;
        g.translate(0, rowHeight * startRow);
        for (int i = startRow; i < table.getRowCount(); i++) {
            Object rowObject = list.get(i);
            boolean isSelected = table.getSelectionModel().isSelectedIndex(i);
            Component component = renderer.getTableCellRendererComponent(table, rowObject, isSelected, false, i, -1);
            component.setBounds(0, 0, rowHeaderSize.width, rowHeight);
            component.paint(g);
            g.translate(0, rowHeight);
            if (i >= endRow) break;
        }
    }
}
