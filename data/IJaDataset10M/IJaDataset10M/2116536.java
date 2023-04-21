package org.xulbooster.eclipse.xb.ui.editors.svg.tabletree;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.xulbooster.eclipse.xb.ui.editors.svg.tabletree.SVGEditorMessages;

public class TreeExtension implements PaintListener {

    protected Tree fTree;

    protected EditManager editManager;

    protected String[] fColumnProperties;

    protected ICellModifier cellModifier;

    protected int columnPosition = 300;

    protected int columnHitWidth = 5;

    protected Color tableLineColor;

    protected int controlWidth;

    protected DelayedDrawTimer delayedDrawTimer;

    private boolean fisUnsupportedInput = false;

    public TreeExtension(Tree tree) {
        this.fTree = tree;
        InternalMouseListener listener = new InternalMouseListener();
        tree.addMouseMoveListener(listener);
        tree.addMouseListener(listener);
        tree.addPaintListener(this);
        editManager = new EditManager(tree);
        delayedDrawTimer = new DelayedDrawTimer(tree);
        tableLineColor = tree.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
    }

    public void dispose() {
        tableLineColor.dispose();
    }

    public void setCellModifier(ICellModifier modifier) {
        cellModifier = modifier;
    }

    public void resetCachedData() {
    }

    public ICellModifier getCellModifier() {
        return cellModifier;
    }

    public List getItemList() {
        List list = new Vector();
        getItemListHelper(fTree.getItems(), list);
        return list;
    }

    protected void getItemListHelper(TreeItem[] items, List list) {
        for (int i = 0; i < items.length; i++) {
            TreeItem item = items[i];
            list.add(item);
            getItemListHelper(item.getItems(), list);
        }
    }

