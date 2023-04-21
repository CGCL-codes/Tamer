package com.l2fprod.common.swing;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableColumn;

/**
 * HeaderlessColumnResizer. <br>
 * 
 * Allows table columns to be resized not only using the header but from any
 * rows. Based on the BasicTableHeaderUI code.
 */
public class HeaderlessColumnResizer extends MouseInputAdapter {

    private static Cursor resizeCursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

    private int mouseXOffset;

    private Cursor otherCursor = resizeCursor;

    private JTable table;

    public HeaderlessColumnResizer(JTable table) {
        this.table = table;
        table.addMouseListener(this);
        table.addMouseMotionListener(this);
    }

    private boolean canResize(TableColumn column) {
        return (column != null) && table.getTableHeader().getResizingAllowed() && column.getResizable();
    }

    private TableColumn getResizingColumn(Point p) {
        return getResizingColumn(p, table.columnAtPoint(p));
    }

    private TableColumn getResizingColumn(Point p, int column) {
        if (column == -1) {
            return null;
        }
        int row = table.rowAtPoint(p);
        Rectangle r = table.getCellRect(row, column, true);
        r.grow(-3, 0);
        if (r.contains(p)) {
            return null;
        }
        int midPoint = r.x + r.width / 2;
        int columnIndex;
        if (table.getTableHeader().getComponentOrientation().isLeftToRight()) {
            columnIndex = (p.x < midPoint) ? column - 1 : column;
        } else {
            columnIndex = (p.x < midPoint) ? column : column - 1;
        }
        if (columnIndex == -1) {
            return null;
        }
        return table.getTableHeader().getColumnModel().getColumn(columnIndex);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        table.getTableHeader().setDraggedColumn(null);
        table.getTableHeader().setResizingColumn(null);
        table.getTableHeader().setDraggedDistance(0);
        Point p = e.getPoint();
        int index = table.columnAtPoint(p);
        if (index != -1) {
            TableColumn resizingColumn = getResizingColumn(p, index);
            if (canResize(resizingColumn)) {
                table.getTableHeader().setResizingColumn(resizingColumn);
                if (table.getTableHeader().getComponentOrientation().isLeftToRight()) {
                    mouseXOffset = p.x - resizingColumn.getWidth();
                } else {
                    mouseXOffset = p.x + resizingColumn.getWidth();
                }
            }
        }
    }

    private void swapCursor() {
        Cursor tmp = table.getCursor();
        table.setCursor(otherCursor);
        otherCursor = tmp;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (canResize(getResizingColumn(e.getPoint())) != (table.getCursor() == resizeCursor)) {
            swapCursor();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mouseX = e.getX();
        TableColumn resizingColumn = table.getTableHeader().getResizingColumn();
        boolean headerLeftToRight = table.getTableHeader().getComponentOrientation().isLeftToRight();
        if (resizingColumn != null) {
            int oldWidth = resizingColumn.getWidth();
            int newWidth;
            if (headerLeftToRight) {
                newWidth = mouseX - mouseXOffset;
            } else {
                newWidth = mouseXOffset - mouseX;
            }
            resizingColumn.setWidth(newWidth);
            Container container;
            if ((table.getTableHeader().getParent() == null) || ((container = table.getTableHeader().getParent().getParent()) == null) || !(container instanceof JScrollPane)) {
                return;
            }
            if (!container.getComponentOrientation().isLeftToRight() && !headerLeftToRight) {
                if (table != null) {
                    JViewport viewport = ((JScrollPane) container).getViewport();
                    int viewportWidth = viewport.getWidth();
                    int diff = newWidth - oldWidth;
                    int newHeaderWidth = table.getWidth() + diff;
                    Dimension tableSize = table.getSize();
                    tableSize.width += diff;
                    table.setSize(tableSize);
                    if ((newHeaderWidth >= viewportWidth) && (table.getAutoResizeMode() == JTable.AUTO_RESIZE_OFF)) {
                        Point p = viewport.getViewPosition();
                        p.x = Math.max(0, Math.min(newHeaderWidth - viewportWidth, p.x + diff));
                        viewport.setViewPosition(p);
                        mouseXOffset += diff;
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        table.getTableHeader().setResizingColumn(null);
        table.getTableHeader().setDraggedColumn(null);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
