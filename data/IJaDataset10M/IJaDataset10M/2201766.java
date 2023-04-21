package kompilator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class AnalizatorLeksykalnyTest {

    public AnalizatorLeksykalnyTest() {
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
     * Test of toString method, of class AnalizatorLeksykalny.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        AnalizatorLeksykalny instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of lineno method, of class AnalizatorLeksykalny.
     */
    @Test
    public void testLineno() {
        System.out.println("lineno");
        AnalizatorLeksykalny instance = null;
        int expResult = 0;
        int result = instance.lineno();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of nextToken method, of class AnalizatorLeksykalny.
     */
    @Test
    public void testNextToken() throws Exception {
        System.out.println("nextToken");
        AnalizatorLeksykalny instance = null;
        int expResult = 0;
        int result = instance.nextToken();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
