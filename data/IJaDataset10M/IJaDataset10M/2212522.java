package druid.util.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Vector;
import org.dlib.gui.flextable.FlexTableColumn;
import org.dlib.gui.flextable.FlexTableModel;
import org.dlib.tools.TVector;
import ddf.lib.SqlMapper;
import ddf.type.MemoryBlob;
import ddf.type.MemoryClob;
import ddf.type.SqlType;
import druid.core.jdbc.JdbcConnection;
import druid.core.jdbc.entities.RecordBasedEntity;
import druid.util.gui.Dialogs;

public class ResultSetEditor implements FlexTableModel {

    public static final int NEWRECORD_START = 0;

    public static final int NEWRECORD_END = 1;

    public static final int NEWRECORD_ERROR = 2;

    public static final int DEFAULT_WIDTH = 80;

    private ResultSet rSet;

    private boolean bEditable;

    private boolean bScrollable;

    private ResultSetMetaData rMeta;

    private int colCount;

    private int rowCount;

    private Vector vColumns = new Vector();

    private SqlType[] aColTypes;

    /** Table's name */
    private String sTable;

    private String sWhere;

    private String sQuery;

    private RecordBasedEntity eNode;

    private Vector vRecords;

    private JdbcConnection jdbcConn;

    private static final int QUERY = 0;

    private static final int TABLE = 1;

    private int mode;

    private static final int NORMAL = 0;

    private static final int INSERT = 1;

    private int editMode;

    public ResultSetEditor(RecordBasedEntity node) {
        mode = TABLE;
        eNode = node;
        sTable = node.getFullName();
        jdbcConn = node.getJdbcConnection();
        editMode = NORMAL;
    }

    public ResultSetEditor(JdbcConnection conn) {
        mode = QUERY;
        jdbcConn = conn;
        editMode = NORMAL;
    }

    /** Reload data from table into memory
	  * in TABLE mode query is the WHERE clause
	  * in QUERY mode query is the full query
	  */
    public void refresh(String query) throws SQLException {
        editMode = NORMAL;
        if (mode == QUERY) sQuery = query; else sWhere = query;
        loadResultSet();
        colCount = rMeta.getColumnCount();
        bScrollable = (rSet.getType() != ResultSet.TYPE_FORWARD_ONLY);
        bEditable = (rSet.getConcurrency() != ResultSet.CONCUR_READ_ONLY);
        bEditable = bEditable && bScrollable;
        vColumns = new Vector();
        aColTypes = new SqlType[colCount];
        for (int i = 0; i < colCount; i++) {
            String label = rMeta.getColumnName(i + 1);
            int type = rMeta.getColumnType(i + 1);
            String dbType = rMeta.getColumnTypeName(i + 1);
            int decimals = rMeta.getScale(i + 1);
            int prefWidth = rMeta.getColumnDisplaySize(i + 1);
            int width = DEFAULT_WIDTH;
            if (prefWidth < DEFAULT_WIDTH) prefWidth = DEFAULT_WIDTH;
            FlexTableColumn ftc = new FlexTableColumn(label, width);
            ftc.setUserObject(new Integer(prefWidth));
            vColumns.addElement(ftc);
            aColTypes[i] = SqlMapper.map("" + type, dbType, "" + decimals, jdbcConn.getConnection());
            ftc.setEditable(isSimpleEditable(i));
        }
        Vector vCurrRec;
        vRecords = new Vector();
        for (rowCount = 0; rSet.next() && rowCount < jdbcConn.getMaxRows(); rowCount++) {
            vCurrRec = new Vector();
            for (int j = 0; j < colCount; j++) vCurrRec.addElement(getFieldValue(j));
            vRecords.addElement(vCurrRec);
        }
    }

    public int newRecord() {
        if (editMode != NORMAL) {
            if (insertRow()) {
                editMode = NORMAL;
                return NEWRECORD_END;
            } else return NEWRECORD_ERROR;
        } else {
            Vector v = new Vector();
            for (int i = 0; i < vColumns.size(); i++) {
                if (aColTypes[i].isInteger()) v.addElement(new Long(0)); else if (aColTypes[i].isReal()) v.addElement(new Double(0)); else v.addElement(null);
            }
            vRecords.addElement(v);
            rowCount++;
            editMode = INSERT;
            return NEWRECORD_START;
        }
    }

