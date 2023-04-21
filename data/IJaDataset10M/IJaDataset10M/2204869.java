package org.apache.poi.ss.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.record.formula.SheetNameFormatter;
import org.apache.poi.ss.SpreadsheetVersion;

/**
 *
 * @author  Avik Sengupta
 * @author  Dennis Doubleday (patch to seperateRowColumns())
 */
public class CellReference {

    /**
	 * Used to classify identifiers found in formulas as cell references or not.
	 */
    public enum NameType {

        CELL, NAMED_RANGE, COLUMN, ROW, BAD_CELL_OR_NAMED_RANGE
    }

    /** The character ($) that signifies a row or column value is absolute instead of relative */
    private static final char ABSOLUTE_REFERENCE_MARKER = '$';

    /** The character (!) that separates sheet names from cell references */
    private static final char SHEET_NAME_DELIMITER = '!';

    /** The character (') used to quote sheet names when they contain special characters */
    private static final char SPECIAL_NAME_DELIMITER = '\'';

    /**
	 * Matches a run of one or more letters followed by a run of one or more digits.
	 * The run of letters is group 1 and the run of digits is group 2.
	 * Each group may optionally be prefixed with a single '$'.
	 */
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("\\$?([A-Za-z]+)\\$?([0-9]+)");

    /**
	 * Matches a run of one or more letters.  The run of letters is group 1.
	 * The text may optionally be prefixed with a single '$'.
	 */
    private static final Pattern COLUMN_REF_PATTERN = Pattern.compile("\\$?([A-Za-z]+)");

    /**
	 * Matches a run of one or more digits.  The run of digits is group 1.
	 * The text may optionally be prefixed with a single '$'.
	 */
    private static final Pattern ROW_REF_PATTERN = Pattern.compile("\\$?([0-9]+)");

    /**
	 * Named range names must start with a letter or underscore.  Subsequent characters may include
	 * digits or dot.  (They can even end in dot).
	 */
    private static final Pattern NAMED_RANGE_NAME_PATTERN = Pattern.compile("[_A-Za-z][_.A-Za-z0-9]*");

    private final int _rowIndex;

    private final int _colIndex;

    private final String _sheetName;

    private final boolean _isRowAbs;

    private final boolean _isColAbs;

    /**
	 * Create an cell ref from a string representation.  Sheet names containing special characters should be
	 * delimited and escaped as per normal syntax rules for formulas.
	 */
    public CellReference(String cellRef) {
        String[] parts = separateRefParts(cellRef);
        _sheetName = parts[0];
        String colRef = parts[1];
        if (colRef.length() < 1) {
            throw new IllegalArgumentException("Invalid Formula cell reference: '" + cellRef + "'");
        }
        _isColAbs = colRef.charAt(0) == '$';
        if (_isColAbs) {
            colRef = colRef.substring(1);
        }
        _colIndex = convertColStringToIndex(colRef);
        String rowRef = parts[2];
        if (rowRef.length() < 1) {
            throw new IllegalArgumentException("Invalid Formula cell reference: '" + cellRef + "'");
        }
        _isRowAbs = rowRef.charAt(0) == '$';
        if (_isRowAbs) {
            rowRef = rowRef.substring(1);
        }
        _rowIndex = Integer.parseInt(rowRef) - 1;
    }

    public CellReference(int pRow, int pCol) {
        this(pRow, pCol, false, false);
    }

    public CellReference(int pRow, short pCol) {
        this(pRow, pCol & 0xFFFF, false, false);
    }

    public CellReference(int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        this(null, pRow, pCol, pAbsRow, pAbsCol);
    }

    public CellReference(String pSheetName, int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        if (pRow < -1) {
            throw new IllegalArgumentException("row index may not be negative");
        }
        if (pCol < -1) {
            throw new IllegalArgumentException("column index may not be negative");
        }
        _sheetName = pSheetName;
        _rowIndex = pRow;
        _colIndex = pCol;
        _isRowAbs = pAbsRow;
        _isColAbs = pAbsCol;
    }

