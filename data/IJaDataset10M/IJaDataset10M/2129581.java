package fitgoodies.references.processors;

import fitgoodies.FitGoodiesTestCase;

/**
 * $Id: NamespaceHashMapTest.java 185 2009-08-17 13:47:24Z jwierum $
 * @author jwierum
 */
public class NamespaceHashMapTest extends FitGoodiesTestCase {

    private final NamespaceHashMap<String> hm = new NamespaceHashMap<String>();

    @Override
    public final void setUp() throws Exception {
        super.setUp();
        hm.put("ns1", "key1", "val1");
        hm.put("ns1", "key2", "val2");
        hm.put("ns1", "key1", "val3");
        hm.put("ns2", "key1", "val7");
    }

    public final void testSize() {
        assertEquals(3, hm.size());
        hm.put("ns1", "key3", "val4");
        assertEquals(4, hm.size());
    }

    public final void testDelete() {
        String actual = hm.delete("ns1", "key1");
        assertEquals("val3", actual);
        assertEquals(2, hm.size());
        actual = hm.delete("neverbeenhere", "neverbeenthere");
        assertNull(actual);
    }

    public final void testGet() {
        String actual = hm.get("ns1", "key1");
        assertEquals("val3", actual);
        actual = hm.get("ns2", "key1");
        assertEquals("val7", actual);
        actual = hm.get("ns9", "key99");
        assertNull(actual);
    }
}
