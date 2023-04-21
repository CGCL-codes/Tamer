package pk_managers_employee;

import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author u19730
 */
public class ManagerEmployeeTest {

    public ManagerEmployeeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetColeagues() {
        System.out.println("getColeagues");
        String userIdStr = "U15377";
        ManagerEmployee instance = new ManagerEmployee();
        List expResult = null;
        List result = instance.getColeagues(userIdStr);
        System.out.println("END getColeagues");
    }
}