    public int getRow() {
        return _rowIndex;
    }

    public short getCol() {
        return (short) _colIndex;
    }

    public boolean isRowAbsolute() {
        return _isRowAbs;
    }

    public boolean isColAbsolute() {
        return _isColAbs;
    }

    /**
	  * @return possibly <code>null</code> if this is a 2D reference.  Special characters are not
	  * escaped or delimited
	  */
    public String getSheetName() {
        return _sheetName;
    }

    public static boolean isPartAbsolute(String part) {
        return part.charAt(0) == ABSOLUTE_REFERENCE_MARKER;
    }

    /**
	 * takes in a column reference portion of a CellRef and converts it from
	 * ALPHA-26 number format to 0-based base 10.
	 * 'A' -> 0
	 * 'Z' -> 25
	 * 'AA' -> 26
	 * 'IV' -> 255
	 * @return zero based column index
	 */
    public static int convertColStringToIndex(String ref) {
        int pos = 0;
        int retval = 0;
        for (int k = ref.length() - 1; k >= 0; k--) {
            char thechar = ref.charAt(k);
            if (thechar == ABSOLUTE_REFERENCE_MARKER) {
                if (k != 0) {
                    throw new IllegalArgumentException("Bad col ref format '" + ref + "'");
                }
                break;
            }
            int shift = (int) Math.pow(26, pos);
            retval += (Character.getNumericValue(thechar) - 9) * shift;
            pos++;
        }
        return retval - 1;
    }

    /**
	 * Classifies an identifier as either a simple (2D) cell reference or a named range name
	 * @return one of the values from <tt>NameType</tt>
	 */
    public static NameType classifyCellReference(String str, SpreadsheetVersion ssVersion) {
        int len = str.length();
        if (len < 1) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
        char firstChar = str.charAt(0);
        switch(firstChar) {
            case ABSOLUTE_REFERENCE_MARKER:
            case '.':
            case '_':
                break;
            default:
                if (!Character.isLetter(firstChar) && !Character.isDigit(firstChar)) {
                    throw new IllegalArgumentException("Invalid first char (" + firstChar + ") of cell reference or named range.  Letter expected");
                }
        }
        if (!Character.isDigit(str.charAt(len - 1))) {
            return validateNamedRangeName(str, ssVersion);
        }
        Matcher cellRefPatternMatcher = CELL_REF_PATTERN.matcher(str);
        if (!cellRefPatternMatcher.matches()) {
            return validateNamedRangeName(str, ssVersion);
        }
        String lettersGroup = cellRefPatternMatcher.group(1);
        String digitsGroup = cellRefPatternMatcher.group(2);
        if (cellReferenceIsWithinRange(lettersGroup, digitsGroup, ssVersion)) {
            return NameType.CELL;
        }
        if (str.indexOf(ABSOLUTE_REFERENCE_MARKER) >= 0) {
            return NameType.BAD_CELL_OR_NAMED_RANGE;
        }
        return NameType.NAMED_RANGE;
    }

    private static NameType validateNamedRangeName(String str, SpreadsheetVersion ssVersion) {
        Matcher colMatcher = COLUMN_REF_PATTERN.matcher(str);
        if (colMatcher.matches()) {
            String colStr = colMatcher.group(1);
            if (isColumnWithnRange(colStr, ssVersion)) {
                return NameType.COLUMN;
            }
        }
        Matcher rowMatcher = ROW_REF_PATTERN.matcher(str);
        if (rowMatcher.matches()) {
            String rowStr = rowMatcher.group(1);
            if (isRowWithnRange(rowStr, ssVersion)) {
                return NameType.ROW;
            }
        }
        if (!NAMED_RANGE_NAME_PATTERN.matcher(str).matches()) {
            return NameType.BAD_CELL_OR_NAMED_RANGE;
        }
        return NameType.NAMED_RANGE;
    }

