package es.optsicom.lib.analysis.table;

import java.security.InvalidParameterException;
import java.util.List;
import es.optsicom.lib.tablecreator.Alias;
import es.optsicom.lib.util.Properties;

public class ComplexTable {

    private double[][] values;

    private List<Properties> rowTitles;

    private List<Properties> columnTitles;

    private List<NumberFormat> numberFormats;

    private List<Alias> rowAliases;

    private List<Alias> columnAliases;

    public ComplexTable(List<Properties> rowTitles, List<Properties> columnTitles) {
        this(rowTitles, columnTitles, new double[rowTitles.size()][columnTitles.size()]);
    }

    public ComplexTable(List<Properties> rowTitles, List<Properties> columnTitles, double[][] values) {
        super();
        setTitlesAndData(rowTitles, columnTitles, values);
    }

    public void setTitlesAndData(List<Properties> rowTitles, List<Properties> columnTitles, double[][] values) {
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

    public List<Properties> getRowTitles() {
        return rowTitles;
    }

    public List<Properties> getColumnTitles() {
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

    public void setRowAliases(List<Alias> rowAliases) {
        this.rowAliases = rowAliases;
    }

    public List<Alias> getRowAliases() {
        return rowAliases;
    }

    public void setColumnAliases(List<Alias> columnAliases) {
        this.columnAliases = columnAliases;
    }

    public List<Alias> getColumnAliases() {
        return columnAliases;
    }
}
