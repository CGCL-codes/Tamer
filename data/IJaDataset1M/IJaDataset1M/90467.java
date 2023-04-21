package uk.org.ogsadai.resource.dataresource.jdbc;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import uk.org.ogsadai.converters.databaseschema.ColumnMetaData;
import uk.org.ogsadai.converters.databaseschema.ColumnMetaDataImpl;
import uk.org.ogsadai.converters.databaseschema.ResultSetTableMetaDataException;
import uk.org.ogsadai.converters.databaseschema.TableMetaData;
import uk.org.ogsadai.converters.databaseschema.TableMetaDataImpl;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.dataresource.jdbc.JDBCColumnTypeMapper;
import uk.org.ogsadai.tuple.ColumnMetadata;
import uk.org.ogsadai.tuple.SimpleColumnMetadata;
import uk.org.ogsadai.tuple.SimpleTupleMetadata;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * Oracle Metadata handler.
 * 
 * @author The OGSA-DAI Project Team
 *
 */
public class OracleMetaDataHandler implements MetaDataHandler {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007.";

    /**
     * {@inheritDoc}
     */
    public TupleMetadata produceResultSetMetaData(ResultSetMetaData queryMetadata, JDBCColumnTypeMapper mapper, ResourceID dataResourceID, URI dresUri) throws SQLException {
        int numberOfColumns = queryMetadata.getColumnCount();
        List columnmetadata = new ArrayList();
        for (int i = 1; i <= numberOfColumns; i++) {
            String tableName = queryMetadata.getTableName(i);
            if (tableName.length() == 0) {
                tableName = null;
            }
            ColumnMetadata colmetadata = new SimpleColumnMetadata(queryMetadata.getColumnLabel(i), tableName, dataResourceID, dresUri, mapper.mapSQLTypeToODTupleType(queryMetadata.getColumnType(i), queryMetadata.getColumnTypeName(i)), queryMetadata.getPrecision(i), queryMetadata.isNullable(i), queryMetadata.getColumnDisplaySize(i));
            columnmetadata.add(colmetadata);
        }
        return new SimpleTupleMetadata(columnmetadata);
    }

    /**
     * {@inheritDoc}
     */
    public TableMetaData produceTableColumnMetaData(ResultSet tableMetadata, JDBCColumnTypeMapper mapper, String catalog, String schema, String name) throws SQLException, ResultSetTableMetaDataException {
        TableMetaDataImpl table = new TableMetaDataImpl(catalog, schema, name);
        this.setTableColumns(tableMetadata, mapper, schema, table);
        return table;
    }

    /**
     * Extracts information from the given ResultSet and populates the column
     * metadata.
     * 
     * Notes: Specific to OracleExtractTableSchema. Order of value extraction
     * seems to matter.
     * 
     * @param columns
     *            ResultSet containing column metadata.
     * @param mapper 
     * @param schemaName
     *            Oracle Schema name.
     * @param table
     *            Target Table Metadata.
     * @throws ResultSetTableMetaDataException
     *             if there is a problem accessing information from the
     *             ResultSet.
     */
    private void setTableColumns(ResultSet columns, JDBCColumnTypeMapper mapper, String schemaName, TableMetaDataImpl table) throws ResultSetTableMetaDataException {
        Set<ColumnMetaData> columnSet = new HashSet<ColumnMetaData>();
        String tableName = table.getName();
        try {
            while (columns.next()) {
                String defaultValue = columns.getString("COLUMN_DEF");
                ColumnMetaDataImpl column = new ColumnMetaDataImpl(columns.getString(TableMetaDataImpl.JDBC_COL_NAME), columns.getInt(TableMetaDataImpl.JDBC_COL_POSITION), table);
                column.setFullName(tableName + "_" + column.getName());
                int dataType = columns.getInt(TableMetaDataImpl.JDBC_COL_DATA_TYPE);
                String typeName = columns.getString(TableMetaDataImpl.JDBC_COL_TYPE_NAME);
                column.setDataType(dataType);
                column.setTypeName(typeName);
                column.setTupleType(mapper.mapSQLTypeToODTupleType(dataType, typeName));
                column.setColumnSize(columns.getInt(TableMetaDataImpl.JDBC_COL_SIZE));
                column.setDecimalDigits(columns.getInt(TableMetaDataImpl.JDBC_COL_DEC_DIGITS));
                column.setNullable(columns.getInt("NULLABLE") == 1);
                column.setDefaultValue(defaultValue);
                columnSet.add(column);
            }
            columns.close();
        } catch (SQLException e) {
            throw new ResultSetTableMetaDataException(table.getCatalogName(), schemaName, tableName, e);
        }
        table.setColumns((ColumnMetaData[]) columnSet.toArray(new ColumnMetaData[0]));
    }
}
