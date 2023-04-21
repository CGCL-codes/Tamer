package org.springframework.jdbc.support;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import junit.framework.TestCase;

/**
 * Tests for the KeyHolder and GeneratedKeyHolder
 * and it appears that JdbcUtils doesn't work exactly as documented.
 *
 * @author trisberg
 * @since Jul 18, 2004
 */
public class KeyHolderTests extends TestCase {

    private KeyHolder kh;

    public void setUp() {
        kh = new GeneratedKeyHolder();
    }

    public void testSingleKey() {
        LinkedList l = new LinkedList();
        HashMap m = new HashMap(1);
        m.put("key", new Integer(1));
        l.add(m);
        kh.getKeyList().addAll(l);
        assertEquals("single key should be returned", 1, kh.getKey().intValue());
    }

    public void testSingleKeyNonNumeric() {
        LinkedList l = new LinkedList();
        HashMap m = new HashMap(1);
        m.put("key", "1");
        l.add(m);
        kh.getKeyList().addAll(l);
        try {
            kh.getKey().intValue();
        } catch (DataRetrievalFailureException e) {
            assertTrue(e.getMessage().startsWith("The generated key is not of a supported numeric type."));
        }
    }

    public void testNoKeyReturnedInMap() {
        LinkedList l = new LinkedList();
        HashMap m = new HashMap();
        l.add(m);
        kh.getKeyList().addAll(l);
        try {
            kh.getKey();
        } catch (DataRetrievalFailureException e) {
            assertTrue(e.getMessage().startsWith("Unable to retrieve the generated key."));
        }
    }

    public void testMultipleKeys() {
        LinkedList l = new LinkedList();
        HashMap m = new HashMap(1);
        m.put("key", new Integer(1));
        m.put("seq", new Integer(2));
        l.add(m);
        kh.getKeyList().addAll(l);
        Map keyMap = kh.getKeys();
        assertEquals("two keys should be in the map", 2, keyMap.size());
        try {
            kh.getKey();
        } catch (InvalidDataAccessApiUsageException e) {
            assertTrue(e.getMessage().startsWith("The getKey method should only be used when a single key is returned."));
        }
    }

    public void testMultipleKeyRows() {
        LinkedList l = new LinkedList();
        HashMap m = new HashMap(1);
        m.put("key", new Integer(1));
        m.put("seq", new Integer(2));
        l.add(m);
        l.add(m);
        kh.getKeyList().addAll(l);
        assertEquals("two rows should be in the list", 2, kh.getKeyList().size());
        try {
            kh.getKeys();
        } catch (InvalidDataAccessApiUsageException e) {
            assertTrue(e.getMessage().startsWith("The getKeys method should only be used when keys for a single row are returned."));
        }
    }
}
