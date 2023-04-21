package algs.model.tests.search;

import org.junit.Test;
import algs.model.search.AssociativeHashTable;
import algs.model.search.StandardHash;
import junit.framework.TestCase;

/**
 * Test our Associate Hash Table implementation that stores lists of <K,V> 
 * entries.
 */
public class AssociativeHashTableTest extends TestCase {

    @Test
    public void testSimple() {
        AssociativeHashTable<String, Integer> ht = new AssociativeHashTable<String, Integer>(16, new StandardHash<String>(16));
        assertEquals(0, ht.size());
        ht.add("January", 99);
        assertEquals(1, ht.size());
        assertTrue(ht.search("January"));
        assertFalse(ht.search("February"));
        ht.add("February", 43);
        assertEquals(2, ht.size());
        assertTrue(ht.search("February"));
        ht.remove("February");
        assertFalse(ht.search("February"));
        assertEquals(1, ht.size());
    }

    @Test
    public void testDefault() {
        AssociativeHashTable<String, Integer> ht = new AssociativeHashTable<String, Integer>(16);
        assertEquals(0, ht.size());
        ht.add("January", 99);
        assertEquals(1, ht.size());
        assertTrue(99 == ht.remove("January"));
        assertTrue(ht.remove("NOTHING") == null);
        String hypo = "hypoplankton";
        String unh = "unheavenly";
        ht.add(hypo, 99);
        assertEquals(1, ht.size());
        ht.add(unh, 11);
        assertEquals(2, ht.size());
        ht.add(unh, 22);
        assertEquals(2, ht.size());
        assertTrue(22 == ht.remove(unh));
        assertTrue(99 == ht.remove(hypo));
        assertTrue(null == ht.remove(hypo));
    }

    @Test
    public void testDuplicates() {
        AssociativeHashTable<String, Integer> ht = new AssociativeHashTable<String, Integer>(16, new StandardHash<String>(16));
        assertEquals(0, ht.size());
        ht.add("January", 99);
        assertEquals(1, ht.size());
        assertTrue(ht.search("January"));
        ht.add("January", 103);
        assertTrue(ht.search("January"));
        assertEquals(1, ht.size());
    }

    @Test
    public void testDuplicateHash() {
        StandardHash<String> hash = new StandardHash<String>(16);
        AssociativeHashTable<String, Integer> ht = new AssociativeHashTable<String, Integer>(16, hash);
        String hypo = "hypoplankton";
        String unh = "unheavenly";
        assertEquals(0, ht.size());
        ht.add(hypo, 99);
        assertEquals(1, ht.size());
        assertEquals(hash.hash(hypo), hash.hash(unh));
        assertTrue(ht.search(hypo));
        assertFalse(ht.search(unh));
        ht.add(unh, 101);
        assertEquals(2, ht.size());
    }
}
