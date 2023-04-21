package br.ufg.integrate.wrapper.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *This class Class provides facility to navigate on the Result Set.
 *
 * @author     Chetan Gupta
 * @author     Rogerio Arantes Gaioso
 * @version    $Id: CSVScrollableReader.java,v 1.2 2007/09/19
 */
public class CSVScrollableReader extends CSVReaderAdapter {

    private static final int FIRST_RECORD = 0;

    private ArrayList<Integer> alRecordNos = null;

    private ArrayList<String> alRecordsArray = null;

    private int iRecordNo = 0;

    private int iRecordArrayNo = 0;

    /**
	 *Constructor for the CsvReader object
	 *
	 * @param  fileName       Description of Parameter
	 * @exception  Exception  Description of Exception
	 * @since
	 */
    public CSVScrollableReader(String fileName) throws Exception {
        this(fileName, ',', false, null);
    }

    /**
	 * Can be put in adpater apart from the last line
	 *
	 * Insert the method's description here.
	 *
	 * Creation date: (6-11-2001 15:02:42)
	 *
	 * @param  fileName                 java.lang.String
	 * @param  separator                char
	 * @param  suppressHeaders          boolean
	 * @exception  java.lang.Exception  The exception description.
	 * @since
	 */
    public CSVScrollableReader(String fileName, char separator, boolean suppressHeaders, String charset) throws java.lang.Exception {
        BufferedReader input = null;
        String buf = null;
        this.separator = separator;
        this.suppressHeaders = suppressHeaders;
        this.fileName = fileName;
        this.charset = charset;
        if (charset != null) {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
        } else {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        }
        if (this.suppressHeaders) {
            buf = input.readLine();
            String[] data = parseCsvLine(buf);
            columnNames = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                columnNames[i] = "COLUMN" + String.valueOf(i + 1);
            }
            data = null;
        } else {
            String headerLine = input.readLine();
            columnNames = parseCsvLine(headerLine);
        }
        loopAndFetchData(input, buf);
        fillRecordNoMapping();
        iRecordNo = FIRST_RECORD - 1;
        iRecordArrayNo = FIRST_RECORD - 1;
    }

    private void fillRecordNoMapping() throws SQLException {
        alRecordNos = new ArrayList<Integer>();
        iRecordNo = FIRST_RECORD - 1;
        iRecordArrayNo = FIRST_RECORD - 1;
        if (alRecordsArray.size() > 1) {
            alRecordNos.add(0, new Integer(0));
        }
        for (int i = 1; readDataFirstTime() && ((alRecordsArray.size() - 1) > iRecordArrayNo); i++) {
            alRecordNos.add(i, new Integer(iRecordArrayNo + 1));
        }
    }

    /**
	 * 
	 * @return
	 * @throws SQLException
	 */
    private boolean readDataFirstTime() throws SQLException {
        columns = new String[columnNames.length];
        String dataLine = null;
        iRecordArrayNo++;
        if ((iRecordArrayNo < FIRST_RECORD) || (iRecordArrayNo >= alRecordsArray.size())) {
            return false;
        }
        dataLine = (String) alRecordsArray.get(iRecordArrayNo);
        columns = parseCsvLine(dataLine);
        return true;
    }

    /**
	 *
	 * @return
	 * @exception  SQLException
	 * @since
	 */
    private boolean loopAndFetchData(BufferedReader input, String buf) throws SQLException {
        alRecordsArray = new ArrayList<String>();
        while (true) {
            columns = new String[columnNames.length];
            String dataLine = null;
            try {
                if (suppressHeaders && (buf != null)) {
                    dataLine = buf;
                    buf = null;
                } else {
                    dataLine = input.readLine();
                }
                if (dataLine == null) {
                    input.close();
                    return false;
                }
            } catch (IOException e) {
                throw new SQLException(e.toString());
            }
            alRecordsArray.add(dataLine);
        }
    }

    /**
	 * 
	 */
    protected String[] parseCsvLine(String line) throws SQLException {
        ArrayList<String> values = new ArrayList<String>();
        boolean inQuotedString = false;
        String value = "";
        String orgLine = line;
        int currentPos = 0;
        int fullLine = 0;
        while (fullLine == 0) {
            currentPos = 0;
            line += separator;
            while (currentPos < line.length()) {
                char currentChar = line.charAt(currentPos);
                if ((value.length() == 0) && (currentChar == '"') && !inQuotedString) {
                    currentPos++;
                    inQuotedString = true;
                    continue;
                }
                if (currentChar == '"') {
                    char nextChar = line.charAt(currentPos + 1);
                    if (nextChar == '"') {
                        value += currentChar;
                        currentPos++;
                    } else {
                        if (!inQuotedString) {
                            throw new SQLException("Unexpected '\"' in position " + currentPos + ". Line=" + orgLine);
                        }
                        if (inQuotedString && (nextChar != separator)) {
                            throw new SQLException("Expecting " + separator + " in position " + (currentPos + 1) + ". Line=" + orgLine);
                        }
                        values.add(value);
                        value = "";
                        inQuotedString = false;
                        currentPos++;
                    }
                } else {
                    if (currentChar == separator) {
                        if (inQuotedString) {
                            value += currentChar;
                        } else {
                            values.add(value);
                            value = "";
                        }
                    } else {
                        value += currentChar;
                    }
                }
                currentPos++;
            }
            if (inQuotedString) {
                value = value.substring(0, value.length() - 1);
                try {
                    line = (String) alRecordsArray.get(++iRecordArrayNo);
                } finally {
                }
            } else {
                fullLine = 1;
            }
        }
        return (String[]) values.toArray();
    }

    /**
	 * Method close.
	 */
    public void close() {
        alRecordNos = null;
        alRecordsArray = null;
    }

    /**
	 *
	 * @return Is has next element.
	 * @exception  SQLException 
	 * @since
	 */
    public boolean next() throws SQLException {
        ++iRecordNo;
        return readData();
    }

    /**
	 * Moves the cursor to the previous row in this
	 * <code>ResultSet</code> object.
	 *
	 * @return <code>true</code> if the cursor is on a valid row;
	 * <code>false</code> if it is off the result set
	 * @exception SQLException if a database access error
	 * occurs or the result set type is <code>TYPE_FORWARD_ONLY</code>
	 */
    public boolean previous() throws SQLException {
        --iRecordNo;
        return readData();
    }

    /**
	 * Retrieves whether the cursor is before the first row in
	 * this <code>ResultSet</code> object.
	 *
	 * @return <code>true</code> if the cursor is before the first row;
	 * <code>false</code> if the cursor is at any other position or the
	 * result set contains no rows
	 * @exception SQLException if a database access error occurs
	 */
    public boolean isBeforeFirst() throws SQLException {
        return (getRecordNo() < FIRST_RECORD);
    }

    /**
	 * Retrieves whether the cursor is after the last row in
	 * this <code>ResultSet</code> object.
	 *
	 * @return <code>true</code> if the cursor is after the last row;
	 * <code>false</code> if the cursor is at any other position or the
	 * result set contains no rows
	 * @exception SQLException if a database access error occurs
	 */
    public boolean isAfterLast() throws SQLException {
        return (getRecordNo() >= alRecordNos.size());
    }

    /**
	 * Retrieves whether the cursor is on the first row of
	 * this <code>ResultSet</code> object.
	 *
	 * @return <code>true</code> if the cursor is on the first row;
	 * <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs
	 */
    public boolean isFirst() throws SQLException {
        return (getRecordNo() == FIRST_RECORD);
    }

    /**
	 * Retrieves whether the cursor is on the last row of
	 * this <code>ResultSet</code> object.
	 * Note: Calling the method <code>isLast</code> may be expensive
	 * because the JDBC driver
	 * might need to fetch ahead one row in order to determine
	 * whether the current row is the last row in the result set.
	 *
	 * @return <code>true</code> if the cursor is on the last row;
	 * <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs
	 */
    public boolean isLast() throws SQLException {
        return (getRecordNo() == (alRecordNos.size() - 1));
    }

    /**
	 * Moves the cursor to the front of
	 * this <code>ResultSet</code> object, just before the
	 * first row. This method has no effect if the result set contains no rows.
	 *
	 * @exception SQLException if a database access error
	 * occurs or the result set type is <code>TYPE_FORWARD_ONLY</code>
	 */
    public void beforeFirst() throws SQLException {
        iRecordNo = FIRST_RECORD - 1;
    }

    /**
	 * Moves the cursor to the end of
	 * this <code>ResultSet</code> object, just after the
	 * last row. This method has no effect if the result set contains no rows.
	 * @exception SQLException if a database access error
	 * occurs or the result set type is <code>TYPE_FORWARD_ONLY</code>
	 */
    public void afterLast() throws SQLException {
        iRecordNo = alRecordNos.size();
    }

    /**
	 * Moves the cursor to the first row in
	 * this <code>ResultSet</code> object.
	 *
	 * @return <code>true</code> if the cursor is on a valid row;
	 * <code>false</code> if there are no rows in the result set
	 * @exception SQLException if a database access error
	 * occurs or the result set type is <code>TYPE_FORWARD_ONLY</code>
	 */
    public boolean first() throws SQLException {
        iRecordNo = FIRST_RECORD;
        return readData();
    }

    /**
	 * Moves the cursor to the last row in
	 * this <code>ResultSet</code> object.
	 *
	 * @return <code>true</code> if the cursor is on a valid row;
	 * <code>false</code> if there are no rows in the result set
	 * @exception SQLException if a database access error
	 * occurs or the result set type is <code>TYPE_FORWARD_ONLY</code>
	 */
    public boolean last() throws SQLException {
        iRecordNo = (alRecordNos.size() - 1);
        return readData();
    }

    /**
	 * Retrieves the current row number.  The first row is number 1, the
	 * second number 2, and so on.
	 *
	 * @return the current row number; <code>0</code> if there is no current row
	 * @exception SQLException if a database access error occurs
	 */
    public int getRow() throws SQLException {
        return (((getRecordNo() < FIRST_RECORD) || (getRecordNo() >= alRecordNos.size())) ? 0 : (getRecordNo() + 1));
    }

    /**
	 * Moves the cursor to the given row number in
	 * this <code>ResultSet</code> object.
	 *
	 * <p>If the row number is positive, the cursor moves to
	 * the given row number with respect to the
	 * beginning of the result set.  The first row is row 1, the second
	 * is row 2, and so on.
	 *
	 * <p>If the given row number is negative, the cursor moves to
	 * an absolute row position with respect to
	 * the end of the result set.  For example, calling the method
	 * <code>absolute(-1)</code> positions the
	 * cursor on the last row; calling the method <code>absolute(-2)</code>
	 * moves the cursor to the next-to-last row, and so on.
	 *
	 * <p>An attempt to position the cursor beyond the first/last row in
	 * the result set leaves the cursor before the first row or after
	 * the last row.
	 *
	 * <p><B>Note:</B> Calling <code>absolute(1)</code> is the same
	 * as calling <code>first()</code>. Calling <code>absolute(-1)</code>
	 * is the same as calling <code>last()</code>.
	 *
	 * @param row the number of the row to which the cursor should move.
	 *        A positive number indicates the row number counting from the
	 *        beginning of the result set; a negative number indicates the
	 *        row number counting from the end of the result set
	 * @return <code>true</code> if the cursor is on the result set;
	 * <code>false</code> otherwise
	 * @exception SQLException if a database access error
	 * occurs, or the result set type is <code>TYPE_FORWARD_ONLY</code>
	 */
    public boolean absolute(int row) throws SQLException {
        if (row >= 0) {
            iRecordNo = row - 1;
        } else {
            iRecordNo = alRecordNos.size() + (row);
        }
        return readData();
    }

    /**
	 * Moves the cursor a relative number of rows, either positive or negative.
	 * Attempting to move beyond the first/last row in the
	 * result set positions the cursor before/after the
	 * the first/last row. Calling <code>relative(0)</code> is valid, but does
	 * not change the cursor position.
	 *
	 * <p>Note: Calling the method <code>relative(1)</code>
	 * is identical to calling the method <code>next()</code> and
	 * calling the method <code>relative(-1)</code> is identical
	 * to calling the method <code>previous()</code>.
	 *
	 * @param rows an <code>int</code> specifying the number of rows to
	 *        move from the current row; a positive number moves the cursor
	 *        forward; a negative number moves the cursor backward
	 * @return <code>true</code> if the cursor is on a row;
	 *         <code>false</code> otherwise
	 * @exception SQLException if a database access error occurs,
	 *            there is no current row, or the result set type is
	 *            <code>TYPE_FORWARD_ONLY</code>
	 */
    public boolean relative(int rows) throws SQLException {
        iRecordNo = getRecordNo() + rows;
        return readData();
    }

    private int getRecordNo() {
        if (iRecordNo < FIRST_RECORD) {
            iRecordNo = FIRST_RECORD - 1;
        } else if (iRecordNo >= alRecordNos.size()) {
            iRecordNo = alRecordNos.size();
        }
        return iRecordNo;
    }

    /**
	 * 
	 * @return
	 * @throws SQLException
	 */
    private boolean readData() throws SQLException {
        columns = new String[columnNames.length];
        String dataLine = null;
        if ((getRecordNo() < FIRST_RECORD) || (getRecordNo() >= alRecordNos.size())) {
            return false;
        }
        iRecordArrayNo = ((Integer) alRecordNos.get(iRecordNo)).intValue();
        dataLine = (String) alRecordsArray.get(iRecordArrayNo);
        columns = parseCsvLine(dataLine);
        return true;
    }
}
