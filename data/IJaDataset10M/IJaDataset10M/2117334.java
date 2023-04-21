package net.sourceforge.pseudoq.solver;

import java.util.HashSet;
import java.util.Set;
import junit.framework.*;
import net.sourceforge.pseudoq.model.Coordinate;
import net.sourceforge.pseudoq.model.Region;

/**
 * JUnit test class for {@link RegionValueCounter}.
 * @author <a href="http://sourceforge.net/users/stevensa">Andrew Stevens</a>
 */
public class RegionValueCounterTest extends TestCase {

    public RegionValueCounterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RegionValueCounterTest.class);
        return suite;
    }

    /**
     * Test of constructor, of class net.sourceforge.pseudoq.solver.RegionValueCounter.
     */
    public void testConstructor() {
        System.out.println("testConstructor");
        try {
            RegionValueCounter counter = new RegionValueCounter(null);
            fail("Constructor didn't throw an exception for null");
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Test of place method, of class net.sourceforge.pseudoq.solver.RegionValueCounter.
     */
    public void testPlace() {
        System.out.println("testPlace");
        Set<Coordinate> coords = new HashSet<Coordinate>();
        coords.add(new Coordinate(0, 0));
        coords.add(new Coordinate(0, 1));
        Region region = new Region("test", coords);
        RegionValueCounter counter = new RegionValueCounter(region);
        try {
            counter.place(null, 1);
            fail("place() didn't throw an exception for null coordinates");
        } catch (IllegalArgumentException ignored) {
        }
        assertEquals("Initial count", 0, counter.getCount());
        counter.place(new Coordinate(1, 0), 1);
        assertEquals("Non-matching coordinate", 0, counter.getCount());
        counter.place(new Coordinate(0, 0), 2);
        assertEquals("Matching placement", 1, counter.getCount());
        counter.place(new Coordinate(0, 1), 1);
        assertEquals("Matching placement", 2, counter.getCount());
    }
}
