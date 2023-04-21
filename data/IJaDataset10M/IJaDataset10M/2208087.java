package org.vastenhouw.swing;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import org.vastenhouw.util.Debug;

/**
 * A Windows L&F implementation of ListUI.
 * <p>
 *
 */
public class Basic2DListUI extends BasicListUI {

    protected static final int columnsPerRowChanged = cellRendererChanged << 1;

    protected int columnsPerRow = 1;

    protected int[] cellWidths = null;

    private static boolean tracing = true;

    /**
     * Paint the rows that intersect the Graphics objects clipRect.  This
     * Paint the rows that intersect the Graphics objects clipRect.  This
     * Paint the rows that intersect the Graphics objects clipRect.  This
     * method calls paintCell as necessary.  Subclasses
     * may want to override these methods.
     *
     * @see #paintCell
     */
    public void paint(Graphics g, JComponent c) {
        maybeUpdateLayoutState();
        ListCellRenderer renderer = list.getCellRenderer();
        ListModel dataModel = list.getModel();
        ListSelectionModel selModel = list.getSelectionModel();
        if ((renderer == null) || (dataModel.getSize() == 0)) {
            return;
        }
        Rectangle paintBounds = g.getClipBounds();
        int firstPaintColumn = convertXToColumn(paintBounds.x);
        int firstPaintRow = convertYToRow(paintBounds.y);
        int lastPaintColumn = convertXToColumn((paintBounds.x + paintBounds.width) - 1);
        int lastPaintRow = convertYToRow((paintBounds.y + paintBounds.height) - 1);
        if (firstPaintRow == -1) {
            firstPaintRow = 0;
        }
        if (lastPaintRow == -1) {
            lastPaintRow = (dataModel.getSize() - 1) / columnsPerRow;
        }
        if (firstPaintColumn == -1) {
            firstPaintColumn = 0;
        }
        if (lastPaintColumn == -1) {
            lastPaintColumn = columnsPerRow - 1;
        }
        Rectangle itemBounds = getCellBoundsPerColumnRow(list, firstPaintColumn, firstPaintRow);
        if (itemBounds == null) {
            return;
        }
        int leadIndex = list.getLeadSelectionIndex();
        int startItemBoundX = itemBounds.x;
        for (int row = firstPaintRow; row <= lastPaintRow; row++) {
            itemBounds.x = startItemBoundX;
            for (int column = firstPaintColumn; column <= lastPaintColumn; column++) {
                int index = row * columnsPerRow + column;
                if (index >= dataModel.getSize()) {
                    break;
                }
                itemBounds.width = getItemWidth(index);
                itemBounds.height = getItemHeight(index);
                g.setClip(itemBounds.x, itemBounds.y, itemBounds.width, itemBounds.height);
                g.clipRect(paintBounds.x, paintBounds.y, paintBounds.width, paintBounds.height);
                paintCell(g, index, itemBounds, renderer, dataModel, selModel, leadIndex);
                itemBounds.x += itemBounds.width;
            }
            itemBounds.y += itemBounds.height;
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        maybeUpdateLayoutState();
        int lastItem = list.getModel().getSize() - 1;
        if (lastItem < 0) {
            return new Dimension(0, 0);
        }
        Insets insets = list.getInsets();
        int width = convertItemToX(columnsPerRow - 1) + getItemWidth(lastItem) + insets.right;
        int height = convertItemToY(lastItem) + getItemHeight(lastItem) + insets.bottom;
        return new Dimension(width, height);
    }

    /**
     * @returns The preferred size.
     * @see #getPreferredSize
     */
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }

    /**
     * @returns The preferred size.
     * @see #getPreferredSize
     */
    public Dimension getMaximumSize(JComponent c) {
        return getPreferredSize(c);
    }

