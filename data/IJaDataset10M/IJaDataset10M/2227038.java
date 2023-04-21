package org.openide.util.lookup;

import java.util.Collection;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Lookup;

/**
 * Contains tests of class {@code SingletonLookup}.
 *
 * @author  Marian Petras
 */
public class SingletonLookupTest extends NbTestCase {

    public SingletonLookupTest(String testName) {
        super(testName);
    }

    public void testBasics() {
        Object orig = new Object();
        Lookup p1 = new SingletonLookup(orig);
        Object obj = p1.lookup(Object.class);
        assertTrue(obj == orig);
        assertNull(p1.lookup(String.class));
        assertTrue(orig == p1.lookup(Object.class));
        Lookup p2 = new SingletonLookup("test");
        assertNotNull(p2.lookup(Object.class));
        assertNotNull(p2.lookup(String.class));
        assertNotNull(p2.lookup(java.io.Serializable.class));
    }

    public void testId() {
        Object orig = new Object();
        Lookup l = new SingletonLookup(orig, "id");
        doTest(l, orig);
    }

    public void testDefaultId() {
        Object orig = "id";
        Lookup l = new SingletonLookup(orig);
        doTest(l, orig);
    }

    private void doTest(Lookup l, Object orig) {
        Collection allInstances;
        allInstances = l.lookup(new Lookup.Template<Object>(Object.class, null, null)).allInstances();
        assertNotNull(allInstances);
        assertFalse(allInstances.isEmpty());
        assertEquals(1, allInstances.size());
        assertTrue(allInstances.iterator().next() == orig);
        allInstances = l.lookup(new Lookup.Template<Object>(Object.class, "id", null)).allInstances();
        assertNotNull(allInstances);
        assertFalse(allInstances.isEmpty());
        assertEquals(1, allInstances.size());
        assertTrue(allInstances.iterator().next() == orig);
        allInstances = l.lookup(new Lookup.Template<Object>(Object.class, "not", null)).allInstances();
        assertNotNull(allInstances);
        assertTrue(allInstances.isEmpty());
        if (!(orig instanceof String)) {
            allInstances = l.lookup(new Lookup.Template<String>(String.class, null, null)).allInstances();
            assertNotNull(allInstances);
            assertTrue(allInstances.isEmpty());
            allInstances = l.lookup(new Lookup.Template<String>(String.class, "id", null)).allInstances();
            assertNotNull(allInstances);
            assertTrue(allInstances.isEmpty());
            allInstances = l.lookup(new Lookup.Template<String>(String.class, "not", null)).allInstances();
            assertNotNull(allInstances);
            assertTrue(allInstances.isEmpty());
        }
        allInstances = l.lookup(new Lookup.Template<Number>(Number.class, null, null)).allInstances();
        assertNotNull(allInstances);
        assertTrue(allInstances.isEmpty());
        allInstances = l.lookup(new Lookup.Template<Number>(Number.class, "id", null)).allInstances();
        assertNotNull(allInstances);
        assertTrue(allInstances.isEmpty());
        allInstances = l.lookup(new Lookup.Template<Number>(Number.class, "not", null)).allInstances();
        assertNotNull(allInstances);
        assertTrue(allInstances.isEmpty());
    }

    public void testSize() {
        final Object obj = new Object();
        assertSize("The singleton lookup instance should be small", 24, new SingletonLookup(obj));
    }
}
