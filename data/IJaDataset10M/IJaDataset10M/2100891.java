package com.newbee.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import javax.sql.DataSource;
import com.newbee.util.Format;

public class DataEngine {

    private static final DataEngine instance = new DataEngine();

    private DataSource dataSource = null;

    private DataEngine() {
        if (dataSource == null) {
            dataSource = DataSourceUtils.getDataSource();
        }
    }

    public static DataEngine getInstance() {
        return instance;
    }

    /**
	 * 执行SELECT查询语句
	 */
    public Table executeQuery(String strSQL) throws Exception {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Connection conn = null;
        Table table = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(strSQL);
            resultSet = ps.executeQuery();
            table = new Table(resultSet);
        } catch (Exception e) {
            throw e;
        } finally {
            closeResultSet(resultSet);
            closeStatement(ps);
            closeConnection(conn);
        }
        return table;
    }

    /**
	 * 执行SELECT查询语句
	 */
    public Table executeQueryTopn(String strSQL, int maxRows) throws Exception {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Connection conn = null;
        Table table = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(strSQL);
            ps.setMaxRows(maxRows);
            resultSet = ps.executeQuery();
            table = new Table(resultSet);
        } catch (Exception e) {
            throw e;
        } finally {
            closeResultSet(resultSet);
            closeStatement(ps);
            closeConnection(conn);
        }
        return table;
    }

    /**
	 * 执行INSERT,UPDATE,DELETE语句
	 * @param conn
	 * @param strSQL
	 * @param recPara
	 * @return
	 * @throws Exception
	 */
    public int executeUpdate(String strSQL) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(strSQL);
        int nAffectedRecordCount = -1;
        try {
            nAffectedRecordCount = ps.executeUpdate();
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(conn);
        }
        return nAffectedRecordCount;
    }

    public int executeBatch(ArrayList strSQL) throws Exception {
        Connection conn = getConnection();
        Statement ps = conn.createStatement();
        for (int i = 0; i < strSQL.size(); i++) {
            ps.addBatch((String) strSQL.get(i));
        }
        int nAffectedRecordCount = -1;
        try {
            int[] rs = ps.executeBatch();
            for (int i = 0; i < rs.length; i++) {
                nAffectedRecordCount += rs[i];
            }
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(conn);
        }
        return nAffectedRecordCount;
    }

    public int updateOrInsert(Table table) {
        int[] rs = updateTable(table);
        if (rs != null) {
            for (int i = rs.length; i > 0; i--) {
                if (rs[i - 1] != 0) {
                    table.removeRow(i - 1);
                }
            }
        }
        int count = insertTable(table);
        return count;
    }

    public int insertTable(Table table) {
        int count = 0;
        try {
            int[] rs = _insertTable(table);
            for (int i = 0; i < rs.length; i++) {
                count += rs[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int[] updateTable(Table table) {
        int[] rs = new int[0];
        try {
            rs = _updateTable(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
	 * 取得数据库名称 <br>
	 * 微软SQLSERVER数据库返回：Microsoft SQL Server<br>
	 * 甲骨文Oracle数据库返回：Oracle
	 * 
	 * @EditTime 2008-9-28 上午10:46:41
	 * @Author sean
	 * @return String 数据库名称
	 */
    public String getDatabaseProductName() {
        String dbName = "";
        Connection conn = null;
        try {
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            dbName = metaData.getDatabaseProductName();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return dbName;
    }

    /**
	 * 返回指定表主键列名集合
	 * 
	 * @Param tableName 表名
	 * @return Vector 表主键列名集合
	 * @EditTime 2008-9-28 上午11:06:19
	 * @Author sean
	 */
    public Vector<String> getPrimaryKeys(String tableName) {
        Vector<String> v = new Vector<String>();
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            rs = conn.getMetaData().getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                v.add(rs.getString("COLUMN_NAME"));
            }
        } catch (Exception e) {
            try {
                rs = conn.getMetaData().getPrimaryKeys(null, null, tableName.toUpperCase());
                while (rs.next()) {
                    v.add(rs.getString("COLUMN_NAME"));
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            closeResultSet(rs);
            closeConnection(conn);
        }
        return v;
    }

    /**
	 * 从Table的指定行取得一个update语句
	 * 
	 * @param table 数据集合
	 * @param rowIndex 行号
	 */
    public String getUpdateSql(Table table, int rowIndex) {
        String tableName = table.getName();
        StringBuffer updateSql = new StringBuffer("UPDATE " + tableName + " SET ");
        StringBuffer whereSql = new StringBuffer(" WHERE 1=1 ");
        Vector pks = table.getPrimaryKeys();
        for (int j = 0; j < table.getColumnCount(); j++) {
            String name = table.getColumnName(j);
            if (!table.getPrimaryKeys().contains(name)) {
                Object value0 = table.getCellObjectValue(rowIndex, j);
                if (value0 != null) {
                    String value1 = ((value0 instanceof Number) ? "" : "'") + Format.getSQLInsertValue(String.valueOf(value0)) + ((value0 instanceof Number) ? "" : "'");
                    updateSql.append(name + " = " + value1 + ",");
                }
            }
        }
        for (int p = 0; p < pks.size(); p++) {
            String name = (String) pks.get(p);
            Object value0 = table.getCellObjectValue(rowIndex, name);
            String value1 = String.valueOf(value0);
            whereSql.append(" AND " + name + " = " + ((value0 instanceof Number) ? "" : "'") + value1 + ((value0 instanceof Number) ? "" : "'"));
        }
        return updateSql.substring(0, updateSql.length() - 1) + whereSql;
    }

    /**
	 * 从Table的指定行取得一个insert语句
	 * 
	 * @param table 数据集合
	 * @param rowIndex 行号
	 */
    public String getInsertSql(Table table, int rowIndex) {
        String tableName = table.getName();
        StringBuffer insertSql = new StringBuffer("INSERT INTO ");
        StringBuffer columns = new StringBuffer(" (");
        StringBuffer values = new StringBuffer(" VALUES(");
        for (int j = 0; j < table.getColumnCount(); j++) {
            Object value0 = table.getCellObjectValue(rowIndex, j);
            if (value0 != null && !value0.equals("")) {
                String value1 = ((value0 instanceof Number) ? "" : "'") + Format.getSQLInsertValue(value0.toString()) + ((value0 instanceof Number) ? "" : "'");
                if (j != 0) {
                    columns.append(",");
                    values.append(",");
                }
                String name = table.getColumnName(j);
                columns.append(name);
                values.append(value1);
            }
        }
        columns.append(")");
        values.append(")");
        insertSql.append(tableName);
        insertSql.append(columns);
        insertSql.append(values);
        return insertSql.toString();
    }

    /**
	 * 获取一个数据库连接
	 * @return
	 */
    private Connection getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            return null;
        }
    }

    private void rollback(Connection conn) {
        try {
            if (conn.getAutoCommit() == false) {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commit(Connection conn) {
        try {
            if (conn.getAutoCommit() == false) {
                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 *关闭结果集
	 * 
	 * @param rs
	 */
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
    }

    /**
	 * 关闭Statement
	 * 
	 * @param stat
	 */
    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            stmt = null;
        }
    }

    /**
	 * 关闭Connection
	 * @param conn
	 */
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (conn.getAutoCommit() == false) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e1) {
            }
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    /**
	 * 把Table中的数据update到表中
	 * 
	 * @param table 数据集合
	 * @throws Exception 
	 */
    private int[] _updateTable(Table table) throws Exception {
        int[] rs = null;
        String tableName = table.getName();
        StringBuffer updateSql = new StringBuffer("UPDATE " + tableName + " SET ");
        StringBuffer whereSql = new StringBuffer(" WHERE 1=1 ");
        Vector pks = table.getPrimaryKeys();
        if (pks == null || pks.size() <= 0) {
            throw new Exception("--- Primary not to assigned ---");
        }
        for (int j = 0; j < table.getColumnCount(); j++) {
            String name = table.getColumnName(j);
            if (!table.getPrimaryKeys().contains(name)) {
                updateSql.append(name + " = ?,");
            }
        }
        updateSql.setLength(updateSql.length() - 1);
        String str1 = updateSql.toString();
        for (int i = 0; i < pks.size(); i++) {
            String name = (String) pks.get(i);
            whereSql.append(" AND " + name + " = ?,");
        }
        whereSql.setLength(whereSql.length() - 1);
        String str2 = whereSql.toString();
        PreparedStatement ps = null;
        Connection conn = getConnection();
        try {
            ps = conn.prepareStatement(str1 + str2);
            for (int i = 0; i < table.getRowCount(); i++) {
                int paraIndex = 1;
                for (int j = 0; j < table.getColumnCount(); j++) {
                    String name = table.getColumnName(j);
                    if (!table.getPrimaryKeys().contains(name)) {
                        ps.setObject((paraIndex++), table.getCellObjectValue(i, name));
                    }
                }
                for (int j = 0; j < table.getColumnCount(); j++) {
                    String name = table.getColumnName(j);
                    if (table.getPrimaryKeys().contains(name)) {
                        ps.setObject((paraIndex++), table.getCellObjectValue(i, name));
                    }
                }
                ps.addBatch();
            }
            rs = ps.executeBatch();
            commit(conn);
        } catch (SQLException e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(conn);
        }
        return rs;
    }

    /**
	 * 把Table的数据insert到表中
	 * 
	 * @param table 数据集合
	 * @throws Exception 
	 */
    private int[] _insertTable(Table table) throws Exception {
        int[] rs = null;
        Connection conn = getConnection();
        String tableName = table.getName();
        if (tableName == null || tableName.equals("")) {
            throw new Exception("---- Table name is required.----");
        }
        StringBuffer insertSql = new StringBuffer("INSERT INTO ");
        StringBuffer columns = new StringBuffer(" (");
        StringBuffer values = new StringBuffer(" VALUES(");
        for (int j = 0; j < table.getColumnCount(); j++) {
            if (j != 0) {
                columns.append(",");
                values.append(",");
            }
            String name = table.getColumnName(j);
            columns.append(name);
            values.append("?");
        }
        columns.append(")");
        values.append(")");
        insertSql.append(tableName);
        insertSql.append(columns);
        insertSql.append(values);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(insertSql.toString());
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    String name = table.getColumnName(j);
                    ps.setObject((j + 1), table.getCellObjectValue(i, name));
                }
                ps.addBatch();
            }
            rs = ps.executeBatch();
            commit(conn);
        } catch (SQLException e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            closeConnection(conn);
        }
        return rs;
    }
}
