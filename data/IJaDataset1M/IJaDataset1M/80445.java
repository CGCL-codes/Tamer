package gui;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * 
 * @author ilaggoodly & Ice_Phoenix
 * A table for use in the Prefsound tab
 */
@SuppressWarnings("serial")
public class SoundTable extends JTable {

    final int ENABLED_POS = 1;

    final int FILEPATH_POS = 2;

    final int NAME_POS = 0;

    /**
	 * Creates a soundtable
	 * @param a --a table datamodel
	 * @param b --column headers
	 */
    public SoundTable(Object[][] a, Object[] b) {
        super(a, b);
        super.dataModel = new SoundTableModel(a, b);
        super.tableHeader.setReorderingAllowed(false);
        super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Overrides getColumnClass to force it to return Boolean (for checkmarks)
     */
    @SuppressWarnings("unchecked")
    public Class getColumnClass(int column) {
        Class dataType = super.getColumnClass(column);
        if (column == ENABLED_POS) {
            dataType = Boolean.class;
        }
        return dataType;
    }

    /**
	 * Returns the tablemodel
	 */
    public TableModel getModel() {
        return super.dataModel;
    }

    /**
     * Updates a row with new information
     * @param newValue --the new value for the row
     * @param rowToRefresh --the row number to refresh
     */
    public void refreshRow(String newValue, int rowToRefresh) {
        this.dataModel.setValueAt(newValue, rowToRefresh, 2);
        this.tableChanged(new TableModelEvent(this.getModel()));
    }

    /**
     * A DefaultTableModel for use with this table
     *
     */
    public class SoundTableModel extends DefaultTableModel {

        public SoundTableModel(Object[][] a, Object[] b) {
            super(a, b);
        }

        /**
    	 * Returns false if its a filepath or name column
    	 * @param --the row of the cellrequest
    	 * @param --the column of the cellrequest
    	 * @return --false if cell is in Filepath or Name
    	 */
        public boolean isCellEditable(int row, int column) {
            if (column == FILEPATH_POS || column == NAME_POS) return false; else return true;
        }
    }
}
