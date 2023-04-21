package org.ufacekit.ui.swing.jface.viewers.internal;

import org.ufacekit.ui.swing.jface.viewers.internal.swt.widgets.TableColumn;

/**
 * ViewerColumn implementation for TableViewer to enable column-specific label
 * providers and editing support.
 *
 * @param <ModelElement>
 *            the model element displayed in the viewer
 * @since 3.3
 */
public final class TableViewerColumn<ModelElement> extends ViewerColumn<ModelElement> {

    private TableColumn column;

    /**
	 * Creates a new viewer column for the given {@link TableViewer} on the given
	 * {@link TableColumn}.
	 *
	 * @param viewer
	 *            the table viewer to which this column belongs
	 * @param column
	 *            the underlying table column
	 */
    public TableViewerColumn(TableViewer<ModelElement, ?> viewer, javax.swing.table.TableColumn column) {
        this(viewer, new TableColumn(viewer.getTable(), column));
    }

    /**
	 * Creates a new viewer column for the given {@link TableViewer} on the given
	 * {@link TableColumn}.
	 *
	 * @param viewer
	 *            the table viewer to which this column belongs
	 * @param column
	 *            the underlying table column
	 */
    private TableViewerColumn(TableViewer<ModelElement, ?> viewer, TableColumn column) {
        super(viewer, column);
        this.column = column;
    }

    /**
	 * @return the underlying SWT table column
	 */
    public TableColumn getColumn() {
        return column;
    }
}
