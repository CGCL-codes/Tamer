package org.jaffa.persistence.blackboxtests;

import java.util.Collection;
import java.util.Date;
import junit.framework.TestCase;
import org.jaffa.persistence.UOW;
import org.jaffa.persistence.Criteria;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jaffa.datatypes.DateTime;
import org.jaffa.persistence.domainobjects.ItemMeta;
import org.jaffa.persistence.domainobjects.PartMeta;
import org.jaffa.persistence.domainobjects.ValidFieldValueMeta;

/** Tests for the various SQL Functions through the Jaffa Persistence Engine.
 *
 * @author GautamJ
 */
public class FunctionTest extends TestCase {

    private UOW m_uow = null;

    /** Assembles and returns a test suite containing all known tests.
     * @return A test suite.
     */
    public static Test suite() {
        return new Wrapper(new TestSuite(FunctionTest.class));
    }

    /** Creates new QueryTest
     * @param name The name of the test case.
     */
    public FunctionTest(String name) {
        super(name);
    }

    /** Sets up the fixture, by creating the UOW. This method is called before a test is executed.
     */
    protected void setUp() {
        try {
            m_uow = new UOW();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create a UOW: " + e.toString());
        }
    }

    /** Tears down the fixture, by closing the UOW. This method is called after a test is executed.
     */
    protected void tearDown() {
        try {
            if (m_uow != null) m_uow.rollback();
            m_uow = null;
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to rollback a UOW: " + e.toString());
        }
    }

