package org.datascooter.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datascooter.meta.Column;
import org.datascooter.meta.DataTypeInfo;
import org.datascooter.meta.MetaColumn;
import org.datascooter.meta.MetaIndex;
import org.datascooter.meta.MetaLink;
import org.datascooter.meta.MetaPrimaryKey;
import org.datascooter.meta.MetaTable;
import org.datascooter.meta.TableType;
import org.datascooter.utils.SnipUtils;

public class ServerTableManager {

    private Connection conn;

    private Map<String, DataTypeInfo> typeInfoMap;

    public ServerTableManager(Connection connection) {
        this.conn = connection;
    }

    private void logError(Snip snip, Exception ex) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.log(Level.SEVERE, "***Error " + SnipUtils.snipToString(snip), ex);
    }

    public MetaTable getTableData(MetaTable table) throws SQLException {
        try {
            PreparedStatement prepareStatement = conn.prepareStatement("select * from  " + table.getName());
            ResultSet rs = prepareStatement.executeQuery();
            try {
                if (rs != null) {
                    while (rs.next()) {
                        Object[] array = new Object[table.getColumns().size()];
                        int a = 0;
                        for (MetaColumn column : table.getColumns()) {
                            array[a++] = rs.getObject(column.COLUMN_NAME);
                        }
                        table.addRow(array);
                    }
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
                prepareStatement.close();
            }
        } catch (SQLException e) {
            throw e;
        }
        return table;
    }

    public List<MetaLink> getExportedTableKeys(String scheme, String table) throws SQLException {
        List<MetaLink> result = new ArrayList<MetaLink>();
        ResultSet rs = conn.getMetaData().getExportedKeys(null, scheme, table);
        try {
            if (rs != null) {
                while (rs.next()) {
                    result.add(new MetaLink(rs));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    public List<MetaLink> getImportedTableKeys(String scheme, String table) throws SQLException {
        List<MetaLink> result = new ArrayList<MetaLink>();
        ResultSet rs = conn.getMetaData().getImportedKeys(null, scheme, table);
        try {
            if (rs != null) {
                while (rs.next()) {
                    result.add(new MetaLink(rs));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    public List<MetaTable> getTables(String scheme) throws SQLException {
        List<MetaTable> result = new ArrayList<MetaTable>();
        try {
            typeInfoMap = readTypeInfoMap();
            ResultSet rs = conn.getMetaData().getTables(null, scheme, null, new String[] { TableType.TABLE.name() });
            try {
                if (rs != null) {
                    while (rs.next()) {
                        result.add(getTable(rs, scheme));
                    }
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
        } catch (SQLException e) {
            logError(null, e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    throw e1;
                }
            }
            throw e;
        }
        return result;
    }

    private MetaTable getTable(ResultSet rs, String scheme) throws SQLException {
        String tableName = rs.getString("TABLE_NAME");
        return new MetaTable(scheme, tableName, getColumns(scheme, tableName), getImportedTableKeys(scheme, tableName), getExportedTableKeys(scheme, tableName), getPrimaryTableKeys(scheme, tableName), getIndexes(scheme, tableName));
    }

    private List<MetaPrimaryKey> getPrimaryTableKeys(String scheme, String tableName) throws SQLException {
        List<MetaPrimaryKey> result = new ArrayList<MetaPrimaryKey>();
        ResultSet rs = conn.getMetaData().getPrimaryKeys(null, scheme, tableName);
        try {
            if (rs != null) {
                while (rs.next()) {
                    result.add(new MetaPrimaryKey(rs));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    private List<MetaIndex> getIndexes(String scheme, String tableName) throws SQLException {
        List<MetaIndex> result = new ArrayList<MetaIndex>();
        ResultSet rs = conn.getMetaData().getIndexInfo(null, scheme, tableName, false, false);
        try {
            if (rs != null) {
                while (rs.next()) {
                    result.add(new MetaIndex(rs));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    @SuppressWarnings("nls")
    public boolean isTableExists(String scheme, String tableName) throws SQLException {
        boolean result = false;
        try {
            ResultSet set = conn.getMetaData().getTables(null, scheme, tableName, new String[] { TableType.TABLE.name() });
            try {
                if (set != null) {
                    result = set.next();
                }
            } finally {
                if (set != null) {
                    set.close();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            logError(null, e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    throw e1;
                }
            }
            throw e;
        }
        return result;
    }

    public Map<String, Column> getCurrentColumns(String shemaName, String tableName) throws SQLException {
        Map<String, Column> result = new HashMap<String, Column>();
        try {
            List<String> primaryKeys = getPrimaryKeysInt(conn, null, shemaName, tableName);
            ResultSet rs = conn.getMetaData().getColumns(null, shemaName, tableName, null);
            try {
                while (rs.next()) {
                    Column column = getColumn(rs, primaryKeys);
                    result.put(column.name.toLowerCase(), column);
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            logError(null, e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    throw e1;
                }
            }
            throw e;
        }
        return result;
    }

    private List<MetaColumn> getColumns(String shemaName, String tableName) throws SQLException {
        List<MetaColumn> result = new ArrayList<MetaColumn>();
        ResultSet rs = conn.getMetaData().getColumns(null, shemaName, tableName, null);
        try {
            while (rs.next()) {
                MetaColumn column = new MetaColumn(rs);
                column.setDataTypeInfo(typeInfoMap.get(column.TYPE_NAME));
                result.add(column);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    public Map<String, DataTypeInfo> readTypeInfoMap() throws SQLException {
        Map<String, DataTypeInfo> result = new HashMap<String, DataTypeInfo>();
        ResultSet rs = conn.getMetaData().getTypeInfo();
        try {
            while (rs.next()) {
                DataTypeInfo info = new DataTypeInfo(rs);
                result.put(info.name, info);
            }
        } finally {
            rs.close();
        }
        return result;
    }

    private List<String> getPrimaryKeysInt(Connection conn, String catalog, String shemaName, String tableName) throws SQLException {
        List<String> primaryKeys = new ArrayList<String>();
        ResultSet keysSet = conn.getMetaData().getPrimaryKeys(catalog, shemaName, tableName);
        try {
            while (keysSet.next()) {
                primaryKeys.add(keysSet.getString("COLUMN_NAME"));
            }
        } finally {
            keysSet.close();
        }
        return primaryKeys;
    }

    private Column getColumn(ResultSet rs, List<String> primaryKeys) throws SQLException {
        return new Column(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"), rs.getInt("COLUMN_SIZE"), rs.getInt("DECIMAL_DIGITS"), rs.getInt("NULLABLE"), primaryKeys.indexOf(rs.getString("COLUMN_NAME")) >= 0);
    }
}
