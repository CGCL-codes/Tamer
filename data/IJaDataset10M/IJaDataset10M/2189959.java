package org.dwgsoftware.raistlin.composition.data.test;

import junit.framework.TestCase;
import org.dwgsoftware.raistlin.composition.data.ContextDirective;
import org.dwgsoftware.raistlin.composition.data.EntryDirective;
import org.dwgsoftware.raistlin.composition.data.ImportDirective;

/**
 * ContextDirectiveTestCase does XYZ
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version CVS $ Revision: 1.1 $
 */
public class ContextDirectiveTestCase extends TestCase {

    public ContextDirectiveTestCase(String name) {
        super(name);
    }

    public void testConstructor() {
        try {
            new ContextDirective(null);
        } catch (NullPointerException npe) {
            fail("NullPointerException should not be thrown - null indicates default");
        }
        try {
            new ContextDirective(null, new EntryDirective[0]);
        } catch (NullPointerException npe) {
            fail("NullPointerException should not be thrown - null indicates default");
        }
        try {
            new ContextDirective(null, null);
        } catch (NullPointerException npe) {
            fail("NullPointerException should not be thrown - null indicated default");
        }
    }

    public void testContextDirective() {
        EntryDirective[] entries = new EntryDirective[0];
        ContextDirective cd = new ContextDirective(getClass().getName(), entries);
        assertEquals("classname", getClass().getName(), cd.getClassname());
        assertEquals("entries", entries, cd.getEntryDirectives());
        assertEquals("length", entries.length, cd.getEntryDirectives().length);
    }

    public void testGetEntry() {
        String key = "key";
        String val = "val";
        ImportDirective imp = new ImportDirective(key, "xxx");
        EntryDirective[] entries = new EntryDirective[] { imp };
        ContextDirective cd = new ContextDirective(entries);
        assertNull(cd.getClassname());
        assertEquals(entries, cd.getEntryDirectives());
        assertEquals(entries.length, cd.getEntryDirectives().length);
        assertEquals(entries[0], cd.getEntryDirective(key));
        assertNull(cd.getEntryDirective(val));
    }
}
