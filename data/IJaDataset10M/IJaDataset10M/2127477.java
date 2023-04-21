package auto_test.tests;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
import jtq.QueryResult;
import jtq.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import auto_test.tables.main_test.Row;
import auto_test.tables.main_test.Table;

public class InsertQuery {

    @Before
    public void before() throws Throwable {
        HelperFunctions.clearTables(Table.INSTANCE);
    }

    @Test
    public void insertAndDeleteTest_01() throws Throwable {
        final int rowsToInsert = 10;
        for (int index = 0; index < rowsToInsert; index++) {
            UUID key = UUID.randomUUID();
            String text = "" + index;
            Integer integer = new Integer(index);
            BigDecimal decimal = new BigDecimal(index + (index / 97));
            Boolean bool = index % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            insertRow(key, text, integer, decimal, bool, timestamp);
        }
        Assert.assertTrue(HelperFunctions.getRowCount(Table.INSTANCE) == rowsToInsert);
        for (int index = 0; index < rowsToInsert; index++) {
            deleteRow(Integer.valueOf(index));
            Assert.assertTrue(HelperFunctions.getRowCount(Table.INSTANCE) == (rowsToInsert - index - 1));
        }
    }

    @Test
    public void insertAndDeleteTest_02() throws Throwable {
        final int rowsToInsert = 10;
        for (int index = 0; index < rowsToInsert; index++) {
            UUID key = UUID.randomUUID();
            String text = "" + index;
            Integer integer = new Integer(index);
            BigDecimal decimal = new BigDecimal(index + (index / 97));
            Boolean bool = index % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            insertRowAsARowObject(key, text, integer, decimal, bool, timestamp);
        }
        Assert.assertTrue(HelperFunctions.getRowCount(Table.INSTANCE) == rowsToInsert);
        Assert.assertTrue(HelperFunctions.getRowCount(Table.INSTANCE) == rowsToInsert);
        for (int index = 0; index < rowsToInsert; index++) {
            deleteRow(Integer.valueOf(index));
            Assert.assertTrue(HelperFunctions.getRowCount(Table.INSTANCE) == (rowsToInsert - index - 1));
        }
    }

    public static void deleteRow(Integer pInteger) throws Throwable {
        Table table = Table.INSTANCE;
        Transaction transaction = new Transaction();
        try {
            int rows = jtq.Query.delete(table).where(table.integer.equalTo(pInteger)).execute(transaction);
            Assert.assertTrue(rows == 1);
            transaction.commit();
        } catch (Throwable e) {
            transaction.rollBack();
            throw e;
        }
    }

    public static void insertRow(UUID pKey, String pText, Integer pInteger, BigDecimal pDecimal, Boolean pBoolean, Timestamp pTimestamp) throws Throwable {
        Table table = Table.INSTANCE;
        Transaction transaction = new Transaction();
        try {
            int inserted = jtq.Query.insertInto(table).set(table.key, pKey).set(table.text, pText).set(table.integer, pInteger).set(table.decimal, pDecimal).set(table.bool, pBoolean).set(table.timestamp, pTimestamp).execute(transaction);
            Assert.assertTrue(inserted == 1);
            QueryResult result = jtq.Query.selectAll().from(table).where(table.integer.equalTo(pInteger)).execute(transaction);
            Assert.assertTrue(result.size() == 1);
            Row row = result.getRow(table, 0);
            Assert.assertEquals(row.getKey(), pKey);
            Assert.assertEquals(row.getText(), pText);
            Assert.assertEquals(row.getInteger(), pInteger);
            Assert.assertTrue(row.getDecimal().compareTo(pDecimal) == 0);
            Assert.assertEquals(row.getBoolean(), pBoolean);
            Timestamp timestampColumn = row.getTimestamp();
            Assert.assertTrue(HelperFunctions.Compare(timestampColumn, pTimestamp));
            transaction.commit();
        } catch (Throwable e) {
            transaction.rollBack();
            throw e;
        }
    }

    public static void insertRowAsARowObject(UUID pKey, String pText, Integer pInteger, BigDecimal pDecimal, Boolean pBoolean, Timestamp pTimestamp) throws Throwable {
        Table table = Table.INSTANCE;
        Transaction transaction = new Transaction();
        try {
            Row row = new Row();
            row.setKey(pKey);
            row.setText(pText);
            row.setInteger(pInteger);
            row.setDecimal(pDecimal);
            row.setBoolean(pBoolean);
            row.setTimestamp(pTimestamp);
            row.update(transaction);
            QueryResult result = jtq.Query.selectAll().from(table).where(table.integer.equalTo(pInteger)).execute(transaction);
            Assert.assertTrue(result.size() == 1);
            row = result.getRow(table, 0);
            Assert.assertEquals(row.getKey(), pKey);
            Assert.assertEquals(row.getText(), pText);
            Assert.assertEquals(row.getInteger(), pInteger);
            Assert.assertTrue(row.getDecimal().compareTo(pDecimal) == 0);
            Assert.assertEquals(row.getBoolean(), pBoolean);
            Assert.assertTrue(HelperFunctions.Compare(row.getTimestamp(), pTimestamp));
            transaction.commit();
        } catch (Throwable e) {
            transaction.rollBack();
            throw e;
        }
    }
}
