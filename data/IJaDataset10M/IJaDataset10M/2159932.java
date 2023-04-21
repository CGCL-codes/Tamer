package com.codestudio.sql;

import com.codestudio.util.JDBCPool;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * This JDBC 2.0 ResultSet implementation transforms
 * underlying ResultSets so they can support more advanced
 * JDBC 2.0 features (scrolling, updates, etc.).
 * <p>
 * It is implemented as an ArrayList representing the rows
 * in a ResultSet. Each element in this ArrayList is a "row"
 * that is in turn represented as an ArrayList of
 * com.codestudio.sql.Result objects.
 * <p>
 * A cursor, implemented as a simple int index, indicates the
 * current row by indicating the index of the element in the
 * top-level ArrayList of rows.
 * <p>
 * Yet another ArrayList represents a virtual row, one that is
 * based on the current row but is being updated. This structure
 * is used to permanently alter the original row when <code>
 * updateRow()</code> is invoked. If that method is never invoked,
 * then the changes are discarded via the clearUpdates() method,
 * which is invoked whenever the cursor moves.
 *
 * @author PS Neville
 */
public class PoolManResultSet implements ResultSet {

    /** The PoolManStatement that generated this ResultSet. */
    private Statement statement;

    /** The array of Row objects in this ResultSet. */
    private ArrayList rowlist;

    /** The current cursor position. */
    private int pos;

    /** The index of the last read column. */
    private int lastColIndex;

    /** The scrollable type of this ResultSet, TYPE_FORWARD_ONLY by default. */
    private int scrollableType = TYPE_FORWARD_ONLY;

    /** The concurrency type, CONCUR_READ_ONLY by default. */
    private int concurType = CONCUR_READ_ONLY;

    /** The fetch direction of this ResultSet, FETCH_UNKNOWN by default. */
    private int fetchDirection = FETCH_UNKNOWN;

    private int fetchSize = 1;

    /** Metadata returned by the underlying driver */
    private ResultSetMetaData metaData;

    /**
     * The list of new Results with which to update the current row
     * once updateRow() is called.
     */
    private ArrayList updatedResults;

    private ArrayList updatedIndexes;

    /**
     * The insert row implementation.
     */
    private ArrayList insertRow;

    private boolean onInsertRow = false;

    private ArrayList insertedIndexes;

    private boolean closed = false;

    public PoolManResultSet() {
    }

    public PoolManResultSet(Statement s, ArrayList rowlist, ResultSetMetaData meta, int rScrollableType, int rConcurType) throws java.sql.SQLException {
        this.pos = 0;
        this.statement = s;
        this.rowlist = rowlist;
        this.metaData = PoolManResultSetMetaData.getCopy(meta);
        this.scrollableType = rScrollableType;
        this.concurType = rConcurType;
        this.updatedResults = new ArrayList();
        composeInsertRow();
        this.updatedIndexes = new ArrayList();
        this.insertedIndexes = new ArrayList();
    }

    public void setScrollableType(int n) {
        this.scrollableType = n;
    }

    public void setConcurrencyType(int n) {
        this.concurType = n;
    }

    public ResultSet cloneSet() {
        try {
            ResultSet set = new PoolManResultSet(this.statement, this.rowlist, this.metaData, this.scrollableType, this.concurType);
            return set;
        } catch (java.sql.SQLException e) {
            return null;
        }
    }

