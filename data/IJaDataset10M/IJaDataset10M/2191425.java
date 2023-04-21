package net.walend.collection.test;

import java.util.Map;
import java.util.HashMap;
import junit.framework.TestSuite;
import junit.framework.Test;
import net.walend.toolkit.junit.TestCase;
import net.walend.collection.MapAlgebra;

/**
@author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
*/
public class MapAlgebraTest extends TestCase {

    public MapAlgebraTest(String testName) {
        super(testName);
    }

    protected void fillMap(Map map) {
        map.put("George", "Jetson");
        map.put("L Ron", "Hubbard");
        map.put("Buck", "Rogers");
    }

    protected void backFillMap(Map map) {
        map.put("Jetson", "George");
        map.put("Hubbard", "L Ron");
        map.put("Rogers", "Buck");
    }

    public void testReverse() {
        Map source = new HashMap();
        fillMap(source);
        Map victem = new HashMap();
        Map expected = new HashMap();
        backFillMap(expected);
        MapAlgebra.reverse(source, victem);
        assertTrue("Got " + victem + " but expected " + expected, victem.equals(expected));
    }

    public void testReverseSafely() {
        Map source = new HashMap();
        fillMap(source);
        Map victem = new HashMap();
        Map expected = new HashMap();
        backFillMap(expected);
        try {
            MapAlgebra.reverseSafely(source, victem);
            assertTrue("Got " + victem + " but expected " + expected, victem.equals(expected));
        } catch (MapAlgebra.UnsafeReverseException ure) {
            fail(ure);
        }
        try {
            source.put("Ginger", "Rogers");
            MapAlgebra.reverseSafely(source, victem);
            fail("Should have thrown an exception.");
        } catch (MapAlgebra.UnsafeReverseException ure) {
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new MapAlgebraTest("testReverse"));
        suite.addTest(new MapAlgebraTest("testReverseSafely"));
        return suite;
    }
}
