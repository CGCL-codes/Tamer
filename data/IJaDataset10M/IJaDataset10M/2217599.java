package hidb2.gui.util;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableColumn;

/**
 * A listener which is dedicated to sorting of tables.
 *
 * @see de.soltenborn.jface.util.sorting.AbstractInvertableSorter
 *
 * @author Christian Soltenborn
 */
public class TableSortSelectionListener implements SelectionListener {

    private final TableViewer viewer;

    private final TableColumn column;

    private final InvertableSorter sorter;

    private final boolean keepDirection;

    private InvertableSorter currentSorter;

    /**
   * The constructor of this listener.
   *
   * @param viewer
   *            the tableviewer this listener belongs to
   * @param column
   *            the column this listener is responsible for
   * @param sorter
   *            the sorter this listener uses
   * @param defaultDirection
   *            the default sorting direction of this Listener. Possible
   *            values are {@link SWT.UP} and {@link SWT.DOWN}
   * @param keepDirection
   *            if true, the listener will remember the last sorting direction
   *            of the associated column and restore it when the column is
   *            reselected. If false, the listener will use the default soting
   *            direction
   *
   */
    public TableSortSelectionListener(TableViewer viewer, TableColumn column, InvertableSorter sorter, int defaultDirection, boolean keepDirection) {
        this.viewer = viewer;
        this.column = column;
        this.keepDirection = keepDirection;
        this.sorter = (defaultDirection == SWT.UP) ? sorter : sorter.getInverseSorter();
        this.currentSorter = this.sorter;
        this.column.addSelectionListener(this);
    }

    /**
   * Same as
   * {@link TableSortSelectionListener#TableSortSelectionListener(TableViewer, TableColumn, AbstractSorter, boolean, false)}
   *
   * @param viewer
   *            the tableviewer this listener belongs to
   * @param column
   *            the column this listener is responsible for
   * @param sorter
   *            the sorter this listener uses
   * @param defaultDirection
   *            the default sorting direction of this Listener. Possible
   *            values are {@link SWT.UP} and {@link SWT.DOWN}
   */
    public TableSortSelectionListener(TableViewer viewer, TableColumn column, AbstractInvertableSorter sorter, int defaultDirection) {
        this(viewer, column, sorter, defaultDirection, false);
    }

    /**
   * Same as
   * {@link TableSortSelectionListener#TableSortSelectionListener(TableViewer, TableColumn, AbstractSorter, SWT.UP, false)}
   *
   * @param viewer
   *            the tableviewer this listener belongs to
   * @param column
   *            the column this listener is responsible for
   * @param sorter
   *            the sorter this listener uses
   */
    public TableSortSelectionListener(TableViewer viewer, TableColumn column, AbstractInvertableSorter sorter) {
        this(viewer, column, sorter, SWT.UP);
    }

    /**
   * Chooses the colum of this listener for sorting of the table. Mainly used
   * when first initialising the table.
   */
    public void chooseColumnForSorting() {
        viewer.getTable().setSortColumn(column);
        viewer.getTable().setSortDirection(currentSorter.getSortDirection());
        viewer.getControl().setRedraw(false);
        viewer.setSorter(currentSorter);
        viewer.getControl().setRedraw(true);
    }

    /**
   * @inheritDoc
   */
    public void widgetSelected(SelectionEvent e) {
        InvertableSorter newSorter;
        if (viewer.getTable().getSortColumn().equals(column)) {
            newSorter = ((InvertableSorter) viewer.getSorter()).getInverseSorter();
        } else {
            if (keepDirection) {
                newSorter = currentSorter;
            } else {
                newSorter = sorter;
            }
        }
        currentSorter = newSorter;
        chooseColumnForSorting();
    }

    /**
   * @inheritDoc
   */
    public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
    }
}
