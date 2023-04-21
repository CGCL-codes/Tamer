package caregiversystem;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dbeyter
 */
public class AdminTableTest {

    public AdminTableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of search method, of class AdminTable.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        String userName = "Admin";
        String password = "admin";
        AdminTable instance = new AdminTable();
        boolean expResult = true;
        boolean result = instance.search(userName, password);
        assertEquals(expResult, result);
    }
}
