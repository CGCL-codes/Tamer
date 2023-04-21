package server.monitor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dan
 */
public class ByteBufferTest {

    /**
     * 
     */
    public ByteBufferTest() {
    }

    /**
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * 
     */
    @Before
    public void setUp() {
    }

    /**
     * 
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of getByteArray method, of class ByteBuffer.
     */
    @Test
    public void testGetByteArray() throws Exception {
        System.out.println("getByteArray");
        ByteBuffer instance = new ByteBuffer(10);
        int expResult = 10;
        int result = instance.getByteArray().length;
        assertEquals(expResult, result);
    }

    /**
     * Test of setString method, of class ByteBuffer.
     */
    @Test
    public void testSetString() throws Exception {
        System.out.println("setString");
        String str = "lol";
        ByteBuffer instance = new ByteBuffer(10);
        instance.setString(str);
        String result = instance.getString();
        assertEquals("lol", result);
    }

    /**
     * Test of getString method, of class ByteBuffer.
     */
    @Test
    public void testGetString() throws Exception {
        System.out.println("getString");
        ByteBuffer instance = new ByteBuffer(10);
        instance.setString("lol");
        String expResult = "lol";
        String result = instance.getString();
        assertEquals(expResult, result);
    }

    /**
     * Test of length method, of class ByteBuffer.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        ByteBuffer instance = new ByteBuffer(10);
        int expResult = 10;
        int result = instance.length();
        assertEquals(expResult, result);
    }

    /**
     * Test of reset method, of class ByteBuffer.
     */
    @Test
    public void testReset() throws Exception {
        System.out.println("reset");
        ByteBuffer instance = new ByteBuffer(10);
        String expResult = "";
        String result = instance.getString();
        instance.reset();
        assertEquals(expResult, result);
    }
}
