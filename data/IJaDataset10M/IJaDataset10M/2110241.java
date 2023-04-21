package es.optsicom.lib.analysis.table;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import es.optsicom.lib.util.Sorter;

public class SimpleTable {

    private double[][] values;

    private List<String> rowTitles;

    private List<String> columnTitles;

    private List<NumberFormat> numberFormats;

    public SimpleTable(List<String> rowTitles, List<String> columnTitles) {
        this(rowTitles, columnTitles, new double[rowTitles.size()][columnTitles.size()]);
    }

    public SimpleTable(List<String> rowTitles, List<String> columnTitles, double[][] values) {
        super();
        this.rowTitles = rowTitles;
        this.columnTitles = columnTitles;
        this.values = values;
        if (rowTitles.size() != values.length || columnTitles.size() != values[0].length) {
            throw new InvalidParameterException("Row and Column titles must be coherent with values");
        }
    }

    public double[][] getValues() {
        return values;
    }

    public List<String> getRowTitles() {
        return rowTitles;
    }

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    public void setValue(double value, int row, int column) {
        values[row][column] = value;
    }

    public List<NumberFormat> getNumberFormats() {
        return numberFormats;
    }

    public void setNumberFormats(List<NumberFormat> numberFormats) {
        this.numberFormats = numberFormats;
    }

    public int getNumColumns() {
        return values[0].length;
    }

    public int getNumRows() {
        return values.length;
    }

    public void orderByColumn(int column) {
        double[] columnValues = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            columnValues[i] = values[i][column];
        }
        int[] newPostions = Sorter.sort(columnValues);
        double[][] oldValues = values;
        this.values = new double[values.length][values[0].length];
        List<String> oldRowTitles = rowTitles;
        rowTitles = new ArrayList<String>();
        for (int i = 0; i < newPostions.length; i++) {
            values[i] = oldValues[newPostions[i]];
            rowTitles.add(oldRowTitles.get(newPostions[i]));
        }
    }

    public SheetTable createSheetTable() {
        SheetTable st = new SheetTable(this.getNumRows() + 1, this.getNumColumns() + 1);
        int numColumn = 1;
        for (String columnTitle : this.columnTitles) {
            st.setCell(0, numColumn, new Cell(columnTitle));
            numColumn++;
        }
        int numRow = 1;
        for (String rowTitle : this.rowTitles) {
            st.setCell(numRow, 0, new Cell(rowTitle));
            numRow++;
        }
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                CellFormat format = null;
                if (numberFormats != null) {
                    format = numberFormats.get(j);
                }
                st.setCell(i + 1, j + 1, new Cell(values[i][j], format));
            }
        }
        return st;
    }
}
