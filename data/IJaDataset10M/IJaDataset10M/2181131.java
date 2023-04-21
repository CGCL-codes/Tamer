package net.sf.unofficialjunit.csv.filters;

import java.util.List;
import net.sf.unofficialjunit.csv.InvalidCSVFormatError;

/**
 * Convert a column into a boolean value. Boolean "strings" are : "true",
 * "false", "t", "f" (upper/lower case is ignored). If the string "ignore" is
 * encountered, the exception {@link CSVIgnoreRow} is thrown. Otherwise,
 * {@link InvalidCSVFormatError} is thrown.
 * 
 */
public class BooleanFilter extends ColumnFilterImpl {

    /**
     * @param column
     *            the column that is to be converted into a boolean value
     */
    public BooleanFilter(final int column) {
        super(column);
    }

    /**
     * @param column
     *            the column to be filtered
     * @param enableIgnore
     *            if true, and the string to be parsed is equal to the "ignore"
     *            , the filter will throw the exception {@link CSVIgnoreRow}.
     */
    public BooleanFilter(final int column, final boolean enableIgnore) {
        super(column, enableIgnore);
    }

    @Override
    public Boolean apply(final List<String> row) throws CSVIgnoreRow {
        String value = getColumn(row);
        if (value.compareToIgnoreCase("true") == 0 || value.compareToIgnoreCase("t") == 0) {
            return Boolean.TRUE;
        }
        if (value.compareToIgnoreCase("false") == 0 || value.compareToIgnoreCase("f") == 0) {
            return Boolean.FALSE;
        }
        throw new InvalidCSVFormatError("Cannot convert " + value + " to boolean.");
    }
}
