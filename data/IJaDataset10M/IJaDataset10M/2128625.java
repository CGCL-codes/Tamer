package source;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class miscTest {

    public miscTest() {
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
     * Test of println method, of class misc.
     */
    @Test
    public void testPrintln() {
        System.out.println("println");
        String str = "";
        misc.println(str);
        fail("The test case is a prototype.");
    }

    /**
     * Test of Hex method, of class misc.
     */
    @Test
    public void testHex_byteArr() {
        System.out.println("Hex");
        byte[] data = null;
        String expResult = "";
        String result = misc.Hex(data);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of Hex method, of class misc.
     */
    @Test
    public void testHex_3args() {
        System.out.println("Hex");
        byte[] data = null;
        int offset = 0;
        int len = 0;
        String expResult = "";
        String result = misc.Hex(data, offset, len);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of HexToInt method, of class misc.
     */
    @Test
    public void testHexToInt() {
        System.out.println("HexToInt");
        byte[] data = null;
        int offset = 0;
        int len = 0;
        int expResult = 0;
        int result = misc.HexToInt(data, offset, len);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of random method, of class misc.
     */
    @Test
    public void testRandom() {
        System.out.println("random");
        int range = 0;
        int expResult = 0;
        int result = misc.random(range);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of random2 method, of class misc.
     */
    @Test
    public void testRandom2() {
        System.out.println("random2");
        int range = 0;
        int expResult = 0;
        int result = misc.random2(range);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of random3 method, of class misc.
     */
    @Test
    public void testRandom3() {
        System.out.println("random3");
        int range = 0;
        int expResult = 0;
        int result = misc.random3(range);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of random4 method, of class misc.
     */
    @Test
    public void testRandom4() {
        System.out.println("random4");
        int range = 0;
        int expResult = 0;
        int result = misc.random4(range);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of textUnpack method, of class misc.
     */
    @Test
    public void testTextUnpack() {
        System.out.println("textUnpack");
        byte[] packedData = null;
        int size = 0;
        String expResult = "";
        String result = misc.textUnpack(packedData, size);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of optimizeText method, of class misc.
     */
    @Test
    public void testOptimizeText() {
        System.out.println("optimizeText");
        String text = "";
        String expResult = "";
        String result = misc.optimizeText(text);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of textPack method, of class misc.
     */
    @Test
    public void testTextPack() {
        System.out.println("textPack");
        byte[] packedData = null;
        String text = "";
        misc.textPack(packedData, text);
        fail("The test case is a prototype.");
    }

    /**
     * Test of direction method, of class misc.
     */
    @Test
    public void testDirection() {
        System.out.println("direction");
        int srcX = 0;
        int srcY = 0;
        int destX = 0;
        int destY = 0;
        int expResult = 0;
        int result = misc.direction(srcX, srcY, destX, destY);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of longToPlayerName method, of class misc.
     */
    @Test
    public void testLongToPlayerName() {
        System.out.println("longToPlayerName");
        long l = 0L;
        String expResult = "";
        String result = misc.longToPlayerName(l);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of playerNameToLong method, of class misc.
     */
    @Test
    public void testPlayerNameToLong() {
        System.out.println("playerNameToLong");
        String s = "";
        long expResult = 0L;
        long result = misc.playerNameToLong(s);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of playerNameToInt64 method, of class misc.
     */
    @Test
    public void testPlayerNameToInt64() {
        System.out.println("playerNameToInt64");
        String s = "";
        long expResult = 0L;
        long result = misc.playerNameToInt64(s);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
