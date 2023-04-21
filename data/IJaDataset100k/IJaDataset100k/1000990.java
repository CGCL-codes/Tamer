package KFramework30.Widgets.DataBrowser.UI;

import KFramework30.Widgets.DataBrowser.filterClass;
import javax.swing.*;
import java.util.*;
import java.awt.Component;
import KFramework30.Base.*;
import KFramework30.Widgets.KDataBrowserBaseClass;

public class customCriteriaClass extends javax.swing.table.AbstractTableModel {

    private KConfigurationClass configuration;

    private KLogClass log;

    private java.awt.Window parentWindow;

    private KDataBrowserBaseClass targetBrowser;

    private String[][] rowData;

    private java.util.List headers;

    /**     Check numeric data format.
            The format is operator (not, < and >) + decimal digit and 
            delimiter character ','. */
    private boolean isValidData(String data) {
        boolean returnVal = true;
        boolean pointFlag = false;
        StringTokenizer tokens = new StringTokenizer(data.trim(), ",");
        while (tokens.hasMoreTokens()) {
            String tempData = tokens.nextToken().trim();
            if (tempData.length() != 0) {
                if (tempData.startsWith("NOT") || tempData.startsWith("not")) tempData = tempData.substring(3); else if (tempData.startsWith("<") || tempData.startsWith(">")) tempData = tempData.substring(1);
                tempData = tempData.trim();
                if (tempData.length() != 0) {
                    for (int i = 0; i < tempData.length(); i++) {
                        if (pointFlag && tempData.charAt(i) == '.') {
                            returnVal = false;
                            break;
                        } else if (!pointFlag && tempData.charAt(i) == '.') pointFlag = true; else if (!Character.isDigit(tempData.charAt(i))) {
                            returnVal = false;
                            break;
                        }
                    }
                } else returnVal = false;
            }
        }
        return returnVal;
    }

    /**     Go through each cell. If it is numeric type, check the data format.
            If one is invalid format, display error message and return false. */
    public boolean isValidTableData() throws KExceptionClass {
        boolean returnVal = true;
        for (int col = 0; col < headers.size(); col++) {
            String name = (String) headers.get(col);
            int columnType = targetBrowser.getColumnType(name);
            for (int row = 0; row < rowData.length; row++) {
                if ((columnType == KDataBrowserBaseClass.BROWSER_COLUMN_TYPE_NUMERIC || columnType == KDataBrowserBaseClass.BROWSER_COLUMN_TYPE_NUMERIC2 || columnType == KDataBrowserBaseClass.BROWSER_COLUMN_TYPE_CURRENCY) && rowData[row][col] != null && rowData[row][col].length() != 0 && !isValidData(rowData[row][col])) {
                    returnVal = false;
                    String message = "*** Invalid input data **** \n" + "'" + rowData[row][col] + "' is invalid data at row [" + row + "], column [" + col + "].";
                    KMetaUtilsClass.showErrorMessageFromText1(parentWindow, message);
                }
            }
        }
        return returnVal;
    }

    /** Creates new customCriteriaClass */
    public customCriteriaClass(KConfigurationClass configurationParam, KLogClass logParam, java.awt.Window parentWindowParam, KDataBrowserBaseClass tableFillerParam) throws KExceptionClass {
        super();
        configuration = configurationParam;
        log = logParam;
        parentWindow = parentWindowParam;
        targetBrowser = tableFillerParam;
        headers = new ArrayList();
        targetBrowser.getColumnNames(headers);
        String[][] tempRowData = targetBrowser.GetCustomCriteriaRowData();
        rowData = new String[tempRowData.length][tempRowData[0].length];
        for (int row = 0; row < tempRowData.length; row++) for (int col = 0; col < tempRowData[0].length; col++) if (tempRowData[row][col] != null) rowData[row][col] = new String(tempRowData[row][col]);
    }

    /** Return visible table size  */
    public int getRowCount() {
        return rowData.length;
    }

    /** Return column size  */
    public int getColumnCount() {
        return headers.size();
    }

    /** Return table value at row , col  */
    public Object getValueAt(int row, int col) {
        return (Object) rowData[row][col];
    }

    /** Return column name at col  */
    public String getColumnName(int col) {
        return (String) headers.get(col);
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = (String) value;
        fireTableCellUpdated(row, col);
    }

    /**     Retrieve data from the table  */
    public void processCriteria() throws KExceptionClass {
        targetBrowser.clearCustomCriteria();
        for (int row = 0; row < rowData.length; row++) {
            java.util.List rowFilters = new ArrayList();
            for (int col = 0; col < headers.size(); col++) {
                String name = (String) headers.get(col);
                int columnType = targetBrowser.getColumnType(name);
                String tempData;
                if (rowData[row][col] != null && ((String) rowData[row][col]).trim().length() != 0) {
                    StringTokenizer tokens = new StringTokenizer(((String) rowData[row][col]).trim(), ",");
                    while (tokens.hasMoreTokens()) {
                        filterClass cellValue = new filterClass(name, tokens.nextToken().trim(), columnType);
                        rowFilters.add(cellValue);
                    }
                }
            }
            if (rowFilters.size() != 0) targetBrowser.addCustomCriteria(rowFilters);
        }
        targetBrowser.setCustomCriteriaRowData(rowData);
        targetBrowser.refresh();
    }

    /**     Clear all data in the table  */
    public void clearAll() {
        for (int row = 0; row < rowData.length; row++) for (int col = 0; col < headers.size(); col++) rowData[row][col] = null;
        fireTableDataChanged();
    }
}
