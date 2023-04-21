package grammarbrowser.browser.list;

import grammarbrowser.parser.MutableGrammaticalRelation;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;

/**
 * Transfer handler for drag and drop operations
 * 
 * @author Bernard Bou
 * 
 */
public class TableTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    /**
     * Data flavour
     */
    private static DataFlavor theFlavor;

    /**
     * Indices of selected rows
     */
    private int[] theSelectedRowsIndices = null;

    /**
     * Location where items were added
     */
    private int theInsertIndex = -1;

    /**
     * Number of items added.
     */
    private int theInsertCount = 0;

    /**
     * Table row
     * 
     */
    class Row {

        /**
	 * Grammatical relation
	 */
        public MutableGrammaticalRelation theRelation;

        /**
	 * Color
	 */
        public Color theColor;

        /**
	 * Constructor
	 * 
	 * @param thisRelation
	 *                grammatical relation
	 * @param thisColor
	 *                color
	 */
        public Row(MutableGrammaticalRelation thisRelation, Color thisColor) {
            theRelation = thisRelation;
            theColor = thisColor;
        }
    }

    /**
     * Transferable rows
     * 
     */
    class TransferableRows implements Transferable {

        /**
	 * List of rows
	 */
        private List<Row> theRows;

        /**
	 * Constructor
	 * 
	 * @param theseRows
	 *                rows
	 */
        public TransferableRows(List<Row> theseRows) {
            theRows = theseRows;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            return theRows;
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] theseFlavors = { theFlavor };
            return theseFlavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor thisFlavor) {
            return thisFlavor == theFlavor;
        }
    }

    static {
        try {
            theFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + List.class.getName() + "\"");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] theseFlavors) {
        for (int i = 0; i < theseFlavors.length; i++) {
            if (theseFlavors[i].equals(theFlavor)) return true;
        }
        return false;
    }

    @Override
    protected Transferable createTransferable(JComponent thisSourceComponent) {
        JTable thisSourceTable = (JTable) thisSourceComponent;
        return new TransferableRows(getRows(thisSourceTable));
    }

    @Override
    protected void exportDone(JComponent thisComponent, Transferable data, int action) {
        JTable thisTable = (JTable) thisComponent;
        removeRows(thisTable);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean importData(JComponent thisComponent, Transferable thisTransferable) {
        if (canImport(thisComponent, thisTransferable.getTransferDataFlavors())) {
            try {
                JTable thisDestTable = (JTable) thisComponent;
                List<Row> theseRows = (List<Row>) thisTransferable.getTransferData(theFlavor);
                putRows(thisDestTable, theseRows);
                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) {
            }
        }
        return false;
    }

    /**
     * Get selected row items
     * 
     * @param thisSourceTable
     *                table
     * @return selected row items
     */
    protected List<Row> getRows(JTable thisSourceTable) {
        List<Row> theseRows = new ArrayList<Row>();
        theSelectedRowsIndices = thisSourceTable.getSelectedRows();
        for (int i = 0; i < theSelectedRowsIndices.length; i++) {
            Color thisColor = (Color) thisSourceTable.getValueAt(theSelectedRowsIndices[i], 0);
            MutableGrammaticalRelation thisRelation = (MutableGrammaticalRelation) thisSourceTable.getValueAt(theSelectedRowsIndices[i], 1);
            Row thisRow = new Row(thisRelation, thisColor);
            theseRows.add(thisRow);
        }
        return theseRows;
    }

    /**
     * Put rows
     * 
     * @param thisDestTable
     *                table
     * @param theseRows
     *                rows
     */
    protected void putRows(JTable thisDestTable, List<Row> theseRows) {
        DefaultTableModel thisModel = (DefaultTableModel) thisDestTable.getModel();
        int thisInsertRowIndex = thisDestTable.getSelectedRow();
        if (theSelectedRowsIndices != null && thisInsertRowIndex >= theSelectedRowsIndices[0] - 1 && thisInsertRowIndex <= theSelectedRowsIndices[theSelectedRowsIndices.length - 1]) {
            theSelectedRowsIndices = null;
            return;
        }
        int thisRowCount = thisModel.getRowCount();
        if (thisInsertRowIndex < 0) {
            thisInsertRowIndex = thisRowCount;
        } else {
            thisInsertRowIndex++;
            if (thisInsertRowIndex > thisRowCount) {
                thisInsertRowIndex = thisRowCount;
            }
        }
        theInsertIndex = thisInsertRowIndex;
        theInsertCount = theseRows.size();
        for (Row thisRow : theseRows) {
            Object[] theseObjects = { thisRow.theColor, thisRow.theRelation };
            thisModel.insertRow(thisInsertRowIndex++, theseObjects);
        }
    }

    /**
     * Remove rows
     * 
     * @param thisTable
     *                table
     */
    protected void removeRows(JTable thisTable) {
        if (theSelectedRowsIndices != null) {
            if (theInsertCount > 0) {
                for (int i = 0; i < theSelectedRowsIndices.length; i++) {
                    if (theSelectedRowsIndices[i] > theInsertIndex) theSelectedRowsIndices[i] += theInsertCount;
                }
            }
            DefaultTableModel thisModel = (DefaultTableModel) thisTable.getModel();
            for (int i = theSelectedRowsIndices.length - 1; i >= 0; i--) {
                thisModel.removeRow(theSelectedRowsIndices[i]);
            }
        }
        theSelectedRowsIndices = null;
        theInsertCount = 0;
        theInsertIndex = -1;
    }
}
