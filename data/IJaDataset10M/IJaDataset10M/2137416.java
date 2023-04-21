package org.easyrec.store.dao.core.types;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import static junit.framework.Assert.*;

@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "spring/easyrecDataSource.xml", "spring/core/dao/types/AggregateTypeDAO.xml" })
@DataSet(value = AggregateTypeDAOTest.DATA_FILENAME)
public class AggregateTypeDAOTest {

    private static final Integer TENANT_ID = 0;

    public static final String DATA_FILENAME = "/dbunit/core/dao/types/aggregatetype.xml";

    public static final String DATA_FILENAME_ONE_LESS = "/dbunit/core/dao/types/aggregatetype_one_less.xml";

    public static final String DATA_FILENAME_CHANGED_IDS = "/dbunit/core/dao/types/aggregatetype_changed_ids.xml";

    @SpringBeanByName
    private AggregateTypeDAO aggregateTypeDAO;

    @Test
    public void testGetAggregateTypeById() {
        String aggregateType = aggregateTypeDAO.getTypeById(TENANT_ID, 1);
        assertEquals("AVERAGE", aggregateType);
        aggregateType = aggregateTypeDAO.getTypeById(TENANT_ID, 2);
        assertEquals("FIRST", aggregateType);
        try {
            aggregateType = aggregateTypeDAO.getTypeById(TENANT_ID, 12345);
            fail("IllegalArgumentException should have been thrown, since 'id' for aggregateType was unknown");
        } catch (IllegalArgumentException e) {
        }
        aggregateType = aggregateTypeDAO.getTypeById(TENANT_ID, null);
        assertNull(aggregateType);
    }

    @Test
    public void testGetIdOfAggregateType() {
        Integer id = aggregateTypeDAO.getIdOfType(TENANT_ID, "AVERAGE");
        assertEquals(new Integer(1), id);
        id = aggregateTypeDAO.getIdOfType(TENANT_ID, "FIRST");
        assertEquals(new Integer(2), id);
        try {
            id = aggregateTypeDAO.getIdOfType(TENANT_ID, "ASDF");
            fail("IllegalArgumentException should have been thrown, since 'aggregateType' was unknown");
        } catch (IllegalArgumentException e) {
        }
        id = aggregateTypeDAO.getIdOfType(TENANT_ID, null);
        assertNull(id);
    }

    @Test
    @DataSet(DATA_FILENAME_ONE_LESS)
    @ExpectedDataSet(DATA_FILENAME)
    public void testInsertAggregateType() {
        aggregateTypeDAO.insertOrUpdate(TENANT_ID, "OLDEST", 6);
    }

    @Test
    @ExpectedDataSet(DATA_FILENAME_CHANGED_IDS)
    public void testUpdateAggregateType() {
        aggregateTypeDAO.insertOrUpdate(TENANT_ID, "AVERAGE", 10);
        aggregateTypeDAO.insertOrUpdate(TENANT_ID, "FIRST", 20);
        aggregateTypeDAO.insertOrUpdate(TENANT_ID, "MAXIMUM", 30);
        aggregateTypeDAO.insertOrUpdate(TENANT_ID, "MOST_FREQUENT", 40);
        aggregateTypeDAO.insertOrUpdate(TENANT_ID, "NEWEST", 50);
        aggregateTypeDAO.insertOrUpdate(TENANT_ID, "OLDEST", 60);
    }

    @Test
    public void testExistsAggregateTypeTable() {
        assertTrue(aggregateTypeDAO.existsTable());
    }

    @Test
    public void testGetTypes() {
        Set<String> expectedTypes = new TreeSet<String>();
        expectedTypes.add("AVERAGE");
        expectedTypes.add("FIRST");
        expectedTypes.add("MAXIMUM");
        expectedTypes.add("MOST_FREQUENT");
        expectedTypes.add("NEWEST");
        expectedTypes.add("OLDEST");
        assertEquals(expectedTypes, aggregateTypeDAO.getTypes(TENANT_ID));
    }

    @Test
    public void testGetMapping() {
        HashMap<String, Integer> expectedMapping = new HashMap<String, Integer>();
        expectedMapping.put("AVERAGE", 1);
        expectedMapping.put("FIRST", 2);
        expectedMapping.put("MAXIMUM", 3);
        expectedMapping.put("MOST_FREQUENT", 4);
        expectedMapping.put("NEWEST", 5);
        expectedMapping.put("OLDEST", 6);
        HashMap<String, Integer> actualMapping = aggregateTypeDAO.getMapping(TENANT_ID);
        assertEquals(expectedMapping, actualMapping);
    }
}
