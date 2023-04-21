package net.sourceforge.ws_jdbc.jdbc;

import java.util.LinkedList;
import org.apache.axis.encoding.Base64;

public class ResultSet implements java.sql.ResultSet {

    net.sourceforge.ws_jdbc.client_stubs.ResultSet_T theData;

    Object[][] rows;

    int curRow = 0;

    public ResultSet(net.sourceforge.ws_jdbc.client_stubs.ResultSet_T theData) {
        this.theData = theData;
    }

    /*****************************************************
 **********                                 **********
 ********** Beginning of API implementation **********
 **********                                 **********
 *****************************************************/
    public boolean next() throws java.sql.SQLException {
        if (rows == null) if (theData.isBinary()) decodeRows(); else rows = theData.getRows();
        curRow++;
        return (curRow <= theData.getNumRows());
    }

    public void close() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean wasNull() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public String getString(int columnIndex) throws java.sql.SQLException {
        return (String) getObject(columnIndex);
    }

    public boolean getBoolean(int columnIndex) throws java.sql.SQLException {
        Boolean res = (Boolean) getObject(columnIndex);
        return res.booleanValue();
    }

    public byte getByte(int columnIndex) throws java.sql.SQLException {
        Byte res = (Byte) getObject(columnIndex);
        return res.byteValue();
    }

    public short getShort(int columnIndex) throws java.sql.SQLException {
        Short res = (Short) getObject(columnIndex);
        return res.shortValue();
    }

    public int getInt(int columnIndex) throws java.sql.SQLException {
        Integer res = (Integer) getObject(columnIndex);
        return res.intValue();
    }

    public long getLong(int columnIndex) throws java.sql.SQLException {
        Long res = (Long) getObject(columnIndex);
        return res.longValue();
    }

    public float getFloat(int columnIndex) throws java.sql.SQLException {
        Float res = (Float) getObject(columnIndex);
        return res.floatValue();
    }

    public double getDouble(int columnIndex) throws java.sql.SQLException {
        Double res = (Double) getObject(columnIndex);
        return res.doubleValue();
    }

    public java.math.BigDecimal getBigDecimal(int columnIndex, int scale) throws java.sql.SQLException {
        return (java.math.BigDecimal) getObject(columnIndex);
    }

    public byte[] getBytes(int columnIndex) throws java.sql.SQLException {
        return (byte[]) getObject(columnIndex);
    }

    public java.sql.Date getDate(int columnIndex) throws java.sql.SQLException {
        return (java.sql.Date) getObject(columnIndex);
    }

    public java.sql.Time getTime(int columnIndex) throws java.sql.SQLException {
        return (java.sql.Time) getObject(columnIndex);
    }

    public java.sql.Timestamp getTimestamp(int columnIndex) throws java.sql.SQLException {
        return (java.sql.Timestamp) getObject(columnIndex);
    }

    public java.io.InputStream getAsciiStream(int columnIndex) throws java.sql.SQLException {
        return (java.io.InputStream) getObject(columnIndex);
    }

    public java.io.InputStream getUnicodeStream(int columnIndex) throws java.sql.SQLException {
        return (java.io.InputStream) getObject(columnIndex);
    }

    public java.io.InputStream getBinaryStream(int columnIndex) throws java.sql.SQLException {
        return (java.io.InputStream) getObject(columnIndex);
    }

    public String getString(String columnName) throws java.sql.SQLException {
        return getString(findColumn(columnName));
    }

    public boolean getBoolean(String columnName) throws java.sql.SQLException {
        return getBoolean(findColumn(columnName));
    }

    public byte getByte(String columnName) throws java.sql.SQLException {
        return getByte(findColumn(columnName));
    }

    public short getShort(String columnName) throws java.sql.SQLException {
        return getShort(findColumn(columnName));
    }

    public int getInt(String columnName) throws java.sql.SQLException {
        return getInt(findColumn(columnName));
    }

    public long getLong(String columnName) throws java.sql.SQLException {
        return getLong(findColumn(columnName));
    }

    public float getFloat(String columnName) throws java.sql.SQLException {
        return getFloat(findColumn(columnName));
    }

    public double getDouble(String columnName) throws java.sql.SQLException {
        return getDouble(findColumn(columnName));
    }

    public java.math.BigDecimal getBigDecimal(String columnName, int scale) throws java.sql.SQLException {
        return getBigDecimal(findColumn(columnName), scale);
    }

    public byte[] getBytes(String columnName) throws java.sql.SQLException {
        return getBytes(findColumn(columnName));
    }

