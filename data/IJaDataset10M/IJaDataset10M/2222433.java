package net.sourceforge.xconf.toolbox;

import java.util.Collection;
import java.util.Map;
import junit.framework.TestCase;

/**
 * @author Tom Czarniecki
 */
public class TestUtilsTest extends TestCase {

    public void testNewInstance() {
        assertNotNull(TestUtils.newInstance(TestObject.class));
    }

    public void testGetField() {
        TestObject object = new TestObject("value");
        assertEquals("value", TestUtils.getField(object, "field"));
    }

    public void testSetField() {
        TestObject object = new TestObject("value");
        TestUtils.setField(object, "field", "newValue");
        assertEquals("newValue", object.getField());
    }

    public void testWrapperMethods() {
        String key = "key";
        String value = "value";
        assertSingleCollection(TestUtils.toSet(value), value);
        assertSingleCollection(TestUtils.toSortedSet(value), value);
        assertSingleCollection(TestUtils.toList(value), value);
        Map map = TestUtils.toMap(key, value);
        assertEquals(1, map.size());
        assertEquals(value, map.get(key));
    }

    private void assertSingleCollection(Collection coll, Object expected) {
        assertEquals(1, coll.size());
        assertEquals(expected, coll.iterator().next());
    }
}
