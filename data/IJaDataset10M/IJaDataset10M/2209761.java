package com.bluebrim.swing.client;

import java.awt.*;
import java.beans.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

/**
	A more client-server friendly subclass to <code>BasicListUI</code> as it 
	assumes that the model is representing a server object and therefore it's
	necessary to cache some values in the UI,i e the size of the list.
	<br>
	Has internal versions of several methods from <code>BasicListUI</code>
	with an extra argument for the size and reimplements the methods that uses 
	these methods.
	<br>
	Its mouselistener now sends a ProperyChangeEvent to all VetoableChangeListeners 
	of the list when the selection is about to change.
	In this way it's now possible for, for example, a catalog to veto a new selection
	if the validaton of the old selection fails.
	<p>
	This class is used as UI class by <code>CoList</code>.
 */
public class CoBasicListUI extends BasicListUI {

    /**
 * Return the JList relative Y coordinate of the origin of the specified 
 * row or -1 if row isn't valide.
 * 
 * @return The Y coordinate of the origin of row, or -1.
 * @see #getRowHeight
 * @see #updateLayoutState
 */
    private final int convertRowToY(int row, int listSize) {
        int nrows = listSize;
        Insets insets = list.getInsets();
        if ((row < 0) || (row > nrows)) {
            return -1;
        }
        if (cellHeights == null) {
            return insets.top + (cellHeight * row);
        } else if (row > cellHeights.length) {
            return -1;
        } else {
            int y = insets.top;
            for (int i = 0; i < row; i++) {
                y += cellHeights[i];
            }
            return y;
        }
    }

    /**
 * Convert the JList relative coordinate to the row that contains it,
 * based on the current layout.  If y0 doesn't fall within any row, 
 * return -1.
 * 
 * @return The row that contains y0, or -1.
 * @see #getRowHeight
 * @see #updateLayoutState
 */
    private final int convertYToRow(int y0, int listSize) {
        int nrows = listSize;
        Insets insets = list.getInsets();
        if (cellHeights == null) {
            int row = (cellHeight == 0) ? 0 : ((y0 - insets.top) / cellHeight);
            return ((row < 0) || (row >= nrows)) ? -1 : row;
        } else if (nrows > cellHeights.length) {
            return -1;
        } else {
            int y = insets.top;
            int row = 0;
            for (int i = 0; i < nrows; i++) {
                if ((y0 >= y) && (y0 < y + cellHeights[i])) {
                    return row;
                }
                y += cellHeights[i];
                row += 1;
            }
            return -1;
        }
    }

    protected FocusListener createFocusListener() {
        return new BasicListUI.FocusHandler() {

            protected void repaintCellFocus() {
                JList tList = getList();
                int leadIndex = tList.getLeadSelectionIndex();
                if (leadIndex != -1) {
                    Rectangle r = getCellBounds(tList, leadIndex, leadIndex, getListSize());
                    if (r != null) {
                        tList.repaint(r.x, r.y, r.width, r.height);
                    }
                }
            }
        };
    }

    protected ListSelectionListener createListSelectionListener() {
        return new BasicListUI.ListSelectionHandler() {

            public void valueChanged(ListSelectionEvent e) {
                int tListSize = getListSize();
                maybeUpdateLayoutState(tListSize);
                JList tList = getList();
                int minY = convertRowToY(e.getFirstIndex(), tListSize);
                int maxY = convertRowToY(e.getLastIndex(), tListSize);
                if ((minY == -1) || (maxY == -1)) {
                    tList.repaint(0, 0, tList.getWidth(), tList.getHeight());
                } else {
                    maxY += getRowHeight(e.getLastIndex(), tListSize);
                    tList.repaint(0, minY, tList.getWidth(), maxY - minY);
                }
            }
        };
    }

