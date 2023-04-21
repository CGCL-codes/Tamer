package nz.ac.waikato.mcennis.rat.graph.query.property;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import org.xml.sax.Attributes;

/**
 *
 * @author Daniel McEnnis
 */
public class NullPropertyQueryTest extends TestCase {

    Property empty;

    Property test;

    public NullPropertyQueryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        empty = PropertyFactory.newInstance().create("empty", String.class);
        test = PropertyFactory.newInstance().create("test", String.class);
        test.add("Test");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class NullPropertyQuery.
     */
    public void testNullExecute() {
        System.out.println("execute");
        Property property = null;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(false);
        boolean expResult = false;
        boolean result = instance.execute(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class NullPropertyQuery.
     */
    public void testEmptyExecute() {
        System.out.println("execute");
        Property property = empty;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(false);
        boolean expResult = false;
        boolean result = instance.execute(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class NullPropertyQuery.
     */
    public void testValueExecute() {
        System.out.println("execute");
        Property property = test;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(false);
        boolean expResult = false;
        boolean result = instance.execute(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class NullPropertyQuery.
     */
    public void testNegatedNullExecute() {
        System.out.println("execute");
        Property property = null;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(true);
        boolean expResult = false;
        boolean result = instance.execute(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class NullPropertyQuery.
     */
    public void testNegatedEmptyExecute() {
        System.out.println("execute");
        Property property = empty;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(true);
        boolean expResult = true;
        boolean result = instance.execute(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of execute method, of class NullPropertyQuery.
     */
    public void testNegatedValueExecute() {
        System.out.println("execute");
        Property property = test;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(true);
        boolean expResult = true;
        boolean result = instance.execute(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of exportQuery method, of class NullPropertyQuery.
     */
    public void testExportQuery() {
        System.out.println("exportQuery");
        Writer writer = new OutputStreamWriter(new ByteOutputStream());
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.exportQuery(writer);
    }

    /**
     * Test of buildQuery method, of class NullPropertyQuery.
     */
    public void testPositiveBuildQuery() {
        System.out.println("buildQuery");
        boolean ret = true;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(ret);
    }

    /**
     * Test of buildQuery method, of class NullPropertyQuery.
     */
    public void testNegativeBuildQuery() {
        System.out.println("buildQuery");
        boolean ret = false;
        NullPropertyQuery instance = new NullPropertyQuery();
        instance.buildQuery(ret);
    }

    /**
     * Test of buildingStatus method, of class NullPropertyQuery.
     */
    public void testBuildingStatus() {
        System.out.println("buildingStatus");
        NullPropertyQuery instance = new NullPropertyQuery();
        assertEquals(State.UNINITIALIZED, instance.buildingStatus());
        instance.buildQuery(true);
        assertEquals(State.READY, instance.buildingStatus());
    }

    /**
     * Test of prototype method, of class NullPropertyQuery.
     */
    public void testPrototype() {
        System.out.println("prototype");
        NullPropertyQuery instance = new NullPropertyQuery();
        NullPropertyQuery result = instance.prototype();
        assertNotSame(instance, result);
    }
}
