package test.javax.management.openmbean;

import java.util.Collection;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.7 $
 */
public class CompositeDataSupportTest extends TestCase {

    private String[] itemNames = null;

    private String[] itemDescriptions = null;

    private OpenType[] itemTypes;

    private CompositeType tShirtType;

    private String[] indexNames;

    private TabularType allTShirtTypes;

    private TabularDataSupport tabularSupport;

    private CompositeDataSupport compositeData;

    public CompositeDataSupportTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        itemNames = new String[] { "model", "color", "size", "price" };
        itemDescriptions = new String[] { "TShirt's model name", "TShirt's color", "TShirt's size", "TShirt's price" };
        itemTypes = new OpenType[] { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.FLOAT };
        indexNames = new String[] { "model", "color", "size" };
        tShirtType = new CompositeType("tShirt", "a TShirt", itemNames, itemDescriptions, itemTypes);
        allTShirtTypes = new TabularType("tShirts", "List of available TShirts", tShirtType, indexNames);
        Object[] itemValues = new Object[] { "MX4J", "red", "L", new Float(15.0f) };
        compositeData = new CompositeDataSupport(tShirtType, itemNames, itemValues);
        tabularSupport = new TabularDataSupport(allTShirtTypes);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstructor() {
        try {
            Object[] itemValues = new Object[] { "MX4J", "red", "L", new Float(15.0f) };
            CompositeDataSupport support = new CompositeDataSupport(tShirtType, itemNames, itemValues);
            assertTrue(support != null);
        } catch (OpenDataException e) {
            e.printStackTrace();
        }
        try {
            Object[] itemValues = new Object[] { "MX4J", "red", null, new Float(15.0f) };
            CompositeDataSupport support = new CompositeDataSupport(tShirtType, itemNames, itemValues);
            assertTrue(support != null);
        } catch (OpenDataException e) {
            e.printStackTrace();
        }
    }

    public void testGet() {
        String expected = "MX4J";
        String obj = (String) compositeData.get("model");
        assertTrue("expected was stored as the value against model", expected.equals(obj));
    }

    public void testGetAll() {
        int expectedLength = 4;
        Object[] obj = compositeData.getAll(itemNames);
        assertEquals(expectedLength, obj.length);
    }

    public void testValues() {
        int expected = 4;
        Collection result = compositeData.values();
        assertEquals(expected, result.size());
    }

    public void testSparseValues() throws Exception {
        Object[] sparsevalues = new Object[] { "MX4J", null, "L", new Float(15.0f) };
        CompositeDataSupport sparsedata = new CompositeDataSupport(tShirtType, itemNames, sparsevalues);
        assertTrue("Null instance", sparsedata != null);
    }

    public void testEquals() throws Exception {
        Object[] testvalues = { "MX4J", "White", "XL", new Float(15.0f) };
        CompositeDataSupport cdone = new CompositeDataSupport(tShirtType, itemNames, testvalues);
        assertFalse("cdone equals 'null'", cdone.equals(null));
        assertFalse("cdone equals Integer value", cdone.equals(new Integer(42)));
        String[] items = new String[] { "model", "color", "size", "price" };
        String[] descriptions = new String[] { "TShirt's model name", "TShirt's color", "TShirt's size", "TShirt's price" };
        OpenType[] types = new OpenType[] { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.FLOAT };
        CompositeType shirt = new CompositeType("tShirt", "A 'Tee' Shirt", items, descriptions, types);
        CompositeDataSupport cdtwo = new CompositeDataSupport(shirt, items, testvalues);
        assertTrue("cdtwo not equal to cdone", cdtwo.equals(cdone));
        cdone = new CompositeDataSupport(tShirtType, items, new Object[] { "GAP", null, "S", new Float(30.0f) });
        cdtwo = new CompositeDataSupport(shirt, items, new Object[] { "GAP", null, "S", new Float(30.0f) });
        assertTrue("sparse cdtwo not equal to sparse cdone", cdtwo.equals(cdone));
    }
}
