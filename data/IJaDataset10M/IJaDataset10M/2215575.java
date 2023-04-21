package org.apache.commons.collections.map;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.Unmodifiable;

/**
 * Extension of {@link AbstractTestOrderedMap} for exercising the 
 * {@link UnmodifiableOrderedMap} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 */
public class TestUnmodifiableOrderedMap extends AbstractTestOrderedMap {

    public TestUnmodifiableOrderedMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestUnmodifiableOrderedMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestUnmodifiableOrderedMap.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        return UnmodifiableOrderedMap.decorate(ListOrderedMap.decorate(new HashMap()));
    }

    public boolean isPutChangeSupported() {
        return false;
    }

    public boolean isPutAddSupported() {
        return false;
    }

    public boolean isRemoveSupported() {
        return false;
    }

    public Map makeFullMap() {
        OrderedMap m = ListOrderedMap.decorate(new HashMap());
        addSampleMappings(m);
        return UnmodifiableOrderedMap.decorate(m);
    }

    public void testUnmodifiable() {
        assertTrue(makeEmptyMap() instanceof Unmodifiable);
        assertTrue(makeFullMap() instanceof Unmodifiable);
    }

    public void testDecorateFactory() {
        Map map = makeFullMap();
        assertSame(map, UnmodifiableOrderedMap.decorate((OrderedMap) map));
        try {
            UnmodifiableOrderedMap.decorate(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }
}
