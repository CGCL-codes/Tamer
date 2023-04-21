package softwareengineering.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author optimus prime
 */
public class TagNodeTest {

    String name = "jack";

    ArrayList<Node> children = new ArrayList<Node>();

    Map<String, String> props;

    boolean closed = false;

    public TagNodeTest() {
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
     * Test of setClosed method, of class TagNode.
     */
    @Test
    public void testSetClosed() {
        System.out.println("setClosed");
        boolean closed = false;
        TagNode instance = new TagNode(name, children, props, closed);
        instance.setClosed(closed);
        boolean expResult = closed;
        boolean result = instance.isClosed();
        assertEquals(expResult, result);
    }

    /**
     * Test of isClosed method, of class TagNode.
     */
    @Test
    public void testIsClosed() {
        System.out.println("isClosed");
        TagNode instance = null;
        boolean expResult = false;
        boolean result = instance.isClosed();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasProperty method, of class TagNode.
     */
    @Test
    public void testHasProperty() {
        System.out.println("hasProperty");
        String key = "";
        TagNode instance = null;
        boolean expResult = false;
        boolean result = instance.hasProperty(key);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProperty method, of class TagNode.
     */
    @Test
    public void testGetProperty() {
        System.out.println("getProperty");
        String key = "";
        TagNode instance = null;
        String expResult = "";
        String result = instance.getProperty(key);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChildren method, of class TagNode.
     */
    @Test
    public void testGetChildren() {
        System.out.println("getChildren");
        TagNode instance = null;
        List<Node> expResult = null;
        List<Node> result = instance.getChildren();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of addChildren method, of class TagNode.
     */
    @Test
    public void testAddChildren() {
        System.out.println("addChildren");
        Collection<Node> nodes = null;
        TagNode instance = null;
        instance.addChildren(nodes);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class TagNode.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        TagNode instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasName method, of class TagNode.
     */
    @Test
    public void testHasName() {
        System.out.println("hasName");
        TagNode instance = null;
        boolean expResult = false;
        boolean result = instance.hasName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class TagNode.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        TagNode instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
