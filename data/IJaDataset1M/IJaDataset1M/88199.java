package org.unitils.dataset.util;

import org.dbmaintain.database.IdentifierProcessor;
import org.unitils.database.DatabaseUnitils;
import org.unitils.database.UnitilsDataSource;
import org.unitils.dataset.database.DataSourceWrapper;
import org.unitils.dataset.model.database.Column;
import org.unitils.dataset.model.database.Row;
import org.unitils.dataset.model.database.TableName;
import org.unitils.dataset.model.database.Value;
import javax.sql.DataSource;
import static java.sql.Types.INTEGER;
import static org.dbmaintain.database.StoredIdentifierCase.UPPER_CASE;
import static org.unitils.util.CollectionUtils.asSet;

/**
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class DataSetTestUtils {

    public static TableName createTableName() {
        return new TableName("schema", "table", "schema.table");
    }

    public static TableName createTableName(String schemaName, String tableName) {
        return new TableName(schemaName, tableName, schemaName + "." + tableName);
    }

    public static Row createRow() {
        return new Row(createTableName());
    }

    public static Row createRow(String schemaName, String tableName) {
        return new Row(createTableName(schemaName, tableName));
    }

    public static Row createRowWithIdentifier(String identifier) {
        return createRowWithIdentifier(identifier, "schema", "table");
    }

    public static Row createRowWithIdentifier(String identifier, String schemaName, String tableName) {
        return new Row(identifier, createTableName(schemaName, tableName));
    }

    public static Row createRow(Value... values) {
        return createRow("schema", "table", values);
    }

    public static Row createRow(String schemaName, String tableName, Value... values) {
        Row row = createRow(schemaName, tableName);
        for (Value value : values) {
            row.addValue(value);
        }
        return row;
    }

    public static Value createValue(String columnName, Object value) {
        return new Value(value, false, new Column(columnName, INTEGER, true));
    }

    public static IdentifierProcessor createIdentifierProcessor() {
        return new IdentifierProcessor(UPPER_CASE, "\"", "public");
    }

    public static DataSourceWrapper createDataSourceWrapper(String... schemaNames) {
        IdentifierProcessor identifierProcessor = createIdentifierProcessor();
        DataSource dataSource = DatabaseUnitils.getDataSource();
        UnitilsDataSource unitilsDataSource = new UnitilsDataSource(dataSource, asSet(schemaNames));
        return new DataSourceWrapper(unitilsDataSource, identifierProcessor);
    }
}
