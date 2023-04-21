package electrode;

import electrode.gel.Gel;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cgranade
 */
public class MainTest {

    public MainTest() {
    }

    /**
     * Test of dumpException method, of class Main.
     */
    @Test
    public void testDumpException() {
        System.out.println("dumpException");
        Exception e = null;
        Main.dumpException(e);
        fail("The test case is a prototype.");
    }

    /**
     * Test of debugString method, of class Main.
     */
    @Test
    public void testDebugString_String() {
        System.out.println("debugString");
        String str = "";
        Main.debugString(str);
        fail("The test case is a prototype.");
    }

    /**
     * Test of debugString method, of class Main.
     */
    @Test
    public void testDebugString_String_ObjectArr() {
        System.out.println("debugString");
        String str = "";
        Object[] args = null;
        Main.debugString(str, args);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGameWindow method, of class Main.
     */
    @Test
    public void testGetGameWindow() {
        System.out.println("getGameWindow");
        GameWindow expResult = null;
        GameWindow result = Main.getGameWindow();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAPIScriptAsStream method, of class Main.
     */
    @Test
    public void testGetAPIScriptAsStream() {
        System.out.println("getAPIScriptAsStream");
        String path = "imports.py";
        InputStream result = Main.getAPIScriptAsStream(path);
        assertNotNull(result);
    }

    /**
     * Test of pathJoin method, of class Main.
     */
    @Test
    public void testPathJoin() {
        System.out.println("pathJoin");
        String result;
        result = Main.pathJoin("/foo", "/bar", "qoo", "qux");
        assertEquals("/foo/bar/qoo/qux", result);
        result = Main.pathJoin("foo/", "/bar");
        assertEquals("foo/bar", result);
    }

    /**
     * Test of getRunningGel method, of class Main.
     */
    @Test
    public void testGetRunningGel() {
        System.out.println("getRunningGel");
        Gel expResult = null;
        Gel result = Main.getRunningGel();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
