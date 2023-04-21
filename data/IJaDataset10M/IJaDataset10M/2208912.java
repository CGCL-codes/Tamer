package nz.ac.massey.softwarec.group3.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Natalie
 */
public class DetectiveInterfaceTest {

    Detective instance;

    public DetectiveInterfaceTest() {
    }

    @Before
    public void setUp() {
        instance = new Detective();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMrXTokens method, of class DetectiveInterface.
     */
    @Test
    public void testGetMrXTokens() {
        System.out.println("getMrXTokens");
        int expResult = 0;
        int result = instance.getMrXTokens();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDoubleMoveTokens method, of class DetectiveInterface.
     */
    @Test
    public void testGetDoubleMoveTokens() {
        System.out.println("getDoubleMoveTokens");
        int expResult = 0;
        int result = instance.getDoubleMoveTokens();
        assertEquals(expResult, result);
    }
}
