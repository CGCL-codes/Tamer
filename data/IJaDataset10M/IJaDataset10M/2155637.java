package jstorm.mapping;

import jstorm.test.database.*;
import jstorm.test.*;
import jstorm.factory.*;
import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (5/26/2001 5:39:39 PM)
 * @author: Administrator
 */
public class TestSqlRunner extends junit.framework.TestCase {

    private SqlRunner runner;

    private Mapping mapping;

    private Mapping secondMapping;

    private Mapping compositeMapping;

    private DummyConnection connection;

    /**
     * TestSqlRunner constructor comment.
     * @param name java.lang.String
     */
    public TestSqlRunner(String name) {
        super(name);
    }

    /**
     * Insert the method's description here.
     * Creation date: (6/28/2001 3:31:29 PM)
     * @return java.util.Date
     */
    public void compareSqlDates(java.sql.Timestamp first, java.sql.Timestamp second) {
        long oldMilis = first.getTime();
        long newMilis = second.getTime();
        assertTrue(oldMilis < newMilis);
    }

    /**
     * Insert the method's description here.
     * Creation date: (6/28/2001 3:31:29 PM)
     * @return java.util.Date
     */
    public java.sql.Timestamp generateOldConcurrencyStamp() {
        return new java.sql.Timestamp(System.currentTimeMillis() - 1000);
        ;
    }

