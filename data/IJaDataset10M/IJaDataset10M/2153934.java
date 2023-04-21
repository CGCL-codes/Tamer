package mapeditor.utilitaire;

import mapTool.utilitaire.Distance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cody
 */
public class DistanceTest {

    public DistanceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMetre method, of class Distance.
     */
    @Test
    public void testGetMetre() {
        System.out.println("getMetre");
        Distance instance = new Distance(10);
        int expResult = 10;
        int result = instance.getMetre();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMetre method, of class Distance.
     */
    @Test
    public void testSetMetre() {
        System.out.println("setMetre");
        int _metre = 0;
        Distance instance = new Distance(10);
        instance.setMetre(_metre);
        int expResult = _metre;
        int result = instance.getMetre();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Distance.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        System.out.println(100 % 1000);
        Distance instance = new Distance(100);
        String expResult = "100m";
        String result = instance.toString();
        assertEquals(expResult, result);
        instance.setMetre(1000);
        expResult = "1km";
        result = instance.toString();
        assertEquals(expResult, result);
        instance.setMetre(1001);
        expResult = "1km 1m";
        result = instance.toString();
        assertEquals(expResult, result);
    }
}
