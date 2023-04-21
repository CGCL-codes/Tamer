package org.inqle.user.data.extraction;

import java.util.List;

/**
 * @author David Donohue
 * Jan 1, 2008
 */
public class DataTableWriter {

    public static String dataTableToString(DataTable dataTable) {
        String s = "\n";
        s += "\n=======================================================================";
        s += "\n==============BEGIN: STRING REPRESENTATION OF DATATABLE==============";
        s += "\nID Column Index=";
        s += dataTable.getIdColumnIndex();
        s += columnsToString(dataTable);
        s += rowsToString(dataTable);
        s += "\n===============END: STRING REPRESENTATION OF DATATABLE===============";
        s += "\n=======================================================================";
        s += "\n";
        return s;
    }

    private static String rowsToString(DataTable dataTable) {
        String s = "\nDATA";
        List<List<DataCell>> rows = dataTable.getRows();
        int rowNum = 0;
        for (List<DataCell> row : rows) {
            s += "\nRow " + rowNum + ": " + rowToString(row);
            rowNum++;
        }
        return s;
    }

    private static String rowToString(List<DataCell> row) {
        String s = "";
        int i = 0;
        for (DataCell cell : row) {
            if (i > 0) s += " | ";
            s += i + "=" + cell.getStringVal();
            i++;
        }
        return s;
    }

    public static String columnsToString(DataTable dataTable) {
        String s = "\nCOLUMNS";
        List<DataColumn> columns = dataTable.getColumns();
        int i = 0;
        for (DataColumn column : columns) {
            s += "\n" + i + ") " + column.getQueryLabel() + ": <" + column.getColumnUri() + "> Path=" + column.getRdfPath();
            i++;
        }
        return s;
    }
}
