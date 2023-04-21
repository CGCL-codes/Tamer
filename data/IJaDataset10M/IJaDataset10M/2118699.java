package org.akrogen.tkui.gui.swt.ex.viewers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * <p>
 * Generic <code>ViewerSorter</code> extension for <code>Viewer</code>
 * instances using <code>ITableContentProvider</code> implementations.
 * </p>
 * 
 * @author <a href="mailto:smachhau@users.sourceforge.net">Sebastian Machhausen</a>
 */
public class TableViewerSorter extends ViewerSorter {

    /** <p> The column that the sorting is done by </p> */
    private int sortingColumn;

    /**
     * <p>
	 * <code>true</code> indicates ascending (default), <code>false</code>
	 * descending sort order
	 * </p>
	 */
    private boolean ascending = true;

    /**<p> The <code>Viewer</code> that the sorting is done for </p> */
    private Viewer viewer;

    /**
	 * <p>
	 * The <code>ITableContentProvider</code> used to query the underlying
	 * model
	 * </p>
	 */
    private ITableContentProvider contentProvider;

    /**
	 * <p>
	 * Creates a new <code>TableViewerSorter</code> instance linked to the
	 * specified <code>Viewer</code>.
	 * </p>
	 * 
	 * @param viewer the <code>Viewer</code> to link this
	 * <code>TableViewerSorter</code> to
	 */
    public TableViewerSorter(Viewer viewer, ITableContentProvider contentProvider) {
        this.viewer = viewer;
        this.contentProvider = contentProvider;
    }

    /**
     * <p>
     * Gets the column index by which the sorting is done.
     * </p>
     * 
     * @return the column index by which the sorting is done
     * 
     * @see #getSortingColumn()
     */
    public int getSortingColumn() {
        return (this.sortingColumn);
    }

    /**
     * <p>
     * Sets the column index by which the sorting is to be done.
     * </p>
     * 
     * @param columnIndex the column index by which the sorting is to be done
     * 
     * @see #getSortingColumn()
     */
    public void setSortingColumn(int columnIndex) {
        this.sortingColumn = columnIndex;
    }

    /**
     * <p>
     * Gets the sort order; <code>true<Code> indicates ascending,
     * <code>false</code> descending sort order.
     * </p>
     *
     * @return <code>true<Code> for ascending, <code>false</code> for descending
     * sort order
     * 
     * @see #setAscending(boolean)
     */
    public boolean isAscending() {
        return (this.ascending);
    }

    /**
     * <p>
     * Sets the sort order to be used; <code>true<Code> indicates ascending,
     * <code>false</code> descending sort order.
     * </p>
     *
     * @param ascending <code>true<Code> for ascending, <code>false</code> for
     * descending sort order
     * 
     * @see #isAscending()
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * <p>
     * Sorts the underlying model data and refreshes the associated
     * <code>TableViewer</code> to reflect the new sorting.
     * </p>
     */
    public void sort() {
        this.viewer.refresh();
    }

    /**
	 * <p>
	 * Returns a negative, zero, or positive number depending on whether the
	 * first element is less than, equal to, or greater than the second element.
	 * </p>
	 * 
	 * @param viewer the viewer
	 * @param e1 the first element
	 * @param e2 the second element
	 * 
	 * @return a negative number if the first element is less than the second
	 * element; the value <code>0</code> if the first element is equal to the
	 * second element; and a positive number if the first element is greater
	 * than the second element
	 */
    public int compare(Viewer viewer, Object e1, Object e2) {
        int category1 = this.category(e1);
        int category2 = this.category(e2);
        if (category1 != category2) {
            return (category1 - category2);
        }
        Object value1 = this.contentProvider.getColumnValue(e1, this.getSortingColumn());
        Object value2 = this.contentProvider.getColumnValue(e2, this.getSortingColumn());
        if (value1 instanceof String && value2 instanceof String) {
            if (value1 == null) {
                value1 = "";
            }
            if (value2 == null) {
                value2 = "";
            }
            return (this.isAscending() ? this.collator.compare(value1, value2) : (-this.collator.compare(value1, value2)));
        } else {
            if (value1 == null && value2 == null) {
                return (0);
            } else if (value1 != null && value2 == null) {
                return (-1);
            } else if (value1 == null && value2 != null) {
                return (1);
            } else if (value1 instanceof Comparable && value2 instanceof Comparable) {
                return (this.isAscending() ? ((Comparable) value1).compareTo(value2) : -((Comparable) value1).compareTo(value2));
            } else {
                return (this.isAscending() ? this.collator.compare(value1, value2) : (-this.collator.compare(value1, value2)));
            }
        }
    }
}
