package databaseVersionControl.domain.db.sqlBuilder;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import databaseVersionControl.domain.db.Column;
import databaseVersionControl.domain.db.DataType;
import databaseVersionControl.domain.db.Table;
import databaseVersionControl.domain.db.sqlBuilder.CreateTable;
import databaseVersionControl.domain.dialect.oracle.OracleDialect;

public class CreateTableTest {

    OracleDialect _dialect = new OracleDialect();

    @Test
    public void createTableWithOneColumn() {
        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("COLUMNNAMEA", DataType.BOOLEAN));
        Table table = new Table("TABLENAME", columns);
        CreateTable createTable = new CreateTable(table);
        assertEquals("CREATE TABLE TABLENAME (\nCOLUMNNAMEA VARCHAR(1)\n);", createTable.buildSql(_dialect));
    }

    @Test
    public void createTableWithManyColumns() {
        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("COLUMNNAMEA", DataType.INTEGER, 8, false, true));
        columns.add(new Column("COLUMNNAMEB", DataType.BOOLEAN, null, false, false));
        columns.add(new Column("COLUMNNAMEC", DataType.DATE));
        columns.add(new Column("COLUMNNAMED", DataType.DOUBLE));
        Table table = new Table("TABLENAME", columns);
        CreateTable createTable = new CreateTable(table);
        StringBuilder sql = new StringBuilder("CREATE TABLE TABLENAME (\n");
        sql.append("COLUMNNAMEA NUMBER NOT NULL PRIMARY KEY,\n");
        sql.append("COLUMNNAMEB VARCHAR(1) NOT NULL,\n");
        sql.append("COLUMNNAMEC DATE,\n");
        sql.append("COLUMNNAMED NUMBER\n);");
        assertEquals(sql.toString(), createTable.buildSql(_dialect));
    }
}
