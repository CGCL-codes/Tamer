package gov.sns.apps.jeri.apps.signalfieldcolumnselect;

import java.awt.*;
import java.util.*;
import gov.sns.apps.jeri.data.SignalFieldType;
import gov.sns.apps.jeri.tools.swing.ColumnSelectDialog;

/**
 * Provides a class that handles the issues associated with the select columns
 * dialog in the signal browser. Because the fields are displayed more than once 
 * in the table, but only once in the list, a class is needed to keep up with 
 * the one that aren't in either.
 * 
 * @author Chris Fowlkes
 */
public class SignalFieldColumnSelectDialog extends ColumnSelectDialog {

    /**
   * Holds the hidden columns. Hidden columns are the columns that are not in 
   * the table or the list. 
   */
    private ArrayList hiddenColumns = new ArrayList();

    /**
   * Holds the model used in the table.
   */
    private SignalFieldColumnSelectTableModel model = new SignalFieldColumnSelectTableModel();

    /**
   * Creates a new, non-modal <CODE>ColumnSelectDialog</CODE>.
   */
    public SignalFieldColumnSelectDialog() {
        this(null, "", false);
    }

    /**
   * Creates a new <CODE>SignalFieldColumnSelectDialog</CODE>.
   */
    public SignalFieldColumnSelectDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Component initialization.
   * 
   * @throws java.lang.Exception Thrown on initialization error.
   */
    private void jbInit() throws Exception {
        this.setColumnsTableModel(model);
    }

    /**
   * Selects the given columns. This method moves the given columns to the 
   * selected list and then removes any fields with the same ID still in the 
   * table.
   * 
   * @param columns The columns to select.
   */
    @Override
    protected void selectColumns(Object[] columns) {
        super.selectColumns(columns);
        String[] fieldIDs = new String[columns.length];
        for (int i = 0; i < fieldIDs.length; i++) fieldIDs[i] = ((SignalFieldType) columns[(i)]).getID();
        Arrays.sort(fieldIDs);
        ArrayList columnsToHide = new ArrayList();
        SignalFieldColumnSelectTableModel availableColumns = (SignalFieldColumnSelectTableModel) getColumnsTableModel();
        int rowCount = availableColumns.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            SignalFieldType currentType = availableColumns.getSignalFieldTypeAt(i);
            if (Arrays.binarySearch(fieldIDs, currentType.getID()) >= 0) columnsToHide.add(currentType);
        }
        Object[] columnsHidden = removeColumnsFromTable(columnsToHide.toArray());
        hiddenColumns.addAll(Arrays.asList(columnsHidden));
    }

    /**
   * Drops the given columns. This method moves the given columns to the 
   * available table and also adds any that were removed and have the same field
   * ID.
   * 
   * @param columns The columns to drop.
   */
    @Override
    protected void dropColumns(Object[] columns) {
        super.dropColumns(columns);
        String[] fieldIDs = new String[columns.length];
        for (int i = 0; i < fieldIDs.length; i++) fieldIDs[i] = ((SignalFieldType) columns[(i)]).getID();
        Arrays.sort(fieldIDs);
        int hiddenCount = hiddenColumns.size();
        ArrayList columnsToShow = new ArrayList();
        for (int i = 0; i < hiddenCount; i++) {
            SignalFieldType currentType = (SignalFieldType) hiddenColumns.get(i);
            if (Arrays.binarySearch(fieldIDs, currentType.getID()) >= 0) columnsToShow.add(currentType);
        }
        addColumnsToTable(columnsToShow.toArray());
        hiddenColumns.removeAll(columnsToShow);
    }

    /**
   * This method sets the selected columns for the dialog. It accepts the 
   * selected columns, which are instances of <CODE>SignalFieldType</CODE> in 
   * this case. The instances of <CODE>SignalFieldType</CODE> can have duplicate
   * field IDs. The duplicates are hidden, but if the column is dropped, all are 
   * displayed in the table.
   * 
   * @param selectedColumns The columns that are selected in the table.
   */
    @Override
    public void setSelectedColumns(ArrayList selectedColumns) {
        ArrayList fieldIDs = new ArrayList();
        for (int i = selectedColumns.size() - 1; i >= 0; i--) {
            String currentID = ((SignalFieldType) selectedColumns.get(i)).getID();
            if (fieldIDs.contains(currentID)) hiddenColumns.add(selectedColumns.remove(i)); else fieldIDs.add(currentID);
        }
        super.setSelectedColumns(selectedColumns);
    }
}
