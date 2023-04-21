package test.jts.junit.index;

import junit.framework.TestCase;
import com.vividsolutions.jts.index.strtree.Interval;

/**
 * @version 1.7
 */
public class IntervalTest extends TestCase {

    public IntervalTest(String Name_) {
        super(Name_);
    }

    public static void main(String[] args) {
        String[] testCaseName = { IntervalTest.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public void testIntersectsBasic() {
        assertTrue(new Interval(5, 10).intersects(new Interval(7, 12)));
        assertTrue(new Interval(7, 12).intersects(new Interval(5, 10)));
        assertTrue(!new Interval(5, 10).intersects(new Interval(11, 12)));
        assertTrue(!new Interval(11, 12).intersects(new Interval(5, 10)));
        assertTrue(new Interval(5, 10).intersects(new Interval(10, 12)));
        assertTrue(new Interval(10, 12).intersects(new Interval(5, 10)));
    }

    public void testIntersectsZeroWidthInterval() {
        assertTrue(new Interval(10, 10).intersects(new Interval(7, 12)));
        assertTrue(new Interval(7, 12).intersects(new Interval(10, 10)));
        assertTrue(!new Interval(10, 10).intersects(new Interval(11, 12)));
        assertTrue(!new Interval(11, 12).intersects(new Interval(10, 10)));
        assertTrue(new Interval(10, 10).intersects(new Interval(10, 12)));
        assertTrue(new Interval(10, 12).intersects(new Interval(10, 10)));
    }

    public void testCopyConstructor() {
        assertEquals(new Interval(3, 4), new Interval(3, 4));
        assertEquals(new Interval(3, 4), new Interval(new Interval(3, 4)));
    }

    public void testGetCentre() {
        assertEquals(6.5, new Interval(4, 9).getCentre(), 1E-10);
    }

    public void testExpandToInclude() {
        assertEquals(new Interval(3, 8), new Interval(3, 4).expandToInclude(new Interval(7, 8)));
        assertEquals(new Interval(3, 7), new Interval(3, 7).expandToInclude(new Interval(4, 5)));
        assertEquals(new Interval(3, 8), new Interval(3, 7).expandToInclude(new Interval(4, 8)));
    }
}
