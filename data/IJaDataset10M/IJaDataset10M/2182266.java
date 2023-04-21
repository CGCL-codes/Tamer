package com.ivis.xprocess.ui.tables.viewmanagers;

import java.util.HashMap;
import java.util.List;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.draganddrop.util.IDropTargetRoot;
import com.ivis.xprocess.ui.inplaceediting.InplaceEditorManager;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.tables.columns.ColumnManager;
import com.ivis.xprocess.ui.tables.columns.XProcessColumnManager;
import com.ivis.xprocess.ui.tables.columns.definition.XProcessColumn;
import com.ivis.xprocess.ui.util.ViewUtil;
import com.ivis.xprocess.ui.widgets.IDirtyListener;

public abstract class ViewManager implements IViewManager, IDropTargetRoot, IDoubleClickListener {

    public static int TREE_VIEW = 1;

    public static int TABLE_VIEW = 2;

    public static int CURRENT_VIEW = 1;

    protected HashMap<Integer, Item> indexToDisplayedColumn;

    protected HashMap<Item, XProcessColumn> displayColumnToXProcessColumn;

    protected HashMap<XProcessColumn, Integer> xProcessColumnToIndex;

    protected ColumnManager columnManager;

    protected Object input;

    protected boolean isDialogReordering = false;

    protected Image ascendingImage;

    protected Image descendingImage;

    protected Object layoutData;

    protected IDirtyListener dirtyListener;

    private boolean staticView = false;

    protected boolean allowSorting = true;

    public ViewManager() {
    }

    public ViewManager(XProcessColumnManager columnManager) {
        this.columnManager = columnManager;
        ascendingImage = UIType.sort_ascending.image;
        descendingImage = UIType.sort_descending.image;
    }

    public abstract void setColumns(XProcessColumn[] columns);

    public abstract void createColumns();

    public void recreateAsync() {
        Display display = ViewUtil.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                recreate();
            }
        });
    }

    public abstract void recreate();

    public abstract void setLayoutData(Object layoutData);

    public abstract void rename();

    public abstract Control getControl();

    public XProcessColumn getXProcessColumn(int index) {
        return (XProcessColumn) columnManager.getDisplayedColumns()[index];
    }

    public Object[] getAllColumns() {
        return columnManager.getAllColumns();
    }

    public void setDialogReordering(boolean isDialogReordering) {
        this.isDialogReordering = isDialogReordering;
    }

    public boolean isDialogReordering() {
        return this.isDialogReordering;
    }

    public void updateSortColumnIcons() {
        Object[] displayedColumns = columnManager.getDisplayedColumns();
        for (int i = 0; i < displayedColumns.length; i++) {
            Integer index = new Integer(i);
            Item item = indexToDisplayedColumn.get(index);
            XProcessColumn column = (XProcessColumn) displayedColumns[i];
            item.setImage(null);
            if (column.isCurrentSortColumn()) {
                switch(column.getSortDirection()) {
                    case ASCENDING:
                        item.setImage(ascendingImage);
                        break;
                    case DESCENDING:
                        item.setImage(descendingImage);
                        break;
                }
            }
        }
    }

    public abstract void saveText(Item item, String text, int column);

    public abstract void save();

    public Object getRootObject() {
        return input;
    }

    public abstract void setLabelProvider(ITableLabelProvider tableLabelProvider);

    public abstract void setSorterProvider(ViewerSorter sorter);

    public abstract void setContentProvider(IStructuredContentProvider treeContentProvider);

    public abstract IStructuredContentProvider getContentProvider();

    public void resize() {
    }

    public abstract void refresh();

    public abstract void refreshTree(IElementWrapper element);

    public void refreshViewer(List<ChangeRecord> changeRecords) {
    }

    public abstract boolean refreshEvent(ChangeRecord changeRecord);

    public void dispose() {
    }

    public void addDirtyListener(IDirtyListener dirtyListener) {
        this.dirtyListener = dirtyListener;
    }

    public void deSelectAll() {
    }

    public abstract InplaceEditorManager getInplaceEditorManager();

    public void setSortable(boolean lock) {
    }

    public boolean isStaticView() {
        return staticView;
    }

    public void setStaticView(boolean staticView) {
        this.staticView = staticView;
    }

    public boolean isAllowSorting() {
        return allowSorting;
    }

    public void setAllowSorting(boolean allowSorting) {
        this.allowSorting = allowSorting;
    }
}