    public java.sql.Date getDate(String columnName) throws java.sql.SQLException {
        return getDate(findColumn(columnName));
    }

    public java.sql.Time getTime(String columnName) throws java.sql.SQLException {
        return getTime(findColumn(columnName));
    }

    public java.sql.Timestamp getTimestamp(String columnName) throws java.sql.SQLException {
        return getTimestamp(findColumn(columnName));
    }

    public java.io.InputStream getAsciiStream(String columnName) throws java.sql.SQLException {
        return getAsciiStream(findColumn(columnName));
    }

    public java.io.InputStream getUnicodeStream(String columnName) throws java.sql.SQLException {
        return getUnicodeStream(findColumn(columnName));
    }

    public java.io.InputStream getBinaryStream(String columnName) throws java.sql.SQLException {
        return getBinaryStream(findColumn(columnName));
    }

    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void clearWarnings() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public String getCursorName() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
        return new ResultSetMetaData(theData.getMetaData());
    }

    public Object getObject(int columnIndex) throws java.sql.SQLException {
        return rows[curRow - 1][columnIndex - 1];
    }

    public Object getObject(String columnName) throws java.sql.SQLException {
        return getObject(findColumn(columnName));
    }

    public int findColumn(String columnName) throws java.sql.SQLException {
        net.sourceforge.ws_jdbc.client_stubs.ResultSetMetaData_T metadata = theData.getMetaData();
        java.lang.String[] columnNames = metadata.getColumnNames();
        int res = -1;
        for (int i = 0; i < columnNames.length && res == -1; i++) if (columnNames[i].equalsIgnoreCase(columnName)) res = i;
        if (res > -1) return res; else throw new java.sql.SQLException("No column named" + columnName + " exists");
    }

    public java.io.Reader getCharacterStream(int columnIndex) throws java.sql.SQLException {
        return (java.io.Reader) getObject(columnIndex);
    }

    public java.io.Reader getCharacterStream(String columnName) throws java.sql.SQLException {
        return getCharacterStream(findColumn(columnName));
    }

    public java.math.BigDecimal getBigDecimal(int columnIndex) throws java.sql.SQLException {
        return (java.math.BigDecimal) getObject(columnIndex);
    }

    public java.math.BigDecimal getBigDecimal(String columnName) throws java.sql.SQLException {
        return getBigDecimal(findColumn(columnName));
    }

    public boolean isBeforeFirst() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean isAfterLast() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean isFirst() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean isLast() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void beforeFirst() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void afterLast() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean first() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean last() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public int getRow() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean absolute(int row) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean relative(int rows) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean previous() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void setFetchDirection(int direction) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public int getFetchDirection() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void setFetchSize(int rows) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public int getFetchSize() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public int getType() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public int getConcurrency() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean rowUpdated() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean rowInserted() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public boolean rowDeleted() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateNull(int columnIndex) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBoolean(int columnIndex, boolean x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateByte(int columnIndex, byte x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateShort(int columnIndex, short x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateInt(int columnIndex, int x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateLong(int columnIndex, long x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateFloat(int columnIndex, float x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateDouble(int columnIndex, double x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBigDecimal(int columnIndex, java.math.BigDecimal x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateString(int columnIndex, String x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBytes(int columnIndex, byte x[]) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateDate(int columnIndex, java.sql.Date x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateTime(int columnIndex, java.sql.Time x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateTimestamp(int columnIndex, java.sql.Timestamp x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateObject(int columnIndex, Object x, int scale) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateObject(int columnIndex, Object x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateNull(String columnName) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBoolean(String columnName, boolean x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateByte(String columnName, byte x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateShort(String columnName, short x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateInt(String columnName, int x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateLong(String columnName, long x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateFloat(String columnName, float x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateDouble(String columnName, double x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBigDecimal(String columnName, java.math.BigDecimal x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateString(String columnName, String x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBytes(String columnName, byte x[]) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateDate(String columnName, java.sql.Date x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateTime(String columnName, java.sql.Time x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateTimestamp(String columnName, java.sql.Timestamp x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateAsciiStream(String columnName, java.io.InputStream x, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBinaryStream(String columnName, java.io.InputStream x, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateCharacterStream(String columnName, java.io.Reader reader, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateObject(String columnName, Object x, int scale) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateObject(String columnName, Object x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void insertRow() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateRow() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void deleteRow() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void refreshRow() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void cancelRowUpdates() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void moveToInsertRow() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void moveToCurrentRow() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public java.sql.Statement getStatement() throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public Object getObject(int i, java.util.Map map) throws java.sql.SQLException {
        return rows[curRow - 1][i - 1];
    }

    public java.sql.Ref getRef(int i) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public java.sql.Blob getBlob(int i) throws java.sql.SQLException {
        return (java.sql.Blob) getObject(i);
    }

    public java.sql.Clob getClob(int i) throws java.sql.SQLException {
        return (java.sql.Clob) getObject(i);
    }

    public java.sql.Array getArray(int i) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public Object getObject(String columnName, java.util.Map map) throws java.sql.SQLException {
        return getObject(findColumn(columnName), map);
    }

    public java.sql.Ref getRef(String columnName) throws java.sql.SQLException {
        return getRef(findColumn(columnName));
    }

    public java.sql.Blob getBlob(String columnName) throws java.sql.SQLException {
        return getBlob(findColumn(columnName));
    }

    public java.sql.Clob getClob(String columnName) throws java.sql.SQLException {
        return getClob(findColumn(columnName));
    }

    public java.sql.Array getArray(String columnName) throws java.sql.SQLException {
        return getArray(findColumn(columnName));
    }

    public java.sql.Date getDate(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
        return (java.sql.Date) getObject(columnIndex);
    }

    public java.sql.Date getDate(String columnName, java.util.Calendar cal) throws java.sql.SQLException {
        return getDate(findColumn(columnName), cal);
    }

    public java.sql.Time getTime(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
        return (java.sql.Time) getObject(columnIndex);
    }

    public java.sql.Time getTime(String columnName, java.util.Calendar cal) throws java.sql.SQLException {
        return getTime(findColumn(columnName), cal);
    }

    public java.sql.Timestamp getTimestamp(int columnIndex, java.util.Calendar cal) throws java.sql.SQLException {
        return (java.sql.Timestamp) getObject(columnIndex);
    }

    public java.sql.Timestamp getTimestamp(String columnName, java.util.Calendar cal) throws java.sql.SQLException {
        return getTimestamp(findColumn(columnName), cal);
    }

    public java.net.URL getURL(int columnIndex) throws java.sql.SQLException {
        return (java.net.URL) getObject(columnIndex);
    }

    public java.net.URL getURL(String columnName) throws java.sql.SQLException {
        return getURL(findColumn(columnName));
    }

    public void updateRef(int columnIndex, java.sql.Ref x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateRef(String columnName, java.sql.Ref x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBlob(int columnIndex, java.sql.Blob x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateBlob(String columnName, java.sql.Blob x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateClob(int columnIndex, java.sql.Clob x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateClob(String columnName, java.sql.Clob x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateArray(int columnIndex, java.sql.Array x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void updateArray(String columnName, java.sql.Array x) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    /*****************************************************
	 **********                                 **********
	 **********    End of API implementation    **********
	 **********                                 **********
	 *****************************************************/
    private int getNumRows() {
        return theData.getNumRows();
    }

    public String toString() {
        return toString(getNumRows());
    }

    public String toString(int maxRows) {
        String res = "";
        int max = getNumRows();
        if (maxRows > -1 && maxRows < max) {
            res += "Showing " + maxRows + "/" + max + " results\n";
            max = maxRows;
        } else res += "Showing all " + max + " results\n";
        if (max > 0) {
            for (int i = 0; i < max; i++) {
                Object[] curRow = rows[i];
                Object curObject;
                for (int j = 0; j < curRow.length; j++) {
                    curObject = curRow[j];
                    if (curObject != null) {
                        if (curObject.getClass().getName().startsWith("[B")) {
                            byte[] curArray = (byte[]) curObject;
                            for (int k = 0; k < curArray.length; k++) res += curArray[k];
                        } else res += curObject.toString();
                    } else res += "null";
                    res += "\t";
                }
                res += "\n";
            }
        } else res = "No results retrieved";
        return res;
    }

    public void decodeRows() {
        String[] binaryRows = theData.getBinaryRows();
        int maxRow = binaryRows.length, maxCol;
        rows = new Object[maxRow][];
        LinkedList curRow;
        for (int curRowNum = 0; curRowNum < maxRow; curRowNum++) {
            curRow = net.sourceforge.ws_jdbc.shared.BinaryConversion.unpack(Base64.decode(binaryRows[curRowNum]));
            maxCol = curRow.size();
            rows[curRowNum] = new Object[maxCol];
            for (int curColNum = 0; curColNum < maxCol; curColNum++) rows[curRowNum][curColNum] = curRow.get(curColNum);
        }
    }
}
