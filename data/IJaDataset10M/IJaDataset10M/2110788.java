package org.dbunit.dataset;

import junit.framework.TestCase;
import org.dbunit.dataset.datatype.DataType;

/**
 * @author Manuel Laflamme
 * @version $Revision: 421 $
 * @since Feb 17, 2002
 */
public class LowerCaseTableMetaDataTest extends TestCase {

    public LowerCaseTableMetaDataTest(String s) {
        super(s);
    }

    public void testGetTableName() throws Exception {
        String original = "TABLE_NAME";
        String expected = original.toLowerCase();
        ITableMetaData metaData = new LowerCaseTableMetaData(new DefaultTableMetaData(original, new Column[0]));
        assertEquals("table name", expected, metaData.getTableName());
    }

    public void testGetColumns() throws Exception {
        Column[] columns = new Column[] { new Column("NUMBER_COLUMN", DataType.NUMERIC, "qwerty", Column.NULLABLE), new Column("STRING_COLUMN", DataType.VARCHAR, "toto", Column.NO_NULLS), new Column("BOOLEAN_COLUMN", DataType.BOOLEAN) };
        ITableMetaData metaData = new LowerCaseTableMetaData("TABLE_NAME", columns);
        Column[] lowerColumns = metaData.getColumns();
        assertEquals("column count", columns.length, lowerColumns.length);
        for (int i = 0; i < columns.length; i++) {
            Column column = columns[i];
            Column lowerColumn = lowerColumns[i];
            assertEquals("name", column.getColumnName().toLowerCase(), lowerColumn.getColumnName());
            assertTrue("name not equals", !column.getColumnName().equals(lowerColumn.getColumnName()));
            assertEquals("type", column.getDataType(), lowerColumn.getDataType());
            assertEquals("sql type", column.getSqlTypeName(), lowerColumn.getSqlTypeName());
            assertEquals("nullable", column.getNullable(), lowerColumn.getNullable());
        }
        assertEquals("key count", 0, metaData.getPrimaryKeys().length);
    }

    public void testGetPrimaryKeys() throws Exception {
        Column[] columns = new Column[] { new Column("NUMBER_COLUMN", DataType.NUMERIC, "qwerty", Column.NULLABLE), new Column("STRING_COLUMN", DataType.VARCHAR, "toto", Column.NO_NULLS), new Column("BOOLEAN_COLUMN", DataType.BOOLEAN) };
        String[] keyNames = new String[] { "Boolean_Column", "Number_Column" };
        ITableMetaData metaData = new LowerCaseTableMetaData("TABLE_NAME", columns, keyNames);
        Column[] keys = metaData.getPrimaryKeys();
        assertEquals("key count", keyNames.length, keys.length);
        for (int i = 0; i < keys.length; i++) {
            assertTrue("name not equals", !keyNames[i].equals(keys[i].getColumnName()));
            assertEquals("key name", keyNames[i].toLowerCase(), keys[i].getColumnName());
        }
    }
}
