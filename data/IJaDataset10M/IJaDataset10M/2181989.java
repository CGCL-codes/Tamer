package com.jtstand.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author albert_kurucz
 */
public class Util {

    public static final Class<?>[] emptyContructor = {};

    public static void centerOnParent(JDialog dialog) {
        centerOn(dialog, dialog.getParent());
    }

    public static void centerOn(JDialog dialog, Component other) {
        int w = dialog.getWidth();
        int h = dialog.getHeight();
        Rectangle bounds = other.getBounds();
        int bw = bounds.width;
        int bh = bounds.height;
        dialog.setLocation(bounds.x + (bw - w) / 2, bounds.y + (bh - h) / 2);
    }

    /**
     * Returns the row index of the last visible row.
     */
    public static int getFirstVisibleRowIndex(JTable table) {
        ComponentOrientation or = table.getComponentOrientation();
        Rectangle r = table.getVisibleRect();
        if (!or.isLeftToRight()) {
            r.translate((int) r.getWidth() - 1, 0);
        }
        return table.rowAtPoint(r.getLocation());
    }

    /**
     * Returns the row index of the last visible row.
     */
    public static int getLastVisibleRowIndex(JTable table) {
        ComponentOrientation or = table.getComponentOrientation();
        Rectangle r = table.getVisibleRect();
        r.translate(0, (int) r.getHeight() - 1);
        if (or.isLeftToRight()) {
            r.translate((int) r.getWidth() - 1, 0);
        }
        if (table.rowAtPoint(r.getLocation()) == -1) {
            if (getFirstVisibleRowIndex(table) == -1) {
                return -1;
            } else {
                return table.getModel().getRowCount() - 1;
            }
        }
        return table.rowAtPoint(r.getLocation());
    }

    public static void scrollToCenter(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            System.out.println("Parent is not a JViewport, but:" + table.getParent());
            return;
        }
        JViewport viewport = (JViewport) table.getParent();
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
        Rectangle viewRect = viewport.getViewRect();
        rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);
        int centerX = (viewRect.width - rect.width) / 2;
        int centerY = (viewRect.height - rect.height) / 2;
        if (rect.x < centerX) {
            centerX = -centerX;
        }
        if (rect.y < centerY) {
            centerY = -centerY;
        }
        rect.translate(centerX, centerY);
        viewport.scrollRectToVisible(rect);
    }

    public static int packColumns(JTable table, int margin) {
        int width = 0;
        for (int c = 0; c < table.getColumnCount(); c++) {
            width += packColumn(table, c, margin);
        }
        return width;
    }

    public static int getRowCount(JTable table, int h) {
        int rh = table.getRowHeight();
        h -= table.getTableHeader().getHeight();
        if (h <= 0) {
            return 0;
        }
        return Math.min((h + rh / 2) / rh, table.getRowCount());
    }

    public static int getHeight(JTable table, int rowCount, JScrollPane jScrollPane, JSplitPane jSplitPane) {
        int h = getHeight(table, rowCount);
        if (jScrollPane != null) {
            Insets i = jScrollPane.getInsets();
            if (i != null) {
                h += (i.bottom + i.top);
            }
        }
        if (jSplitPane != null) {
            Insets i = jSplitPane.getInsets();
            if (i != null) {
                h += i.top + i.bottom;
            }
        }
        return h;
    }

    public static int getHeight(JTable table, int rowCount) {
        int h = table.getRowHeight() * rowCount + table.getTableHeader().getPreferredSize().height - table.getRowMargin();
        return h;
    }

    public static int packColumn(JTable table, int vColIndex, int margin) {
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        width = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0).getPreferredSize().width;
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            width = Math.max(width, renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false, r, vColIndex).getPreferredSize().width);
        }
        width += 2 * margin;
        col.setPreferredWidth(width);
        return width;
    }

    public static Dimension getMaximumWindowDimension() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        return new Dimension(bounds.width, bounds.height);
    }
}
