package no.uio.edd.model.geo;

import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import no.uio.edd.utils.datautils.TableCallbackInterface;
import no.uio.edd.utils.datautils.TableDialog;

/**
 * This abstract class is used to hold the general parts of the two table views,
 * the one for entities and the one for properties.
 * 
 * @author oeide
 * 
 */
public abstract class GeoModelTableView extends JPanel implements TableCallbackInterface, TableModelListener {

    private static final long serialVersionUID = 1L;

    protected TableDialog thisTableDialog;

    protected String[] columnHeaders, columnNames;

    protected TableCallbackInterface myResultTableCallbackInterface;

    /**
	 * The abstract part of the creator does not do much, only makes sure we
	 * have a link back to a runner object, based on interface connection.
	 * 
	 * @param inResultTableCallbackInterface
	 *            Gives us a link back to something implementing the right
	 *            interface.
	 */
    public GeoModelTableView(TableCallbackInterface inResultTableCallbackInterface) {
        myResultTableCallbackInterface = inResultTableCallbackInterface;
    }

    /**
	 * Adds the necessary listeners connecting the table model to objects of
	 * subclasses of this class.
	 */
    void addListener() {
        thisTableDialog.getTableModel().addTableModelListener(this);
    }

    /**
	 * Dumps all rows in the table held by this object in XML.
	 * 
	 * @return An XML dump of my table data.
	 */
    String dumpTable() {
        String returnString = "<?xml version='1.0' encoding='UTF-8'?><table>";
        int numRows = thisTableDialog.getTableModel().getRowCount();
        int numCols = thisTableDialog.getTableModel().getColumnCount();
        int i, j;
        for (i = 0; i < numRows; i++) {
            returnString = returnString + "<row>";
            for (j = 0; j < numCols; j++) returnString = returnString + "<" + columnNames[j] + ">" + thisTableDialog.getTableModel().getValueAt(i, j) + "</" + columnNames[j] + ">";
            returnString = returnString + "</row>";
        }
        return returnString + "</table>";
    }

    public void tableChanged(TableModelEvent e) {
        int col = e.getColumn();
        int rowFirst = e.getFirstRow();
        int rowLast = e.getLastRow();
        if (col >= 0 && rowFirst == rowLast && rowFirst >= 0) cellLeft(col, rowFirst);
    }

    /**
	 * Delivers a pointer to the table dialog object.
	 * 
	 * @return The table dialog object.
	 */
    TableDialog getTableDialog() {
        return thisTableDialog;
    }

    /**
	 * Find the index number of the selected row in the table held by this
	 * object.
	 * 
	 * @return The selected row number. -1 if none selected.
	 */
    int getSelectedRow() {
        return thisTableDialog.getSelectedRow();
    }

    /**
	 * Find the index number of the selected rows in the table held by this
	 * object.
	 * 
	 * @return The selected row numbers. Empty array if none selected.
	 */
    int[] getSelectedRows() {
        return thisTableDialog.getSelectedRows();
    }

    /**
	 * Return the node ID value from a specific line in the entity table.
	 * 
	 * @param rowNum
	 *            The row number in the table.
	 * @return The ID value of the record in this row. If the low number is out
	 *         of bonds, null is returned.
	 */
    abstract String getNodeIdInRow(int rowNum);

    boolean removeTableRow(int rowNum) {
        return thisTableDialog.removeRow(rowNum);
    }

    public void addSorter() {
        thisTableDialog.addSorter();
    }

    public void removeSorter() {
        thisTableDialog.removeSorter();
    }

    /**
	 * Set focus to the last row of the underlying table, to the cell selected
	 * by the parameter cellNum.
	 * 
	 * @param cellNum
	 *            The column in the last row to be focused.
	 */
    void setFocusToLastLine(int cellNum) {
        int newRowNum = thisTableDialog.getTableModel().getRowCount() - 1;
        thisTableDialog.setFocusToCell(newRowNum, cellNum);
    }

    /**
	 * Sets the table to empty.
	 */
    void clearTable() {
        thisTableDialog.clearTableModel();
        thisTableDialog.setColumnNames(columnHeaders);
        thisTableDialog.setOpaque(true);
    }
}
