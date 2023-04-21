package com.hyd.dao.dbtool;

import com.hyd.dao.database.ColumnInfo;
import com.hyd.dao.database.Connector;
import com.hyd.dao.database.commandbuilder.helper.CommandBuilderHelper;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 获取数据库信息的类<br/>
 * <p/>
 * name: TABLE_CAT
 * type: VARCHAR2
 * ---------------------
 * name: TABLE_SCHEM
 * type: VARCHAR2
 * ---------------------
 * name: TABLE_NAME
 * type: VARCHAR2
 * ---------------------
 * name: COLUMN_NAME
 * type: VARCHAR2
 * ---------------------
 * name: DATA_TYPE
 * type: NUMBER
 * ---------------------
 * name: TYPE_NAME
 * type: VARCHAR2
 * ---------------------
 * name: COLUMN_SIZE
 * type: NUMBER
 * ---------------------
 * name: BUFFER_LENGTH
 * type: NUMBER
 * ---------------------
 * name: DECIMAL_DIGITS
 * type: NUMBER
 * ---------------------
 * name: NUM_PREC_RADIX
 * type: NUMBER
 * ---------------------
 * name: NULLABLE
 * type: NUMBER
 * ---------------------
 * name: REMARKS
 * type: VARCHAR2
 * ---------------------
 * name: COLUMN_DEF
 * type: LONG
 * ---------------------
 * name: SQL_DATA_TYPE
 * type: NUMBER
 * ---------------------
 * name: SQL_DATETIME_SUB
 * type: NUMBER
 * ---------------------
 * name: CHAR_OCTET_LENGTH
 * type: NUMBER
 * ---------------------
 * name: ORDINAL_POSITION
 * type: NUMBER
 * ---------------------
 * name: IS_NULLABLE
 * type: VARCHAR2
 * ---------------------
 */
@SuppressWarnings({ "unchecked" })
public class DbMetaTool {

    private String dsName;

    public DbMetaTool(String dsName) {
        this.dsName = dsName;
    }

    public String[] getTableNames() throws SQLException {
        Connection conn = Connector.getConnection(dsName);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(conn.getCatalog(), conn.getMetaData().getUserName().toUpperCase(), "%", null);
            ArrayList names = new ArrayList();
            while (tables.next()) {
                names.add(tables.getString("TABLE_NAME"));
            }
            return (String[]) names.toArray(new String[names.size()]);
        } finally {
            conn.close();
        }
    }

    public String[] getColumnNames(String tableName) throws SQLException {
        tableName = tableName.toUpperCase();
        Connection conn = Connector.getConnection(dsName);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getColumns(conn.getCatalog(), conn.getMetaData().getUserName().toUpperCase(), tableName, "%");
            ArrayList names = new ArrayList();
            while (tables.next()) {
                names.add(tables.getString("COLUMN_NAME"));
            }
            return (String[]) names.toArray(new String[names.size()]);
        } finally {
            conn.close();
        }
    }

    public ColumnInfo[] getColumnInfos(String tableName) throws SQLException {
        Connection conn = Connector.getConnection(dsName);
        try {
            String schema = conn.getMetaData().getUserName().toUpperCase();
            return CommandBuilderHelper.getHelper(conn).getColumnInfos(schema, tableName);
        } finally {
            conn.close();
        }
    }

    public String[] getProcedureNames() throws SQLException {
        Connection conn = Connector.getConnection(dsName);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            String schema = metaData.getUserName().toUpperCase();
            ResultSet procedures = metaData.getProcedures(conn.getCatalog(), schema, "%");
            ArrayList names = new ArrayList();
            while (procedures.next()) {
                names.add(procedures.getString("PROCEDURE_NAME"));
            }
            return (String[]) names.toArray(new String[names.size()]);
        } finally {
            conn.close();
        }
    }

    public static void showRsMeta(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            System.out.println("name: " + meta.getColumnName(i + 1));
            System.out.println("type: " + meta.getColumnTypeName(i + 1));
            System.out.println("---------------------");
        }
    }
}
