package net.sf.excompcel.spreadsheet;

/**
 * 
 * <ul>
 * <li>C = Cell</li>
 * <li>S = Style</li>
 * <li>F = Font</li>
 * <li>T = Richtext</li>
 * </ul>
 * 
 * @author Detlev Struebig
 * @version 0.8
 */
public interface ECCellDataCommon<C, S, F, T> {

    public static final String TYPE_BLANK_STRING = "BLANK";

    public static final String TYPE_BOOLEAN_STRING = "BOOLEAN";

    public static final String TYPE_NUMERIC_STRING = "NUMERIC";

    public static final String TYPE_STRING_STRING = "STRING";

    public static final String TYPE_ERROR_STRING = "ERROR";

    public static final String TYPE_FORMULA_STRING = "FORMULA";

    public static final String TYPE_UNKNOWN_STRING = "UNKNOWN";

    /** default Text to fill cell, when it is a null cell. */
    public static final String FILL_NULL_TEXT = "[Null]";

    /**
	 * Get Name of Cell type.
	 * 
	 * @param cellType
	 *            Cell type as defined in original Cell Object.
	 * @return Cell type as String
	 */
    public String convertCellTypeToString(int cellType);

    /**
	 * 
	 * @param cell {@link ECCell}
	 * @return String of Formula Result.
	 */
    public String getFormulaResult(ECCell<C, S, F, T> cell);

    /**
	 * Get value of Cell as String.
	 * 
	 * @param cell
	 *            ECCell
	 * @return Cell Content as String. If the Parameter is null, it returns an
	 *         empty String. If there is no Content, it returns empty String.
	 */
    public String getStringForViewFromCell(ECCell<C, S, F, T> cell);

    /**
	 * Get value of ECCell as String.
	 * 
	 * @param cell
	 *            {@link ECCell}
	 * @return Cell Content as String. If the Parameter is null, it returns an
	 *         empty String. If there is no Content, it returns empty String.
	 */
    public String getStringFromCell(ECCell<C, S, F, T> cell);

    /**
	 * Convert the Cell Column Index to the Column Index Name.
	 * e.g.
	 * <ul>
	 * <li>0 to A</li>
	 * <li>9 to J</li>
	 * </ul>
	 * and so on.
	 * 
	 * @param index The Column Index.
	 * @return Column Index name (e.g. A or Z)
	 */
    public String convertColumnIndexToColumnName(int index);

    /**
	 * Convert the Column name to the Cell Column Index.
	 * e.g.
	 * <ul>
	 * <li>A to 0</li>
	 * <li>J to 9</li>
	 * </ul>
	 * and so on.
	 * 
	 * @param columnIndexName Column Name.
	 * @return Column Index name (e.g. A or Z)
	 */
    public int convertColumnNameToColumnIndex(String columnIndexName);
}