    public void copyRecord(int index) {
        vRecords.addElement(((Vector) vRecords.get(index)).clone());
        rowCount++;
        editMode = INSERT;
    }

    public boolean removeRecord(int index) {
        try {
            if (editMode != INSERT || index != rowCount - 1) {
                if (bEditable) {
                    rSet.absolute(index + 1);
                    rSet.deleteRow();
                    loadResultSet();
                } else {
                    String query = "DELETE FROM " + sTable + " WHERE " + getWhereString(index);
                    jdbcConn.execute(query, getWhereParameters(index));
                }
            } else {
                editMode = NORMAL;
            }
            rowCount--;
            vRecords.removeElementAt(index);
            return true;
        } catch (Exception e) {
            Dialogs.showException(e);
        }
        return false;
    }

    public boolean isIScrollable() {
        return bScrollable;
    }

    public boolean isInserting() {
        return (editMode == INSERT);
    }

    public boolean isEditable() {
        return bEditable;
    }

    public String getCurrentQuery() {
        return sQuery;
    }

    public SqlType getSqlType(int col) {
        return aColTypes[col];
    }

    public int getColumnCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public FlexTableColumn getColumnAt(int index) {
        if ((index < 0) || (index >= colCount)) return null;
        return (FlexTableColumn) vColumns.elementAt(index);
    }

    public Object getValueAt(int row, int col) {
        return ((Vector) vRecords.elementAt(row)).elementAt(col);
    }

    public void setValueAt(Object o, int row, int col) {
        try {
            if (editMode != INSERT) {
                if (bEditable) {
                    rSet.absolute(row + 1);
                    setFieldValue(o, col);
                    rSet.updateRow();
                } else updateNotEditableValue(convertToParam(col, o), row, col);
            }
            Vector vRow = (Vector) vRecords.elementAt(row);
            if (o == null || o.toString().equals("")) vRow.setElementAt(null, col); else vRow.setElementAt(o, col);
        } catch (Exception e) {
            Dialogs.showException(e);
        }
    }

    public byte[] getBinaryValueAt(int row, int col) throws SQLException, IOException {
        if (bEditable) {
            rSet.absolute(row + 1);
            return rSet.getBytes(col + 1);
        } else {
            ResultSet rs = getNotEditableResultSet(row, col);
            if (!rs.next()) return null;
            return rs.getBytes(1);
        }
    }

    public void setBinaryValueAt(byte[] data, int row, int col) throws SQLException, IOException {
        if (bEditable) {
            rSet.absolute(row + 1);
            if (data != null) rSet.updateBinaryStream(col + 1, new ByteArrayInputStream(data), data.length); else rSet.updateNull(col + 1);
            rSet.updateRow();
        } else {
            if (data != null) updateNotEditableValue(new ByteArrayInputStream(data), row, col); else updateNotEditableValue(null, row, col);
        }
    }

    public String getLongStringAt(int row, int col) throws SQLException, IOException {
        if (bEditable) {
            rSet.absolute(row + 1);
            return rSet.getString(col + 1);
        } else {
            ResultSet rs = getNotEditableResultSet(row, col);
            if (!rs.next()) return null;
            return rs.getString(1);
        }
    }

    public void setLongStringAt(String data, int row, int col) throws SQLException, IOException {
        if (bEditable) {
            if (data != null) updateNotEditableValue(new StringBuffer(data), row, col); else updateNotEditableValue(null, row, col);
            rSet.absolute(row + 1);
            rSet.refreshRow();
        } else {
            if (data != null) updateNotEditableValue(new StringBuffer(data), row, col); else updateNotEditableValue(null, row, col);
        }
    }

