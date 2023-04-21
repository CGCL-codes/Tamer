package org.fao.fenix.persistence.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fao.fenix.persistence.BaseDaoTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author etj
 */
public class DBTableInfoTest extends BaseDaoTest {

    private DBTableInfo dbTableInfo;

    public DBTableInfoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
	 * Test of getFields method, of class DBTableInfo.
	 */
    @Test
    public void _testGetFields() {
        String tableName = "gaul0";
        try {
            List list = dbTableInfo.getFields(tableName);
            assertTrue(list.size() > 0);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DBTableInfoTest.class.getName()).log(Level.SEVERE, "Table gaul0 not found. This test will be skipped", ex);
        }
    }

    @Test
    public void testFailingGetFields() {
        String tableName = "table_with_an_improbable_name_that_should_not_exist";
        try {
            dbTableInfo.getFields(tableName);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void setDbTableInfo(DBTableInfo dbTableInfo) {
        this.dbTableInfo = dbTableInfo;
    }
}
