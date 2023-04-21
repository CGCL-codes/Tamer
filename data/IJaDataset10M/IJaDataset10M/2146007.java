package Cosmo.util.cautils.datastructures;

import java.util.Iterator;
import java.util.Vector;
import Cosmo.util.Constants;
import Cosmo.util.Utils;

/**
 * @author sajjan
 * 
 * This class provides an object representation of a grid structure.
 * 
 *
 */
public class DataGrid {

    /**
	 * The underlying Vector of Vectors
	 */
    private Vector<Vector<String>> grid;

    /**
	 * Column names
	 */
    private Vector<String> columnNames;

    /**
	 * Constructors
	 * @param values
	 * @param columnNames
	 */
    public DataGrid(Vector<Vector<String>> values, Vector<String> columnNames) {
        this.columnNames = columnNames;
        this.grid = values;
    }

    public DataGrid(Vector<Vector<String>> values) {
        this.grid = values;
    }

    /**
	 * Gets value at row with index = index
	 * @param rowIndex
	 * @return Vector of values
	 */
    public Vector<String> getRow(int rowIndex) {
        if (rowIndex > this.grid.size()) return null;
        return this.grid.elementAt(rowIndex);
    }

    /**
	 * Gets value at column with index = index
	 * @param colIndex
	 * @return Vector of values
	 */
    public Vector<String> getColumn(int colIndex) {
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < this.grid.size(); i++) {
            v.add(this.grid.elementAt(i).elementAt(colIndex).toString());
        }
        return v;
    }

    /**
	 * Gets value at column with columnName
	 * @param columnName
	 * @return Vector of values
	 */
    public Vector<String> getColumn(String columnName) {
        for (int i = 0; i <= columnNames.size(); i++) {
            if (columnNames.elementAt(i).equalsIgnoreCase(columnName)) return getColumn(i);
        }
        return null;
    }

    /**
	 * Gets value at row,col
	 * @param row
	 * @param column
	 * @return
	 */
    public String getValueAt(int row, int column) {
        return this.grid.elementAt(row).elementAt(column).toString();
    }

    /**
	 * Utility method to print the grid in a formatted way
	 */
    public void printDataGrid() {
        if (this.columnNames != null && this.columnNames.size() > 0) {
            System.out.println("Column Names=" + this.columnNames);
        }
        if (this.grid == null || this.grid.size() == 0) {
            System.out.println("Data Grid Emtpy!");
        } else {
            for (int i = 0; i < this.getNoOfRows(); i++) {
                for (int j = 0; j < this.getNoOfColumns(); j++) {
                    System.out.print(this.getValueAt(i, j) + Constants.TAB);
                }
                System.out.print(Constants.NEW_LINE);
            }
        }
    }

    /**
	 * @return true if grid is empty
	 */
    public boolean isDataGridNullOrEmpty() {
        if (this.grid == null) return true; else if (this.grid.size() == 0) return true; else return false;
    }

    /**
	 * @return no of rows
	 */
    public int getNoOfRows() {
        if (!isDataGridNullOrEmpty()) {
            return this.grid.size();
        }
        return 0;
    }

    /**
	 * @return no of columns
	 */
    public int getNoOfColumns() {
        if (!isDataGridNullOrEmpty()) {
            return this.grid.elementAt(0).size();
        }
        return 0;
    }

    public String toString() {
        return "---------Contents of Datagrid----------" + "No of rows:" + getNoOfRows() + "\n" + "No of cols:" + getNoOfColumns() + "\n" + "Values" + getFormattedVector(this.grid) + "\n---------End of Contents of Datagrid----------";
    }

    /**
	 * Deletes blank rows
	 * @param whichColumnToCheck
	 */
    public void removeUnfilledRows(int whichColumnToCheck) {
        Iterator<Vector<String>> iter = this.grid.iterator();
        while (iter.hasNext()) {
            Vector<String> v = iter.next();
            if (v.elementAt(whichColumnToCheck) == null) {
                iter.remove();
            } else if ("".equals(v.elementAt(whichColumnToCheck))) {
                iter.remove();
            }
        }
    }

    /**
	 * Setter method
	 * @param row
	 * @param column
	 * @param value
	 */
    public void setValueAt(int row, int column, String value) {
        this.grid.elementAt(row).add(column, value);
    }

    /**
	 * @return the underlying grid
	 */
    public Vector<Vector<String>> getDataGridAsVector() {
        return this.grid;
    }

    public String getFormattedVector(Vector<Vector<String>> v) {
        String str = "";
        if (v == null) return str;
        for (int i = 0; i < v.size(); i++) {
            str += v.elementAt(i).toString() + "\n";
        }
        return str;
    }

    /**
	 * Appneds the input datagrid to the current one
	 * @param inputGrid
	 */
    public void append(DataGrid inputGrid) {
        int noOfRows = inputGrid.getNoOfRows();
        for (int i = 0; i < noOfRows; i++) {
            this.grid.add(inputGrid.getRow(i));
        }
    }

    public void addRow(Vector<String> row) {
        this.grid.add(row);
    }

    /**
	 * Getter
	 * @param rowIndex
	 * @param columnName
	 * @return
	 */
    public String getValueAt(int rowIndex, String columnName) {
        return getValueAt(rowIndex, columnNames.indexOf(columnName));
    }

    /**
	 * 
	 * @param existingCellValues
	 * @param existingCellValues2
	 * @param hasRowBeenDeleted
	 * @return-true if any add/delete has been made
	 */
    public static boolean hasAnyRowBeenAddedOrDeleted(DataGrid existingCellValues, DataGrid existingCellValues2, boolean hasRowBeenDeleted) {
        int noOfExistingValues = existingCellValues.getNoOfRows();
        int noOfNewValues = existingCellValues2.getNoOfRows();
        System.out.println("size of exisiting :" + noOfExistingValues + ",new:" + noOfNewValues);
        ;
        if (noOfExistingValues == noOfNewValues) if (hasRowBeenDeleted) return true; else return false; else return true;
    }

    public Vector<String> getRow(String SVName) {
        for (int i = 0; i < this.grid.size(); i++) {
            if ((this.grid.elementAt(i).elementAt(0)).equals(SVName)) return this.grid.elementAt(i);
        }
        return null;
    }
}