    /**
     * Insert the method's description here.
     * Creation date: (6/8/2001 5:07:29 PM)
     */
    public void setUp() {
        DummyDatabaseFactory databaseFactory = new DummyDatabaseFactory();
        connection = new DummyConnection();
        DummyResultSet resultSet = new DummyResultSet();
        Hashtable row = new Hashtable();
        row.put("Test_Attribute", "Attribute string");
        row.put("Test_Attribute1", "Attribute1 string");
        row.put("Test_IntAttribute", new Integer(10));
        row.put("Test_FloatAttribute", new Float(1.1));
        row.put("Test_DateAttribute", new java.sql.Date(0));
        row.put("Test_DoubleAttribute", new Double(1.111));
        row.put("TestAggregate_Attribute", "Aggregate Attribute String");
        row.put("Test_TableId", new Long(1));
        row.put("TestAggregate_TableId", new Long(1));
        row.put("Sequence", new Long(100));
        resultSet.addRow(row);
        connection.getPreparedStatement().setResultSet(resultSet);
        databaseFactory.setConnection(connection);
        DatabaseFactory.setConnectionFactory(databaseFactory);
        runner = new SqlRunner();
        mapping = new TestObject().getMapping();
        secondMapping = new TestAggregateObject().getMapping();
        compositeMapping = new Mapping();
        compositeMapping.setObjectClass(TestObject.class);
        compositeMapping.setTableName("TestComposite");
        compositeMapping.addAttribute(new Attribute("attribute", "Attribute", String.class));
        compositeMapping.addAttribute(new Attribute("tableId", "TableId", Long.TYPE, true));
        compositeMapping.addAttribute(new Attribute("intAttribute", "IntAttribute", Integer.TYPE, true));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testConcurrencyFailureForDelete() throws Exception {
        java.sql.Timestamp oldConcurrencyStamp = generateOldConcurrencyStamp();
        TestConcurrencyObject object = new TestConcurrencyObject();
        object.setKeyField(1);
        object.setConcurrencyStamp(oldConcurrencyStamp);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        statement.setReturnCount(0);
        try {
            runner.doDelete(object, object.getMapping());
            fail("Should have thrown concurrency exception");
        } catch (ConcurrencyException ex) {
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testConcurrencyFailureForUpdate() throws Exception {
        java.sql.Timestamp oldConcurrencyStamp = generateOldConcurrencyStamp();
        TestConcurrencyObject object = new TestConcurrencyObject();
        object.setKeyField(1);
        object.setConcurrencyStamp(oldConcurrencyStamp);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        statement.setReturnCount(0);
        try {
            runner.doUpdate(object, object.getMapping());
            fail("Should have thrown concurrency exception");
        } catch (ConcurrencyException ex) {
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoAggregateSelect() throws Exception {
        Iterator objects = runner.doAggregateSelect(mapping, secondMapping, "Test.TableId", "TestAggregate.TableId").iterator();
        TestAggregateObject aggregateObject = (TestAggregateObject) objects.next();
        assertEquals(1, aggregateObject.getAggregateTableId());
        assertEquals("Aggregate Attribute String", aggregateObject.getAggregateAttribute());
        TestObject testObject = aggregateObject.getTestObject();
        assertEquals("Attribute string", testObject.getAttribute());
        assertEquals(1, testObject.getTableId());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoCompositeDelete() throws Exception {
        TestObject object = new TestObject();
        object.setTableId(1);
        object.setIntAttribute(2);
        runner.doDelete(object, compositeMapping);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("DELETE FROM TestComposite WHERE TableId = ? AND IntAttribute = ?", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertTrue(statement.isExecuteUpdate());
        assertEquals(new Long(1), insertedValues.get(new Integer(1)));
        assertEquals(new Integer(2), insertedValues.get(new Integer(2)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoCompositeUpdate() throws Exception {
        TestObject object = new TestObject();
        object.setTableId(1);
        object.setIntAttribute(2);
        object.setAttribute("TestAttribute");
        runner.doUpdate(object, compositeMapping);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("UPDATE TestComposite SET Attribute = ?, TableId = ?, IntAttribute = ? WHERE TableId = ? AND IntAttribute = ?", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertTrue(statement.isExecuteUpdate());
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        assertEquals(new Long(1), insertedValues.get(new Integer(2)));
        assertEquals(new Integer(2), insertedValues.get(new Integer(3)));
        assertEquals(new Long(1), insertedValues.get(new Integer(4)));
        assertEquals(new Integer(2), insertedValues.get(new Integer(5)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoDelete() throws Exception {
        TestObject object = new TestObject();
        object.setTableId(1);
        runner.doDelete(object, mapping);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("DELETE FROM Test WHERE TableId = ?", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertTrue(statement.isExecuteUpdate());
        assertEquals(new Long(1), insertedValues.get(new Integer(1)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoDeleteWithConcurrency() throws Exception {
        java.sql.Timestamp oldConcurrencyStamp = generateOldConcurrencyStamp();
        TestConcurrencyObject object = new TestConcurrencyObject();
        object.setKeyField(1);
        object.setConcurrencyStamp(oldConcurrencyStamp);
        runner.doDelete(object, object.getMapping());
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("DELETE FROM TestConcurrency WHERE KeyField = ? AND Concurrency_Stamp = ?", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertTrue(statement.isExecuteUpdate());
        assertEquals(new Long(1), insertedValues.get(new Integer(1)));
        assertEquals(oldConcurrencyStamp, insertedValues.get(new Integer(2)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoInsert() throws Exception {
        TestObject object = new TestObject();
        object.setAttribute("TestAttribute");
        runner.doInsert(object, mapping);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("INSERT INTO Test (Attribute, Attribute1, TableId, IntAttribute, FloatAttribute, DoubleAttribute, DateAttribute) VALUES (?, ?, null, ?, ?, ?, ?)", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        assertEquals(100, object.getTableId());
        assertTrue(statement.isExecuteUpdate());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoInsertWithConcurrency() throws Exception {
        java.sql.Timestamp oldConcurrencyStamp = generateOldConcurrencyStamp();
        TestConcurrencyObject object = new TestConcurrencyObject();
        object.setAttribute("TestAttribute");
        object.setKeyField(1);
        object.setConcurrencyStamp(oldConcurrencyStamp);
        runner.doInsert(object, object.getMapping());
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("INSERT INTO TestConcurrency (AttributeColumn, KeyField, Concurrency_Stamp) VALUES (?, null, ?)", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        compareSqlDates(oldConcurrencyStamp, (java.sql.Timestamp) insertedValues.get(new Integer(2)));
        assertEquals(100, object.getKeyField());
        assertTrue(statement.isExecuteUpdate());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoInsertWithoutKey() throws Exception {
        TestAggregateObject object = new TestAggregateObject();
        object.setAggregateAttribute("TestAttribute");
        runner.doInsert(object, secondMapping);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("INSERT INTO TestAggregate (Attribute, TableId) VALUES (?, ?)", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoSelect() throws Exception {
        Iterator objects = runner.doSelect(mapping).iterator();
        TestObject object = (TestObject) objects.next();
        assertEquals("Attribute string", object.getAttribute());
        assertEquals(1, object.getTableId());
        assertEquals(10, object.getIntAttribute());
        assertEquals(new Float(1.1), new Float(object.getFloatAttribute()));
        assertEquals(new java.sql.Date(0), object.getDateAttribute());
        assertEquals(1.111, object.getDoubleAttribute(), 0);
        assertTrue(connection.getPreparedStatement().isExecuteQuery());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoSelectWithWhere() throws Exception {
        runner.doSelect(mapping, "ProjectId = 1");
        String expectedStatement = "SELECT Test.Attribute AS Test_Attribute, Test.Attribute1 AS Test_Attribute1, Test.TableId AS Test_TableId, Test.IntAttribute AS Test_IntAttribute, Test.FloatAttribute AS Test_FloatAttribute, Test.DoubleAttribute AS Test_DoubleAttribute, Test.DateAttribute AS Test_DateAttribute FROM Test WHERE ProjectId = 1";
        assertEquals(expectedStatement, connection.getPreparedStatement().getSqlQuery());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoUpdate() throws Exception {
        TestObject object = new TestObject();
        object.setTableId(1);
        object.setAttribute("TestAttribute");
        runner.doUpdate(object, mapping);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("UPDATE Test SET Attribute = ?, Attribute1 = ?, IntAttribute = ?, FloatAttribute = ?, DoubleAttribute = ?, DateAttribute = ? WHERE TableId = ?", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertTrue(statement.isExecuteUpdate());
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        assertEquals(new Long(1), insertedValues.get(new Integer(7)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoUpdateWithConcurrency() throws Exception {
        java.sql.Timestamp oldConcurrencyStamp = generateOldConcurrencyStamp();
        TestConcurrencyObject object = new TestConcurrencyObject();
        object.setAttribute("TestAttribute");
        object.setKeyField(1);
        object.setConcurrencyStamp(oldConcurrencyStamp);
        runner.doUpdate(object, object.getMapping());
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("UPDATE TestConcurrency SET AttributeColumn = ?, Concurrency_Stamp = ? WHERE KeyField = ? AND Concurrency_Stamp = ?", connection.getPrepareCallSql());
        Hashtable insertedValues = statement.getInsertedValues();
        assertTrue(statement.isExecuteUpdate());
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        compareSqlDates(oldConcurrencyStamp, (java.sql.Timestamp) insertedValues.get(new Integer(2)));
        assertEquals(new Long(1), insertedValues.get(new Integer(3)));
        assertEquals(oldConcurrencyStamp, insertedValues.get(new Integer(4)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testSetValuesOnStatement() throws Exception {
        TestObject object = new TestObject();
        object.setAttribute("TestAttribute");
        object.setAttribute1("TestAttribute1");
        object.setTableId(1);
        object.setIntAttribute(10);
        object.setFloatAttribute(1.1f);
        object.setDoubleAttribute(1.111);
        object.setDateAttribute(new java.sql.Date(0));
        DummyPreparedStatement statement = connection.getPreparedStatement();
        runner.setValuesOnStatement(mapping, statement, object);
        Hashtable insertedValues = statement.getInsertedValues();
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        assertEquals("TestAttribute1", insertedValues.get(new Integer(2)));
        assertEquals(new Integer(10), insertedValues.get(new Integer(3)));
        assertEquals(new Float(1.1f), insertedValues.get(new Integer(4)));
        assertEquals(new Double(1.111), insertedValues.get(new Integer(5)));
        assertEquals(new Date(0), insertedValues.get(new Integer(6)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testSetValuesOnStatementComAndCon() throws Exception {
        java.sql.Timestamp concurrencyStamp = generateOldConcurrencyStamp();
        TestCompositeConcurrencyObject object = new TestCompositeConcurrencyObject();
        object.setAttribute1("TestAttribute1");
        object.setAttribute2("TestAttribute2");
        object.setSecondTableId(10);
        object.setTableId(20);
        object.setConcurrencyStamp(concurrencyStamp);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        runner.setValuesOnStatement(object.getMapping(), statement, object);
        Hashtable insertedValues = statement.getInsertedValues();
        compareSqlDates(concurrencyStamp, (java.sql.Timestamp) insertedValues.get(new Integer(1)));
        assertEquals("TestAttribute1", insertedValues.get(new Integer(2)));
        assertEquals("TestAttribute2", insertedValues.get(new Integer(3)));
        assertEquals(new Long(20), insertedValues.get(new Integer(4)));
        assertEquals(new Long(10), insertedValues.get(new Integer(5)));
        compareSqlDates(concurrencyStamp, object.getNewConcurrencyStamp());
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testSetValuesOnStatementForComposite() throws Exception {
        TestObject object = new TestObject();
        object.setAttribute("TestAttribute");
        object.setTableId(1);
        object.setIntAttribute(10);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        runner.setValuesOnStatement(compositeMapping, statement, object);
        Hashtable insertedValues = statement.getInsertedValues();
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        assertEquals(new Long(1), insertedValues.get(new Integer(2)));
        assertEquals(new Integer(10), insertedValues.get(new Integer(3)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testSetValuesOnStatementWithConcurrencyStamp() throws Exception {
        java.sql.Timestamp concurrencyStamp = generateOldConcurrencyStamp();
        TestConcurrencyObject object = new TestConcurrencyObject();
        object.setAttribute("TestAttribute");
        object.setKeyField(1);
        object.setConcurrencyStamp(concurrencyStamp);
        DummyPreparedStatement statement = connection.getPreparedStatement();
        runner.setValuesOnStatement(object.getMapping(), statement, object);
        Hashtable insertedValues = statement.getInsertedValues();
        assertEquals("TestAttribute", insertedValues.get(new Integer(1)));
        compareSqlDates(concurrencyStamp, (java.sql.Timestamp) insertedValues.get(new Integer(2)));
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoSelectWithConstraint() throws Exception {
        TestObject testObject = new TestObject();
        testObject.setAttribute("Attribute string");
        Iterator objects = runner.doSelect(testObject).iterator();
        String sqlExecuted = connection.getPreparedStatement().getSqlQuery();
        assertEquals("SELECT Test.Attribute AS Test_Attribute, Test.Attribute1 AS Test_Attribute1, Test.TableId AS Test_TableId, Test.IntAttribute AS Test_IntAttribute, Test.FloatAttribute AS Test_FloatAttribute, Test.DoubleAttribute AS Test_DoubleAttribute, Test.DateAttribute AS Test_DateAttribute FROM Test WHERE Test.Attribute = 'Attribute string'", sqlExecuted);
        TestObject object = (TestObject) objects.next();
        assertEquals("Attribute string", object.getAttribute());
        assertEquals(1, object.getTableId());
        assertEquals(10, object.getIntAttribute());
        assertEquals(new Float(1.1), new Float(object.getFloatAttribute()));
        assertEquals(new java.sql.Date(0), object.getDateAttribute());
        assertEquals(1.111, object.getDoubleAttribute(), 0);
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/26/2001 5:40:14 PM)
     */
    public void testDoAggregateSelectWithWhere() throws Exception {
        runner.doAggregateSelect(mapping, secondMapping, "Test.TableId", "TestAggregate.TableId", "TEST WHERE CLAUSE");
        DummyPreparedStatement statement = connection.getPreparedStatement();
        assertEquals("SELECT Test.Attribute AS Test_Attribute, Test.Attribute1 AS Test_Attribute1, Test.TableId AS Test_TableId, Test.IntAttribute AS Test_IntAttribute, Test.FloatAttribute AS Test_FloatAttribute, Test.DoubleAttribute AS Test_DoubleAttribute, Test.DateAttribute AS Test_DateAttribute, TestAggregate.Attribute AS TestAggregate_Attribute, TestAggregate.TableId AS TestAggregate_TableId FROM Test, TestAggregate WHERE Test.TableId = TestAggregate.TableId AND TEST WHERE CLAUSE", statement.getSqlQuery());
    }
}
