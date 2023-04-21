package org.gvsig.tools.locator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import junit.framework.TestCase;
import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.LocatorObjectFactory;

/**
 * Unit tests for the AbstractLocator class.
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class AbstractLocatorTest extends TestCase {

    private TestLocator locator;

    protected void setUp() throws Exception {
        super.setUp();
        locator = new TestLocator();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.gvsig.tools.locator.AbstractLocator#getNames()}.
     */
    public void testGetNames() {
        assertNull("Empty locator must not return any names", locator.getNames());
        String name1 = "test1";
        String name2 = "test2";
        locator.register(name1, String.class);
        locator.register(name2, String.class);
        String[] names = locator.getNames();
        assertEquals("Number of registered names incorrect, must be 2", 2, names.length);
        Collection namesColl = Arrays.asList(names);
        assertTrue("The list of names does not contain the registered name: " + name1, namesColl.contains(name1));
        assertTrue("The list of names does not contain the registered name: " + name2, namesColl.contains(name2));
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.locator.AbstractLocator#get(java.lang.String)} and
     * {@link org.gvsig.tools.locator.AbstractLocator#register(java.lang.String, java.lang.Class)}
     */
    public void testGetAndRegisterClass() {
        Class clazz = String.class;
        String name = "test";
        locator.register(name, clazz);
        Object ref = locator.get(name);
        assertEquals(clazz, ref.getClass());
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.locator.AbstractLocator#get(java.lang.String)} and
     * {@link org.gvsig.tools.locator.AbstractLocator#register(String, LocatorObjectFactory)

     */
    public void testGetAndRegisterFactory() {
        final Object ref = new Object();
        LocatorObjectFactory factory = new LocatorObjectFactory() {

            public Object create() {
                return ref;
            }

            public Object create(Object[] args) {
                return ref;
            }

            public Object create(Map args) {
                return ref;
            }
        };
        String name = "test";
        locator.register(name, factory);
        Object locatorRef = locator.get(name);
        assertEquals(ref, locatorRef);
    }

    public class TestLocator extends AbstractLocator {

        public String getLocatorName() {
            return "TestLocator";
        }
    }
}
