package org.hironico.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hironico.database.driver.ConnectionPool;
import org.hironico.database.driver.PooledConnection;

/**
 * Modélise une colonne de table dans les meta données JDBC.
 * @author hironico
 * @since 0.0.2
 */
public class SQLTableColumn extends SQLObject {

    private static final Logger logger = Logger.getLogger("org.hironico.database");

    private String catalogName = null;

    private String schemaName = null;

    private String tableName = null;

    private int dataType = -1;

    private String typeName = null;

    private int columnSize = 0;

    private int decimalDigits = 0;

    private boolean nullable = true;

    private int ordinalPosition = 0;

    private String columnDef = null;

    public SQLTableColumn(String columnName, String tableName, ConnectionPool pool) {
        this(null, null, columnName, tableName, pool, true);
    }

    public SQLTableColumn(String tableCatalog, String tableSchema, String columnName, String tableName, ConnectionPool pool, boolean loadMetaData) {
        super(pool, columnName);
        this.catalogName = tableCatalog;
        this.schemaName = tableSchema;
        this.tableName = tableName;
        if (loadMetaData) {
            loadMetaData();
        }
    }

    @Override
    public String toSQLString(ScriptOperation operation) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean loadMetaData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ScriptOperation> getSupportedScriptOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exists() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static List<SQLTableColumn> getTableColumns(String catalogName, String schemaName, String tableName, ConnectionPool pool) {
        List<SQLTableColumn> colList = new ArrayList<SQLTableColumn>();
        PooledConnection pcon = pool.lockConnection(true);
        try {
            DatabaseMetaData metaData = pcon.getMetaData();
            ResultSet rsColonnes = metaData.getColumns(catalogName, schemaName, tableName, "%");
            while (rsColonnes.next()) {
                SQLTableColumn col = new SQLTableColumn(catalogName, schemaName, rsColonnes.getString("COLUMN_NAME"), tableName, pool, false);
                col.setDataType(rsColonnes.getShort("DATA_TYPE"));
                col.setColumnSize(rsColonnes.getInt("COLUMN_SIZE"));
                col.setDecimalDigits(rsColonnes.getInt("DECIMAL_DIGITS"));
                col.setOrdinalPosition(rsColonnes.getInt("ORDINAL_POSITION"));
                col.setTypeName(rsColonnes.getString("TYPE_NAME"));
                col.setNullable(rsColonnes.getString("IS_NULLABLE").equals("YES"));
                String defaultValue = rsColonnes.getString("COLUMN_DEF");
                if (defaultValue != null) {
                    if (defaultValue.startsWith("\"") && defaultValue.endsWith("\"")) {
                        if (defaultValue.length() > 2) {
                            defaultValue = "'" + defaultValue.substring(1, defaultValue.length() - 2) + "'";
                        } else {
                            defaultValue = "''";
                        }
                    }
                }
                col.setColumnDef(defaultValue);
                colList.add(col);
            }
            rsColonnes.close();
        } catch (SQLException sqle) {
            logger.error("Cannot get column metadata information for table '" + tableName + "'", sqle);
        } finally {
            pool.freeConnection(pcon);
        }
        return colList;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }
}
