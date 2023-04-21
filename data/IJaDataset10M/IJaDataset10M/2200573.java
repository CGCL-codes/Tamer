package org.akrogen.tkui.gui.swt.widgets.trees;

import java.util.ArrayList;
import java.util.List;
import org.akrogen.tkui.core.gui.GuiConstants;
import org.akrogen.tkui.core.gui.widgets.IGuiOptions;
import org.akrogen.tkui.core.gui.widgets.trees.GuiTreeOptions;
import org.akrogen.tkui.core.gui.widgets.trees.IGuiTree;
import org.akrogen.tkui.core.gui.widgets.trees.IGuiTreeColumn;
import org.akrogen.tkui.core.gui.widgets.trees.IGuiTreeRow;
import org.akrogen.tkui.gui.swt.widgets.SwtGuiControlImpl;
import org.akrogen.tkui.gui.swt.widgets.trees.viewers.SwtGuiTreeCellModifier;
import org.akrogen.tkui.gui.swt.widgets.trees.viewers.SwtGuiTreeContentProvider;
import org.akrogen.tkui.gui.swt.widgets.trees.viewers.SwtGuiTreeLabelProvider;
import org.akrogen.tkui.gui.swt.widgets.trees.viewers.SwtTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * GUI Tree implemented into SWT.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SwtGuiTreeImpl extends SwtGuiControlImpl implements IGuiTree {

    protected static final IGuiTreeColumn[] EMPTY_TREECOLUMNS = new IGuiTreeColumn[0];

    private SwtTreeViewer treeCellsViewer;

    protected static IGuiTreeRow[] EMPTY_TREEROWS = new IGuiTreeRow[0];

    protected List guiTreeRows = new ArrayList();

    private int rowIndex;

    private int rowIndex2;

    private int count = -1;

    private String[] columnNames;

    public Object buildWidget(Object parent, IGuiOptions options) {
        int style = getStyle(options);
        this.widget = createTree((Composite) parent, style);
        this.initializeTree(getTree());
        return widget;
    }

    protected Tree createTree(Composite parent, int style) {
        return new Tree(parent, style);
    }

    protected void initializeTree(final Tree tree) {
        tree.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    TreeItem treeItem = (TreeItem) event.item;
                    TreeItem[] selectedItems = tree.getSelection();
                    for (int i = 0; i < tree.getSelectionCount(); i++) {
                        TreeItem selectedItem = selectedItems[i];
                        if (treeItem == selectedItem) return;
                    }
                    tree.setSelection(((TreeItem) event.item));
                }
            }
        });
    }

    protected int getStyle(IGuiOptions options) {
        int style = SWT.NONE;
        if (options instanceof GuiTreeOptions) {
            GuiTreeOptions o = (GuiTreeOptions) options;
            if (o.checked) style |= SWT.CHECK;
            if (o.selection == GuiConstants.MULTIPLE) style |= SWT.MULTI; else style |= SWT.SINGLE;
        }
        return style;
    }

    public void setHeaderVisible(boolean show) {
        Tree tree = getTree();
        tree.setHeaderVisible(show);
    }

    public int getCurrentIndex() {
        this.rowIndex = -1;
        Tree tree = (Tree) getWidget();
        if (tree.getSelectionCount() == 1) {
            TreeItem selectedItem = tree.getSelection()[0];
            TreeItem[] treeItems = tree.getItems();
            buildRowIndex(treeItems, selectedItem);
        }
        return this.rowIndex;
    }

    private boolean buildRowIndex(TreeItem[] treeItems, TreeItem selectedItem) {
        for (int i = 0; i < treeItems.length; i++) {
            rowIndex++;
            TreeItem treeItem = treeItems[i];
            if (treeItem == selectedItem) return true;
            if (treeItem.getExpanded()) {
                if (buildRowIndex(treeItem.getItems(), selectedItem)) return true;
            }
        }
        return false;
    }

    private TreeItem buildTreeRowIndex(int index, TreeItem[] treeItems) {
        TreeItem item = null;
        for (int i = 0; i < treeItems.length; i++) {
            TreeItem treeItem = treeItems[i];
            rowIndex2++;
            if (rowIndex2 == index) return treeItem;
            if (treeItem.getExpanded()) {
                item = buildTreeRowIndex(index, treeItem.getItems());
                if (item != null) return item;
            }
        }
        return item;
    }

    public IGuiTreeRow getRowAtIndex(int index) {
        Tree tree = (Tree) getWidget();
        TreeItem[] treeItems = tree.getItems();
        rowIndex2 = -1;
        TreeItem treeItem = buildTreeRowIndex(index, treeItems);
        if (treeItem != null) {
            return (IGuiTreeRow) treeItem.getData();
        }
        return null;
    }

    public int getWidth() {
        Tree tree = getTree();
        return tree.getSize().x - 2 * tree.getVerticalBar().getSize().x;
    }

    protected Tree getTree() {
        return ((Tree) widget);
    }

    public IGuiTreeColumn[] getColumns() {
        List columns = new ArrayList();
        Tree tree = getTree();
        for (int i = 0; i < tree.getColumnCount(); i++) {
            columns.add(getColumn(i));
        }
        return (IGuiTreeColumn[]) columns.toArray(EMPTY_TREECOLUMNS);
    }

    public IGuiTreeColumn getColumn(int index) {
        Tree tree = getTree();
        TreeColumn column = tree.getColumn(index);
        return (IGuiTreeColumn) column.getData(SWT_DATA_KEY);
    }

    public void onChildrenWidgetParsed() {
        super.onChildrenWidgetParsed();
        this.treeCellsViewer = createTreeViewer(getTree());
        IGuiTreeRow[] treeRowsExpanded = treeCellsViewer.getTreeRowsExpanded();
        treeCellsViewer.setExpandedElements(treeRowsExpanded);
    }

    protected SwtTreeViewer createTreeViewer(Tree tree) {
        SwtTreeViewer treeCellsViewer = new SwtTreeViewer(tree);
        treeCellsViewer.setColumnProperties(getColumnNames());
        treeCellsViewer.setContentProvider(new SwtGuiTreeContentProvider());
        treeCellsViewer.setLabelProvider(new SwtGuiTreeLabelProvider(this));
        treeCellsViewer.setCellModifier(new SwtGuiTreeCellModifier(this));
        treeCellsViewer.setInput(this);
        return treeCellsViewer;
    }

    public SwtTreeViewer getTreeViewer() {
        return treeCellsViewer;
    }

    public String[] getColumnNames() {
        if (columnNames != null) return columnNames;
        Tree tree = getTree();
        TreeColumn[] columns = tree.getColumns();
        this.columnNames = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnNames[i] = columns[i].getText();
        }
        return columnNames;
    }

    public IGuiTreeRow[] getRows() {
        return (IGuiTreeRow[]) guiTreeRows.toArray(EMPTY_TREEROWS);
    }

    public void addRow(IGuiTreeRow row) {
        guiTreeRows.add(row);
        row.setParent(this);
    }

    public int indexOfRow(IGuiTreeRow row) {
        return guiTreeRows.indexOf(row);
    }

    public IGuiTreeRow getRow(int index) {
        return (IGuiTreeRow) guiTreeRows.get(index);
    }

    public IGuiTreeRow[] getSelectedRows() {
        IStructuredSelection selection = (IStructuredSelection) treeCellsViewer.getSelection();
        return (IGuiTreeRow[]) selection.toList().toArray(EMPTY_TREEROWS);
    }
}