    /** Tests the current DateTime from the server
     */
    public void testObtainCurrentDateTime() {
        try {
            Criteria c = new Criteria();
            c.addFunction(Criteria.FUNCTION_CURRENT_DATE_TIME, null, "cdt");
            DateTime localValue = new DateTime();
            Map m = (Map) m_uow.query(c).iterator().next();
            DateTime dbValue = (DateTime) m.get("cdt");
            assertEquals("Year on the database server does not match the current value", localValue.year(), dbValue.year());
            assertEquals("Month on the database server does not match the current value", localValue.month(), dbValue.month());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the count() function
     * select count(*), count(CREATED_DATETIME), count(ALL CREATED_DATETIME), count(DISTINCT CREATED_DATETIME),
     * count(KEY_REF), count(ALL KEY_REF), count(DISTINCT KEY_REF) from item;
     */
    public void testCount() {
        try {
            Criteria c = new Criteria();
            c.setTable(ItemMeta.getName());
            c.addFunction(Criteria.FUNCTION_COUNT, null, "count-star");
            c.addFunction(Criteria.FUNCTION_COUNT, ItemMeta.CREATED_DATETIME, "count");
            c.addFunction(Criteria.FUNCTION_COUNT, false, ItemMeta.CREATED_DATETIME, "count-all");
            c.addFunction(Criteria.FUNCTION_COUNT, true, ItemMeta.CREATED_DATETIME, "count-distinct");
            c.addFunction(Criteria.FUNCTION_COUNT, ItemMeta.KEY_REF, "count-keyref");
            c.addFunction(Criteria.FUNCTION_COUNT, false, ItemMeta.KEY_REF, "count-keyref-all");
            c.addFunction(Criteria.FUNCTION_COUNT, true, ItemMeta.KEY_REF, "count-keyref-distinct");
            Map m = (Map) m_uow.query(c).iterator().next();
            assertEquals("count(*)", 3, ((Number) m.get("count-star")).intValue());
            assertEquals("count(CREATED_DATETIME)", 2, ((Number) m.get("count")).intValue());
            assertEquals("count(ALL CREATED_DATETIME)", 2, ((Number) m.get("count-all")).intValue());
            assertEquals("count(DISTINCT CREATED_DATETIME)", 2, ((Number) m.get("count-distinct")).intValue());
            assertEquals("count(KEY_REF)", 3, ((Number) m.get("count-keyref")).intValue());
            assertEquals("count(ALL KEY_REF)", 3, ((Number) m.get("count-keyref-all")).intValue());
            assertEquals("count(DISTINCT KEY_REF)", 1, ((Number) m.get("count-keyref-distinct")).intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the count() function
     * select count(*), count(CREATED_DATETIME), count(ALL CREATED_DATETIME), count(DISTINCT CREATED_DATETIME),
     * count(KEY_REF), count(ALL KEY_REF), count(DISTINCT KEY_REF) from item
     * where ITEM_ID >= 'Z-TESTITEM-02' and ITEM_ID <= 'Z-TESTITEM-03'
     */
    public void testCountWithWhereClause() {
        try {
            Criteria c = new Criteria();
            c.setTable(ItemMeta.getName());
            c.addFunction(Criteria.FUNCTION_COUNT, null, "count-star");
            c.addFunction(Criteria.FUNCTION_COUNT, ItemMeta.CREATED_DATETIME, "count");
            c.addFunction(Criteria.FUNCTION_COUNT, false, ItemMeta.CREATED_DATETIME, "count-all");
            c.addFunction(Criteria.FUNCTION_COUNT, true, ItemMeta.CREATED_DATETIME, "count-distinct");
            c.addFunction(Criteria.FUNCTION_COUNT, ItemMeta.KEY_REF, "count-keyref");
            c.addFunction(Criteria.FUNCTION_COUNT, false, ItemMeta.KEY_REF, "count-keyref-all");
            c.addFunction(Criteria.FUNCTION_COUNT, true, ItemMeta.KEY_REF, "count-keyref-distinct");
            c.addCriteria(ItemMeta.ITEM_ID, Criteria.RELATIONAL_GREATER_THAN_EQUAL_TO, "Z-TESTITEM-02");
            c.addCriteria(ItemMeta.ITEM_ID, Criteria.RELATIONAL_SMALLER_THAN_EQUAL_TO, "Z-TESTITEM-03");
            Map m = (Map) m_uow.query(c).iterator().next();
            assertEquals("count(*)", 2, ((Number) m.get("count-star")).intValue());
            assertEquals("count(CREATED_DATETIME)", 1, ((Number) m.get("count")).intValue());
            assertEquals("count(ALL CREATED_DATETIME)", 1, ((Number) m.get("count-all")).intValue());
            assertEquals("count(DISTINCT CREATED_DATETIME)", 1, ((Number) m.get("count-distinct")).intValue());
            assertEquals("count(KEY_REF)", 2, ((Number) m.get("count-keyref")).intValue());
            assertEquals("count(ALL KEY_REF)", 2, ((Number) m.get("count-keyref-all")).intValue());
            assertEquals("count(DISTINCT KEY_REF)", 1, ((Number) m.get("count-keyref-distinct")).intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the count() function
     * select count(*) from (select item.item_id, item.part from items, cat1 where cat1.part = item.part and cat1.noun = 'Z-TESTNOUN%')
     */
    public void testCountWithJoin() {
        try {
            Criteria inner = new Criteria();
            inner.setTable(PartMeta.getName());
            inner.addInnerCriteria(PartMeta.PART, ItemMeta.PART);
            inner.addCriteria(PartMeta.NOUN, Criteria.RELATIONAL_BEGINS_WITH, "Z-TESTNOUN");
            Criteria c = new Criteria();
            c.setTable(ItemMeta.getName());
            c.addAggregate(inner);
            c.addFunction(Criteria.FUNCTION_COUNT, null, "count-star");
            Map m = (Map) m_uow.query(c).iterator().next();
            assertEquals("count(*)", 3, ((Number) m.get("count-star")).intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the max() function
     * select max(CREATED_DATETIME) from item;
     */
    public void testMax() {
        try {
            Criteria c = new Criteria();
            c.setTable(ItemMeta.getName());
            c.addFunction(Criteria.FUNCTION_MAX, ItemMeta.CREATED_DATETIME, "max");
            Map m = (Map) m_uow.query(c).iterator().next();
            assertEquals("max(CREATED_DATETIME)", new DateTime(2003, DateTime.SEPTEMBER, 10, 20, 30, 40, 0), m.get("max"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the min() function
     * select min(CREATED_DATETIME) from item;
     */
    public void testMin() {
        try {
            Criteria c = new Criteria();
            c.setTable(ItemMeta.getName());
            c.addFunction(Criteria.FUNCTION_MIN, ItemMeta.CREATED_DATETIME, "min");
            Map m = (Map) m_uow.query(c).iterator().next();
            assertEquals("min(CREATED_DATETIME)", new DateTime(2003, DateTime.SEPTEMBER, 10), m.get("min"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the sum() function
     * select sum(QTY) from item;
     */
    public void testSum() {
        try {
            Criteria c = new Criteria();
            c.setTable(ItemMeta.getName());
            c.addFunction(Criteria.FUNCTION_SUM, ItemMeta.QTY, "sum");
            Map m = (Map) m_uow.query(c).iterator().next();
            assertEquals("sum(QTY)", 6, ((Number) m.get("sum")).intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the avg() function
     * select avg(QTY) from item;
     */
    public void testAvg() {
        try {
            Criteria c = new Criteria();
            c.setTable(ItemMeta.getName());
            c.addFunction(Criteria.FUNCTION_AVG, ItemMeta.QTY, "avg");
            Map m = (Map) m_uow.query(c).iterator().next();
            assertEquals("avg(QTY)", 2, ((Number) m.get("avg")).intValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** Tests the group-by clause
     * select table_name, field_name, current_timestamp, count(*), max(legal_value), min(legal_value)
     * from valid_field_value
     * group by table_name, field_name
     * order by table_name, field_name;
     */
    public void testGroupBy() {
        try {
            Criteria c = new Criteria();
            c.setTable(ValidFieldValueMeta.getName());
            c.addFunction(Criteria.FUNCTION_CURRENT_DATE_TIME, null, "sysdate");
            c.addFunction(Criteria.FUNCTION_COUNT, null, "count(*)");
            c.addFunction(Criteria.FUNCTION_MAX, ValidFieldValueMeta.LEGAL_VALUE, "max");
            c.addFunction(Criteria.FUNCTION_MIN, ValidFieldValueMeta.LEGAL_VALUE, "min");
            c.addGroupBy(ValidFieldValueMeta.TABLE_NAME, "tableName");
            c.addGroupBy(ValidFieldValueMeta.FIELD_NAME, "fieldName");
            c.addOrderBy(ValidFieldValueMeta.TABLE_NAME);
            c.addOrderBy(ValidFieldValueMeta.FIELD_NAME);
            Collection col = m_uow.query(c);
            assertEquals("The GroupBy query should have returned 2 records", 2, col.size());
            Map m = null;
            Iterator i = col.iterator();
            m = (Map) i.next();
            assertEquals("tableName", "ZZ_JUT_ITEM", m.get("tableName"));
            assertEquals("fieldName", "KEY_REF", m.get("fieldName"));
            assertEquals("count(*)", 3, ((Number) m.get("count(*)")).intValue());
            assertEquals("max", "Z-TEST-KEY-REF-3", m.get("max"));
            assertEquals("min", "Z-TEST-KEY-REF", m.get("min"));
            m = (Map) i.next();
            assertEquals("tableName", "ZZ_JUT_ITEM", m.get("tableName"));
            assertEquals("fieldName", "STATUS", m.get("fieldName"));
            assertEquals("count(*)", 8, ((Number) m.get("count(*)")).intValue());
            assertEquals("max", "Y", m.get("max"));
            assertEquals("min", "1", m.get("min"));
            DateTime dbValue = (DateTime) m.get("sysdate");
            DateTime localValue = new DateTime();
            assertEquals("Year on the database server does not match the current value", localValue.year(), dbValue.year());
            assertEquals("Month on the database server does not match the current value", localValue.month(), dbValue.month());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** To obtain distinct values this test uses the following group-by clause 
     * select table_name
     * from valid_field_value
     * group by table_name;
     */
    public void testDistinctUsingGroupBy() {
        try {
            Criteria c = new Criteria();
            c.setTable(ValidFieldValueMeta.getName());
            c.addGroupBy(ValidFieldValueMeta.TABLE_NAME, "tableName");
            Collection col = m_uow.query(c);
            assertEquals("The GroupBy query should have returned 1 record", 1, col.size());
            Map m = null;
            Iterator i = col.iterator();
            m = (Map) i.next();
            assertEquals("tableName", "ZZ_JUT_ITEM", m.get("tableName"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /** To obtain distinct values this test uses the following group-by clause 
     * select table_name, field_name
     * from valid_field_value
     * group by table_name, field_name
     * order by table_name, field_name;
     */
    public void testDistinctTwoFieldsUsingGroupBy() {
        try {
            Criteria c = new Criteria();
            c.setTable(ValidFieldValueMeta.getName());
            c.addGroupBy(ValidFieldValueMeta.TABLE_NAME, "tableName");
            c.addGroupBy(ValidFieldValueMeta.FIELD_NAME, "fieldName");
            c.addOrderBy(ValidFieldValueMeta.TABLE_NAME);
            c.addOrderBy(ValidFieldValueMeta.FIELD_NAME);
            Collection col = m_uow.query(c);
            assertEquals("The GroupBy query should have returned 2 records", 2, col.size());
            Map m = null;
            Iterator i = col.iterator();
            m = (Map) i.next();
            assertEquals("tableName", "ZZ_JUT_ITEM", m.get("tableName"));
            assertEquals("fieldName", "KEY_REF", m.get("fieldName"));
            m = (Map) i.next();
            assertEquals("tableName", "ZZ_JUT_ITEM", m.get("tableName"));
            assertEquals("fieldName", "STATUS", m.get("fieldName"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