    /**
     * Move the cursor to a specific row number.
     * NOTE: row 1 and pos 1 == rowlist index of 0.
     */
    public boolean absolute(int row) throws SQLException {
        clearUpdates();
        if (row == 0) throw new SQLException("Invalid Row Number 0");
        if (scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Invalid method call for this ResultSet type");
        if (row < 0) {
            if (Math.abs(row) > rowlist.size()) {
                beforeFirst();
                return false;
            } else {
                pos = rowlist.size() + row + 1;
                return true;
            }
        }
        if (row > rowlist.size()) {
            afterLast();
            return false;
        }
        pos = row;
        return true;
    }

    /** Move the cursor one step beyond the final row. */
    public void afterLast() throws SQLException {
        if (this.scrollableType == TYPE_FORWARD_ONLY) {
            throw new SQLException("Illegal Operation when ResultSet is TYPE_FORWARD_ONLY");
        }
        clearUpdates();
        this.pos = rowlist.size() + 1;
    }

    /** Move the cursor one step before the first row. */
    public void beforeFirst() throws SQLException {
        if (this.scrollableType == TYPE_FORWARD_ONLY) {
            throw new SQLException("Illegal Operation when ResultSet is TYPE_FORWARD_ONLY");
        }
        clearUpdates();
        this.pos = 0;
    }

    /** Clear the updates made to the current row. */
    public void cancelRowUpdates() throws SQLException {
        if (this.concurType == CONCUR_READ_ONLY) {
            throw new SQLException("Illegal Operation when ResultSet is CONCUR_READ_ONLY");
        }
        clearUpdates();
    }

    public void clearWarnings() throws SQLException {
        assertNotInserting();
    }

    public void close() throws SQLException {
        assertNotInserting();
        this.updatedResults = null;
        this.insertRow = null;
        this.rowlist = null;
        ((PoolManStatement) statement).removeOpenResultSet(this);
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    /** Delete the current row from this ResultSet AND from the database. */
    public void deleteRow() throws SQLException {
        if (pos > rowlist.size()) throw new SQLException("Illegal Operation, cursor is after the last row");
        if (pos < 1) throw new SQLException("Illegal Operation, cursor is before the first row");
        assertNotInserting();
        ArrayList row;
        synchronized (rowlist) {
            row = (ArrayList) rowlist.get(pos - 1);
        }
        ArrayList touchedTables = new ArrayList(1);
        for (int i = 0; i < row.size(); i++) {
            com.codestudio.sql.Result result = (com.codestudio.sql.Result) row.get(i);
            if (touchedTables.contains(result.tableName)) continue;
            String deleteSQL;
            if (result.colValue instanceof java.lang.String) {
                deleteSQL = "DELETE FROM " + result.tableName + " WHERE " + result.colName + " = '" + result.colValue.toString() + "'";
            } else {
                deleteSQL = "DELETE FROM " + result.tableName + " WHERE " + result.colName + " = " + result.colValue.toString();
            }
            com.codestudio.sql.PoolManStatement smarts = (PoolManStatement) statement;
            Connection con = smarts.getConnection();
            Statement stm = con.createStatement();
            stm.execute(deleteSQL);
            touchedTables.add(result.tableName);
        }
        synchronized (rowlist) {
            rowlist.set(pos - 1, null);
            row = null;
        }
    }

    /**
     * Retrieve the index of a column given its label.
     * @param String The column name
     * @return The index of the column
     */
    public int findColumn(String colName) throws SQLException {
        try {
            ArrayList row = (ArrayList) rowlist.get(0);
            for (int i = 0; i < row.size(); i++) {
                Result r = (Result) row.get(i);
                if (r.colName.equalsIgnoreCase(colName)) return 1 + i;
            }
        } catch (Exception e) {
        }
        throw new SQLException("COLUMN NAME INVALID: " + colName);
    }

    /**
     * Move the cursor to the first row in the ResultSet.
     * @return true if the row is valid, false if
     * the ResultSet is empty
     */
    public boolean first() throws SQLException {
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Invalid method call for this ResultSet type TYPE_FORWARD_ONLY");
        clearUpdates();
        this.pos = 1;
        return true;
    }

    public Array getArray(int i) throws SQLException {
        try {
            return (Array) getObject(i);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Array getArray(String colName) throws SQLException {
        try {
            return (Array) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public InputStream getAsciiStream(int colIndex) throws SQLException {
        try {
            return (InputStream) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public InputStream getAsciiStream(String colName) throws SQLException {
        try {
            return (InputStream) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public BigDecimal getBigDecimal(int colIndex) throws SQLException {
        try {
            return (BigDecimal) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    /** @deprecated */
    public BigDecimal getBigDecimal(int colIndex, int scale) throws SQLException {
        try {
            return (BigDecimal) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public BigDecimal getBigDecimal(String colName) throws SQLException {
        try {
            return (BigDecimal) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    /** @deprecated */
    public BigDecimal getBigDecimal(String colName, int scale) throws SQLException {
        try {
            return (BigDecimal) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public InputStream getBinaryStream(int colIndex) throws SQLException {
        try {
            return (InputStream) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public InputStream getBinaryStream(String colName) throws SQLException {
        try {
            return (InputStream) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Blob getBlob(int i) throws SQLException {
        try {
            return (Blob) getObject(i);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Blob getBlob(String colName) throws SQLException {
        try {
            return (Blob) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public boolean getBoolean(int colIndex) throws SQLException {
        try {
            Boolean b = (Boolean) getObject(colIndex);
            return b.booleanValue();
        } catch (NullPointerException ne) {
        }
        return false;
    }

    public boolean getBoolean(String colName) throws SQLException {
        try {
            Boolean b = (Boolean) getObject(colName);
            return b.booleanValue();
        } catch (NullPointerException ne) {
        }
        return false;
    }

    public byte getByte(int colIndex) throws SQLException {
        try {
            Byte b = (Byte) getObject(colIndex);
            return b.byteValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public byte getByte(String colName) throws SQLException {
        try {
            Byte b = (Byte) getObject(colName);
            return b.byteValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public byte[] getBytes(int colIndex) throws SQLException {
        try {
            try {
                return ((byte[]) getObject(colIndex));
            } catch (java.lang.ClassCastException cce) {
                return ((String) getObject(colIndex)).getBytes();
            }
        } catch (NullPointerException ne) {
        }
        return new byte[] { -1 };
    }

    public byte[] getBytes(String colName) throws SQLException {
        try {
            try {
                return ((byte[]) getObject(colName));
            } catch (java.lang.ClassCastException cce) {
                return ((String) getObject(colName)).getBytes();
            }
        } catch (NullPointerException ne) {
        }
        return new byte[] { -1 };
    }

    public Reader getCharacterStream(int colIndex) throws SQLException {
        try {
            return (Reader) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Reader getCharacterStream(String colName) throws SQLException {
        try {
            return (Reader) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Clob getClob(int colIndex) throws SQLException {
        try {
            return (Clob) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Clob getClob(String colName) throws SQLException {
        try {
            return (Clob) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public int getConcurrency() throws SQLException {
        return this.concurType;
    }

    /**
     * This implementation does not support named cursors.
     * ResultSets can be easily updated without them now,
     * so they are not necessary.
     */
    public String getCursorName() throws SQLException {
        throw new UnsupportedOperationException("The PoolMan Library Does Not Support Named Cursors");
    }

    public java.sql.Date getDate(int colIndex) throws SQLException {
        try {
            if (getObject(colIndex) instanceof java.sql.Timestamp) {
                java.sql.Timestamp timestamp = (java.sql.Timestamp) getObject(colIndex);
                return new java.sql.Date(timestamp.getTime() + timestamp.getNanos() / 1000000);
            } else {
                return (java.sql.Date) getObject(colIndex);
            }
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public java.sql.Date getDate(int colIndex, Calendar cal) throws SQLException {
        try {
            java.sql.Date d = getDate(colIndex);
            DateFormat df = DateFormat.getInstance();
            df.setCalendar(cal);
            java.util.Date newDate = df.parse(d.toString());
            return new java.sql.Date(newDate.getTime());
        } catch (NullPointerException ne) {
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public java.sql.Date getDate(String colName) throws SQLException {
        try {
            if (getObject(colName) instanceof java.sql.Timestamp) {
                java.sql.Timestamp timestamp = (java.sql.Timestamp) getObject(colName);
                return new java.sql.Date(timestamp.getTime() + timestamp.getNanos() / 1000000);
            } else {
                return (java.sql.Date) getObject(colName);
            }
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public java.sql.Date getDate(String colName, Calendar cal) throws SQLException {
        try {
            java.sql.Date d = getDate(colName);
            DateFormat df = DateFormat.getInstance();
            df.setCalendar(cal);
            java.util.Date newDate = df.parse(d.toString());
            return new java.sql.Date(newDate.getTime());
        } catch (NullPointerException ne) {
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public double getDouble(int colIndex) throws SQLException {
        try {
            Number i = (Number) getObject(colIndex);
            return i.doubleValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public double getDouble(String colName) throws SQLException {
        try {
            Number i = (Number) getObject(colName);
            return i.doubleValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public int getFetchDirection() throws SQLException {
        return this.fetchDirection;
    }

    public int getFetchSize() throws SQLException {
        return this.fetchSize;
    }

    public float getFloat(int colIndex) throws SQLException {
        try {
            Number i = (Number) getObject(colIndex);
            return i.floatValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public float getFloat(String colName) throws SQLException {
        try {
            Number i = (Number) getObject(colName);
            return i.floatValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public int getInt(int colIndex) throws SQLException {
        try {
            Number i = (Number) getObject(colIndex);
            return i.intValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public int getInt(String colName) throws SQLException {
        try {
            Number i = (Number) getObject(colName);
            return i.intValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public long getLong(int colIndex) throws SQLException {
        try {
            Number i = (Number) getObject(colIndex);
            return i.longValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public long getLong(String colName) throws SQLException {
        try {
            Number i = (Number) getObject(colName);
            return i.longValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return PoolManResultSetMetaData.getCopy(this.metaData);
    }

    public Object getObject(int colIndex) throws SQLException {
        lastColIndex = colIndex;
        ArrayList row = (ArrayList) rowlist.get(pos - 1);
        try {
            Result result = (Result) row.get(colIndex - 1);
            return result.colValue;
        } catch (NullPointerException ne) {
        }
        return null;
    }

    /** Custom UDT mapping is not supported, default Map is always used. */
    public Object getObject(int i, Map map) throws SQLException {
        throw new UnsupportedOperationException("PoolMan does not support custom UDT mapping");
    }

    public Object getObject(String colName) throws SQLException {
        ArrayList row = (ArrayList) rowlist.get(pos - 1);
        try {
            for (int n = 0; n < row.size(); n++) {
                Result result = (Result) row.get(n);
                if (result.colName.equalsIgnoreCase(colName)) {
                    lastColIndex = n + 1;
                    return result.colValue;
                }
            }
        } catch (NullPointerException ne) {
        }
        return null;
    }

    /** Custom UDT mapping is not supported. */
    public Object getObject(String colName, Map map) throws SQLException {
        return getObject(colName);
    }

    public Ref getRef(int i) throws SQLException {
        try {
            return (Ref) getObject(i);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Ref getRef(String colName) throws SQLException {
        try {
            return (Ref) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public int getRow() throws SQLException {
        if (this.pos > rowlist.size()) return 0;
        return this.pos;
    }

    public short getShort(int colIndex) throws SQLException {
        try {
            Number i = (Number) getObject(colIndex);
            return i.shortValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public short getShort(String colName) throws SQLException {
        try {
            Number i = (Number) getObject(colName);
            return i.shortValue();
        } catch (NullPointerException ne) {
        }
        return 0;
    }

    public Statement getStatement() throws SQLException {
        return this.statement;
    }

    public String getString(int colIndex) throws SQLException {
        try {
            return getObject(colIndex).toString();
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public String getString(String colName) throws SQLException {
        try {
            return getObject(colName).toString();
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Time getTime(int colIndex) throws SQLException {
        try {
            return (Time) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public java.sql.Time getTime(int colIndex, Calendar cal) throws SQLException {
        try {
            java.sql.Time t = (java.sql.Time) getObject(colIndex);
            DateFormat df = DateFormat.getInstance();
            df.setCalendar(cal);
            java.util.Date newDate = df.parse(t.toString());
            return new java.sql.Time(newDate.getTime());
        } catch (NullPointerException ne) {
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public Time getTime(String colName) throws SQLException {
        try {
            return (Time) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Time getTime(String colName, Calendar cal) throws SQLException {
        try {
            java.sql.Time t = (java.sql.Time) getObject(colName);
            DateFormat df = DateFormat.getInstance();
            df.setCalendar(cal);
            java.util.Date newDate = df.parse(t.toString());
            return new java.sql.Time(newDate.getTime());
        } catch (NullPointerException ne) {
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public Timestamp getTimestamp(int colIndex) throws SQLException {
        try {
            return (Timestamp) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Timestamp getTimestamp(int colIndex, Calendar cal) throws SQLException {
        try {
            java.sql.Timestamp t = (java.sql.Timestamp) getObject(colIndex);
            DateFormat df = DateFormat.getInstance();
            df.setCalendar(cal);
            java.util.Date newDate = df.parse(t.toString());
            return (Timestamp) getObject(colIndex);
        } catch (NullPointerException ne) {
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public Timestamp getTimestamp(String colName) throws SQLException {
        try {
            return (Timestamp) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public Timestamp getTimestamp(String colName, Calendar cal) throws SQLException {
        try {
            java.sql.Timestamp t = (java.sql.Timestamp) getObject(colName);
            DateFormat df = DateFormat.getInstance();
            df.setCalendar(cal);
            java.util.Date newDate = df.parse(t.toString());
            return new java.sql.Timestamp(newDate.getTime());
        } catch (NullPointerException ne) {
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public int getType() throws SQLException {
        return this.scrollableType;
    }

    /** @deprecated */
    public InputStream getUnicodeStream(int colIndex) throws SQLException {
        try {
            return (InputStream) getObject(colIndex);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    /** @deprecated */
    public InputStream getUnicodeStream(String colName) throws SQLException {
        try {
            return (InputStream) getObject(colName);
        } catch (NullPointerException ne) {
        }
        return null;
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void insertRow() throws SQLException {
        com.codestudio.sql.Result result = (com.codestudio.sql.Result) insertRow.get(0);
        StringBuffer SQL = new StringBuffer("INSERT INTO ");
        SQL.append(result.tableName);
        SQL.append(" VALUES(");
        for (int i = 0; i < insertRow.size(); i++) {
            com.codestudio.sql.Result newResult = (com.codestudio.sql.Result) insertRow.get(i);
            if (newResult.colValue instanceof java.lang.String) {
                SQL.append("'");
                SQL.append(newResult.colValue.toString());
                SQL.append("'");
            } else if (newResult.type == java.sql.Types.NULL) {
                SQL.append("null");
            } else {
                SQL.append(newResult.colValue.toString());
            }
            if (i != (insertRow.size() - 1)) SQL.append(",");
        }
        SQL.append(")");
        com.codestudio.sql.PoolManStatement smarts = (com.codestudio.sql.PoolManStatement) statement;
        Connection con = smarts.getConnection();
        Statement stm = con.createStatement();
        stm.execute(SQL.toString());
        rowlist.add(new ArrayList(insertRow));
        this.insertedIndexes.add(new Integer(pos));
        moveToCurrentRow();
        composeInsertRow();
        PoolManStatement sst = (PoolManStatement) this.statement;
        JDBCPool pool = (JDBCPool) sst.getPool();
        pool.refreshCache();
    }

    public boolean isAfterLast() throws SQLException {
        assertNotInserting();
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Invalid method call for this ResultSet type TYPE_FORWARD_ONLY");
        if (pos > rowlist.size()) return true;
        return false;
    }

    public boolean isBeforeFirst() throws SQLException {
        assertNotInserting();
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Invalid method call for ResultSet type TYPE_FORWARD_ONLY");
        if (pos == 0) return true;
        return false;
    }

    public boolean isFirst() throws SQLException {
        assertNotInserting();
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Invalid method call for ResultSet type TYPE_FORWARD_ONLY");
        if (pos == 1) return true;
        return false;
    }

    public boolean isLast() throws SQLException {
        assertNotInserting();
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Invalid method call for ResultSet type TYPE_FORWARD_ONLY");
        if (pos == rowlist.size()) return true;
        return false;
    }

    public boolean last() throws SQLException {
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Invalid method call for ResultSet type TYPE_FORWARD_ONLY");
        clearUpdates();
        this.pos = rowlist.size();
        return true;
    }

    /**
     * If currently on the insert row, this method will move the cursor back
     * to an active row -- either the row the cursor was on before moving to the
     * insert row or the row that the cursor was moved to while handling the
     * insert row. If not on the insert row, this method has no effect.
     */
    public void moveToCurrentRow() throws SQLException {
        if (this.concurType == CONCUR_READ_ONLY) throw new SQLException("ResultSet not updatable");
        onInsertRow = false;
    }

    /**
     * After invoking this method, all updates and gets will be invoked
     * on the insert row rather than the current row. The current row will
     * be remembered, however, and can be recovered by invoking <code>
     * moveToCurrentRow()</code>.
     */
    public void moveToInsertRow() throws SQLException {
        if (this.concurType == CONCUR_READ_ONLY) throw new SQLException("ResultSet not updatable");
        if (insertRow == null) throw new SQLException("Unable to create insert row, incomplete metadata from driver");
        onInsertRow = true;
    }

    public boolean next() throws SQLException {
        this.pos++;
        clearUpdates();
        if (pos > rowlist.size()) return false;
        return true;
    }

    public boolean previous() throws SQLException {
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Operation not permitted when ResultSet is TYPE_FORWARD_ONLY");
        this.pos--;
        clearUpdates();
        if (pos < 1) {
            beforeFirst();
            return false;
        }
        return true;
    }

    public void refreshRow() throws SQLException {
        assertNotInserting();
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Operation not permitted when ResultSet is TYPE_FORWARD_ONLY");
        if (this.scrollableType == TYPE_SCROLL_INSENSITIVE) return;
        invokeRowRefresh();
    }

    private void invokeRowRefresh() throws SQLException {
        clearUpdates();
        ResultSet res = null;
        Statement s = null;
        try {
            PoolManStatement smarts = (PoolManStatement) this.statement;
            Connection con = smarts.getConnection();
            s = ((PoolManStatement) con.createStatement()).getNativeStatement();
            res = s.executeQuery(smarts.getSQL());
            ResultSetMetaData rmeta = PoolManResultSetMetaData.getCopy(res.getMetaData());
            int curpos = 0;
            int cols = rmeta.getColumnCount();
            while (res.next()) {
                curpos++;
                if (curpos == pos) {
                    ArrayList row = new ArrayList(1);
                    for (int i = 1; i < cols; i++) {
                        String tableName = null;
                        try {
                            tableName = rmeta.getTableName(i);
                            if (tableName.trim().length() < 1) tableName = null;
                        } catch (Exception te) {
                            tableName = null;
                        }
                        if (null == tableName) tableName = smarts.fabricateTableName(smarts.getSQL(), i);
                        Result result = new Result(tableName, rmeta.getColumnLabel(i), res.getObject(i), rmeta.getColumnType(i));
                        row.add(result);
                    }
                    rowlist.set(pos - 1, row);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (null != res) try {
                JDBCPool.closeResources(s, res);
            } catch (Exception ee) {
            }
        }
    }

    public boolean relative(int rows) throws SQLException {
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Operation Not permitted when ResultSet is TYPE_FORWARD_ONLY");
        if ((pos == 0) || (pos > rowlist.size())) throw new SQLException("Operation not permitted when cursor is not on a valid row: " + " Cursor was either before the first or after the last row");
        clearUpdates();
        this.pos = pos + rows;
        if (pos < 1) {
            beforeFirst();
            return false;
        }
        if (pos > rowlist.size()) {
            afterLast();
            return false;
        }
        return true;
    }

    public boolean rowDeleted() throws SQLException {
        assertNotInserting();
        if (this.concurType == CONCUR_READ_ONLY) {
            throw new SQLException("Illegal Operation when ResultSet is CONCUR_READ_ONLY");
        }
        if (null == rowlist.get(pos - 1)) return true;
        return false;
    }

    public boolean rowInserted() throws SQLException {
        assertNotInserting();
        if (this.concurType == CONCUR_READ_ONLY) {
            throw new SQLException("Illegal Operation when ResultSet is CONCUR_READ_ONLY");
        }
        for (int n = 0; n < insertedIndexes.size(); n++) {
            Integer i = (Integer) insertedIndexes.get(n);
            if (i.intValue() == pos) return true;
        }
        return false;
    }

    public boolean rowUpdated() throws SQLException {
        assertNotInserting();
        if (this.concurType == CONCUR_READ_ONLY) {
            throw new SQLException("Illegal Operation when ResultSet is CONCUR_READ_ONLY");
        }
        for (int n = 0; n < updatedIndexes.size(); n++) {
            Integer i = (Integer) updatedIndexes.get(n);
            if (i.intValue() == pos) return true;
        }
        return false;
    }

    public void setFetchDirection(int direction) throws SQLException {
        if (this.scrollableType == TYPE_FORWARD_ONLY) throw new SQLException("Operation Not permitted when ResultSet is TYPE_FORWARD_ONLY");
        this.fetchDirection = direction;
    }

    public void setFetchSize(int rows) throws SQLException {
        this.fetchSize = rows;
    }

    public void updateAsciiStream(int colIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateAsciiStream(String colName, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateBigDecimal(int colIndex, BigDecimal x) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if ((r.type == java.sql.Types.NUMERIC) || (r.type == java.sql.Types.DECIMAL)) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.add((colIndex - 1), new Result(r.tableName, r.colName, x, r.type)); else updatedResults.add(new Result(r.tableName, r.colName, x, r.type));
    }

    public void updateBigDecimal(String colName, BigDecimal x) throws SQLException {
        updateBigDecimal(findColumn(colName), x);
    }

    public void updateBinaryStream(int colIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateBinaryStream(String colName, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateBoolean(int colIndex, boolean x) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.BIT) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, new Boolean(x), r.type)); else updatedResults.add(new Result(r.tableName, r.colName, new Boolean(x), r.type));
    }

    public void updateBoolean(String colName, boolean x) throws SQLException {
        updateBoolean(findColumn(colName), x);
    }

    public void updateByte(int colIndex, byte x) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.TINYINT) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, new Byte(x), r.type)); else updatedResults.add(new Result(r.tableName, r.colName, new Byte(x), r.type));
    }

    public void updateByte(String colName, byte x) throws SQLException {
        updateByte(findColumn(colName), x);
    }

    public void updateBytes(int colIndex, byte[] x) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateBytes(String colName, byte[] x) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateCharacterStream(int colIndex, Reader reader, int length) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateCharacterStream(String colName, Reader reader, int length) throws SQLException {
        throw new UnsupportedOperationException("PoolMan doesn't support this method, use native ResultSet " + "(configurable in your poolman.xml) or execute the query using the " + "native Statement returned from PoolManStatement.getNativeStatement() " + "if your underlying driver supports it");
    }

    public void updateDate(int colIndex, java.sql.Date d) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.DATE) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, d, r.type)); else updatedResults.add(new Result(r.tableName, r.colName, d, r.type));
    }

    public void updateDate(String colName, java.sql.Date d) throws SQLException {
        updateDate(findColumn(colName), d);
    }

    public void updateDouble(int colIndex, double d) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.DOUBLE) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, new Double(d), r.type)); else updatedResults.add(new Result(r.tableName, r.colName, new Double(d), r.type));
    }

    public void updateDouble(String colName, double d) throws SQLException {
        updateDouble(findColumn(colName), d);
    }

    public void updateFloat(int colIndex, float f) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.FLOAT) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, new Float(f), r.type)); else updatedResults.add(new Result(r.tableName, r.colName, new Float(f), r.type));
    }

    public void updateFloat(String colName, float f) throws SQLException {
        updateFloat(findColumn(colName), f);
    }

    public void updateInt(int colIndex, int i) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.INTEGER) valid = true;
        if (!valid) throw new SQLException("UpdateInt Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, new Integer(i), r.type)); else updatedResults.add(new Result(r.tableName, r.colName, new Integer(i), r.type));
    }

    public void updateInt(String colName, int i) throws SQLException {
        updateInt(findColumn(colName), i);
    }

    public void updateLong(int colIndex, long l) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.BIGINT || r.type == java.sql.Types.INTEGER || r.type == java.sql.Types.SMALLINT || r.type == java.sql.Types.REAL || r.type == java.sql.Types.FLOAT || r.type == java.sql.Types.DOUBLE || r.type == java.sql.Types.DECIMAL || r.type == java.sql.Types.NUMERIC) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, new Long(l), r.type)); else updatedResults.add(new Result(r.tableName, r.colName, new Long(l), r.type));
    }

    public void updateLong(String colName, long l) throws SQLException {
        updateLong(findColumn(colName), l);
    }

    public void updateNull(int colIndex) throws SQLException {
        ArrayList row = getTrueRow();
        Result r = (Result) row.get(colIndex - 1);
        if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, null, r.type)); else updatedResults.add(new Result(r.tableName, r.colName, null, r.type));
    }

    public void updateNull(String colName) throws SQLException {
        updateNull(findColumn(colName));
    }

    public void updateObject(int colIndex, Object o) throws SQLException {
        ArrayList row = getTrueRow();
        Result r = (Result) row.get(colIndex - 1);
        if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, o, r.type)); else updatedResults.add(new Result(r.tableName, r.colName, o, r.type));
    }

    public void updateObject(int colIndex, Object o, int scale) throws SQLException {
        updateObject(colIndex, o);
    }

    public void updateObject(String colName, Object o) throws SQLException {
        updateObject(findColumn(colName), o);
    }

    public void updateObject(String colName, Object o, int scale) throws SQLException {
        updateObject(findColumn(colName), o);
    }

    /**
     * Update the row with the new Result objects in updatedResults.
     * Also update the database and the SQLCache.
     */
    public void updateRow() throws SQLException {
        ArrayList row = (ArrayList) rowlist.get(pos - 1);
        for (int i = 0; i < updatedResults.size(); i++) {
            com.codestudio.sql.Result newResult = (com.codestudio.sql.Result) updatedResults.get(i);
            com.codestudio.sql.Result oldResult = null;
            for (int n = 0; n < row.size(); n++) {
                com.codestudio.sql.Result r = (com.codestudio.sql.Result) row.get(n);
                if (r.colName.equalsIgnoreCase(newResult.colName)) oldResult = r;
            }
            String SQL;
            if (newResult.colValue instanceof java.lang.String) {
                SQL = "UPDATE " + newResult.tableName + " SET " + newResult.colName + " = '" + newResult.colValue.toString() + "' " + "WHERE " + oldResult.colName + " = '" + oldResult.colValue.toString() + "'";
            } else if (newResult.type == java.sql.Types.NULL) {
                SQL = "UPDATE " + newResult.tableName + " SET " + newResult.colName + " = null " + " WHERE " + oldResult.colName + " = " + oldResult.colValue.toString();
            } else {
                SQL = "UPDATE " + newResult.tableName + " SET " + newResult.colName + " = " + newResult.colValue.toString() + " WHERE " + oldResult.colName + " = " + oldResult.colValue.toString();
            }
            com.codestudio.sql.PoolManStatement smarts = (com.codestudio.sql.PoolManStatement) statement;
            Connection con = smarts.getConnection();
            Statement stm = con.createStatement();
            stm.execute(SQL);
        }
        updatedIndexes.add(new Integer(pos));
        invokeRowRefresh();
        PoolManStatement sst = (PoolManStatement) this.statement;
        JDBCPool pool = (JDBCPool) sst.getPool();
        pool.refreshCache();
    }

    public void updateShort(int colIndex, short s) throws SQLException {
        ArrayList row = getTrueRow();
        boolean valid = false;
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.SMALLINT || r.type == java.sql.Types.TINYINT || r.type == java.sql.Types.INTEGER) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, new Short(s), r.type)); else updatedResults.add(new Result(r.tableName, r.colName, new Short(s), r.type));
    }

    public void updateShort(String colName, short s) throws SQLException {
        updateShort(findColumn(colName), s);
    }

    public void updateString(int colIndex, String s) throws SQLException {
        boolean valid = false;
        ArrayList row = getTrueRow();
        Result r = (Result) row.get(colIndex - 1);
        if ((r.type == java.sql.Types.CHAR) || (r.type == java.sql.Types.VARCHAR) || (r.type == java.sql.Types.LONGVARCHAR)) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, s, r.type)); else updatedResults.add(new Result(r.tableName, r.colName, s, r.type));
    }

    public void updateString(String colName, String s) throws SQLException {
        updateString(findColumn(colName), s);
    }

    public void updateTime(int colIndex, Time t) throws SQLException {
        boolean valid = false;
        ArrayList row = getTrueRow();
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.TIME) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, t, r.type)); else updatedResults.add(new Result(r.tableName, r.colName, t, r.type));
    }

    public void updateTime(String colName, Time t) throws SQLException {
        updateTime(findColumn(colName), t);
    }

    public void updateTimestamp(int colIndex, Timestamp t) throws SQLException {
        boolean valid = false;
        ArrayList row = getTrueRow();
        Result r = (Result) row.get(colIndex - 1);
        if (r.type == java.sql.Types.TIMESTAMP) valid = true;
        if (!valid) throw new SQLException("Update Error: Check column name and argument type"); else if (onInsertRow) row.set((colIndex - 1), new Result(r.tableName, r.colName, t, r.type)); else updatedResults.add(new Result(r.tableName, r.colName, t, r.type));
    }

    public void updateTimestamp(String colName, Timestamp t) throws SQLException {
        updateTimestamp(findColumn(colName), t);
    }

    public boolean wasNull() throws SQLException {
        if (getObject(lastColIndex) == null) return true;
        return false;
    }

    private void clearUpdates() {
        this.updatedResults.clear();
        if (this.insertRow != null) this.insertRow.clear();
    }

    private void composeInsertRow() {
        try {
            ResultSetMetaData meta = PoolManResultSetMetaData.getCopy(getMetaData());
            this.insertRow = new ArrayList();
            for (int n = 1; n <= meta.getColumnCount(); n++) {
                String tableName = null;
                try {
                    tableName = meta.getTableName(n);
                    if (tableName.trim().length() < 1) tableName = null;
                } catch (Exception te) {
                    tableName = null;
                }
                if (tableName == null) {
                    PoolManStatement ps = (PoolManStatement) this.statement;
                    tableName = ps.fabricateTableName(ps.getSQL(), n);
                }
                insertRow.add((n - 1), new Result(tableName, meta.getColumnName(n), null, meta.getColumnType(n)));
            }
        } catch (SQLException se) {
            this.insertRow = null;
        }
    }

    private void assertNotInserting() throws SQLException {
        if (onInsertRow) throw new SQLException("Operation not permitted while cursor is on the insert row");
    }

    private ArrayList getTrueRow() {
        if (onInsertRow) return insertRow; else return (ArrayList) rowlist.get(pos - 1);
    }
}