    protected MouseInputListener createMouseInputListener() {
        return new BasicListUI.MouseInputHandler() {

            public void mouseDragged(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                JList tList = getList();
                if (!tList.isEnabled()) {
                    return;
                }
                if (e.isShiftDown() || e.isControlDown()) {
                    return;
                }
                int tListSize = getListSize();
                int row = convertYToRow(e.getY(), tListSize);
                if (row != -1) {
                    Rectangle cellBounds = getCellBounds(row, row, tListSize);
                    if (cellBounds != null) {
                        tList.scrollRectToVisible(cellBounds);
                        tList.setSelectionInterval(row, row);
                    }
                }
            }

            public void mousePressed(MouseEvent e) {
                CoList tList = getList();
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                if (!tList.isEnabled()) {
                    return;
                }
                int tListSize = getListSize();
                int row = convertYToRow(e.getY(), tListSize);
                if (row != -1) {
                    try {
                        tList.fireVetoableChange(CoList.SELECTION_CHANGED, tList.getSelectedValue(), tList.getModel().getElementAt(row));
                    } catch (PropertyVetoException exception) {
                        e.consume();
                        return;
                    }
                }
                if (!tList.hasFocus()) {
                    tList.requestFocus();
                }
                if (row != -1) {
                    tList.setValueIsAdjusting(true);
                    int anchorIndex = tList.getAnchorSelectionIndex();
                    if (e.isControlDown()) {
                        if (tList.isSelectedIndex(row)) {
                            tList.removeSelectionInterval(row, row);
                        } else {
                            tList.addSelectionInterval(row, row);
                        }
                    } else if (e.isShiftDown() && (anchorIndex != -1)) {
                        tList.setSelectionInterval(anchorIndex, row);
                    } else {
                        tList.setSelectionInterval(row, row);
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                JList tList = getList();
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                tList.setValueIsAdjusting(false);
            }
        };
    }

    /** 
	 * @return The bounds of the index'th cell.
	 * @see ListUI#getCellBounds
	 */
    private final Rectangle getCellBounds(int index1, int index2, int listSize) {
        return getCellBounds(list, index1, index2, listSize);
    }

    /** 
	 * @return The bounds of the index'th cell.
	 * @see ListUI#getCellBounds
	 */
    public final Rectangle getCellBounds(JList list, int index1, int index2, int listSize) {
        maybeUpdateLayoutState();
        int minIndex = Math.min(index1, index2);
        int maxIndex = Math.max(index1, index2);
        int minY = convertRowToY(minIndex, listSize);
        int maxY = convertRowToY(maxIndex, listSize);
        if ((minY == -1) || (maxY == -1)) {
            return null;
        }
        Insets insets = list.getInsets();
        int x = insets.left;
        int y = minY;
        int w = list.getWidth() - (insets.left + insets.right);
        int h = (maxY + getRowHeight(maxIndex, listSize)) - minY;
        return new Rectangle(x, y, w, h);
    }

    private final CoList getList() {
        return (CoList) list;
    }

    private final int getListSize() {
        return list.getModel().getSize();
    }

    public Dimension getPreferredSize(JComponent c) {
        int tListSize = getListSize();
        maybeUpdateLayoutState(tListSize);
        int lastRow = tListSize - 1;
        if (lastRow < 0) {
            return new Dimension(0, 0);
        }
        Insets insets = list.getInsets();
        int width = cellWidth + insets.left + insets.right;
        int height = convertRowToY(lastRow, tListSize) + getRowHeight(lastRow, tListSize) + insets.bottom;
        return new Dimension(width, height);
    }

    /**
	 * Returns the height of the specified row based on the current layout.
	 * 
	 * @return The specified row height or -1 if row isn't valid. 
	 * @see #convertYToRow
	 * @see #convertRowToY
	 * @see #updateLayoutState
	 */
    private final int getRowHeight(int row, int listSize) {
        if ((row < 0) || (row >= listSize)) {
            return -1;
        }
        return (cellHeights == null) ? cellHeight : ((row < cellHeights.length) ? cellHeights[row] : -1);
    }

    private final void maybeUpdateLayoutState(int listSize) {
        if (updateLayoutStateNeeded != 0) {
            updateLayoutState(listSize);
            updateLayoutStateNeeded = 0;
        }
    }

    /**
	 * Paint the rows that intersect the Graphics objects clipRect.  This
	 * method paintCell as necessary.  Subclasses
	 * may want to override these methods.
	 * 
	 * @see #paintBackground
	 * @see #paintCell
	 */
    public void paint(Graphics g, JComponent c) {
        ListModel dataModel = list.getModel();
        int tListSize = dataModel.getSize();
        maybeUpdateLayoutState(tListSize);
        ListCellRenderer renderer = list.getCellRenderer();
        ListSelectionModel selModel = list.getSelectionModel();
        if ((renderer == null) || (tListSize == 0)) {
            return;
        }
        Rectangle paintBounds = g.getClipBounds();
        int firstPaintRow = convertYToRow(paintBounds.y, tListSize);
        int lastPaintRow = convertYToRow((paintBounds.y + paintBounds.height) - 1, tListSize);
        if (firstPaintRow == -1) {
            firstPaintRow = 0;
        }
        if (lastPaintRow == -1) {
            lastPaintRow = tListSize - 1;
        }
        Rectangle rowBounds = getCellBounds(list, firstPaintRow, firstPaintRow, tListSize);
        if (rowBounds == null) {
            return;
        }
        int leadIndex = list.getLeadSelectionIndex();
        for (int row = firstPaintRow; row <= lastPaintRow; row++) {
            rowBounds.height = getRowHeight(row, tListSize);
            g.setClip(rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height);
            g.clipRect(paintBounds.x, paintBounds.y, paintBounds.width, paintBounds.height);
            paintCell(g, row, rowBounds, renderer, dataModel, selModel, leadIndex);
            rowBounds.y += rowBounds.height;
        }
    }

    /**
	 * Selected the previous row and force it to be visible.
	 * Called by the KeyEvent.VK_DOWN keyboard action.
	 * 
	 * @see #registerKeyboardActions
	 * @see JList#ensureIndexIsVisible
	 */
    protected void selectNextIndex() {
        int s = list.getSelectedIndex();
        int tListSize = list.getModel().getSize();
        if ((s + 1) < tListSize) {
            s += 1;
            list.setSelectedIndex(s);
            ((CoList) list).ensureIndexIsVisible(s, tListSize);
        }
    }

    /**
	 * Selected the previous row and force it to be visible.
	 * Called by the KeyEvent.VK_UP keyboard action.
	 * 
	 * @see #registerKeyboardActions
	 * @see JList#ensureIndexIsVisible
	 */
    protected void selectPreviousIndex() {
        int s = list.getSelectedIndex();
        if (s > 0) {
            s -= 1;
            int tListSize = list.getModel().getSize();
            list.setSelectedIndex(s);
            getList().ensureIndexIsVisible(s, getListSize());
        }
    }

    /**
	 * Recompute the value of cellHeight or cellHeights based 
	 * and cellWidth, based on the current font and the current 
	 * values of fixedCellWidth, fixedCellHeight, and prototypeCellValue.
	 * 
	 * @see #maybeUpdateLayoutState
	 */
    protected void updateLayoutState() {
        updateLayoutState(list.getModel().getSize());
    }

    /**
	 * Recompute the value of cellHeight or cellHeights based 
	 * and cellWidth, based on the current font and the current 
	 * values of fixedCellWidth, fixedCellHeight, and prototypeCellValue.
	 * 
	 * @see #maybeUpdateLayoutState
	 */
    private void updateLayoutState(int listSize) {
        int fixedCellHeight = list.getFixedCellHeight();
        int fixedCellWidth = list.getFixedCellWidth();
        cellWidth = (fixedCellWidth != -1) ? fixedCellWidth : -1;
        if (fixedCellHeight != -1) {
            cellHeight = fixedCellHeight;
            cellHeights = null;
        } else {
            cellHeight = -1;
            cellHeights = new int[listSize];
        }
        if ((fixedCellWidth == -1) || (fixedCellHeight == -1)) {
            ListModel dataModel = list.getModel();
            int dataModelSize = listSize;
            ListCellRenderer renderer = list.getCellRenderer();
            if (renderer != null) {
                for (int index = 0; index < dataModelSize; index++) {
                    Object value = dataModel.getElementAt(index);
                    Component c = renderer.getListCellRendererComponent(list, value, index, false, false);
                    rendererPane.add(c);
                    Dimension cellSize = c.getPreferredSize();
                    if (fixedCellWidth == -1) {
                        cellWidth = Math.max(cellSize.width, cellWidth);
                    }
                    if (fixedCellHeight == -1) {
                        cellHeights[index] = cellSize.height;
                    }
                }
            } else {
                if (cellWidth == -1) {
                    cellWidth = 0;
                }
                for (int index = 0; index < dataModelSize; index++) {
                    cellHeights[index] = 0;
                }
            }
        }
        list.invalidate();
    }
}