    public byte[] getBlobAt(int row, int col) throws SQLException, IOException {
        Blob b;
        if (bEditable) {
            rSet.absolute(row + 1);
            b = rSet.getBlob(col + 1);
        } else {
            ResultSet rs = getNotEditableResultSet(row, col);
            if (!rs.next()) return null;
            b = rs.getBlob(1);
        }
        if (b == null) return null;
        return b.getBytes(1, (int) b.length());
    }

    public void setBlobAt(byte[] data, int row, int col) throws SQLException, IOException {
        if (bEditable) {
            rSet.absolute(row + 1);
            if (data != null) rSet.updateBlob(col + 1, new MemoryBlob(data)); else rSet.updateNull(col + 1);
            rSet.updateRow();
        } else {
            if (data != null) updateNotEditableValue(new MemoryBlob(data), row, col); else updateNotEditableValue(null, row, col);
        }
    }

    public String getClobAt(int row, int col) throws SQLException, IOException {
        Clob c;
        if (bEditable) {
            rSet.absolute(row + 1);
            c = rSet.getClob(col + 1);
        } else {
            ResultSet rs = getNotEditableResultSet(row, col);
            if (!rs.next()) return null;
            c = rs.getClob(1);
        }
        if (c == null) return null;
        return c.getSubString(0, (int) c.length());
    }

    public void setClobAt(String data, int row, int col) throws SQLException, IOException {
        if (bEditable) {
            rSet.absolute(row + 1);
            if (data != null) rSet.updateClob(col + 1, new MemoryClob(data)); else rSet.updateNull(col + 1);
            rSet.updateRow();
        } else {
            if (data != null) updateNotEditableValue(new MemoryClob(data), row, col); else updateNotEditableValue(null, row, col);
        }
    }

    private boolean insertRow() {
        try {
            if (bEditable && rowCount > 1) insertEditableRow(); else insertNotEditableRow();
            return true;
        } catch (Exception e) {
            Dialogs.showException(e);
        }
        return false;
    }

    private void insertEditableRow() throws SQLException {
        rSet.moveToInsertRow();
        Vector vRow = (Vector) vRecords.elementAt(rowCount - 1);
        for (int i = 0; i < vColumns.size(); i++) {
            if (isSimpleEditable(i)) setFieldValue(vRow.elementAt(i), i);
        }
        rSet.insertRow();
    }

    private void insertNotEditableRow() throws SQLException, IOException {
        int row = rowCount - 1;
        String query = "INSERT INTO " + sTable + "(" + getInsertNames(row) + ") " + "VALUES(" + getInsertString(row) + ")";
        jdbcConn.execute(query, getWhereParameters(row));
    }

    private String getInsertNames(int row) {
        TVector v = new TVector();
        v.setSeparator(", ");
        for (int i = 0; i < vColumns.size(); i++) {
            FlexTableColumn ftc = (FlexTableColumn) vColumns.elementAt(i);
            Object o = getValueAt(row, i);
            if (isSimpleEditable(i) && o != null) v.addElement(ftc.getHeaderValue());
        }
        return v.toString();
    }

    private String getInsertString(int row) {
        TVector v = new TVector();
        v.setSeparator(", ");
        for (int i = 0; i < vColumns.size(); i++) {
            Object o = getValueAt(row, i);
            if (isSimpleEditable(i) && o != null) v.addElement("?");
        }
        return v.toString();
    }

    private void loadResultSet() throws SQLException {
        if (mode == TABLE) {
            String fields = eNode.getSelFields();
            sQuery = "SELECT " + fields + " FROM " + sTable;
            if (!sWhere.equals("")) {
                if (sWhere.toLowerCase().startsWith("order ")) sQuery += " " + sWhere; else sQuery += " WHERE " + sWhere;
            }
        }
        if (rSet != null) try {
            if (rSet.getStatement() != null) rSet.getStatement().close(); else rSet.close();
        } catch (SQLException e) {
            try {
                rSet.close();
            } catch (SQLException er) {
                rSet = null;
            }
        }
        rSet = jdbcConn.selectUpdate(sQuery);
        rMeta = rSet.getMetaData();
    }

