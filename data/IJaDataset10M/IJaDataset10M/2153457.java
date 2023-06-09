package com.aelitis.azureus.ui.common.table.impl;

import java.util.Iterator;
import org.gudy.azureus2.core3.util.AERunnable;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.ui.swt.Utils;
import com.aelitis.azureus.core.util.CopyOnWriteList;
import com.aelitis.azureus.ui.common.table.*;

/**
 * @author TuxPaper
 * @created Feb 6, 2007
 */
public abstract class TableViewImpl<DATASOURCETYPE> implements TableView<DATASOURCETYPE> {

    private CopyOnWriteList<TableDataSourceChangedListener> listenersDataSourceChanged = new CopyOnWriteList<TableDataSourceChangedListener>();

    private CopyOnWriteList<TableSelectionListener> listenersSelection = new CopyOnWriteList<TableSelectionListener>();

    private CopyOnWriteList<TableLifeCycleListener> listenersLifeCycle = new CopyOnWriteList<TableLifeCycleListener>();

    private CopyOnWriteList<TableRefreshListener> listenersRefresh = new CopyOnWriteList<TableRefreshListener>();

    private CopyOnWriteList<TableCountChangeListener> listenersCountChange = new CopyOnWriteList<TableCountChangeListener>(1);

    private Object parentDataSource;

    public void addSelectionListener(TableSelectionListener listener, boolean bFireSelection) {
        listenersSelection.add(listener);
        if (bFireSelection) {
            TableRowCore[] rows = getSelectedRows();
            listener.selected(rows);
            listener.focusChanged(getFocusedRow());
        }
    }

    public void addTableDataSourceChangedListener(TableDataSourceChangedListener l, boolean trigger) {
        listenersDataSourceChanged.add(l);
        if (trigger) {
            l.tableDataSourceChanged(parentDataSource);
        }
    }

    public void removeTableDataSourceChangedListener(TableDataSourceChangedListener l) {
        listenersDataSourceChanged.remove(l);
    }

    public void setParentDataSource(Object newDataSource) {
        parentDataSource = newDataSource;
        Object[] listeners = listenersDataSourceChanged.toArray();
        for (int i = 0; i < listeners.length; i++) {
            TableDataSourceChangedListener l = (TableDataSourceChangedListener) listeners[i];
            l.tableDataSourceChanged(newDataSource);
        }
    }

    /**
	 * @param selectedRows
	 */
    protected void triggerDefaultSelectedListeners(TableRowCore[] selectedRows, int keyMask) {
        for (Iterator iter = listenersSelection.iterator(); iter.hasNext(); ) {
            TableSelectionListener l = (TableSelectionListener) iter.next();
            l.defaultSelected(selectedRows, keyMask);
        }
    }

    /**
	 * @param eventType
	 */
    protected void triggerLifeCycleListener(int eventType) {
        Object[] listeners = listenersLifeCycle.toArray();
        if (eventType == TableLifeCycleListener.EVENT_INITIALIZED) {
            for (int i = 0; i < listeners.length; i++) {
                TableLifeCycleListener l = (TableLifeCycleListener) listeners[i];
                try {
                    l.tableViewInitialized();
                } catch (Exception e) {
                    Debug.out(e);
                }
            }
        } else {
            for (int i = 0; i < listeners.length; i++) {
                TableLifeCycleListener l = (TableLifeCycleListener) listeners[i];
                try {
                    l.tableViewDestroyed();
                } catch (Exception e) {
                    Debug.out(e);
                }
            }
        }
    }

    protected void triggerSelectionListeners(TableRowCore[] rows) {
        if (rows == null || rows.length == 0) {
            return;
        }
        Object[] listeners = listenersSelection.toArray();
        for (int i = 0; i < listeners.length; i++) {
            TableSelectionListener l = (TableSelectionListener) listeners[i];
            l.selected(rows);
        }
    }

    protected void triggerDeselectionListeners(TableRowCore[] rows) {
        if (rows == null) {
            return;
        }
        Object[] listeners = listenersSelection.toArray();
        for (int i = 0; i < listeners.length; i++) {
            TableSelectionListener l = (TableSelectionListener) listeners[i];
            try {
                l.deselected(rows);
            } catch (Exception e) {
                Debug.out(e);
            }
        }
    }

    protected void triggerMouseEnterExitRow(TableRowCore row, boolean enter) {
        if (row == null) {
            return;
        }
        Object[] listeners = listenersSelection.toArray();
        for (int i = 0; i < listeners.length; i++) {
            TableSelectionListener l = (TableSelectionListener) listeners[i];
            if (enter) {
                l.mouseEnter(row);
            } else {
                l.mouseExit(row);
            }
        }
    }

    protected void triggerFocusChangedListeners(TableRowCore row) {
        Object[] listeners = listenersSelection.toArray();
        for (int i = 0; i < listeners.length; i++) {
            TableSelectionListener l = (TableSelectionListener) listeners[i];
            l.focusChanged(row);
        }
    }

    /**
	 * 
	 */
    protected void triggerTableRefreshListeners() {
        Object[] listeners = listenersRefresh.toArray();
        for (int i = 0; i < listeners.length; i++) {
            TableRefreshListener l = (TableRefreshListener) listeners[i];
            l.tableRefresh();
        }
    }

    public void addLifeCycleListener(TableLifeCycleListener l) {
        listenersLifeCycle.add(l);
        if (!isDisposed()) {
            l.tableViewInitialized();
        }
    }

    public void addRefreshListener(TableRefreshListener l, boolean trigger) {
        listenersRefresh.add(l);
        if (trigger) {
            l.tableRefresh();
        }
    }

    public void addCountChangeListener(TableCountChangeListener listener) {
        listenersCountChange.add(listener);
    }

    public void removeCountChangeListener(TableCountChangeListener listener) {
        listenersCountChange.remove(listener);
    }

    protected void triggerListenerRowAdded(final TableRowCore row) {
        Utils.getOffOfSWTThread(new AERunnable() {

            public void runSupport() {
                for (Iterator iter = listenersCountChange.iterator(); iter.hasNext(); ) {
                    TableCountChangeListener l = (TableCountChangeListener) iter.next();
                    l.rowAdded(row);
                }
            }
        });
    }

    protected void triggerListenerRowRemoved(TableRowCore row) {
        for (Iterator iter = listenersCountChange.iterator(); iter.hasNext(); ) {
            TableCountChangeListener l = (TableCountChangeListener) iter.next();
            l.rowRemoved(row);
        }
    }

    public void runForAllRows(TableGroupRowRunner runner) {
        TableRowCore[] rows = getRows();
        if (runner.run(rows)) {
            return;
        }
        for (int i = 0; i < rows.length; i++) {
            runner.run(rows[i]);
        }
    }
}
