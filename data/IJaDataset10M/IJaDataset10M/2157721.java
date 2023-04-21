package com.jxva.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import com.jxva.dao.entry.KeyEntry;
import com.jxva.dao.entry.TableEntry;
import com.jxva.dao.jdbc.Column;
import com.jxva.dao.jdbc.ResultSetCallback;
import com.jxva.dao.jdbc.ResultSetCreator;
import com.jxva.dao.jdbc.SqlRowSet;
import com.jxva.dao.jdbc.SqlRowSetMetaData;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-03-23 11:24:00 by Jxva
 */
public class Jdbc extends JdbcTemplate {

    public Jdbc(Connection connection) {
        super(connection);
    }

    public List<String> getCatalogs() throws DataAccessException {
        return execute(new ResultSetCreator() {

            public ResultSet createResultSet(Connection conn) throws SQLException {
                return conn.getMetaData().getCatalogs();
            }
        }, new ResultSetCallback<List<String>>() {

            public List<String> doInResultSet(ResultSet rs) throws SQLException, DataAccessException {
                final List<String> catalogs = new ArrayList<String>();
                while (rs.next()) {
                    catalogs.add(rs.getString(1));
                }
                return catalogs;
            }
        });
    }

    public List<String> getSchemas() throws DataAccessException {
        return execute(new ResultSetCreator() {

            public ResultSet createResultSet(Connection conn) throws SQLException {
                return conn.getMetaData().getSchemas();
            }
        }, new ResultSetCallback<List<String>>() {

            public List<String> doInResultSet(ResultSet rs) throws SQLException, DataAccessException {
                final List<String> schemas = new ArrayList<String>();
                while (rs.next()) {
                    schemas.add(rs.getString(1));
                }
                return schemas;
            }
        });
    }

    public List<TableEntry> getTables(final String catalog, final String schema, final String tblPattern, final String[] types) throws DataAccessException {
        return execute(new ResultSetCreator() {

            public ResultSet createResultSet(Connection conn) throws SQLException {
                return conn.getMetaData().getTables(catalog, schema, tblPattern, types);
            }
        }, new ResultSetCallback<List<TableEntry>>() {

            public List<TableEntry> doInResultSet(ResultSet rs) throws SQLException, DataAccessException {
                final List<TableEntry> tables = new ArrayList<TableEntry>();
                while (rs.next()) {
                    String table = rs.getString("TABLE_NAME");
                    boolean b1 = table.indexOf('$') > -1 || table.indexOf('=') > -1;
                    boolean b2 = table.indexOf('/') > -1 || table.indexOf('+') > -1;
                    if (b1 || b2) {
                        continue;
                    }
                    TableEntry tableEntry = new TableEntry();
                    tableEntry.setTableName(table.toLowerCase());
                    tables.add(tableEntry);
                }
                return tables;
            }
        });
    }

