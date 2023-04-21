package com.lettuce.util.seedbuilder.impl;

import static com.lettuce.core.Phrases.P;
import static com.lettuce.core.phrase.Keyword.AND;
import static com.lettuce.core.phrase.Keyword.EQ;
import static com.lettuce.core.phrase.Keyword.FROM;
import static com.lettuce.core.phrase.Keyword.SELECT;
import static com.lettuce.core.phrase.Keyword.WHERE;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.lettuce.client.XDataSource;
import com.lettuce.core.Query;
import com.lettuce.core.Variable;
import com.lettuce.core.phrase.Column;
import com.lettuce.core.phrase.Table;
import com.lettuce.util.seedbuilder.ISeedsInfoProvider;
import com.lettuce.util.seedbuilder.SeedColumnInfo;
import com.lettuce.util.seedbuilder.SeedInfo;

/**
 * Seeds info provider provides seeds information from a db connection.
 * <p>
 * <b>NOTE:</b> IT IS ONLY TESTED FOR ORACLE DATABASE.
 * 
 * @author Zhigang Xie
 * @version 1.0, 2010-007-04
 * @since JDK 1.5
 */
public class ConnectionSeedsInfoProvider implements ISeedsInfoProvider {

    private static final int TABLE_NAME_INDEX = 3;

    private static final int COLUMN_NAME_INDEX = 4;

    private static final int TYPE_NAME_INDEX = 6;

    private static final int COLUMN_SIZE_INDEX = 7;

    private static final int DECIMAL_DIGITS_INDEX = 9;

    private static final int NULLABLE_INDEX = 11;

    private static final String REMARKS = "REMARKS";

    private List<SeedInfo> tableDescs = null;

    private Connection conn;

    private String schema;

    private String tableNamePattern;

    private String encoding;

    public ConnectionSeedsInfoProvider() {
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
	 * Get seeds information from a db connection by reading the meta-data.
	 * 
	 * @param conn
	 *            - the db connection.
	 * @param schema
	 *            - for which schema the seeds information is from.
	 */
    public ConnectionSeedsInfoProvider(Connection conn, String schema) {
        this.conn = conn;
        this.schema = schema;
    }

    /**
	 * Get seeds information from a db connection by reading the meta-data.
	 * 
	 * @param conn
	 *            - the db connection.
	 * @param schema
	 *            - for which schema the seeds information is from.
	 * @param tableNamePattern
	 *            - with a regular expression to match the table names selected
	 *            for seeds.
	 */
    public ConnectionSeedsInfoProvider(Connection conn, String schema, String tableNamePattern) {
        this.conn = conn;
        this.schema = schema;
        this.tableNamePattern = tableNamePattern;
    }

    /**
	 * @param conn
	 *            - the db connection.
	 */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
	 * @param schema
	 *            - for which schema the seeds information is from.
	 */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
	 * @param tableNamePattern
	 *            - with a regular expression to match the table names selected
	 *            for seeds.
	 */
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    private boolean isOracle = false;

    private boolean isMySql = false;

    private void collectSeedsInfo(Connection conn, String schema, String tableNamePattern) throws SQLException {
        String driver = conn.getMetaData().getDriverName().toLowerCase();
        if (driver.contains("oracle")) {
            isOracle = true;
        } else if (driver.contains("mysql")) {
            isMySql = true;
        }
        Pattern tablePtn = null;
        if (tableNamePattern != null) {
            tablePtn = Pattern.compile(tableNamePattern);
        }
        final String TABLE_SCHEMA = schema.toUpperCase();
        XDataSource xds = new XDataSource();
        xds.setConnection(conn);
        DatabaseMetaData dbmd;
        dbmd = xds.getConnection().getMetaData();
        ResultSet tables = dbmd.getTables(null, TABLE_SCHEMA, null, null);
        while (tables.next()) {
            String tableName = tables.getString(TABLE_NAME_INDEX);
            if (tablePtn == null || tablePtn.matcher(tableName).matches()) {
                SeedInfo tableDesc = new SeedInfo();
                tableDesc.setName(tableName);
                tableDesc.setTableName(tableName);
                tableDescs.add(tableDesc);
                tableDesc.setColumns(new ArrayList<SeedColumnInfo>());
            }
        }
        tables.close();
        Column COMMENTS = new Column("COMMENTS");
        Column colTABLE_NAME = new Column("TABLE_NAME");
        Column colCOLUMN_NAME = new Column("COLUMN_NAME");
        Table USER_TAB_COMMENTS = new Table("USER_TAB_COMMENTS");
        Table USER_COL_COMMENTS = new Table("USER_COL_COMMENTS");
        for (SeedInfo tableDesc : tableDescs) {
            tableDesc.setComments("");
            if (isOracle) {
                Variable<String> tableComments = new Variable<String>("");
                Query q = new Query(xds.getConnection(), P(SELECT, COMMENTS, FROM, USER_TAB_COMMENTS, WHERE, colTABLE_NAME, EQ, tableDesc.getTableName()));
                q.retrieve(tableComments);
                tableDesc.setComments(tableComments.value);
                q.close();
            }
            ResultSet columns = dbmd.getColumns(null, TABLE_SCHEMA, tableDesc.getTableName(), null);
            Variable<String> columnComments = new Variable<String>("");
            while (columns.next()) {
                SeedColumnInfo columnDesc = new SeedColumnInfo();
                String colName = columns.getString(COLUMN_NAME_INDEX);
                String colType = columns.getString(TYPE_NAME_INDEX);
                int colSize = columns.getInt(COLUMN_SIZE_INDEX);
                int colDecimal = columns.getInt(DECIMAL_DIGITS_INDEX);
                int colNullable = columns.getInt(NULLABLE_INDEX);
                if (isOracle) {
                    Query q = new Query(xds.getConnection(), P(SELECT, COMMENTS, FROM, USER_COL_COMMENTS, WHERE, colTABLE_NAME, EQ, tableDesc.getTableName(), AND, colCOLUMN_NAME, EQ, colName));
                    q.retrieve(columnComments);
                    q.close();
                } else if (isMySql) {
                    try {
                        if (encoding == null) {
                            columnComments.value = new String(columns.getBytes(REMARKS));
                        } else {
                            columnComments.value = new String(columns.getBytes(REMARKS), encoding);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                columnDesc.setColumnName(colName);
                columnDesc.setName(colName);
                columnDesc.setComments(columnComments.value);
                columnDesc.setColumnSize(colSize);
                columnDesc.setDataType(colType);
                columnDesc.setDecimalDigits(colDecimal);
                columnDesc.setNullable(colNullable > 0);
                tableDesc.getColumns().add(columnDesc);
            }
            columns.close();
        }
    }

    public SeedInfo[] getSeedsInfo() {
        if (this.tableDescs == null) {
            this.tableDescs = new ArrayList<SeedInfo>();
            try {
                collectSeedsInfo(conn, schema, tableNamePattern);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Collect seeds information failed.");
            }
        }
        SeedInfo[] arr = new SeedInfo[this.tableDescs.size()];
        this.tableDescs.toArray(arr);
        return arr;
    }
}
