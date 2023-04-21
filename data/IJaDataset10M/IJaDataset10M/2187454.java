package org.jcvi.platetools.swing.ampchange;

import javax.swing.JTable;
import org.jcvi.glk.TrimSequence;
import org.jcvi.platetools.swing.PrimerTableModel;
import org.jcvi.platetools.swing.action.SimpleDelayedAction;
import org.jcvi.platetools.swing.primerselect.PrimerDisplayPanel;

/**
 * 
 *
 * @author jsitz@jcvi.org
 */
public class SelectPrimerAction extends SimpleDelayedAction {

    /** The Serial Version UID. */
    private static final long serialVersionUID = -2791064693692415744L;

    /** The table view of the primers. */
    private final JTable table;

    /** The backing list of primers. */
    private final PrimerTableModel primers;

    /** The display to deliver the primer to. */
    private final PrimerDisplayPanel primerDisplay;

    /**
     * Creates a new <code>SelectPrimerAction</code>.
     */
    public SelectPrimerAction(JTable table, PrimerTableModel primers, PrimerDisplayPanel primerDisplay) {
        super("Select", "accept");
        this.table = table;
        this.primers = primers;
        this.primerDisplay = primerDisplay;
        this.setDisposeWindow(true);
    }

    @Override
    public void invokeAction() {
        final int selectedIndex = this.table.getRowSorter().convertRowIndexToModel(this.table.getSelectedRow());
        final TrimSequence primer = this.primers.getPrimer(selectedIndex);
        this.primerDisplay.setPrimer(primer);
    }
}
