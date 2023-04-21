package fuzzy.membershipfunctions;

import junit.framework.*;

/**
 * @author balzarot
 */
public class RampFunctionTest extends TestCase {

    public RampFunctionTest(String name) {
        super(name);
    }

    public void testConstructors() {
        RampFunction rf = new RampFunction(5.0f, 10.0f, 0.2f, 0.8f);
        assertEquals("x1", 5.0f, rf.getX1(), 0.01f);
        assertEquals("x2", 10.0f, rf.getX2(), 0.01f);
        assertEquals("y1", 0.2f, rf.getY1(), 0.01f);
        assertEquals("y2", 0.8f, rf.getY2(), 0.01f);
        rf = new RampFunction(5.0f, 10.0f);
        assertEquals("y1", 0.0f, rf.getY1(), 0.01f);
        assertEquals("y2", 1.0f, rf.getY2(), 0.01f);
        try {
            rf = new RampFunction(0f, 0f);
            fail(" (0,0) without exception ");
        } catch (Exception e) {
        }
        try {
            rf = new RampFunction(3f, 2f);
            fail(" (3,2) without exception ");
        } catch (Exception e) {
        }
        try {
            rf = new RampFunction(0f, 1f, 2f, 0f);
            fail(" (0,1,2,0) without exception ");
        } catch (Exception e) {
        }
        try {
            rf = new RampFunction(0f, 1f, 0f, 2f);
            fail(" (0,1,0,2) without exception ");
        } catch (Exception e) {
        }
        try {
            rf = new RampFunction(0f, 1f, -0.5f, 0f);
            fail(" (0,1,-0.5,0) without exception ");
        } catch (Exception e) {
        }
        try {
            rf = new RampFunction(0f, 1f, 0, -0.5f);
            fail(" (0,1,0,-0.5) without exception ");
        } catch (Exception e) {
        }
    }

    public void testGetMembershipValue() {
        RampFunction rf = new RampFunction(0f, 10.0f, 0.2f, 0.8f);
        assertEquals("p1", 0.2f, rf.getMembershipValue(-1f), 0.01f);
        assertEquals("p2", 0.2f, rf.getMembershipValue(0f), 0.01f);
        assertEquals("p3", 0.44f, rf.getMembershipValue(4f), 0.01f);
        assertEquals("p1", 0.68f, rf.getMembershipValue(8f), 0.01f);
        assertEquals("p2", 0.8f, rf.getMembershipValue(10f), 0.01f);
        assertEquals("p3", 0.8f, rf.getMembershipValue(11f), 0.01f);
        rf = new RampFunction(0f, 10.0f, 0.8f, 0.2f);
        assertEquals("p1", 0.8f, rf.getMembershipValue(-1f), 0.01f);
        assertEquals("p2", 0.8f, rf.getMembershipValue(0f), 0.01f);
        assertEquals("p3", 0.56f, rf.getMembershipValue(4f), 0.01f);
        assertEquals("p1", 0.32f, rf.getMembershipValue(8f), 0.01f);
        assertEquals("p2", 0.2f, rf.getMembershipValue(10f), 0.01f);
        assertEquals("p3", 0.2f, rf.getMembershipValue(11f), 0.01f);
    }
}
