package uk.org.ogsadai.resource.dataresource.jdbc;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
 * Interface for classes that provide handling for metadata for a JDBC resource.
 * 
 * @author The OGSA-DAI Project Team
 *
 */
public class DefaultMetaDataHandler implements MetaDataHandler {

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
        table.setColumns(tableMetadata, mapper);
        return table;
    }
}
