package nz.ac.waikato.mcennis.rat.graph.descriptors;

import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.query.Query;
import nz.ac.waikato.mcennis.rat.graph.query.graph.NullGraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.NullLinkQuery;

/**
 *
 * @author mcennis
 */
public class BasicIODescriptorTest extends TestCase {

    public BasicIODescriptorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of appendGraphID method, of class BasicIODescriptor.
     */
    public void testAppendGraphID() {
        System.out.println("appendGraphID");
        BasicIODescriptor instance = new BasicIODescriptor();
        boolean expResult = false;
        boolean result = instance.appendGraphID();
        assertEquals(expResult, result);
        instance.setAppendGraphID(true);
        assertEquals(true, instance.appendGraphID());
    }

    /**
     * Test of setAppendGraphID method, of class BasicIODescriptor.
     */
    public void testSetAppendGraphID() {
        System.out.println("setAppendGraphID");
        boolean appendGraphID = false;
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setAppendGraphID(appendGraphID);
    }

    /**
     * Test of setQuery method, of class BasicIODescriptor.
     */
    public void testNullSetQuery() {
        System.out.println("setQuery");
        Query q = null;
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setQuery(q);
        assertNotNull(instance.getQuery());
    }

    /**
     * Test of setQuery method, of class BasicIODescriptor.
     */
    public void testSetQuery() {
        System.out.println("setQuery");
        Query q = new NullLinkQuery();
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setQuery(q);
        assertSame(q, instance.getQuery());
    }

    /**
     * Test of getQuery method, of class BasicIODescriptor.
     */
    public void testGetQuery() {
        System.out.println("getQuery");
        BasicIODescriptor instance = new BasicIODescriptor();
        Query result = instance.getQuery();
        assertNotNull(result);
        assertTrue(result instanceof NullGraphQuery);
        Query q = new NullLinkQuery();
        instance.setQuery(q);
        assertSame(q, instance.getQuery());
    }

    /**
     * Test of setProperty method, of class BasicIODescriptor.
     */
    public void testSetProperty() {
        System.out.println("setProperty");
        String s = "Test";
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setProperty(s);
    }

    /**
     * Test of setProperty method, of class BasicIODescriptor.
     */
    public void testNullSetProperty() {
        System.out.println("setProperty");
        String s = null;
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setProperty(s);
    }

    /**
     * Test of setClassType method, of class BasicIODescriptor.
     */
    public void testSetClassType() {
        System.out.println("setClassType");
        Type t = Type.GRAPH;
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setClassType(t);
    }

    /**
     * Test of setClassType method, of class BasicIODescriptor.
     */
    public void testSetNullClassType() {
        System.out.println("setClassType");
        BasicIODescriptor instance = new BasicIODescriptor();
        Type t = instance.getClassType();
        instance.setClassType(null);
        assertSame(t, instance.getClassType());
    }

    /**
     * Test of setRelation method, of class BasicIODescriptor.
     */
    public void testNullSetRelation() {
        System.out.println("setRelation");
        BasicIODescriptor instance = new BasicIODescriptor();
        String s = instance.getRelation();
        instance.setRelation(null);
        assertSame(s, instance.getRelation());
    }

    /**
     * Test of setRelation method, of class BasicIODescriptor.
     */
    public void testSetRelation() {
        System.out.println("setRelation");
        String s = "Relation";
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setRelation(s);
    }

    /**
     * Test of setAlgorithmName method, of class BasicIODescriptor.
     */
    public void testNullSetAlgorithmName() {
        System.out.println("setAlgorithmName");
        BasicIODescriptor instance = new BasicIODescriptor();
        String s = instance.getAlgorithmName();
        instance.setAlgorithmName(null);
        assertNotNull(instance.getAlgorithmName());
        assertSame(s, instance.getAlgorithmName());
    }

    /**
     * Test of setAlgorithmName method, of class BasicIODescriptor.
     */
    public void testSetAlgorithmName() {
        System.out.println("setAlgorithmName");
        String s = "Name";
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setAlgorithmName(s);
    }

    /**
     * Test of getAlgorithmName method, of class BasicIODescriptor.
     */
    public void testGetAlgorithmName() {
        System.out.println("getAlgorithmName");
        BasicIODescriptor instance = new BasicIODescriptor();
        String expResult = "Name";
        instance.setAlgorithmName(expResult);
        String result = instance.getAlgorithmName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRelation method, of class BasicIODescriptor.
     */
    public void testGetRelation() {
        System.out.println("getRelation");
        BasicIODescriptor instance = new BasicIODescriptor();
        String expResult = "Relation";
        instance.setRelation(expResult);
        String result = instance.getRelation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getClassType method, of class BasicIODescriptor.
     */
    public void testGetClassType() {
        System.out.println("getClassType");
        BasicIODescriptor instance = new BasicIODescriptor();
        Type expResult = Type.GRAPH_PROPERTY;
        instance.setClassType(expResult);
        Type result = instance.getClassType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProperty method, of class BasicIODescriptor.
     */
    public void testGetProperty() {
        System.out.println("getProperty");
        BasicIODescriptor instance = new BasicIODescriptor();
        String expResult = "Property";
        instance.setProperty(expResult);
        String result = instance.getProperty();
        assertEquals(expResult, result);
    }

    /**
     * Test of prototype method, of class BasicIODescriptor.
     */
    public void testPrototype() {
        System.out.println("prototype");
        BasicIODescriptor instance = new BasicIODescriptor();
        BasicIODescriptor result = instance.prototype();
        assertTrue(result instanceof BasicIODescriptor);
    }

    /**
     * Test of setDescription method, of class BasicIODescriptor.
     */
    public void testSetDescription() {
        System.out.println("setDescription");
        String s = "Descriptions";
        BasicIODescriptor instance = new BasicIODescriptor();
        instance.setDescription(s);
    }

    /**
     * Test of setDescription method, of class BasicIODescriptor.
     */
    public void testNullSetDescription() {
        System.out.println("setDescription");
        BasicIODescriptor instance = new BasicIODescriptor();
        String s = instance.getDescription();
        instance.setDescription(null);
        assertEquals(s, instance.getDescription());
    }

    /**
     * Test of getDescription method, of class BasicIODescriptor.
     */
    public void testGetDescription() {
        System.out.println("getDescription");
        BasicIODescriptor instance = new BasicIODescriptor();
        String expResult = "Description";
        instance.setDescription(expResult);
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }
}
