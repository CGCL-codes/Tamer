package com.safi.workshop.sqlexplorer.dataset.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.Table;

/**
 * Abstract implementation for a context menu action of a DataSetTable. Extend this class
 * to add new actions to the DataSetTable.
 * 
 * @author Davy Vanherbergen
 */
public abstract class AbstractDataSetTableContextAction extends Action {

    protected Table _table;

    protected TableCursor _cursor;

    /**
   * Store table for use in the actions.
   * 
   * @param table
   */
    public final void setTable(Table table) {
        _table = table;
    }

    /**
   * Store table cursor for use in the actions.
   * 
   * @param tableCursor
   */
    public final void setTableCursor(TableCursor cursor) {
        _cursor = cursor;
    }

    /**
   * Implement this method to return true when your action is available for the active
   * table. When true, the action will be included in the context menu, when false it will
   * be ignored.
   * 
   * @return true if the action should be included in the context menu
   */
    public boolean isAvailable() {
        return true;
    }
}