    protected TreeItem getTreeItemOnRow(int px, int py) {
        TreeItem result = null;
        List list = getItemList();
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            TreeItem item = (TreeItem) i.next();
            Rectangle r = item.getBounds();
            if ((r != null) && (px >= r.x) && (py >= r.y) && (py <= r.y + r.height)) {
                result = item;
            }
        }
        return result;
    }

    protected class InternalMouseListener extends MouseAdapter implements MouseMoveListener {

        protected int columnDragged = -1;

        protected boolean isDown = false;

        protected int prevX = -1;

        protected Cursor cursor = null;

        public void mouseMove(MouseEvent e) {
            if ((e.x > columnPosition - columnHitWidth) && (e.x < columnPosition + columnHitWidth)) {
                if (cursor == null) {
                    cursor = new Cursor(fTree.getDisplay(), SWT.CURSOR_SIZEWE);
                    fTree.setCursor(cursor);
                }
            } else {
                if (cursor != null) {
                    fTree.setCursor(null);
                    cursor.dispose();
                    cursor = null;
                }
            }
            if (columnDragged != -1) {
                if (e.x > 20) {
                    columnPosition = e.x;
                    delayedDrawTimer.reset(20);
                }
            }
        }

        public void mouseDown(MouseEvent e) {
            columnDragged = -1;
            editManager.deactivateCellEditor();
            if ((e.x > columnPosition - columnHitWidth) && (e.x < columnPosition + columnHitWidth)) {
                columnDragged = 0;
            }
            TreeItem item = fTree.getItem(new Point(e.x, e.y));
            if (item == null) {
                item = getTreeItemOnRow(e.x, e.y);
                if (item != null) {
                    TreeItem[] items = new TreeItem[1];
                    items[0] = item;
                    fTree.setSelection(items);
                }
            }
        }

        public void mouseUp(MouseEvent e) {
            columnDragged = -1;
        }
    }

    public String[] getColumnProperties() {
        return fColumnProperties;
    }

    public void setColumnProperties(String[] columnProperties) {
        this.fColumnProperties = columnProperties;
    }

    public void paintControl(PaintEvent event) {
        GC gc = event.gc;
        Rectangle treeBounds = fTree.getBounds();
        controlWidth = treeBounds.width;
        Color bg = fTree.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        Color bg2 = fTree.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
        gc.setBackground(bg2);
        if (!fisUnsupportedInput) {
            TreeItem[] items = fTree.getItems();
            if (items.length > 0) {
                gc.setForeground(tableLineColor);
                gc.setBackground(bg);
                gc.fillRectangle(columnPosition, treeBounds.x, treeBounds.width, treeBounds.height);
                Rectangle itemBounds = items[0].getBounds();
                int height = computeTreeItemHeight();
                if (itemBounds != null) {
                    int startY = itemBounds.y;
                    for (int i = startY; i < treeBounds.height; i += height) {
                        if (i >= treeBounds.y) {
                            gc.drawLine(0, i, treeBounds.width, i);
                        }
                    }
                }
                gc.drawLine(columnPosition, 0, columnPosition, treeBounds.height);
                paintItems(gc, items, treeBounds);
            } else {
                addEmptyTreeMessage(gc);
            }
        } else {
            addUnableToPopulateTreeMessage(gc);
        }
    }

    protected int computeTreeItemHeight() {
        int result = -1;
        if (fTree.getItemCount() > 0) {
            TreeItem[] items = fTree.getItems();
            Rectangle itemBounds = items[0].getBounds();
            if (items[0].getExpanded()) {
                TreeItem[] children = items[0].getItems();
                if (children.length > 0) {
                    result = children[0].getBounds().y - itemBounds.y;
                }
            } else if (items.length > 1) {
                result = items[1].getBounds().y - itemBounds.y;
            }
        }
        result = Math.max(fTree.getItemHeight(), result);
        return result;
    }

    protected void addEmptyTreeMessage(GC gc) {
    }

    private void addUnableToPopulateTreeMessage(GC gc) {
        gc.setForeground(fTree.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(fTree.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        gc.drawString(SVGEditorMessages.TreeExtension_0, 10, 10);
    }

    void setIsUnsupportedInput(boolean isUnsupported) {
        fisUnsupportedInput = isUnsupported;
    }

    public void paintItems(GC gc, TreeItem[] items, Rectangle treeBounds) {
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                TreeItem item = items[i];
                if (item != null) {
                    Rectangle bounds = item.getBounds();
                    if (bounds != null) {
                        if (treeBounds.intersects(bounds)) {
                            paintItem(gc, item, bounds);
                        }
                    }
                    if (item.getExpanded()) {
                        paintItems(gc, item.getItems(), treeBounds);
                    }
                }
            }
        }
    }

    protected void paintItem(GC gc, TreeItem item, Rectangle bounds) {
    }

    public interface ICellEditorProvider {

        CellEditor getCellEditor(Object o, int col);
    }

    /**
	 * This class is used to improve drawing during a column resize.
	 */
    public class DelayedDrawTimer implements Runnable {

        protected Control control;

        public DelayedDrawTimer(Control control1) {
            this.control = control1;
        }

        public void reset(int milliseconds) {
            getDisplay().timerExec(milliseconds, this);
        }

        public void run() {
            control.redraw();
        }
    }

    Display getDisplay() {
        return PlatformUI.getWorkbench().getDisplay();
    }

    /**
	 * EditManager
	 */
    public class EditManager {

        protected Tree fTree1;

        protected Control cellEditorHolder;

        protected CellEditorState cellEditorState;

        public EditManager(Tree tree) {
            this.fTree1 = tree;
            this.cellEditorHolder = new Composite(tree, SWT.NONE);
            final Tree theTree = tree;
            MouseAdapter theMouseAdapter = new MouseAdapter() {

                public void mouseDown(MouseEvent e) {
                    deactivateCellEditor();
                    if (e.x > columnPosition + columnHitWidth) {
                        TreeItem[] items = theTree.getSelection();
                        if (items.length == 1) {
                            Rectangle bounds = items[0].getBounds();
                            if ((bounds != null) && (e.y >= bounds.y) && (e.y <= bounds.y + bounds.height)) {
                                int columnToEdit = 1;
                                activateCellEditor(items[0], columnToEdit);
                            }
                        }
                    }
                }
            };
            SelectionListener selectionListener = new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                    applyCellEditorValue();
                }

                public void widgetSelected(SelectionEvent e) {
                    applyCellEditorValue();
                }
            };
            KeyListener keyListener = new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.character == SWT.CR) {
                        deactivateCellEditor();
                        TreeItem[] items = theTree.getSelection();
                        if (items.length == 1) {
                            activateCellEditor(items[0], 1);
                        }
                    }
                }
            };
            tree.addMouseListener(theMouseAdapter);
            tree.addKeyListener(keyListener);
            ScrollBar hBar = tree.getHorizontalBar();
            if (hBar != null) {
                hBar.addSelectionListener(selectionListener);
            }
            ScrollBar vBar = tree.getVerticalBar();
            if (vBar != null) {
                vBar.addSelectionListener(selectionListener);
            }
        }

        public boolean isCellEditorActive() {
            return cellEditorState != null;
        }

        public void applyCellEditorValue() {
            if ((cellEditorState != null) && (cellModifier != null)) {
                TreeItem treeItem = cellEditorState.fTreeItem;
                Object value = cellEditorState.fCellEditor.getValue();
                String property = cellEditorState.fProperty;
                deactivateCellEditor();
                cellModifier.modify(treeItem, property, value);
            }
        }

        public void deactivateCellEditor() {
            if (cellEditorState != null) {
                cellEditorState.deactivate();
                cellEditorState = null;
            }
        }

        public void activateCellEditor(TreeItem treeItem, int column) {
            if (cellModifier instanceof ICellEditorProvider) {
                ICellEditorProvider cellEditorProvider = (ICellEditorProvider) cellModifier;
                Object data = treeItem.getData();
                if (fColumnProperties.length > column) {
                    String property = fColumnProperties[column];
                    if (cellModifier.canModify(data, property)) {
                        CellEditor newCellEditor = cellEditorProvider.getCellEditor(data, column);
                        if (newCellEditor != null) {
                            Control control = newCellEditor.getControl();
                            if (control != null) {
                                cellEditorState = new CellEditorState(newCellEditor, control, treeItem, column, property);
                                cellEditorState.activate();
                            }
                        }
                    }
                }
            }
        }

        /**
		 * this class holds the state that is need on a per cell editor
		 * invocation basis
		 */
        public class CellEditorState implements ICellEditorListener, FocusListener {

            public CellEditor fCellEditor;

            public Control fControl;

            public TreeItem fTreeItem;

            public int fColumnNumber;

            public String fProperty;

            public CellEditorState(CellEditor cellEditor, Control control, TreeItem treeItem, int columnNumber, String property) {
                this.fCellEditor = cellEditor;
                this.fControl = control;
                this.fTreeItem = treeItem;
                this.fColumnNumber = columnNumber;
                this.fProperty = property;
            }

            public void activate() {
                Object element = fTreeItem.getData();
                String value = cellModifier.getValue(element, fProperty).toString();
                if (fControl instanceof Text) {
                    Text text = (Text) fControl;
                    int requiredSize = value.length() + 100;
                    if (text.getTextLimit() < requiredSize) {
                        text.setTextLimit(requiredSize);
                    }
                }
                Rectangle r = fTreeItem.getBounds();
                if (r != null) {
                    fControl.setBounds(columnPosition + 5, r.y + 1, fTree1.getClientArea().width - (columnPosition + 5), r.height - 1);
                    fControl.setVisible(true);
                    fCellEditor.setValue(value);
                    fCellEditor.addListener(this);
                    fCellEditor.setFocus();
                    fControl.addFocusListener(this);
                }
            }

            public void deactivate() {
                fCellEditor.removeListener(this);
                fControl.removeFocusListener(this);
                fCellEditor.deactivate();
                fTree1.forceFocus();
            }

            public void applyEditorValue() {
                applyCellEditorValue();
            }

            public void cancelEditor() {
                deactivateCellEditor();
            }

            public void editorValueChanged(boolean oldValidState, boolean newValidState) {
            }

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                applyCellEditorValue();
            }
        }
    }
}
