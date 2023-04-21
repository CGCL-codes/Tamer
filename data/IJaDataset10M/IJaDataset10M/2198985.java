package gov.sns.apps.jeri.apps.dbimport;

import java.util.*;
import javax.swing.table.*;

/**
 * Holds the data displayed in the substitution tablein the 
 * <CODE>ImportFrameDialog</CODE>.
 * 
 * @author Chris Fowlkes
 */
public class SubstitutionTableModel extends AbstractTableModel {

    /**
   * Holds the macros associated with each file. The keys are file names and the 
   * values are double dimension arrays, the first row of which holds the macro
   * names associated with that file.
   */
    private HashMap data = new HashMap();

    /**
   * Holds the data for the currently selected file. The data consists of a
   * <CODE>HashMap</CODE> for each row containing the macro names for keys and 
   * the macro values for values.
   */
    private ArrayList currentData;

    /**
   * Holds the value in the second column of the last line in the model.
   */
    private Object newLineValue;

    /**
   * Holds the value in the third column of the last line in the model.
   */
    private Object newLineFile = "All";

    /**
   * Holds the names of the columns in the current file. There is one column for
   * each macro in the file and the column name is the macro name.
   */
    private String[] columnNames;

    /**
   * Holds the name of the file that is currently selected.
   */
    private String selectedFileName;

    /**
   * Creates a new <CODE>SubstitutionTableModel</CODE>.
   */
    public SubstitutionTableModel() {
    }

    /**
   * Gets the number of rows in the model.
   * 
   * @return The number of rows in the table.
   */
    public int getRowCount() {
        if (currentData == null) return 0; else return currentData.size();
    }

    /**
   * Gets the number of columns in the model.
   * 
   * @return The number of columns in the table.
   */
    public int getColumnCount() {
        if (columnNames == null) return 0; else return columnNames.length;
    }

    /**
   * Gets the value at the given cell.
   * 
   * @param rowIndex The row index of the cell to return the value of.
   * @param columnIndex The column index of the cell to return the value of.
   * @return The value for the given cell.
   */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((HashMap) currentData.get(rowIndex)).get(columnNames[columnIndex]);
    }

    /**
   * Gets the name of the given column.
   * 
   * @param column The index of the column to return the name of.
   */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
   * Determines of the given cell is editable or not. This method always returns
   * <CODE>true</CODE>.
   * 
   * @param rowIndex The row index of the cell to check.
   * @param columnIndex The column index of the cell to check.
   * @return <CODE>true</CODE>.
   */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
   * Sets the value at the given cell.
   * 
   * @param aValue The value for the given cell.
   * @param rowIndex The row index of the cell to set the value for.
   * @param columnIndex The column index of the cell to set the value for.
   */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ((HashMap) currentData.get(rowIndex)).put(columnNames[columnIndex], aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
   * Clears all of the data in the model.
   */
    public void clear() {
        data.clear();
        currentData = null;
        fireTableStructureChanged();
    }

    /**
   * Adds the macro and value to the table. This method does not validate the 
   * input.
   * 
   * @param fileName The name of the file to whcih the macro will be applied.
   * @param macros A <CODE>HashMap</CODE> containing the macro names as keys.
   */
    public void addMacroValues(String fileName, HashMap macros) {
        ArrayList fileMacros = (ArrayList) data.get(fileName);
        if (fileMacros == null) {
            fileMacros = new ArrayList();
            data.put(fileName, fileMacros);
        }
        fileMacros.add(macros);
        if (fileName.equals(getSelectedFileName())) {
            currentData = fileMacros;
            loadColumnNames();
            fireTableStructureChanged();
        }
    }

    /**
   * Sets the name of the selected file. Setting this determines what is 
   * displayed in the table representing the model.
   * 
   * @param selectedFileName The name of the file to display in the table.
   */
    public void setSelectedFileName(String selectedFileName) {
        this.selectedFileName = selectedFileName;
        currentData = (ArrayList) data.get(selectedFileName);
        loadColumnNames();
        fireTableStructureChanged();
    }

    /**
   * Loads the individual macro names associated with the file currently 
   * selected.
   */
    private void loadColumnNames() {
        if (currentData == null) columnNames = null; else {
            int rowCount = currentData.size();
            ArrayList newColumnNames = new ArrayList();
            for (int i = 0; i < rowCount; i++) {
                Iterator rowMacros = ((HashMap) currentData.get(i)).keySet().iterator();
                while (rowMacros.hasNext()) {
                    Object currentMacro = rowMacros.next();
                    if (!newColumnNames.contains(currentMacro)) newColumnNames.add(currentMacro);
                }
            }
            columnNames = new String[newColumnNames.size()];
            newColumnNames.toArray(columnNames);
        }
    }

    /**
   * Returns the name of the selected file.
   * 
   * @return The name of the selected file.
   */
    public String getSelectedFileName() {
        return selectedFileName;
    }

    /**
   * Gets the macros associated with the given file.
   * 
   * 
   * @param fileName The name of the file or which to return the macros.
   * @return An <CODE>ArrayList</CODE> containing all of the macro sets for the file as instances of <CODE>HashMap</CODE>.
   */
    public ArrayList getFileMacros(String fileName) {
        return (ArrayList) data.get(fileName);
    }

    /**
   * Returns the macros for all files in a <CODE>HashMap</CODE> with the file
   * names as keys and the <CODE>ArrayList</CODE> that would be returned ny the
   * <CODE>getFileMacros</CODE> method for each file.
   * 
   * @return The macros for all files.
   */
    public HashMap getAllFileMacros() {
        return data;
    }
}
