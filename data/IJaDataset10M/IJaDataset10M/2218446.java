package ui.table;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The SelectionListener class provides a listener to a given JTable
 * that responds to changes in the rows that are selected in a SongTable object.
 *
 * @author Brian Gibowski brian@brgib.com
 */
public class SelectionListener implements ListSelectionListener {

    private JTable table;

    private RowCountLabel label;

    /**
     * Creates a new SelectionListener object with the given JTable object
     * and the RowCountLabel object to report the number of selected rows to.
     *
     * @param table The table object to listen to the number of selected rows.
     *
     * @param label The RowCountLabel object to report the number of selected rows to.
     */
    public SelectionListener(JTable table, RowCountLabel label) {
        this.table = table;
        this.label = label;
    }

    /**
     * The method called when the SelectionListener object is called.  The
     * method will determine the number of selected rows and update the RowCountLabel
     * object.
     *
     * @param e The even that calls this method.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        label.setLabelSelectedRows(table.getSelectedRows().length);
    }
}