    /**
	 * Used to decide whether a name of the form "[A-Z]*[0-9]*" that appears in a formula can be
	 * interpreted as a cell reference.  Names of that form can be also used for sheets and/or
	 * named ranges, and in those circumstances, the question of whether the potential cell
	 * reference is valid (in range) becomes important.
	 * <p/>
	 * Note - that the maximum sheet size varies across Excel versions:
	 * <p/>
	 * <blockquote><table border="0" cellpadding="1" cellspacing="0"
	 *                 summary="Notable cases.">
	 *   <tr><th>Version&nbsp;&nbsp;</th><th>File Format&nbsp;&nbsp;</th>
	 *   	<th>Last Column&nbsp;&nbsp;</th><th>Last Row</th></tr>
	 *   <tr><td>97-2003</td><td>BIFF8</td><td>"IV" (2^8)</td><td>65536 (2^14)</td></tr>
	 *   <tr><td>2007</td><td>BIFF12</td><td>"XFD" (2^14)</td><td>1048576 (2^20)</td></tr>
	 * </table></blockquote>
	 * POI currently targets BIFF8 (Excel 97-2003), so the following behaviour can be observed for
	 * this method:
	 * <blockquote><table border="0" cellpadding="1" cellspacing="0"
	 *                 summary="Notable cases.">
	 *   <tr><th>Input&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
	 *       <th>Result&nbsp;</th></tr>
	 *   <tr><td>"A", "1"</td><td>true</td></tr>
	 *   <tr><td>"a", "111"</td><td>true</td></tr>
	 *   <tr><td>"A", "65536"</td><td>true</td></tr>
	 *   <tr><td>"A", "65537"</td><td>false</td></tr>
	 *   <tr><td>"iv", "1"</td><td>true</td></tr>
	 *   <tr><td>"IW", "1"</td><td>false</td></tr>
	 *   <tr><td>"AAA", "1"</td><td>false</td></tr>
	 *   <tr><td>"a", "111"</td><td>true</td></tr>
	 *   <tr><td>"Sheet", "1"</td><td>false</td></tr>
	 * </table></blockquote>
	 *
	 * @param colStr a string of only letter characters
	 * @param rowStr a string of only digit characters
	 * @return <code>true</code> if the row and col parameters are within range of a BIFF8 spreadsheet.
	 */
    public static boolean cellReferenceIsWithinRange(String colStr, String rowStr, SpreadsheetVersion ssVersion) {
        if (!isColumnWithnRange(colStr, ssVersion)) {
            return false;
        }
        return isRowWithnRange(rowStr, ssVersion);
    }

    public static boolean isColumnWithnRange(String colStr, SpreadsheetVersion ssVersion) {
        String lastCol = ssVersion.getLastColumnName();
        int lastColLength = lastCol.length();
        int numberOfLetters = colStr.length();
        if (numberOfLetters > lastColLength) {
            return false;
        }
        if (numberOfLetters == lastColLength) {
            if (colStr.toUpperCase().compareTo(lastCol) > 0) {
                return false;
            }
        } else {
        }
        return true;
    }

    public static boolean isRowWithnRange(String rowStr, SpreadsheetVersion ssVersion) {
        int rowNum = Integer.parseInt(rowStr);
        if (rowNum < 0) {
            throw new IllegalStateException("Invalid rowStr '" + rowStr + "'.");
        }
        if (rowNum == 0) {
            return false;
        }
        return rowNum <= ssVersion.getMaxRows();
    }

    /**
	 * Separates the row from the columns and returns an array of three Strings.  The first element
	 * is the sheet name. Only the first element may be null.  The second element in is the column
	 * name still in ALPHA-26 number format.  The third element is the row.
	 */
    private static String[] separateRefParts(String reference) {
        int plingPos = reference.lastIndexOf(SHEET_NAME_DELIMITER);
        String sheetName = parseSheetName(reference, plingPos);
        int start = plingPos + 1;
        int length = reference.length();
        int loc = start;
        if (reference.charAt(loc) == ABSOLUTE_REFERENCE_MARKER) {
            loc++;
        }
        for (; loc < length; loc++) {
            char ch = reference.charAt(loc);
            if (Character.isDigit(ch) || ch == ABSOLUTE_REFERENCE_MARKER) {
                break;
            }
        }
        return new String[] { sheetName, reference.substring(start, loc), reference.substring(loc) };
    }

