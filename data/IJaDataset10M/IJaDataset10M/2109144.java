package de.sonivis.tool.ontology.view.tables;

import java.util.Iterator;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.swt.EventKTableModel;
import ca.odell.glazedlists.swt.GlazedListsSWT;
import ca.odell.glazedlists.swt.KTableFormat;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableModel;

@SuppressWarnings("unchecked")
public class EventListMetricsTableModel extends MetricsTableModel implements KTableModel, ListEventListener {

    /** the proxy moves events to the SWT thread */
    private TransformedList swtThreadSource = null;

    /**
	 * Create a new {@link EventKTableModel} that uses elements from the specified {@link EventList}
	 * as rows, and the specified {@link TableFormat} to divide row objects across columns.
	 * 
	 * @param tableFormat
	 *            provides logic to divide row objects across columns. If the value implements the
	 *            {@link KTableFormat} interface, those methods will be used to provide further
	 *            details such as cell renderers, cell editors and row heights.
	 */
    public EventListMetricsTableModel(final KTable table, final EventList source, final KTableFormat tableFormat) {
        super(table, tableFormat);
        this.swtThreadSource = GlazedListsSWT.swtThreadProxyList(source, table.getDisplay());
        swtThreadSource.addListEventListener(this);
        initialize();
        if (this.swtThreadSource.size() > 0) {
            Iterator it = swtThreadSource.iterator();
            Object next = null;
            while (it.hasNext() && next == null) {
                next = it.next();
            }
            Object measuredObject = next;
            for (int i = 0; i < tableFormat.getColumnCount(); i++) {
                tableFormat.getColumnValue(measuredObject, i);
            }
        }
    }

    public int getRowOf(Object obj) {
        return swtThreadSource.indexOf(obj) + getFixedHeaderRowCount();
    }

    /** {@inheritDoc} */
    @Override
    public final KTableCellEditor doGetCellEditor(final int column, final int row) {
        if (row < getFixedHeaderRowCount()) {
            return null;
        } else if (kTableFormat instanceof WritableTableFormat) {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                final Object baseObject = swtThreadSource.get(row);
                return kTableFormat.getColumnEditor(baseObject, column);
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final KTableCellRenderer doGetCellRenderer(final int column, final int row) {
        if (row < getFixedHeaderRowCount()) {
            if (column == 0) {
                return fixedRenderer;
            } else {
                return rotatedFixedRenderer;
            }
        } else {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                return kTableFormat.getColumnRenderer(swtThreadSource.get(row - getFixedHeaderRowCount()), column);
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        }
    }

    @Override
    public final Object doGetContentAt(final int col, final int row) {
        if (row < getFixedHeaderRowCount()) {
            return kTableFormat.getColumnHeaderValue(row, col);
        } else {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                return kTableFormat.getColumnValue(swtThreadSource.get(row - getFixedHeaderRowCount()), col);
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public final String doGetTooltipAt(final int column, final int row) {
        if (row < getFixedHeaderRowCount()) {
            if (kTableFormat instanceof EventListMetricsTableFormat) {
                return ((MetricsTableFormat) kTableFormat).getFixedHeaderTooltip(column);
            } else {
                return null;
            }
        } else {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                return kTableFormat.getColumnTooltip(swtThreadSource.get(row - getFixedHeaderRowCount()), column);
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void doSetContentAt(final int column, final int row, final Object value) {
        if (row < getFixedHeaderRowCount()) {
            throw new UnsupportedOperationException("Unexpected set() on column header");
        } else if (kTableFormat instanceof WritableTableFormat) {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                final WritableTableFormat writableTableFormat = (WritableTableFormat) kTableFormat;
                final Object baseObject = swtThreadSource.get(row - getFixedHeaderRowCount());
                final Object updatedObject = writableTableFormat.setColumnValue(baseObject, value, column);
                if (updatedObject != null) {
                    swtThreadSource.set(row - getFixedHeaderRowCount(), updatedObject);
                }
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        } else {
            throw new UnsupportedOperationException("Unexpected set() on read-only table");
        }
    }

    /** {@inheritDoc} */
    @Override
    public final int getRowCount() {
        swtThreadSource.getReadWriteLock().readLock().lock();
        try {
            return swtThreadSource.size() + getFixedHeaderRowCount();
        } finally {
            swtThreadSource.getReadWriteLock().readLock().unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final int getRowHeight(final int row) {
        if (row < getFixedHeaderRowCount()) {
            return 90;
        } else if (row < getRowCount()) {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                return kTableFormat.getRowHeight(swtThreadSource.get(row - getFixedHeaderRowCount()));
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        } else {
            return 20;
        }
    }

    /** {@inheritDoc} */
    public final boolean isRowResizable(final int row) {
        if (row < getFixedHeaderRowCount()) {
            return false;
        } else {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                return kTableFormat.isRowResizable(swtThreadSource.get(row - getFixedHeaderRowCount()));
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        }
    }

    /** {@inheritDoc} */
    public final void listChanged(final ListEvent listChanges) {
        table.redraw();
    }

    /** {@inheritDoc} */
    @Override
    public final void setRowHeight(final int row, final int value) {
        if (row < getFixedHeaderRowCount()) {
            return;
        } else {
            swtThreadSource.getReadWriteLock().readLock().lock();
            try {
                kTableFormat.setRowHeight(swtThreadSource.get(row - getFixedHeaderRowCount()), value);
            } finally {
                swtThreadSource.getReadWriteLock().readLock().unlock();
            }
        }
    }
}
