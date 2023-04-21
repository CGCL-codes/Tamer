package org.demis.dwarf.database.reader.oracle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.demis.dwarf.database.DataBaseColumn;
import org.demis.dwarf.database.DataBaseReference;
import org.demis.dwarf.database.DataBasePrimaryKey;
import org.demis.dwarf.database.DataBaseSchema;
import org.demis.dwarf.database.DataBaseTable;
import org.demis.dwarf.database.reader.AbstractSchemaReader;

/**
 * @version 1.0
 * @author <a href="mailto:demis27@demis27.net">Stéphane kermabon</a>
 */
public class OracleSchemaReader extends AbstractSchemaReader {

    private final Log logger = LogFactory.getLog(OracleSchemaReader.class);

    public OracleSchemaReader() {
    }

    @Override
    public DataBaseSchema read(String url, String login, String password, String schemaName) {
        logger.info("******************************************************");
        logger.info("* read schema <" + schemaName + "> with url <" + url + ">");
        logger.info("******************************************************");
        Map<String, DataBaseTable> tables = new HashMap<String, DataBaseTable>();
        Connection connection = null;
        try {
            if (login != null) {
                connection = DriverManager.getConnection(url, login, password);
            } else {
                connection = DriverManager.getConnection(url);
            }
            DatabaseMetaData metaData = connection.getMetaData();
            tables.putAll(readTables(schemaName, connection));
            logger.info("******************************************************");
            logger.info("* read columns");
            logger.info("******************************************************");
            for (DataBaseTable table : tables.values()) {
                ResultSet resultSet = metaData.getColumns("", schemaName, table.getName(), "");
                logger.info("read columns of table " + table.getName());
                while (resultSet.next()) {
                    DataBaseColumn column = new DataBaseColumn(resultSet.getString("COLUMN_NAME"));
                    column.setType(resultSet.getInt("DATA_TYPE"));
                    column.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
                    column.setScale(resultSet.getInt("DECIMAL_DIGITS"));
                    int nullable = resultSet.getInt("NULLABLE");
                    column.setNotNull(nullable != DatabaseMetaData.columnNullable);
                    column.setRemarks(resultSet.getString("REMARKS"));
                    table.addColumn(column);
                    logger.info("read column : " + column.getName() + " of table " + table.getName());
                }
                resultSet.close();
                resultSet = metaData.getPrimaryKeys(null, schemaName, table.getName());
                DataBasePrimaryKey primaryKey = new DataBasePrimaryKey();
                logger.info("read primary key columns of table " + table.getName());
                while (resultSet.next()) {
                    DataBaseColumn column = table.getColumn(resultSet.getString("COLUMN_NAME"));
                    column.setPrimaryKey(true);
                    primaryKey.addColumn(column);
                    primaryKey.setName(resultSet.getString("PK_NAME"));
                    logger.info("read primary key column : " + column.getName() + " of table " + table.getName());
                }
                if (primaryKey.getName() != null) {
                    table.setPrimaryKey(primaryKey);
                }
                resultSet.close();
            }
            logger.info("******************************************************");
            logger.info("* read foreign keys ");
            logger.info("******************************************************");
            for (DataBaseTable table : tables.values()) {
                ResultSet resultSet = metaData.getExportedKeys(null, schemaName, table.getName());
                DataBaseReference reference = null;
                logger.info("read foreign key columns of table " + table.getName());
                while (resultSet.next()) {
                    String primaryKeyTableName = resultSet.getString("PKTABLE_NAME");
                    DataBaseTable referenceToTable = tables.get(primaryKeyTableName);
                    String primaryKeyColumnName = resultSet.getString("PKCOLUMN_NAME");
                    DataBaseColumn referenceToColumn = referenceToTable.getColumn(primaryKeyColumnName);
                    String foreignKeyTableName = resultSet.getString("FKTABLE_NAME");
                    DataBaseTable referenceFromTable = tables.get(foreignKeyTableName);
                    String foreignKeyColumnName = resultSet.getString("FKCOLUMN_NAME");
                    DataBaseColumn referenceFromColumn = referenceFromTable.getColumn(foreignKeyColumnName);
                    String referenceName = resultSet.getString("FK_NAME");
                    reference = referenceToTable.getToReference(referenceName);
                    if (reference == null) {
                        reference = new DataBaseReference();
                        reference.setName(referenceName);
                        reference.setToRefrenceTable(referenceToTable);
                        reference.setFromRefrenceTable(referenceFromTable);
                        referenceToTable.addToReference(reference);
                    }
                    reference.addReferencedColumn(referenceFromColumn, referenceToColumn);
                    referenceFromTable.addFromReference(reference);
                    logger.info("read foreign key name: <" + reference.getName() + "> " + reference.getToRefrenceTable().getName() + "." + referenceToColumn.getName() + " -> " + reference.getFromRefrenceTable().getName() + "." + referenceFromColumn.getName());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OracleSchemaReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataBaseSchema schema = new DataBaseSchema();
        schema.setName(schemaName);
        schema.addTables(new ArrayList<DataBaseTable>(tables.values()));
        logger.info("******************************************************");
        logger.info("* end of read schema <" + schemaName + "> with url <" + url + ">");
        logger.info("******************************************************");
        return schema;
    }
}