    /**
     * Register keyboard actions for the up and down arrow keys.  The
     * actions just call out to protected methods, subclasses that
     * want to override or extend keyboard behavior should consider
     * just overriding those methods.  This method is called at
     * installUI() time.
     *
     * @see #selectPreviousIndex
     * @see #selectNextIndex
     * @see #installUI
     */
    protected void installKeyboardActions() {
        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, inputMap);
        ActionMap map = getActionMap();
        if (map != null) {
            SwingUtilities.replaceUIActionMap(list, map);
        }
    }

    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_FOCUSED) {
            InputMap j2DList = (InputMap) UIManager.get("2DList.focusInputMap");
            if (j2DList == null) {
                j2DList = (InputMap) UIManager.get("List.focusInputMap");
                addExtendedKeyStrokes(j2DList);
            }
            return j2DList;
        }
        return null;
    }

    void addExtendedKeyStrokes(InputMap inputMap) {
        Object[] extendedKeyStrokes = { "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection" };
        LookAndFeel.loadKeyBindings(inputMap, extendedKeyStrokes);
    }

    ActionMap getActionMap() {
        ActionMap map = (ActionMap) UIManager.get("2DList.actionMap");
        if (map == null) {
            map = createActionMap();
            if (map != null) {
                UIManager.put("2DList.actionMap", map);
            }
        }
        return map;
    }

    ActionMap createActionMap() {
        ActionMap map = new ActionMapUIResource();
        map.put("selectPreviousRow", new IncrementLeadSelectionAction("selectPreviousRow", CHANGE_SELECTION, LEAD_UP));
        map.put("selectPreviousRowExtendSelection", new IncrementLeadSelectionAction("selectPreviousColumnExtendSelection", EXTEND_SELECTION, LEAD_UP));
        map.put("selectPreviousColumn", new IncrementLeadSelectionAction("selectPreviousColumn", CHANGE_SELECTION, LEAD_LEFT));
        map.put("selectPreviousColumnExtendSelection", new IncrementLeadSelectionAction("selectPreviousColumnExtendSelection", EXTEND_SELECTION, LEAD_LEFT));
        map.put("selectNextRow", new IncrementLeadSelectionAction("selectNextRow", CHANGE_SELECTION, LEAD_DOWN));
        map.put("selectNextRowExtendSelection", new IncrementLeadSelectionAction("selectNextRowExtendSelection", EXTEND_SELECTION, LEAD_DOWN));
        map.put("selectNextColumn", new IncrementLeadSelectionAction("selectNextColumn", CHANGE_SELECTION, LEAD_RIGHT));
        map.put("selectNextColumnExtendSelection", new IncrementLeadSelectionAction("selectNextColumnExtendSelection", EXTEND_SELECTION, LEAD_RIGHT));
        map.put("selectFirstRow", new HomeAction("selectFirstRow", CHANGE_SELECTION));
        map.put("selectFirstRowExtendSelection", new HomeAction("selectFirstRowExtendSelection", EXTEND_SELECTION));
        map.put("selectLastRow", new EndAction("selctLastRow", CHANGE_SELECTION));
        map.put("selectLastRowExtendSelection", new EndAction("selectLastRowExtendSelection", EXTEND_SELECTION));
        map.put("scrollUp", new PageUpAction("scrollUp", CHANGE_SELECTION));
        map.put("scrollUpExtendSelection", new PageUpAction("scrollUpExtendSelection", EXTEND_SELECTION));
        map.put("scrollDown", new PageDownAction("scrollDown", CHANGE_SELECTION));
        map.put("scrollDownExtendSelection", new PageDownAction("scrollDownExtendSelection", EXTEND_SELECTION));
        map.put("selectAll", new SelectAllAction("selectAll"));
        map.put("clearSelection", new ClearSelectionAction("clearSelection"));
        return map;
    }

    /**
     * Unregister keyboard actions for the up and down arrow keys.
     * This method is called at uninstallUI() time - subclassess should
     * ensure that all of the keyboard actions registered at installUI
     * time are removed here.
     *
     * @see #selectPreviousIndex
     * @see #selectNextIndex
     * @see #installUI
     */
    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(list, null);
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, null);
    }

    /**
     * Uninitializes <code>this.list</code> by calling <code>uninstallListeners()</code>,
     * <code>uninstallKeyboardActions()</code>, and <code>uninstallDefaults()</code>
     * in order.  Sets this.list to null.
     *
     * @see #uninstallListeners
     * @see #uninstallKeyboardActions
     * @see #uninstallDefaults
     */
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        cellWidths = null;
    }

    /**
     * Returns a new instance of Basic2DListUI.  Basic2DListUI delegates are
     * allocated one per JList.
     *
     * @return A new ListUI implementation for the Windows look and feel.
     */
    public static ComponentUI createUI(JComponent list) {
        return new Basic2DListUI();
    }

    /**
     * @return The index of the cell at location, or -1.
     * @see ListUI#locationToIndex
     */
    public int locationToIndex(JList list, Point location) {
        maybeUpdateLayoutState();
        return convertXYToIndex(location.x, location.y);
    }

    /**
     * @return The origin of the index'th cell, null if index is invalid.
     * @see ListUI#indexToLocation
     */
    public Point indexToLocation(JList list, int index) {
        maybeUpdateLayoutState();
        int x = convertItemToX(index);
        int y = convertItemToY(index);
        return ((y == -1) || (x == -1)) ? null : new Point(x, y);
    }

    public int locationToNearestIndex(J2DList list, Point p) {
        int index = locationToIndex(list, p);
        if (index != -1) {
            Rectangle cell = getCellBounds(list, index, index);
            if (cell.getX() + cell.getWidth() / 2 < p.getX()) {
                index++;
            }
        } else {
            int row = convertYToRow(p.y);
            if (row != -1) {
                index = (row + 1) * columnsPerRow;
            }
        }
        if (index >= list.getModel().getSize()) {
            index = -1;
        }
        return index;
    }

    public Rectangle getCellBoundsPerColumnRow(JList list, int column, int row) {
        int index = row * columnsPerRow + column;
        return getCellBounds(list, index, index);
    }

    /**
     * @return The bounds of the index'th cell.
     * @see ListUI#getCellBounds
     */
    public Rectangle getCellBounds(JList list, int index1, int index2) {
        maybeUpdateLayoutState();
        int minIndex = Math.min(index1, index2);
        int maxIndex = Math.max(index1, index2);
        int minX = convertItemToX(minIndex);
        int maxX = convertItemToX(maxIndex);
        int minY = convertItemToY(minIndex);
        int maxY = convertItemToY(maxIndex);
        if ((minX == -1) || (maxX == -1) || (minY == -1) || (maxY == -1)) {
            return null;
        }
        int x = minX;
        int y = minY;
        int w = (maxX + getItemWidth(maxIndex)) - minX;
        int h = (maxY + getItemHeight(maxIndex)) - minY;
        return new Rectangle(x, y, w, h);
    }

    /**
     * Returns the height of the specified row based on the current layout.
     *
     * @return The specified row height or -1 if row isn't valid.
     * @see #convertYToRow
     * @see #convertRowToY
     * @see #updateLayoutState
     */
    protected int getRowHeight(int row) {
        if (tracing) {
            Exception e = new Exception();
            e.printStackTrace();
        }
        return getItemHeight(row);
    }

    protected int getItemHeight(int index) {
        if ((index < 0) || (index >= list.getModel().getSize())) {
            return -1;
        }
        int row = index / columnsPerRow;
        return (cellHeights == null) ? cellHeight : ((row < cellHeights.length) ? cellHeights[row] : -1);
    }

    protected int getItemWidth(int index) {
        if ((index < 0) || (index >= list.getModel().getSize())) {
            return -1;
        }
        int column = index % columnsPerRow;
        return (cellWidths == null) ? cellWidth : ((column < cellWidths.length) ? cellWidths[column] : -1);
    }

    protected int convertXYToIndex(int x, int y) {
        int column = convertXToColumn(x);
        int row = convertYToRow(y);
        if (column < 0 || row < 0) {
            return -1;
        }
        int index = row * columnsPerRow + column;
        if (index >= list.getModel().getSize()) {
            return -1;
        }
        return index;
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
    protected int convertYToRow(int y0) {
        int nItems = list.getModel().getSize();
        Insets insets = list.getInsets();
        if (nItems <= 0) {
            return -1;
        }
        int rowCount = (nItems - 1) / columnsPerRow + 1;
        if (cellHeights == null) {
            int row = (cellHeight == 0) ? 0 : ((y0 - insets.top) / cellHeight);
            return ((row < 0) || (row >= rowCount)) ? -1 : row;
        } else if (rowCount > cellHeights.length) {
            return -1;
        } else {
            int y = insets.top;
            int row = 0;
            for (int i = 0; i < rowCount; i++) {
                if ((y0 >= y) && (y0 < y + cellHeights[i])) {
                    return row;
                }
                y += cellHeights[i];
                row += 1;
            }
            return -1;
        }
    }

    protected int convertXToColumn(int x0) {
        int nItems = list.getModel().getSize();
        Insets insets = list.getInsets();
        if (nItems <= 0) {
            return -1;
        }
        int columnCount = Math.min(nItems, columnsPerRow);
        if (cellWidths == null) {
            int column = (cellWidth == 0) ? 0 : ((x0 - insets.left) / cellWidth);
            return ((column < 0) || (column >= columnCount)) ? -1 : column;
        } else if (columnCount > cellWidths.length) {
            return -1;
        } else {
            int x = insets.left;
            int column = 0;
            for (int i = 0; i < columnCount; i++) {
                if ((x0 >= x) && (x0 < x + cellWidths[i])) {
                    return column;
                }
                x += cellWidths[i];
                column += 1;
            }
            return -1;
        }
    }

    /**
     * Return the JList relative Y coordinate of the origin of the specified
     * row or -1 if row isn't valid.
     *
     * @return The Y coordinate of the origin of row, or -1.
     * @see #getRowHeight
     * @see #updateLayoutState
     */
    protected int convertRowToY(int row) {
        if (tracing) {
            Exception e = new Exception();
            e.printStackTrace();
        }
        return convertItemToY(row);
    }

    protected int convertItemToY(int index) {
        int nItems = list.getModel().getSize();
        Insets insets = list.getInsets();
        if ((nItems < 0) || (index >= nItems)) {
            return -1;
        }
        int row = index / columnsPerRow;
        if (cellHeights == null) {
            return insets.top + (cellHeight * row);
        } else if (row >= cellHeights.length) {
            return -1;
        } else {
            int y = insets.top;
            for (int i = 0; i < row; i++) {
                y += cellHeights[i];
            }
            return y;
        }
    }

    protected int convertItemToX(int index) {
        int nItems = list.getModel().getSize();
        Insets insets = list.getInsets();
        if ((nItems < 0) || (index >= nItems)) {
            return -1;
        }
        int column = index % columnsPerRow;
        if (cellWidths == null) {
            return insets.top + (cellWidth * column);
        } else if (column >= cellWidths.length) {
            return -1;
        } else {
            int x = insets.left;
            for (int i = 0; i < column; i++) {
                x += cellWidths[i];
            }
            return x;
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
        columnsPerRow = ((J2DList) list).getColumnsPerRow();
        int fixedCellHeight = list.getFixedCellHeight();
        int fixedCellWidth = list.getFixedCellWidth();
        int nItems = list.getModel().getSize();
        if (fixedCellWidth != -1) {
            cellWidth = fixedCellWidth;
            cellWidths = null;
        } else {
            cellWidth = -1;
            cellWidths = new int[Math.min(nItems, columnsPerRow)];
        }
        if (fixedCellHeight != -1) {
            cellHeight = fixedCellHeight;
            cellHeights = null;
        } else {
            cellHeight = -1;
            cellHeights = new int[(nItems - 1) / columnsPerRow + 1];
        }
        if ((fixedCellWidth == -1) || (fixedCellHeight == -1)) {
            ListModel dataModel = list.getModel();
            int dataModelSize = dataModel.getSize();
            ListCellRenderer renderer = list.getCellRenderer();
            if (renderer != null) {
                for (int index = 0; index < dataModelSize; index++) {
                    Object value = dataModel.getElementAt(index);
                    Component c = renderer.getListCellRendererComponent(list, value, index, false, false);
                    rendererPane.add(c);
                    Dimension cellSize = c.getPreferredSize();
                    int column = index % columnsPerRow;
                    int row = index / columnsPerRow;
                    if (fixedCellWidth == -1) {
                        cellWidths[column] = Math.max(cellWidths[column], cellSize.width);
                    }
                    if (fixedCellHeight == -1) {
                        cellHeights[row] = Math.max(cellHeights[row], cellSize.height);
                    }
                }
            } else {
                if (fixedCellWidth == -1) {
                    for (int index = 0; index < cellWidths.length; index++) {
                        cellWidths[index] = 0;
                    }
                }
                if (fixedCellHeight == -1) {
                    for (int index = 0; index < cellHeights.length; index++) {
                        cellHeights[index] = 0;
                    }
                }
            }
        }
        list.invalidate();
    }

    protected int getMaxColumnsPerRow(int width) {
        int fixedCellWidth = list.getFixedCellWidth();
        if (fixedCellWidth != -1) {
            return width / fixedCellWidth;
        } else {
            int maxColumns = -1;
            ListModel dataModel = list.getModel();
            int nItems = dataModel.getSize();
            ListCellRenderer renderer = list.getCellRenderer();
            if (renderer != null) {
                Dimension[] cellDims = new Dimension[nItems];
                for (int index = 0; index < nItems; index++) {
                    Object value = dataModel.getElementAt(index);
                    Component c = renderer.getListCellRendererComponent(list, value, index, false, false);
                    rendererPane.add(c);
                    cellDims[index] = c.getPreferredSize();
                }
                maxColumns = getMaxItemsBasedOnFirstRow(cellDims, width);
                int index = maxColumns;
                while (index < nItems && maxColumns > 0) {
                    int newColMax = getMaxItemsBasedOnRow(index / maxColumns, maxColumns, cellDims, width);
                    if (newColMax != -1 && newColMax < maxColumns) {
                        maxColumns = maxColumns - 1;
                        index = maxColumns;
                    } else {
                        index += maxColumns;
                    }
                }
            }
            if (maxColumns == 0) {
                maxColumns = 1;
            }
            return maxColumns;
        }
    }

    private int getMaxItemsBasedOnFirstRow(Dimension[] dims, int width) {
        return getMaxItemsBasedOnRow(0, 0, dims, width);
    }

    private int getMaxItemsBasedOnRow(int row, int rowsPerCol, Dimension[] dims, int width) {
        int localWidth = 0;
        int index = row * rowsPerCol;
        while (index < dims.length && (localWidth + dims[index].width) < width) {
            localWidth += dims[index].width;
            index++;
        }
        if (index == dims.length) {
            return -1;
        }
        return index - row * rowsPerCol;
    }

    /**
     * Mouse input, and focus handling for JList.  An instance of this
     * class is added to the appropriate java.awt.Component lists
     * at installUI() time.  Note keyboard input is handled with JComponent
     * KeyboardActions, see installKeyboardActions().
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see #createMouseInputListener
     * @see #installKeyboardActions
     * @see #installUI
     */
    public class MouseInputHandler implements MouseInputListener {

        private boolean ignoreMouseReleased = false;

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            ignoreMouseReleased = false;
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            if (!list.isEnabled()) {
                return;
            }
            if (!list.hasFocus()) {
                list.requestFocus();
            }
            int index = convertXYToIndex(e.getX(), e.getY());
            if (index != -1) {
                list.setValueIsAdjusting(true);
                int anchorIndex = list.getAnchorSelectionIndex();
                if (anchorIndex == -1) {
                    anchorIndex = 0;
                }
                if (e.isShiftDown()) {
                    list.setSelectionInterval(anchorIndex, index);
                } else if (e.isControlDown()) {
                    if (list.isSelectedIndex(index)) {
                        list.removeSelectionInterval(index, index);
                    } else {
                        list.addSelectionInterval(index, index);
                    }
                } else {
                    if (!list.isSelectedIndex(index)) {
                        list.setSelectionInterval(index, index);
                    }
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            if (!list.isEnabled()) {
                return;
            }
            if (e.isShiftDown() || e.isControlDown()) {
                return;
            }
            ignoreMouseReleased = true;
            int index = convertXYToIndex(e.getX(), e.getY());
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            list.setValueIsAdjusting(false);
            int index = convertXYToIndex(e.getX(), e.getY());
            if (index != -1) {
                if (!ignoreMouseReleased && !e.isShiftDown() && !e.isControlDown() && list.isSelectedIndex(index)) {
                    list.setSelectionInterval(index, index);
                }
            }
        }
    }

    public class MouseInputHandler2 implements MouseInputListener {

        private boolean ignoreReleaseEvent = false;

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            if (!list.isEnabled()) {
                return;
            }
            if (!list.hasFocus()) {
                list.requestFocus();
            }
            ignoreReleaseEvent = false;
            int index = convertXYToIndex(e.getX(), e.getY());
            if (index != -1) {
                list.setValueIsAdjusting(true);
                int anchorIndex = list.getAnchorSelectionIndex();
                if (e.isControlDown()) {
                    if (list.isSelectedIndex(index)) {
                        list.removeSelectionInterval(index, index);
                    } else {
                        list.addSelectionInterval(index, index);
                    }
                } else if (e.isShiftDown() && (anchorIndex != -1)) {
                    list.setSelectionInterval(anchorIndex, index);
                } else {
                    if (!list.isSelectedIndex(index)) {
                        list.setSelectionInterval(index, index);
                    }
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            if (!list.isEnabled()) {
                return;
            }
            if (e.isShiftDown() || e.isControlDown()) {
                return;
            }
            ignoreReleaseEvent = true;
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            list.setValueIsAdjusting(false);
            int index = convertXYToIndex(e.getX(), e.getY());
            if (index != -1) {
                int anchorIndex = list.getAnchorSelectionIndex();
                if (!ignoreReleaseEvent && !e.isControlDown() && !e.isShiftDown() && list.isSelectedIndex(index)) {
                    list.setSelectionInterval(index, index);
                }
            }
        }
    }

    /**
     * Creates a delegate that implements MouseInputListener.
     * The delegate is added to the corresponding java.awt.Component listener 
     * lists at installUI() time. Subclasses can override this method to return 
     * a custom MouseInputListener, e.g.
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected MouseInputListener <b>createMouseInputListener</b>() {
     *        return new MyMouseInputHandler();
     *    }
     *    public class MyMouseInputHandler extends MouseInputHandler {
     *        public void mouseMoved(MouseEvent e) {
     *            // do some extra work when the mouse moves
     *            super.mouseMoved(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @see MouseInputHandler
     * @see #installUI
     */
    protected MouseInputListener createMouseInputListener() {
        return new MouseInputHandler();
    }

    /**
     * The ListSelectionListener that's added to the JLists selection
     * model at installUI time, and whenever the JList.selectionModel property
     * changes.  When the selection changes we repaint the affected rows.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see #createListSelectionListener
     * @see #getCellBounds
     * @see #installUI
     */
    public class ListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            maybeUpdateLayoutState();
            int first = e.getFirstIndex();
            int last = e.getLastIndex();
            int minX = convertItemToX(first);
            int maxX = convertItemToX(last);
            int minY = convertItemToY(first);
            int maxY = convertItemToY(last);
            if ((minX == -1) || (maxX == -1) || (minY == -1) || (maxY == -1)) {
                list.repaint(0, 0, list.getWidth(), list.getHeight());
            } else {
                maxX += getItemWidth(e.getLastIndex());
                maxY += getItemHeight(e.getLastIndex());
                boolean moreThanOneRow = (first / columnsPerRow) != (last / columnsPerRow);
                if (moreThanOneRow) {
                    list.repaint(0, minY, list.getWidth(), maxY - minY);
                } else {
                    list.repaint(minX, minY, maxX - minX, maxY - minY);
                }
            }
        }
    }

    /**
     * Creates an instance of ListSelectionHandler that's added to
     * the JLists by selectionModel as needed.  Subclasses can override
     * this method to return a custom ListSelectionListener, e.g.
     * <pre>
     * class Basic2DListUI extends BasicListUI {
     *    protected ListSelectionListener <b>createListSelectionListener</b>() {
     *        return new MySelectionListener();
     *    }
     *    public class MySelectionListener extends ListSelectionHandler {
     *        public void valueChanged(ListSelectionEvent e) {
     *            // do some extra work when the selection changes
     *            super.valueChange(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @see ListSelectionHandler
     * @see #installUI
     */
    protected ListSelectionListener createListSelectionListener() {
        return new ListSelectionHandler();
    }

    private void redrawList() {
        list.revalidate();
        list.repaint();
    }

    /**
     * The ListDataListener that's added to the JLists model at
     * installUI time, and whenever the JList.model property changes.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see JList#getModel
     * @see #maybeUpdateLayoutState
     * @see #createListDataListener
     * @see #installUI
     */
    public class ListDataHandler implements ListDataListener {

        public void intervalAdded(ListDataEvent e) {
            updateLayoutStateNeeded = modelChanged;
            int minIndex = Math.min(e.getIndex0(), e.getIndex1());
            int maxIndex = Math.max(e.getIndex0(), e.getIndex1());
            ListSelectionModel sm = list.getSelectionModel();
            if (sm != null) {
                sm.insertIndexInterval(minIndex, maxIndex - minIndex, true);
            }
            int y = Math.max(0, convertItemToY(minIndex));
            int h = list.getHeight() - y;
            list.revalidate();
            list.repaint(0, y, list.getWidth(), h);
        }

        public void intervalRemoved(ListDataEvent e) {
            updateLayoutStateNeeded = modelChanged;
            ListSelectionModel sm = list.getSelectionModel();
            if (sm != null) {
                sm.removeIndexInterval(e.getIndex0(), e.getIndex1());
            }
            int minIndex = Math.min(e.getIndex0(), e.getIndex1());
            int y = Math.max(0, convertItemToY(minIndex));
            int h = list.getHeight() - y;
            list.revalidate();
            list.repaint(0, y, list.getWidth(), h);
        }

        public void contentsChanged(ListDataEvent e) {
            updateLayoutStateNeeded = modelChanged;
            redrawList();
        }
    }

    /**
     * Creates an instance of ListDataListener that's added to
     * the JLists by model as needed.  Subclasses can override
     * this method to return a custom ListDataListener, e.g.
     * <pre>
     * class Basic2DListUI extends BasicListUI {
     *    protected ListDataListener <b>createListDataListener</b>() {
     *        return new MyListDataListener();
     *    }
     *    public class MyListDataListener extends ListDataHandler {
     *        public void contentsChanged(ListDataEvent e) {
     *            // do some extra work when the models contents change
     *            super.contentsChange(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @see ListDataListener
     * @see JList#getModel
     * @see #installUI
     */
    protected ListDataListener createListDataListener() {
        return new ListDataHandler();
    }

    /**
     * The PropertyChangeListener that's added to the JList at
     * installUI time.  When the value of a JList property that
     * affects layout changes, we set a bit in updateLayoutStateNeeded.
     * If the JLists model changes we additionally remove our listeners
     * from the old model.  Likewise for the JList selectionModel.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     *
     * @see #maybeUpdateLayoutState
     * @see #createPropertyChangeListener
     * @see #installUI
     */
    public class PropertyChangeHandler extends BasicListUI.PropertyChangeHandler {

        public void propertyChange(PropertyChangeEvent e) {
            super.propertyChange(e);
            String propertyName = e.getPropertyName();
            if (propertyName.equals("columnsPerRow")) {
                updateLayoutStateNeeded |= columnsPerRowChanged;
                redrawList();
            }
        }
    }

    /**
     * Creates an instance of PropertyChangeHandler that's added to
     * the JList by installUI().  Subclasses can override this method
     * to return a custom PropertyChangeListener, e.g.
     * <pre>
     * class Basic2DListUI extends BasicListUI {
     *    protected PropertyChangeListener <b>createPropertyChangeListener</b>() {
     *        return new MyPropertyChangeListener();
     *    }
     *    public class MyPropertyChangeListener extends PropertyChangeHandler {
     *        public void propertyChange(PropertyChangeEvent e) {
     *            if (e.getPropertyName().equals("model")) {
     *                // do some extra work when the model changes
     *            }
     *            super.propertyChange(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @see PropertyChangeListener
     * @see #installUI
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return new PropertyChangeHandler();
    }

    /** Used by IncrementLeadSelectionAction. Indicates the action should
     * change the lead, and not select it. */
    private static final int CHANGE_LEAD = 0;

    /** Used by IncrementLeadSelectionAction. Indicates the action should
     * change the selection and lead. */
    private static final int CHANGE_SELECTION = 1;

    /** Used by IncrementLeadSelectionAction. Indicates the action should
     * extend the selection from the anchor to the next index. */
    private static final int EXTEND_SELECTION = 2;

    private static final int LEAD_UP = 0;

    private static final int LEAD_DOWN = 1;

    private static final int LEAD_LEFT = 2;

    private static final int LEAD_RIGHT = 3;

    /**
     * Action to increment the selection in the list up/down a row at
     * a type. This also has the option to extend the selection, or
     * only move the lead.
     */
    private static class IncrementLeadSelectionAction extends AbstractAction {

        /** Amount to offset, subclasses will define what this means. */
        protected int direction;

        /** One of CHANGE_LEAD, CHANGE_SELECTION or EXTEND_SELECTION. */
        protected int selectionType;

        protected IncrementLeadSelectionAction(String name, int type) {
            this(name, type, -1);
        }

        protected IncrementLeadSelectionAction(String name, int type, int direction) {
            super(name);
            this.direction = direction;
            this.selectionType = type;
        }

        /**
	 * Returns the next index to select. This is based on the lead
	 * selected index and the <code>direction</code> ivar.
	 */
        protected int getNextIndex(J2DList list) {
            int index = list.getLeadSelectionIndex();
            int size = list.getModel().getSize();
            if (index == -1) {
                if (size > 0) {
                    if (direction == LEAD_RIGHT || direction == LEAD_DOWN) {
                        index = 0;
                    } else {
                        index = size - 1;
                    }
                }
            } else {
                switch(direction) {
                    case LEAD_UP:
                        index -= list.getColumnsPerRow();
                        break;
                    case LEAD_DOWN:
                        index += list.getColumnsPerRow();
                        break;
                    case LEAD_LEFT:
                        index -= 1;
                        break;
                    case LEAD_RIGHT:
                        index += 1;
                        break;
                }
            }
            return index;
        }

        /**
	 * Ensures the particular index is visible. This simply forwards
	 * the method to list.
	 */
        protected void ensureIndexIsVisible(J2DList list, int index) {
            list.ensureIndexIsVisible(index);
        }

        /**
	 * Invokes <code>getNextIndex</code> to determine the next index
	 * to select. If the index is valid (not -1 and < size of the model),
	 * this will either: move the selection to the new index if
	 * the selectionType == CHANGE_SELECTION, move the lead to the
	 * new index if selectionType == CHANGE_LEAD, otherwise the
	 * selection is extend from the anchor to the new index and the
	 * lead is set to the new index.
	 */
        public void actionPerformed(ActionEvent e) {
            J2DList list = (J2DList) e.getSource();
            int old_index = list.getLeadSelectionIndex();
            int new_index = getNextIndex(list);
            int size = list.getModel().getSize();
            if (new_index < 0 && old_index > 0) {
                new_index = 0;
            }
            if (new_index >= size && old_index < (size - 1)) {
                new_index = size - 1;
            }
            if (new_index >= 0 && new_index < size) {
                ListSelectionModel lsm = list.getSelectionModel();
                if (selectionType == EXTEND_SELECTION) {
                    int anchor = lsm.getAnchorSelectionIndex();
                    if (anchor == -1) {
                        anchor = new_index;
                    }
                    list.setSelectionInterval(anchor, new_index);
                    lsm.setAnchorSelectionIndex(anchor);
                    lsm.setLeadSelectionIndex(new_index);
                } else if (selectionType == CHANGE_SELECTION) {
                    list.setSelectedIndex(new_index);
                } else {
                    lsm.setLeadSelectionIndex(new_index);
                }
                ensureIndexIsVisible(list, new_index);
            }
        }
    }

    /**
     * Action to move the selection to the first item in the list.
     */
    private static class HomeAction extends IncrementLeadSelectionAction {

        protected HomeAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(J2DList list) {
            return 0;
        }
    }

    /**
     * Action to move the selection to the last item in the list.
     */
    private static class EndAction extends IncrementLeadSelectionAction {

        protected EndAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(J2DList list) {
            return list.getModel().getSize() - 1;
        }
    }

    /**
     * Action to move up one page.
     */
    private static class PageUpAction extends IncrementLeadSelectionAction {

        protected PageUpAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(J2DList list) {
            ListSelectionModel lsm = list.getSelectionModel();
            int new_index = list.getFirstVisibleIndex();
            int old_index = lsm.getLeadSelectionIndex();
            int colPerRow = list.getColumnsPerRow();
            if (new_index != -1 && old_index != -1) {
                int old_column = old_index % colPerRow;
                int new_row = new_index / colPerRow;
                new_index = new_row * colPerRow + old_column;
            }
            if (old_index == new_index) {
                Rectangle visRect = list.getVisibleRect();
                visRect.y = Math.max(0, visRect.y - visRect.height);
                new_index = list.locationToIndex(visRect.getLocation());
            }
            return new_index;
        }

        protected void ensureIndexIsVisible(J2DList list, int index) {
            Rectangle visRect = list.getVisibleRect();
            Rectangle cellBounds = list.getCellBounds(index, index);
            cellBounds.height = visRect.height;
            list.scrollRectToVisible(cellBounds);
        }
    }

    /**
     * Action to move down one page.
     */
    private static class PageDownAction extends IncrementLeadSelectionAction {

        protected PageDownAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(J2DList list) {
            ListSelectionModel lsm = list.getSelectionModel();
            int new_index = list.getFirstIndexOnLastVisibleRow();
            int old_index = lsm.getLeadSelectionIndex();
            int colPerRow = list.getColumnsPerRow();
            if (new_index != -1 && old_index != -1) {
                int old_column = old_index % colPerRow;
                int new_row = new_index / colPerRow;
                new_index = new_row * colPerRow + old_column;
            }
            if (new_index == -1) {
                new_index = list.getModel().getSize() - 1;
            }
            if (old_index == new_index) {
                Rectangle visRect = list.getVisibleRect();
                visRect.y += visRect.height + visRect.height - 1;
                new_index = list.locationToIndex(visRect.getLocation());
                if (new_index == -1) {
                    new_index = list.getModel().getSize() - 1;
                }
            }
            return new_index;
        }

        protected void ensureIndexIsVisible(J2DList list, int index) {
            Rectangle visRect = list.getVisibleRect();
            Rectangle cellBounds = list.getCellBounds(index, index);
            cellBounds.y = Math.max(0, cellBounds.y + cellBounds.height - visRect.height);
            cellBounds.height = visRect.height;
            list.scrollRectToVisible(cellBounds);
        }
    }

    /**
     * Action to select all the items in the list.
     */
    private static class SelectAllAction extends AbstractAction {

        private SelectAllAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            J2DList list = (J2DList) e.getSource();
            list.setSelectionInterval(0, list.getModel().getSize() - 1);
        }
    }

    /**
     * Action to clear the selection in the list.
     */
    private static class ClearSelectionAction extends AbstractAction {

        private ClearSelectionAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            J2DList list = (J2DList) e.getSource();
            list.clearSelection();
        }
    }
}
