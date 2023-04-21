package net.sf.rcpforms.widgetwrapper.wrapper.advanced.helper;

import net.sf.rcpforms.widgetwrapper.wrapper.advanced.RCPAdvancedViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Generic table sorter. Sorts the viewer input using {@link org.eclipse.jface.viewers.ViewerSorter}
 * . Extracts the value from datamodel by using the given property. if (and only if) both objects
 * are of type comparable than calls compareTo on the first object if sorting direction is
 * descending or the second object if direction is ascending. Otherwise it uses comparing logic from
 * {@link org.eclipse.jface.viewers.ViewerSorter#compare(Viewer, Object, Object)}
 * 
 * <h3><u>Usage:</u></h3></br>
 * Simply add one instance to each TableColumn as <code><b>SelectionListener</b></code>:
 * <pre>
 *   <font color='darkmagenta'>final</font> <i>GenericTableSorter2</i> sorter = <font color='darkmagenta'>new</font> <i>GenericTableSorter2</i>(tableViewer, somePropertyName);
 *   tableColumn.<b>addSelectionListener(sorter)</b>;
 * </pre>
 * Upon selection on the <code>tableColumn</code>'s header it installs itself as <code>ViewerSorter</code>.
 * 
 * @author Loetscher Remo
 * @author Spicher Christian (improved, Nov.2009)
 * @version 1.2 - fixed property path on multi-properties
 *              - added SelectionListener
 *              - fixed sorting bug when sorting by clicking on column A, then B, then A again (remembered its state, now down again)
 *              - added support for 3 (or more) chained sorters in a FIFO List (aka sorter history) 
 */
public class GenericTreeSorter2 extends GenericViewerSorter2<Tree, TreeColumn> {

    public GenericTreeSorter2(final TreeViewer tableViewer, final String property) {
        this(tableViewer, property, false);
    }

    /**
     * Instantiates a new generic table sorter2.
     * 
     * @param tableViewer the table viewer being sorted
     * @param property  property of the column being sorted
     * @param emptyStringHighest if <code>true</code> then empty strings will be treated as higher values
     *        than any non-empty strings --&gt; they will get at the end of the table!
     */
    public GenericTreeSorter2(final TreeViewer tableViewer, final String property, final boolean emptyStringHighest) {
        super(tableViewer, property, emptyStringHighest);
    }

    @Override
    protected RCPAdvancedViewerColumn getAdvancedViewerColumn(final TreeColumn myColumn) {
        return AdvancedTableUtil.getTableColumnsConfigurations2(myColumn);
    }

    @Override
    protected TreeColumn getSortColumn() {
        return m_treeOrTable.getSortColumn();
    }

    @Override
    protected void setSortColumn(final TreeColumn column) {
        m_treeOrTable.setSortColumn(column);
    }

    @Override
    protected void setSortDirection(final int swtConstant) {
        m_treeOrTable.setSortDirection(swtConstant);
    }
}
