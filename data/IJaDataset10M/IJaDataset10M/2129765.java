package org.plazmaforge.framework.client.swt.controls;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.plazmaforge.framework.client.swt.views.ITableCellRenderer;

public class XTableCursor extends Canvas {

    Table table;

    TableItem row = null;

    TableColumn column = null;

    Listener tableListener, resizeListener, disposeItemListener, disposeColumnListener;

    static final int BACKGROUND = SWT.COLOR_LIST_SELECTION_TEXT;

    static final int FOREGROUND = SWT.COLOR_LIST_SELECTION;

    /**
 * Constructs a new instance of this class given its parent
 * table and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a Table control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#BORDER
 * @see Widget#checkSubclass()
 * @see Widget#getStyle()
 */
    public XTableCursor(Table parent, int style) {
        super(parent, style);
        table = parent;
        setBackground(null);
        setForeground(null);
        Listener listener = new Listener() {

            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.Dispose:
                        dispose(event);
                        break;
                    case SWT.FocusIn:
                    case SWT.FocusOut:
                        redraw();
                        break;
                    case SWT.KeyDown:
                        keyDown(event);
                        break;
                    case SWT.Paint:
                        paint(event);
                        break;
                    case SWT.Traverse:
                        traverse(event);
                        break;
                }
            }
        };
        int[] events = new int[] { SWT.Dispose, SWT.FocusIn, SWT.FocusOut, SWT.KeyDown, SWT.Paint, SWT.Traverse };
        for (int i = 0; i < events.length; i++) {
            addListener(events[i], listener);
        }
        tableListener = new Listener() {

            public void handleEvent(Event event) {
                switch(event.type) {
                    case SWT.MouseDown:
                        tableMouseDown(event);
                        break;
                    case SWT.FocusIn:
                        tableFocusIn(event);
                        break;
                }
            }
        };
        table.addListener(SWT.FocusIn, tableListener);
        table.addListener(SWT.MouseDown, tableListener);
        disposeItemListener = new Listener() {

            public void handleEvent(Event event) {
                row = null;
                column = null;
                _resize();
            }
        };
        disposeColumnListener = new Listener() {

            public void handleEvent(Event event) {
                row = null;
                column = null;
                _resize();
            }
        };
        resizeListener = new Listener() {

            public void handleEvent(Event event) {
                _resize();
            }
        };
        ScrollBar hBar = table.getHorizontalBar();
        if (hBar != null) {
            hBar.addListener(SWT.Selection, resizeListener);
        }
        ScrollBar vBar = table.getVerticalBar();
        if (vBar != null) {
            vBar.addListener(SWT.Selection, resizeListener);
        }
    }

    /**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see SelectionEvent
 * @see #removeSelectionListener(SelectionListener)
 * 
 */
    public void addSelectionListener(SelectionListener listener) {
        checkWidget();
        if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
        TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Selection, typedListener);
        addListener(SWT.DefaultSelection, typedListener);
    }

    void dispose(Event event) {
        table.removeListener(SWT.FocusIn, tableListener);
        table.removeListener(SWT.MouseDown, tableListener);
        if (column != null) {
            column.removeListener(SWT.Dispose, disposeColumnListener);
            column.removeListener(SWT.Move, resizeListener);
            column.removeListener(SWT.Resize, resizeListener);
            column = null;
        }
        if (row != null) {
            row.removeListener(SWT.Dispose, disposeItemListener);
            row = null;
        }
        ScrollBar hBar = table.getHorizontalBar();
        if (hBar != null) {
            hBar.removeListener(SWT.Selection, resizeListener);
        }
        ScrollBar vBar = table.getVerticalBar();
        if (vBar != null) {
            vBar.removeListener(SWT.Selection, resizeListener);
        }
    }

    void keyDown(Event event) {
        if (row == null) return;
        switch(event.character) {
            case SWT.CR:
                notifyListeners(SWT.DefaultSelection, new Event());
                return;
        }
        int rowIndex = table.indexOf(row);
        int columnIndex = column == null ? 0 : table.indexOf(column);
        switch(event.keyCode) {
            case SWT.ARROW_UP:
                setRowColumn(Math.max(0, rowIndex - 1), columnIndex, true);
                break;
            case SWT.ARROW_DOWN:
                setRowColumn(Math.min(rowIndex + 1, table.getItemCount() - 1), columnIndex, true);
                break;
            case SWT.ARROW_LEFT:
            case SWT.ARROW_RIGHT:
                {
                    int columnCount = table.getColumnCount();
                    if (columnCount == 0) break;
                    int[] order = table.getColumnOrder();
                    int index = 0;
                    while (index < order.length) {
                        if (order[index] == columnIndex) break;
                        index++;
                    }
                    if (index == order.length) index = 0;
                    int leadKey = (getStyle() & SWT.RIGHT_TO_LEFT) != 0 ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
                    if (event.keyCode == leadKey) {
                        setRowColumn(rowIndex, order[Math.max(0, index - 1)], true);
                    } else {
                        setRowColumn(rowIndex, order[Math.min(columnCount - 1, index + 1)], true);
                    }
                    break;
                }
            case SWT.HOME:
                setRowColumn(0, columnIndex, true);
                break;
            case SWT.END:
                {
                    int i = table.getItemCount() - 1;
                    setRowColumn(i, columnIndex, true);
                    break;
                }
            case SWT.PAGE_UP:
                {
                    int index = table.getTopIndex();
                    if (index == rowIndex) {
                        Rectangle rect = table.getClientArea();
                        TableItem item = table.getItem(index);
                        Rectangle itemRect = item.getBounds(0);
                        rect.height -= itemRect.y;
                        int height = table.getItemHeight();
                        int page = Math.max(1, rect.height / height);
                        index = Math.max(0, index - page + 1);
                    }
                    setRowColumn(index, columnIndex, true);
                    break;
                }
            case SWT.PAGE_DOWN:
                {
                    int index = table.getTopIndex();
                    Rectangle rect = table.getClientArea();
                    TableItem item = table.getItem(index);
                    Rectangle itemRect = item.getBounds(0);
                    rect.height -= itemRect.y;
                    int height = table.getItemHeight();
                    int page = Math.max(1, rect.height / height);
                    int end = table.getItemCount() - 1;
                    index = Math.min(end, index + page - 1);
                    if (index == rowIndex) {
                        index = Math.min(end, index + page - 1);
                    }
                    setRowColumn(index, columnIndex, true);
                    break;
                }
        }
    }

    protected Image getItemImage(TableItem item, int columnIndex) {
        return (Image) item.getData(ITableCellRenderer.CELL_IMAGE + columnIndex);
    }

    void paint(Event event) {
        if (row == null) return;
        int columnIndex = column == null ? 0 : table.indexOf(column);
        GC gc = event.gc;
        Display display = getDisplay();
        gc.setBackground(getBackground());
        gc.setForeground(getForeground());
        gc.fillRectangle(event.x, event.y, event.width, event.height);
        int x = 0;
        Point size = getSize();
        Image image = row.getImage(columnIndex);
        boolean isCellImage = false;
        if (image != null) {
            Rectangle imageSize = image.getBounds();
            int imageY = (size.y - imageSize.height) / 2;
            gc.drawImage(image, x, imageY);
            x += imageSize.width;
        } else {
            image = getItemImage(row, columnIndex);
            if (image != null) {
                isCellImage = true;
                Rectangle imageSize = image.getBounds();
                int imageX = (size.x - imageSize.width) / 2;
                int imageY = (size.y - imageSize.height) / 2;
                gc.drawImage(image, imageX, imageY);
            }
        }
        String text = row.getText(columnIndex);
        if (!isCellImage && text != "") {
            Rectangle bounds = row.getBounds(columnIndex);
            Point extent = gc.stringExtent(text);
            String platform = SWT.getPlatform();
            if ("win32".equals(platform)) {
                if (table.getColumnCount() == 0 || columnIndex == 0) {
                    x += 2;
                } else {
                    int alignmnent = column.getAlignment();
                    switch(alignmnent) {
                        case SWT.LEFT:
                            x += 6;
                            break;
                        case SWT.RIGHT:
                            x = bounds.width - extent.x - 6;
                            break;
                        case SWT.CENTER:
                            x += (bounds.width - x - extent.x) / 2;
                            break;
                    }
                }
            } else {
                if (table.getColumnCount() == 0) {
                    x += 5;
                } else {
                    int alignmnent = column.getAlignment();
                    switch(alignmnent) {
                        case SWT.LEFT:
                            x += 5;
                            break;
                        case SWT.RIGHT:
                            x = bounds.width - extent.x - 2;
                            break;
                        case SWT.CENTER:
                            x += (bounds.width - x - extent.x) / 2 + 2;
                            break;
                    }
                }
            }
            int textY = (size.y - extent.y) / 2;
            gc.drawString(text, x, textY);
        }
        if (isFocusControl()) {
            gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
            gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
            gc.drawFocus(0, 0, size.x, size.y);
        }
    }

    void tableFocusIn(Event event) {
        if (isDisposed()) return;
        if (isVisible()) setFocus();
    }

    void tableMouseDown(Event event) {
        if (isDisposed() || !isVisible()) return;
        Point pt = new Point(event.x, event.y);
        int lineWidth = table.getLinesVisible() ? table.getGridLineWidth() : 0;
        TableItem item = table.getItem(pt);
        if ((table.getStyle() & SWT.FULL_SELECTION) != 0) {
            if (item == null) return;
        } else {
            int start = item != null ? table.indexOf(item) : table.getTopIndex();
            int end = table.getItemCount();
            Rectangle clientRect = table.getClientArea();
            for (int i = start; i < end; i++) {
                TableItem nextItem = table.getItem(i);
                Rectangle rect = nextItem.getBounds(0);
                if (pt.y >= rect.y && pt.y < rect.y + rect.height + lineWidth) {
                    item = nextItem;
                    break;
                }
                if (rect.y > clientRect.y + clientRect.height) return;
            }
            if (item == null) return;
        }
        TableColumn newColumn = null;
        int columnCount = table.getColumnCount();
        if (columnCount > 0) {
            for (int i = 0; i < columnCount; i++) {
                Rectangle rect = item.getBounds(i);
                rect.width += lineWidth;
                rect.height += lineWidth;
                if (rect.contains(pt)) {
                    newColumn = table.getColumn(i);
                    break;
                }
            }
            if (newColumn == null) {
                newColumn = table.getColumn(0);
            }
        }
        setRowColumn(item, newColumn, true);
        setFocus();
        return;
    }

    void traverse(Event event) {
        switch(event.detail) {
            case SWT.TRAVERSE_ARROW_NEXT:
            case SWT.TRAVERSE_ARROW_PREVIOUS:
            case SWT.TRAVERSE_RETURN:
                event.doit = false;
                return;
        }
        event.doit = true;
    }

    void setRowColumn(int row, int column, boolean notify) {
        TableItem item = row == -1 ? null : table.getItem(row);
        TableColumn col = column == -1 || table.getColumnCount() == 0 ? null : table.getColumn(column);
        setRowColumn(item, col, notify);
    }

    void setRowColumn(TableItem row, TableColumn column, boolean notify) {
        if (this.row == row && this.column == column) {
            return;
        }
        if (this.row != null && this.row != row) {
            this.row.removeListener(SWT.Dispose, disposeItemListener);
            this.row = null;
        }
        if (this.column != null && this.column != column) {
            this.column.removeListener(SWT.Dispose, disposeColumnListener);
            this.column.removeListener(SWT.Move, resizeListener);
            this.column.removeListener(SWT.Resize, resizeListener);
            this.column = null;
        }
        if (row != null) {
            if (this.row != row) {
                this.row = row;
                row.addListener(SWT.Dispose, disposeItemListener);
                table.showItem(row);
            }
            if (this.column != column && column != null) {
                this.column = column;
                column.addListener(SWT.Dispose, disposeColumnListener);
                column.addListener(SWT.Move, resizeListener);
                column.addListener(SWT.Resize, resizeListener);
                table.showColumn(column);
            }
            int columnIndex = column == null ? 0 : table.indexOf(column);
            setBounds(row.getBounds(columnIndex));
            redraw();
            if (notify) {
                notifyListeners(SWT.Selection, new Event());
            }
        }
    }

    public void setVisible(boolean visible) {
        checkWidget();
        if (visible) _resize();
        super.setVisible(visible);
    }

    /**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's selection changes.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener(SelectionListener)
 * 
 * @since 3.0
 */
    public void removeSelectionListener(SelectionListener listener) {
        checkWidget();
        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        removeListener(SWT.Selection, listener);
        removeListener(SWT.DefaultSelection, listener);
    }

    void _resize() {
        if (row == null) {
            setBounds(-200, -200, 0, 0);
        } else {
            int columnIndex = column == null ? 0 : table.indexOf(column);
            setBounds(row.getBounds(columnIndex));
        }
    }

    /**
 * Returns the column over which the TableCursor is positioned.
 *
 * @return the column for the current position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
    public int getColumn() {
        checkWidget();
        return column == null ? 0 : table.indexOf(column);
    }

    /**
 * Returns the row over which the TableCursor is positioned.
 *
 * @return the item for the current position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
    public TableItem getRow() {
        checkWidget();
        return row;
    }

    public void setBackground(Color color) {
        if (color == null) color = getDisplay().getSystemColor(BACKGROUND);
        super.setBackground(color);
        redraw();
    }

    public void setForeground(Color color) {
        if (color == null) color = getDisplay().getSystemColor(FOREGROUND);
        super.setForeground(color);
        redraw();
    }

    /**
 * Positions the TableCursor over the cell at the given row and column in the parent table. 
 *
 * @param row the index of the row for the cell to select
 * @param column the index of column for the cell to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
    public void setSelection(int row, int column) {
        checkWidget();
        int columnCount = table.getColumnCount();
        int maxColumnIndex = columnCount == 0 ? 0 : columnCount - 1;
        if (row < 0 || row >= table.getItemCount() || column < 0 || column > maxColumnIndex) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        setRowColumn(row, column, false);
    }

    /**
 * Positions the TableCursor over the cell at the given row and column in the parent table. 
 *
 * @param row the TableItem of the row for the cell to select
 * @param column the index of column for the cell to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
    public void setSelection(TableItem row, int column) {
        checkWidget();
        int columnCount = table.getColumnCount();
        int maxColumnIndex = columnCount == 0 ? 0 : columnCount - 1;
        if (row == null || row.isDisposed() || column < 0 || column > maxColumnIndex) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        setRowColumn(table.indexOf(row), column, false);
    }
}
