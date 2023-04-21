package jdbcbrowser.ui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import jdbcbrowser.common.*;
import jdbcbrowser.util.*;

/**
 * @author takumi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ModelTableResultSet extends AbstractTableModel implements RefreshableModel {

    private Statement stmt;

    private ResultSet rs;

    private Table table;

    private Connection conn;

    private ArrayList newRecord;

    private boolean isEditingNewRecord = false;

    private boolean isModefied = false;

    /**
	 * Constructor TableResultSetDataModel.
	 * @param table
	 */
    public ModelTableResultSet(Table table) throws SQLException {
        this.table = table;
        executeQuery();
        newRecord = new ArrayList(getColumnCount());
    }

    public ResultSet getResultSet() throws SQLException {
        return rs;
    }

    public void setModefied() {
        isModefied = true;
    }

    protected void refreshIfModefied() throws SQLException {
        if (isModefied) {
            executeQuery();
            isModefied = false;
            Object[] listeners = listenerList.getListenerList();
            for (int i = 0; i < listeners.length; i++) {
                if (listeners[i] instanceof JTable) {
                    JTable table = (JTable) listeners[i];
                    table.revalidate();
                }
            }
        }
    }

    protected void executeQuery() throws SQLException {
        if (stmt == null) {
            conn = table.getDatabase().getConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
        rs = stmt.executeQuery("SELECT * FROM " + table.getName());
    }

    /**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
    public int getRowCount() {
        try {
            refreshIfModefied();
            int curRow = rs.getRow();
            rs.last();
            int lastRow = rs.getRow();
            if (curRow > 0) {
                rs.absolute(curRow);
            }
            return lastRow;
        } catch (SQLException e) {
            ErrHandler.handleException(e);
        }
        return 0;
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
    public int getColumnCount() {
        try {
            refreshIfModefied();
            return rs.getMetaData().getColumnCount();
        } catch (SQLException e) {
            ErrHandler.handleException(e);
        }
        return 0;
    }

    /**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            if (rowIndex < getRowCount()) {
                int curRow = rs.getRow();
                rs.absolute(rowIndex + 1);
                Object value = rs.getObject(columnIndex + 1);
                if (curRow > 0) {
                    rs.absolute(curRow);
                }
                return value;
            } else if (rowIndex == getColumnCount()) {
            }
            return null;
        } catch (SQLException e) {
        }
        return null;
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
    public String getColumnName(int columnIndex) {
        try {
            refreshIfModefied();
            return rs.getMetaData().getColumnName(columnIndex + 1);
        } catch (SQLException e) {
            ErrHandler.handleException(e);
        }
        return null;
    }

    /**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        try {
            refreshIfModefied();
        } catch (SQLException e) {
            ErrHandler.handleException(e);
        }
        return false;
    }

    private void update(Object aValue, int rowIndex, int columnIndex) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        StringBuffer sqlUpdate = new StringBuffer();
        sqlUpdate.append("UPDATE \"");
        sqlUpdate.append(table.getName());
        sqlUpdate.append("\" SET \"");
        sqlUpdate.append(rsmd.getColumnName(columnIndex + 1));
        sqlUpdate.append("\"=");
        switch(rsmd.getColumnType(columnIndex + 1)) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                sqlUpdate.append("'");
                sqlUpdate.append(aValue.toString());
                sqlUpdate.append("'");
                break;
            default:
                sqlUpdate.append(aValue.toString());
                break;
        }
        sqlUpdate.append(" WHERE ");
        Iterator pks = table.getPrimaryKeys();
        boolean isFirst = true;
        while (pks.hasNext()) {
            if (!isFirst) {
                sqlUpdate.append(" AND ");
            } else {
                isFirst = false;
            }
            PrimaryKey pk = (PrimaryKey) pks.next();
            sqlUpdate.append("\"");
            sqlUpdate.append(pk.getColumn_name());
            sqlUpdate.append("\"");
            sqlUpdate.append("=");
            switch(pk.getColumn_type()) {
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    sqlUpdate.append("'");
                    sqlUpdate.append(rs.getString(pk.getColumn_name()));
                    sqlUpdate.append("'");
                    break;
                default:
                    sqlUpdate.append(rs.getString(pk.getColumn_name()));
                    break;
            }
        }
        Statement stmtUpdate = conn.createStatement();
        System.out.println(sqlUpdate.toString());
        int ret = stmtUpdate.executeUpdate(sqlUpdate.toString());
        stmtUpdate.close();
    }
}