    private static String parseSheetName(String reference, int indexOfSheetNameDelimiter) {
        if (indexOfSheetNameDelimiter < 0) {
            return null;
        }
        boolean isQuoted = reference.charAt(0) == SPECIAL_NAME_DELIMITER;
        if (!isQuoted) {
            return reference.substring(0, indexOfSheetNameDelimiter);
        }
        int lastQuotePos = indexOfSheetNameDelimiter - 1;
        if (reference.charAt(lastQuotePos) != SPECIAL_NAME_DELIMITER) {
            throw new RuntimeException("Mismatched quotes: (" + reference + ")");
        }
        StringBuffer sb = new StringBuffer(indexOfSheetNameDelimiter);
        for (int i = 1; i < lastQuotePos; i++) {
            char ch = reference.charAt(i);
            if (ch != SPECIAL_NAME_DELIMITER) {
                sb.append(ch);
                continue;
            }
            if (i < lastQuotePos) {
                if (reference.charAt(i + 1) == SPECIAL_NAME_DELIMITER) {
                    i++;
                    sb.append(ch);
                    continue;
                }
            }
            throw new RuntimeException("Bad sheet name quote escaping: (" + reference + ")");
        }
        return sb.toString();
    }

    /**
	 * Takes in a 0-based base-10 column and returns a ALPHA-26
	 *  representation.
	 * eg column #3 -> D
	 */
    public static String convertNumToColString(int col) {
        int excelColNum = col + 1;
        String colRef = "";
        int colRemain = excelColNum;
        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }
            colRemain = (colRemain - thisPart) / 26;
            char colChar = (char) (thisPart + 64);
            colRef = colChar + colRef;
        }
        return colRef;
    }

    /**
	 *  Example return values:
	 *	<table border="0" cellpadding="1" cellspacing="0" summary="Example return values">
	 *	  <tr><th align='left'>Result</th><th align='left'>Comment</th></tr>
	 *	  <tr><td>A1</td><td>Cell reference without sheet</td></tr>
	 *	  <tr><td>Sheet1!A1</td><td>Standard sheet name</td></tr>
	 *	  <tr><td>'O''Brien''s Sales'!A1'&nbsp;</td><td>Sheet name with special characters</td></tr>
	 *	</table>
	 * @return the text representation of this cell reference as it would appear in a formula.
	 */
    public String formatAsString() {
        StringBuffer sb = new StringBuffer(32);
        if (_sheetName != null) {
            SheetNameFormatter.appendFormat(sb, _sheetName);
            sb.append(SHEET_NAME_DELIMITER);
        }
        appendCellReference(sb);
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(formatAsString());
        sb.append("]");
        return sb.toString();
    }

    /**
	 * Returns the three parts of the cell reference, the
	 *  Sheet name (or null if none supplied), the 1 based
	 *  row number, and the A based column letter.
	 * This will not include any markers for absolute
	 *  references, so use {@link #formatAsString()}
	 *  to properly turn references into strings.
	 */
    public String[] getCellRefParts() {
        return new String[] { _sheetName, Integer.toString(_rowIndex + 1), convertNumToColString(_colIndex) };
    }

    void appendCellReference(StringBuffer sb) {
        if (_isColAbs) {
            sb.append(ABSOLUTE_REFERENCE_MARKER);
        }
        sb.append(convertNumToColString(_colIndex));
        if (_isRowAbs) {
            sb.append(ABSOLUTE_REFERENCE_MARKER);
        }
        sb.append(_rowIndex + 1);
    }
}
