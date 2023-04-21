package org.butu.gui.widgets.table;

import org.butu.core.eclipse.BUTU;
import org.butu.core.listeners.BEventListeners;
import org.butu.core.listeners.BListeners;
import org.butu.gui.widgets.Columns;
import org.butu.gui.widgets.CursorableProvider;
import org.butu.gui.widgets.IColumnable;
import org.butu.gui.widgets.ICursorable;
import org.butu.gui.widgets.Columns.ColumnInfo;
import org.butu.gui.widgets.table.TableActionListener.TableActionEvent;
import org.butu.gui.widgets.table.TableEditListener.EditEvent;
import org.butu.gui.widgets.table.TableListener.TableEvent;
import org.butu.mapped.IMappedTableRow;
import org.butu.mapped.MappedTable;
import org.butu.utils.LogicUtils;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TColumn;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class CursorTableViewer extends CheckboxTableViewer implements IColumnable, ICursorable {

    public static int NONE = 0;

    public static int MIN = 1;

    public static int CHECKED = 2;

    public static int PICS = 4;

    public static enum Editing {

        none, cells, rows
    }

    private Editing defaultEditing = Editing.none;

    private IRowEditor rowEditor;

    private CursorableProvider cProvider;

    private MappedTable<?> input;

    private Point cursorPos = new Point(1, -1);

    private int minRow;

    private int style;

    private TableColumn firstColumn;

    private TableItem currentItem;

    private Columns columns = new Columns(this);

    private TableItem item;

    private boolean flag;

    private boolean skate;

    private BListeners<ICheckStateListener> checkListeners = new BListeners<ICheckStateListener>(ICheckStateListener.class);

    private BEventListeners<TableEditListener> editListeners = new BEventListeners<TableEditListener>(TableEditListener.class);

    private BEventListeners<TableListener> tableListeners = new BEventListeners<TableListener>(TableListener.class);

    private BListeners<KeyListener> keyListeners = new BListeners<KeyListener>(KeyListener.class);

    private BEventListeners<TableActionListener> actionListeners = new BEventListeners<TableActionListener>(TableActionListener.class);

    public CursorTableViewer(Table table, int style) {
        super(table);
        style = LogicUtils.copyBit(style, CHECKED, table.getStyle(), SWT.CHECK);
        this.style = style;
        boolean checked = LogicUtils.ande(style, CHECKED);
        int width = 0;
        if (LogicUtils.ande(style, PICS | CHECKED)) {
            width = 50;
        } else if (checked) {
            width = 32;
        } else if (LogicUtils.ande(style, PICS)) {
            width = 31;
        } else {
            width = 15;
        }
        if (LogicUtils.ande(style, MIN)) {
            width -= 7;
        }
        firstColumn = new TableColumn(table, SWT.NONE);
        firstColumn.setWidth(width);
        firstColumn.setResizable(false);
        if (checked) {
            firstColumn.setToolTipText("�������� ���������/�������� ���");
            table.addMouseListener(new MouseAdapter() {

                public void mouseDown(MouseEvent e) {
                    if (e.button == 1) {
                        Point pt = new Point(e.x, e.y);
                        item = getTable().getItem(pt);
                        if (item != null) {
                            skate = true;
                            flag = item.getChecked();
                            getTable().addMouseMoveListener(getMoveListener());
                        }
                    }
                }

                public void mouseUp(MouseEvent e) {
                    if (e.button == 1) {
                        if (skate) {
                            getTable().removeMouseMoveListener(getMoveListener());
                            skate = false;
                        }
                    }
                }
            });
            table.addMouseTrackListener(new MouseTrackAdapter() {

                public void mouseExit(MouseEvent e) {
                    if (skate) {
                        getTable().removeMouseMoveListener(getMoveListener());
                        skate = false;
                    }
                }
            });
            firstColumn.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    boolean state = getCheckedElements().length == 0;
                    setAllChecked(state);
                    notifyCheck(item, state);
                }
            });
        }
        table.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                keyListeners.notify("keyPressed", e);
                if (!e.doit) return;
                Table table = getTable();
                if (e.keyCode == SWT.ARROW_RIGHT) {
                    if (cursorPos.x < table.getColumnCount() - 1) {
                        currentItem.setBackground(cursorPos.x, getBgColor());
                        cursorPos.x++;
                        currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                        fireSelectionChanged(currentItem);
                    }
                } else if (e.keyCode == SWT.ARROW_LEFT) {
                    if (cursorPos.x > 1) {
                        currentItem.setBackground(cursorPos.x, getBgColor());
                        cursorPos.x--;
                        currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                        fireSelectionChanged(currentItem);
                    }
                } else if (e.keyCode == SWT.ARROW_UP) {
                    if (cursorPos.y > minRow) {
                        currentItem.setBackground(cursorPos.x, getBgColor());
                        cursorPos.y--;
                        currentItem = table.getItem(cursorPos.y);
                        currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                    } else {
                        e.doit = false;
                    }
                } else if (e.keyCode == SWT.ARROW_DOWN) {
                    if (cursorPos.y < table.getItemCount() - 1) {
                        currentItem.setBackground(cursorPos.x, getBgColor());
                        cursorPos.y++;
                        currentItem = table.getItem(cursorPos.y);
                        currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                    }
                } else if (e.keyCode == SWT.PAGE_DOWN) {
                    int shift = getVisibleItemCount() - 1;
                    int newIndex = table.getTopIndex() + shift;
                    if (cursorPos.y == newIndex) {
                        newIndex += shift;
                    }
                    newIndex = Math.min(newIndex, table.getItemCount() - 1);
                    currentItem.setBackground(cursorPos.x, getBgColor());
                    cursorPos.y = newIndex;
                    currentItem = table.getItem(cursorPos.y);
                    currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                } else if (e.keyCode == SWT.PAGE_UP) {
                    int newIndex = table.getTopIndex();
                    if (cursorPos.y == newIndex) {
                        newIndex -= getVisibleItemCount() - 1;
                    }
                    if (newIndex < minRow) {
                        newIndex = minRow;
                        e.doit = false;
                        moveTo(newIndex);
                    } else {
                        currentItem.setBackground(cursorPos.x, getBgColor());
                        cursorPos.y = newIndex;
                        currentItem = table.getItem(cursorPos.y);
                        currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                    }
                } else if (e.keyCode == SWT.END) {
                    currentItem.setBackground(cursorPos.x, getBgColor());
                    cursorPos.y = table.getItemCount() - 1;
                    currentItem = table.getItem(cursorPos.y);
                    currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                } else if (e.keyCode == SWT.HOME) {
                    e.doit = false;
                    moveTo(minRow);
                } else if (e.character != 0 && !BUTU.isEscapeChar(e.character)) {
                    tableListeners.notify("beginLookup", new TableEvent(getCurrentColumn(), e.character));
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent event) {
                Point pt = new Point(event.x, event.y);
                Table table = getTable();
                int lineWidth = table.getLinesVisible() ? table.getGridLineWidth() : 0;
                int newX = 0;
                int newY = cursorPos.y;
                TableItem newItem = table.getItem(pt);
                if (newItem == null) {
                    int start = table.getTopIndex();
                    int end = table.getItemCount();
                    Rectangle clientRect = table.getClientArea();
                    for (int i = start; i < end; i++) {
                        TableItem nextItem = table.getItem(i);
                        Rectangle rect = nextItem.getBounds(0);
                        if (pt.y >= rect.y && pt.y < rect.y + rect.height + lineWidth) {
                            newItem = nextItem;
                            newY = i;
                            break;
                        }
                        if (rect.y > clientRect.y + clientRect.height) {
                            return;
                        }
                    }
                    if (newItem != null) {
                        int columnCount = table.getColumnCount();
                        if (columnCount > 0) {
                            for (int i = 0; i < columnCount; i++) {
                                Rectangle rect = newItem.getBounds(i);
                                rect.width += lineWidth;
                                rect.height += lineWidth;
                                if (rect.contains(pt)) {
                                    newX = i;
                                    break;
                                }
                            }
                        }
                        moveTo(newX - 1, java.lang.Math.max(newY, minRow));
                    }
                } else {
                    moveTo(java.lang.Math.max(table.indexOf(newItem), minRow));
                }
            }
        });
    }

    private Color getBgColor() {
        return cProvider.doGetBackground(currentItem.getData(), cursorPos.x);
    }

    /**
     * ������ ����� ���������� � ����� �� �������� ������������, ������� ������ �������� � ������
     * ��������������. ����� ���������� ����� ��� �������������� �������� (�������������� ������
     * ��� �������������� ������), ����� ����� �������� ������������, ����� �������� ���������������
     * ����� (���� ������� ���������). ����� ������ ������, ���� �������������� ���� �����������,
     * ������������ ����������� ����������.
     * @param element
     * @param column
     */
    protected final void tryToEdit(Object element, int column) {
        EditEvent event = new EditEvent();
        event.row = (IMappedTableRow) element;
        event.columnInfo = getCurrentColumn();
        editListeners.notify("beforEdit", event);
        if (event.doIt) {
            if (defaultEditing == Editing.cells) {
                editElement(element, column);
            } else if (defaultEditing == Editing.rows) {
                editRow(element, event);
            }
        }
    }

    /**
     * ��������� �������������� ��������� ������ �������, ���� ��� ��������.
     * @param row
     */
    public final void editRow(Object row, EditEvent event) {
        if (rowEditor != null) {
            if (rowEditor.edit(row)) {
                editListeners.notify("afterRowEdit", event);
                if (!event.preventRefresh) {
                    update(row, null);
                }
            }
        }
    }

    /**
     * ��������� �������������� ������� ������ �������, ���� ��� ��������.
     */
    public final void editRow() {
        EditEvent event = new EditEvent();
        event.row = (IMappedTableRow) getCurrentRow();
        event.columnInfo = getCurrentColumn();
        if (event.row != null) {
            editRow(event.row, event);
        }
    }

    /**
     * @return ������ ColumnInfo, ��������������� ������� �������� ������� �������.
     */
    protected final ColumnInfo getCurrentColumn() {
        return columns.get((String) getColumnProperties()[cursorPos.x]);
    }

    public Object getCurrentRow() {
        if (currentItem != null && !currentItem.isDisposed()) {
            return currentItem.getData();
        } else {
            return null;
        }
    }

    protected void hookEditingSupport(Control control) {
        control.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                if (tryAction()) {
                    tryToEdit(getTable().getItem(cursorPos.y).getData(), cursorPos.x);
                }
            }
        });
        control.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.character == SWT.CR) {
                    if (tryAction()) {
                        tryToEdit(getTable().getItem(cursorPos.y).getData(), cursorPos.x);
                    }
                } else if (e.keyCode == SWT.F4) {
                    tryToEdit(getTable().getItem(cursorPos.y).getData(), cursorPos.x);
                }
            }
        });
    }

    private boolean tryAction() {
        if (!actionListeners.isEmpty()) {
            TableActionEvent event = new TableActionEvent();
            event.row = (IMappedTableRow) getCurrentRow();
            event.columnInfo = getCurrentColumn();
            actionListeners.notify("action", event);
            return event.doIt;
        }
        return true;
    }

    private MouseMoveListener moveListener;

    private MouseMoveListener getMoveListener() {
        if (moveListener == null) {
            moveListener = new MouseMoveListener() {

                public void mouseMove(MouseEvent e) {
                    Point pt = new Point(e.x, e.y);
                    TableItem newItem = getTable().getItem(pt);
                    if (newItem != item && newItem.getChecked() != flag) {
                        item = newItem;
                        item.setChecked(flag);
                        notifyCheck(item, flag);
                        moveTo(getTable().indexOf(newItem));
                    }
                }
            };
        }
        return moveListener;
    }

    protected void fireSelectionChanged(SelectionChangedEvent event) {
        IMappedTableRow selection = null;
        if (!currentItem.isDisposed()) {
            selection = (IMappedTableRow) currentItem.getData();
        }
        super.fireSelectionChanged(new SelectionChangedEvent(this, new CursorTableSelection(selection, columns.getColumnAt(cursorPos.x))));
    }

    private void fireSelectionChanged(TableItem item) {
        IMappedTableRow sel = (IMappedTableRow) ((item == null || item.isDisposed()) ? null : item.getData());
        super.fireSelectionChanged(new SelectionChangedEvent(this, new CursorTableSelection(sel, columns.getColumnAt(cursorPos.x))));
    }

    public void moveTo(int y) {
        if (cursorPos.y != y) {
            Table table = getTable();
            int itemCount = table.getItemCount();
            if (itemCount > 0) {
                if (y >= minRow && y < itemCount) {
                    if (currentItem != null && !currentItem.isDisposed() && cursorPos.x > -1) {
                        currentItem.setBackground(cursorPos.x, cProvider.doGetBackground(currentItem.getData(), cursorPos.x));
                    }
                    cursorPos.y = y;
                    currentItem = table.getItem(y);
                    currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
                    table.setSelection(y);
                }
            } else {
                cursorPos.y = -1;
                currentItem = null;
            }
            fireSelectionChanged(currentItem);
        }
    }

    public void moveTo(int x, int y) {
        if ((cursorPos.x - 1) != x || cursorPos.y != y) {
            if (cursorPos.x > -1 && currentItem != null && !currentItem.isDisposed()) {
                currentItem.setBackground(cursorPos.x, cProvider.doGetBackground(currentItem.getData(), cursorPos.x));
            }
            if (x >= 0) {
                cursorPos.x = x + 1;
            }
            cursorPos.y = y;
            currentItem = getTable().getItem(y);
            currentItem.setBackground(cursorPos.x, cProvider.getCursorColor());
            getTable().setSelection(y);
            fireSelectionChanged(currentItem);
        }
    }

    Point getCursorPos() {
        return cursorPos;
    }

    public void removeRow(int rowIndex) {
        boolean isLast = rowIndex == input.size() - 1;
        if (rowIndex > -1) {
            input.remove(rowIndex);
            refresh();
            if (isLast) {
                cursorPos.y = -1;
                moveTo(rowIndex - 1);
            } else {
                getTable().select(rowIndex);
            }
        }
    }

    public void removeRow() {
        removeRow(cursorPos.y);
    }

    public void setInput(MappedTable<?> input) {
        this.input = input;
        super.setInput(input);
    }

    protected void inputChanged(Object input, Object oldInput) {
        super.inputChanged(input, oldInput);
        int itemCount = getTable().getItemCount();
        if (itemCount > 0) {
            if (oldInput == null) {
                moveTo(0, 0);
            } else if (itemCount <= cursorPos.y) {
                moveTo(itemCount - 1);
            } else {
                currentItem = getTable().getItem(cursorPos.y);
                getTable().setSelection(cursorPos.y);
            }
        } else {
            currentItem = null;
        }
    }

    public void setLabelProvider(IBaseLabelProvider labelProvider) {
        if (cProvider == null || cProvider.getParent() != labelProvider) {
            if (labelProvider instanceof ITableLabelProvider) {
                cProvider = new CursorableProvider((ITableLabelProvider) labelProvider, this);
                super.setLabelProvider(cProvider);
            }
        }
    }

    public boolean isCurrentRow(Object row) {
        if (currentItem != null && !currentItem.isDisposed() && currentItem.getData() != null) {
            return currentItem.getData().equals(row);
        }
        return false;
    }

    public boolean isCurrentCol(int col) {
        return cursorPos.x == col;
    }

    /**
     * @return ���������� ��������� ������� ����� �������.
     */
    public int getVisibleItemCount() {
        Table table = getTable();
        return (table.getClientArea().height - table.getHeaderHeight()) / table.getItemHeight();
    }

    /**
     * ����������� ������ �������, ����������� ��� ���������� ���������� ���������� �����.
     * @param rowCount �������� ���������� ������� ����� � �������.
     * @return ������������� ������ �������, ��� ������� ������ ���������� ����� ����� ���� ���������.
     */
    public int computeHeight(int rowCount) {
        Table table = getTable();
        int height = table.getHeaderVisible() ? table.getHeaderHeight() : 0;
        height += table.getItemHeight() * rowCount;
        return height;
    }

    public TableItem findItem(IMappedTableRow row) {
        return (TableItem) super.findItem(row);
    }

    public TColumn createColumn(int style) {
        return new TableColumn(getTable(), style);
    }

    public TColumn createColumn(int style, int index) {
        return new TableColumn(getTable(), style, index);
    }

    public int getCurrentColIndex() {
        return cursorPos.x - 1;
    }

    public int getCurrentRowIndex() {
        return cursorPos.y;
    }

    public int getStyle() {
        return style;
    }

    public void addCheckStateListener(ICheckStateListener listener) {
        checkListeners.addListener(listener);
    }

    public void removeCheckStateListener(ICheckStateListener listener) {
        checkListeners.removeListener(listener);
    }

    private void notifyCheck(Object item, boolean state) {
        checkListeners.notify("checkStateChanged", new CheckStateChangedEvent(this, item, flag));
    }

    public void addEditListener(TableEditListener listener) {
        editListeners.addListener(listener);
    }

    public void removeEditListener(TableEditListener listener) {
        editListeners.removeListener(listener);
    }

    public void addActionListener(TableActionListener listener) {
        actionListeners.addListener(listener);
    }

    public void removeActionListener(TableActionListener listener) {
        actionListeners.removeListener(listener);
    }

    public void addTableListener(TableListener listener) {
        tableListeners.addListener(listener);
    }

    public void removeTableListener(TableListener listener) {
        tableListeners.removeListener(listener);
    }

    public void addKeyListener(KeyListener listener) {
        keyListeners.addListener(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.removeListener(listener);
    }

    public Editing getDefaultEditing() {
        return defaultEditing;
    }

    public void setDefaultEditing(Editing defaultEditing) {
        this.defaultEditing = defaultEditing;
    }

    public IRowEditor getRowEditor() {
        return rowEditor;
    }

    public void setRowEditor(IRowEditor rowEditor) {
        this.rowEditor = rowEditor;
    }

    private boolean rdRedraw = true;

    private int rdStartCount;

    private int rdIndex;

    private TableItem rdStartItem;

    public void setRedraw(boolean redraw) {
        Table table = getTable();
        if (!redraw) {
            if (rdRedraw) {
                rdRedraw = false;
                rdStartCount = table.getItemCount();
                rdIndex = getCurrentRowIndex();
                if (rdIndex >= 0) {
                    if (rdStartCount > 0) {
                        rdStartItem = table.getItem(rdIndex);
                    } else {
                        rdStartItem = null;
                    }
                } else {
                    rdStartItem = null;
                }
            }
        } else if (!rdRedraw) {
            rdRedraw = true;
            if (rdStartCount != table.getItemCount()) {
                if (table.getItemCount() > 0) {
                    moveTo(minRow);
                } else {
                    cursorPos.y = -1;
                    currentItem = null;
                    fireSelectionChanged(currentItem);
                }
            } else {
                TableItem finishItem = (rdIndex >= 0 && rdIndex < table.getItemCount()) ? table.getItem(rdIndex) : null;
                if (rdStartItem != finishItem) {
                    moveTo(minRow);
                } else {
                    moveTo(rdIndex);
                }
            }
        }
        table.setRedraw(redraw);
    }

    /**
     * ������ ���������� �������
     */
    public void refresh() {
        setRedraw(false);
        super.refresh();
        setRedraw(true);
    }

    public Columns getColumns() {
        return columns;
    }

    public int getMinRow() {
        return minRow;
    }

    public void setMinRow(int minRow) {
        this.minRow = minRow;
        if (minRow > cursorPos.y) {
            moveTo(minRow);
        }
    }
}
