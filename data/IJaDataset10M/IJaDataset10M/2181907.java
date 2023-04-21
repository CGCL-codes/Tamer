package databaseVersionControl.test;

import junit.framework.Assert;
import org.junit.Test;
import databaseVersionControl.domain.db.Column;
import databaseVersionControl.domain.db.DataType;
import databaseVersionControl.domain.db.Table;

public class TableTest extends TestHarness {

    @Test
    public void tableWithoutColumnsTest() {
        String expected = "TABLE USER();";
        Assert.assertEquals(expected, getSQLForDBCompoment(new Table("USER")));
    }

    @Test
    public void tableWith2ColumnsTest() {
        String expected = "TABLE USER(USER VARCHAR(15), PASSWORD VARCHAR(50));";
        Table table = new Table("USER").addColumn(new Column("USER").type(DataType.STRING).size(15)).addColumn(new Column("PASSWORD").type(DataType.STRING).size(50));
        Assert.assertEquals(expected, getSQLForDBCompoment(table));
    }

    @Test
    public void tableWithPrimaryKeyColumn() {
        String expected = "TABLE USER(ID BIGINT PRIMARY KEY);";
        Table table = new Table("USER").addColumn(new Column("ID").type(DataType.LONG).primaryKey());
        Assert.assertEquals(expected, getSQLForDBCompoment(table));
    }
}
