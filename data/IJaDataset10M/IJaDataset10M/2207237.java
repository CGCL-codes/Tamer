package org.apache.commons.collections.keyvalue;

import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.collections.KeyValue;

/**
 * Test the DefaultMapEntry class.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Neil O'Toole
 */
public class TestDefaultMapEntry extends AbstractTestMapEntry {

    public TestDefaultMapEntry(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestDefaultMapEntry.class);
    }

    public static Test suite() {
        return new TestSuite(TestDefaultMapEntry.class);
    }

    /**
     * Make an instance of Map.Entry with the default (null) key and value.
     * Subclasses should override this method to return a Map.Entry
     * of the type being tested.
     */
    public Map.Entry makeMapEntry() {
        return new DefaultMapEntry(null, null);
    }

    /**
     * Make an instance of Map.Entry with the specified key and value.
     * Subclasses should override this method to return a Map.Entry
     * of the type being tested.
     */
    public Map.Entry makeMapEntry(Object key, Object value) {
        return new DefaultMapEntry(key, value);
    }

    /**
     * Subclasses should override this method.
     *
     */
    public void testConstructors() {
        Map.Entry entry = new DefaultMapEntry(key, value);
        assertSame(key, entry.getKey());
        assertSame(value, entry.getValue());
        KeyValue pair = new DefaultKeyValue(key, value);
        assertSame(key, pair.getKey());
        assertSame(value, pair.getValue());
        Map.Entry entry2 = new DefaultMapEntry(entry);
        assertSame(key, entry2.getKey());
        assertSame(value, entry2.getValue());
        entry.setValue(null);
        assertSame(value, entry2.getValue());
    }

    public void testSelfReferenceHandling() {
        Map.Entry entry = makeMapEntry();
        try {
            entry.setValue(entry);
            assertSame(entry, entry.getValue());
        } catch (Exception e) {
            fail("This Map.Entry implementation supports value self-reference.");
        }
    }
}