    public List<Column> getAllColumns(String tblName) throws DataAccessException {
        try {
            SqlRowSet rowSet = queryForRowSet("select * from " + tblName);
            SqlRowSetMetaData rsmd = rowSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            final List<Column> columns = new ArrayList<Column>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                Column column = new Column();
                column.setIndex(i);
                column.setAutoIncrement(rsmd.isAutoIncrement(i));
                column.setPrecision(rsmd.getPrecision(i));
                column.setScale(rsmd.getScale(i));
                column.setName(rsmd.getColumnName(i).toLowerCase());
                column.setType(rsmd.getColumnType(i));
                column.setNullable(rsmd.isNullable(i) == ResultSetMetaData.columnNullable);
                column.setLength(rsmd.getColumnDisplaySize(i));
                columns.add(column);
            }
            return columns;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public Column getAutoIncrementColumn(String tblName) throws DataAccessException {
        try {
            SqlRowSet rowSet = queryForRowSet("select * from " + tblName);
            SqlRowSetMetaData rsmd = rowSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Column column = new Column();
            for (int i = 1; i <= columnCount; i++) {
                if (rsmd.isAutoIncrement(i)) {
                    column.setIndex(i);
                    column.setAutoIncrement(true);
                    column.setPrecision(rsmd.getPrecision(i));
                    column.setScale(rsmd.getScale(i));
                    column.setName(rsmd.getColumnName(i).toLowerCase());
                    column.setType(rsmd.getColumnType(i));
                    column.setNullable(rsmd.isNullable(i) == ResultSetMetaData.columnNullable);
                    column.setLength(rsmd.getColumnDisplaySize(i));
                    return column;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public List<Column> getPrimaryKeyColumns(final String tblName) throws DataAccessException {
        try {
            SqlRowSet rowSet = queryForRowSet("select * from " + tblName);
            SqlRowSetMetaData rsmd = rowSet.getMetaData();
            final Map<String, Integer> typeOfColumnMap = new WeakHashMap<String, Integer>();
            final Map<String, Integer> lengthOfColumnMap = new WeakHashMap<String, Integer>();
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String colName = rsmd.getColumnName(i);
                typeOfColumnMap.put(colName, rsmd.getColumnType(i));
                lengthOfColumnMap.put(colName, rsmd.getColumnDisplaySize(i));
            }
            return execute(new ResultSetCreator() {

                public ResultSet createResultSet(Connection conn) throws SQLException {
                    return conn.getMetaData().getPrimaryKeys(getConnection().getCatalog(), null, tblName);
                }
            }, new ResultSetCallback<List<Column>>() {

                public List<Column> doInResultSet(ResultSet rs) throws SQLException, DataAccessException {
                    final List<Column> pks = new ArrayList<Column>(2);
                    while (rs.next()) {
                        String colName = rs.getString(4);
                        Column column = new Column();
                        column.setName(colName.toLowerCase());
                        column.setIndex(rs.getInt(5));
                        column.setType(((Integer) typeOfColumnMap.get(colName)).intValue());
                        column.setNullable(false);
                        column.setLength(((Integer) lengthOfColumnMap.get(colName)).intValue());
                        pks.add(column);
                    }
                    return pks;
                }
            });
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public List<KeyEntry> getImportedKeys(final String tblName) throws DataAccessException {
        return execute(new ResultSetCreator() {

            public ResultSet createResultSet(Connection conn) throws SQLException {
                return conn.getMetaData().getImportedKeys(conn.getCatalog(), null, tblName.toUpperCase());
            }
        }, new ResultSetCallback<List<KeyEntry>>() {

            public List<KeyEntry> doInResultSet(ResultSet rs) throws SQLException, DataAccessException {
                final List<KeyEntry> pks = new ArrayList<KeyEntry>(4);
                while (rs.next()) {
                    KeyEntry key = new KeyEntry();
                    key.setIndex(rs.getInt(9));
                    key.setPrimaryKeyTableName(rs.getString(3));
                    key.setPrimaryKeyColumnName(rs.getString(4));
                    key.setForeignKeyTableName(rs.getString(7));
                    key.setForeignKeyColumnName(rs.getString(8));
                    pks.add(key);
                }
                return pks;
            }
        });
    }

    public List<KeyEntry> getExportedKeys(final String tblName) throws DataAccessException {
        return execute(new ResultSetCreator() {

            public ResultSet createResultSet(Connection conn) throws SQLException {
                return conn.getMetaData().getExportedKeys(conn.getCatalog(), null, tblName.toUpperCase());
            }
        }, new ResultSetCallback<List<KeyEntry>>() {

            public List<KeyEntry> doInResultSet(ResultSet rs) throws SQLException, DataAccessException {
                final List<KeyEntry> eks = new ArrayList<KeyEntry>(4);
                while (rs.next()) {
                    KeyEntry key = new KeyEntry();
                    key.setIndex(rs.getInt(9));
                    key.setPrimaryKeyTableName(rs.getString(3));
                    key.setPrimaryKeyColumnName(rs.getString(4));
                    key.setForeignKeyTableName(rs.getString(7));
                    key.setForeignKeyColumnName(rs.getString(8));
                    eks.add(key);
                }
                return eks;
            }
        });
    }
}