    private boolean isSimpleEditable(int col) {
        return aColTypes[col].isBoolean() || aColTypes[col].isNumber() || aColTypes[col].isString() || aColTypes[col].isTemporalType();
    }

    public ResultSet getNotEditableResultSet(int row, int col) throws SQLException, IOException {
        FlexTableColumn ftc = (FlexTableColumn) vColumns.elementAt(col);
        String colName = (String) ftc.getHeaderValue();
        String query = "SELECT " + colName + " FROM " + sTable + " WHERE " + getWhereString(row);
        return jdbcConn.select(query, getWhereParameters(row));
    }

    private Vector getWhereParameters(int row) {
        Vector v = new Vector();
        for (int i = 0; i < vColumns.size(); i++) {
            Object o = getValueAt(row, i);
            if (isSimpleEditable(i) && o != null) v.addElement(convertToParam(i, o));
        }
        return v;
    }

    private Object convertToParam(int col, Object o) {
        if (o == null) return null;
        if (aColTypes[col].isDate()) return java.sql.Date.valueOf((String) o);
        if (aColTypes[col].isTime()) return Time.valueOf((String) o);
        if (aColTypes[col].isTimeStamp()) return Timestamp.valueOf((String) o);
        return o;
    }

    private String getWhereString(int row) {
        TVector v = new TVector();
        v.setSeparator(" AND ");
        for (int i = 0; i < vColumns.size(); i++) {
            Object o = getValueAt(row, i);
            FlexTableColumn ftc = (FlexTableColumn) vColumns.elementAt(i);
            if (isSimpleEditable(i) && o != null) v.addElement(ftc.getHeaderValue() + " = ?");
        }
        return v.toString();
    }

    private void updateNotEditableValue(Object o, int row, int col) throws SQLException, IOException {
        FlexTableColumn ftc = (FlexTableColumn) vColumns.elementAt(col);
        String colName = (String) ftc.getHeaderValue();
        String query = "UPDATE " + sTable + " SET " + colName + "= ? WHERE " + getWhereString(row);
        Vector args = getWhereParameters(row);
        args.insertElementAt(o, 0);
        jdbcConn.execute(query, args);
    }

    private void setFieldValue(Object o, int col) throws SQLException {
        if (o == null || o.toString().equals("")) {
            rSet.updateNull(col + 1);
            return;
        }
        if (aColTypes[col].isDate()) rSet.updateDate(col + 1, java.sql.Date.valueOf((String) o)); else if (aColTypes[col].isTime()) rSet.updateTime(col + 1, Time.valueOf((String) o)); else if (aColTypes[col].isTimeStamp()) rSet.updateTimestamp(col + 1, Timestamp.valueOf((String) o)); else rSet.updateObject(col + 1, o);
    }

    private Object getFieldValue(int col) throws SQLException {
        try {
            return getFieldValueI(col);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "<EXCEPTION>";
        }
    }

    private Object getFieldValueI(int col) throws SQLException {
        if (aColTypes[col].isDate()) {
            java.sql.Date d = rSet.getDate(col + 1);
            return (d != null) ? d.toString() : null;
        } else if (aColTypes[col].isTime()) {
            Time t = rSet.getTime(col + 1);
            return (t != null) ? t.toString() : null;
        } else if (aColTypes[col].isTimeStamp()) {
            Timestamp ts = rSet.getTimestamp(col + 1);
            return (ts != null) ? ts.toString() : null;
        } else if (aColTypes[col].isInteger()) {
            String sField = rSet.getString(col + 1);
            return (sField != null) ? new Long(sField) : null;
        } else if (aColTypes[col].isReal()) {
            String sField = rSet.getString(col + 1);
            return (sField != null) ? new Double(sField) : null;
        } else if (aColTypes[col].isBoolean()) {
            Object obj = rSet.getObject(col + 1);
            return (obj != null) ? Boolean.valueOf(rSet.getBoolean(col + 1)) : null;
        } else if (aColTypes[col].isString()) return rSet.getString(col + 1); else return "<UNKNOWN>";
    }
}
