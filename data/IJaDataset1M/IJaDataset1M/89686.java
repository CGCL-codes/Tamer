package net.cattaka.rdbassistant.gui.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.RdbaConnectionUtil;
import net.cattaka.rdbassistant.sql.ColumnConverter;

public class StaticResultSetTableModel extends AbstractTableModel implements ResultSetTableModel {

    private static final long serialVersionUID = 1L;

    private PropertyTable propertyTable;

    public StaticResultSetTableModel() {
        propertyTable = new PropertyTable();
    }

    public void extractResultSetData(RdbaConnection rdbConnection, ResultSet resultSet, String nullString) throws SQLException {
        propertyTable = new PropertyTable();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        String[] columnNames = new String[rsmd.getColumnCount()];
        int[] columnCharacters = new int[rsmd.getColumnCount()];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = rsmd.getColumnLabel(i + 1);
            columnCharacters[i] = columnNames[i].length();
        }
        ColumnConverter[] columnConverterList = RdbaConnectionUtil.createColumnConverterList(rdbConnection, rsmd);
        Class<?>[] columnClasses = new Class<?>[columnConverterList.length];
        for (int i = 0; i < columnConverterList.length; i++) {
            columnClasses[i] = columnConverterList[i].getOutClass();
        }
        propertyTable.setColumnNames(columnNames);
        propertyTable.setColumnClasses(columnClasses);
        ArrayList<Object[]> columnValues = new ArrayList<Object[]>();
        while (resultSet.next()) {
            Object[] objArray = new Object[columnNames.length];
            for (int i = 0; i < objArray.length; i++) {
                Object obj = rdbConnection.extractResultSetData(resultSet, columnClasses[i], i + 1);
                if (obj == null) {
                    obj = nullString;
                }
                objArray[i] = obj;
            }
            columnValues.add(objArray);
        }
        {
            for (Object[] objArray : columnValues) {
                for (int i = 0; i < objArray.length; i++) {
                    if (objArray[i] instanceof String) {
                        int l = objArray[i].toString().length();
                        if (columnCharacters[i] < l) {
                            columnCharacters[i] = l;
                        }
                    }
                }
            }
            for (int i = 0; i < columnCharacters.length; i++) {
                if (columnCharacters[i] < RdbaConstants.MINIMUM_COLUMN_CHARACTERS) {
                    columnCharacters[i] = RdbaConstants.MINIMUM_COLUMN_CHARACTERS;
                }
                if (columnCharacters[i] > RdbaConstants.MAXIMUM_COLUMN_CHARACTERS) {
                    columnCharacters[i] = RdbaConstants.MAXIMUM_COLUMN_CHARACTERS;
                }
            }
        }
        propertyTable.setColumnCharacters(columnCharacters);
        propertyTable.setColumnValues(columnValues);
    }

    public void setTableName(String tableName) {
        this.propertyTable.setTableName(tableName);
    }

    public String getTableName() {
        return this.propertyTable.getTableName();
    }

    public int[] getColumnCharacters() {
        return this.propertyTable.getColumnCharacters();
    }

    public String getColumnName(int column) {
        return this.propertyTable.getColumnNames()[column];
    }

    public Object getRowName(int row) {
        return Integer.valueOf(row + 1);
    }

    public Class<?> getRowHeaderClass() {
        return Integer.class;
    }

    public int getColumnCount() {
        return this.propertyTable.getColumnNames().length;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return this.propertyTable.getColumnClasses()[columnIndex];
    }

    public int getRowCount() {
        return this.propertyTable.getColumnValues().size();
    }

    public Object getValueAt(int arg0, int arg1) {
        try {
            return this.propertyTable.getColumnValues().get(arg0)[arg1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
