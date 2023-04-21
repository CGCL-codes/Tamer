package nl.tue.win.riaca.openmath.lang;

import java.util.Enumeration;
import java.util.Hashtable;
import junit.framework.*;

/**
 * A JUnit test for an OpenMath integer. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMIntegerTest extends TestCase {

    /**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMIntegerTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method. <p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMIntegerTest.class);
        return suite;
    }

    /**
     * Test constructor.
     */
    public void testConstructor() {
        OMInteger integer = new OMInteger(1);
        assertTrue(integer.getInteger().equals("1"));
    }

    /**
     * Test constructor.
     */
    public void testConstructor2() {
        OMInteger integer = new OMInteger(1L);
        assertTrue(integer.getInteger().equals("1"));
    }

    /**
     * Test constructor.
     */
    public void testConstructor3() {
        OMInteger integer = new OMInteger(new Integer(1));
        assertTrue(integer.getInteger().equals("1"));
    }

    /**
     * Test constructor.
     */
    public void testConstructor4() {
        OMInteger integer = new OMInteger(new Long(1));
        assertTrue(integer.getInteger().equals("1"));
    }

    /** 
     * Test of getType method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testGetType() {
        OMInteger integer = new OMInteger("12");
        assertTrue(integer.getType().equals("OMI"));
    }

    /** 
     * Test of setInteger method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testSetInteger() {
        OMInteger integer = new OMInteger("12");
        integer.setInteger("13");
        assertTrue(integer.getInteger().equals("13"));
    }

    /**
     * Test of setInteger method.
     */
    public void testSetInteger2() {
        OMInteger integer = new OMInteger();
        integer.setInteger(1);
        assertTrue(integer.getInteger().equals("1"));
    }

    /**
     * Test of setInteger method.
     */
    public void testSetInteger3() {
        OMInteger integer = new OMInteger();
        integer.setInteger(1L);
        assertTrue(integer.getInteger().equals("1"));
    }

    /**
     * Test of setInteger method.
     */
    public void testSetInteger4() {
        OMInteger integer = new OMInteger();
        integer.setInteger(new Integer(1));
        assertTrue(integer.getInteger().equals("1"));
    }

    /**
     * Test of setInteger method.
     */
    public void testSetInteger5() {
        OMInteger integer = new OMInteger();
        integer.setInteger(new Long(1));
        assertTrue(integer.getInteger().equals("1"));
    }

    /** 
     * Test of getInteger method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testGetInteger() {
        OMInteger integer = new OMInteger("14");
        assertTrue(integer.getInteger().equals("14"));
    }

    /** 
     * Test of longValue method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testLongValue() {
        OMInteger integer = new OMInteger("15");
        assertTrue(integer.longValue() == 15);
    }

    /** 
     * Test of intValue method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testIntValue() {
        OMInteger integer = new OMInteger("16");
        assertTrue(integer.intValue() == 16);
    }

    /** 
     * Test of isAtom method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testIsAtom() {
        OMInteger integer = new OMInteger("17");
        assertTrue(integer.isAtom());
    }

    /** 
     * Test of isComposite method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testIsComposite() {
        OMInteger integer = new OMInteger("18");
        assertTrue(!integer.isComposite());
    }

    /** 
     * Test of toString method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testToString() {
        OMInteger integer = new OMInteger("19");
        assertTrue(integer.toString().equals("<OMI>19</OMI>"));
    }

    /** 
     * Test of clone method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testClone() {
        OMInteger integer = new OMInteger("20");
        OMInteger clone = (OMInteger) integer.clone();
        assertTrue(integer.getInteger().equals(clone.getInteger()));
    }

    /** 
     * Test of copy method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testCopy() {
        OMInteger integer = new OMInteger("20");
        OMInteger copy = (OMInteger) integer.copy();
        assertTrue(integer.getInteger().equals(copy.getInteger()));
    }

    /**
     * Test copy method.
     */
    public void testCopy2() {
        OMInteger integer = new OMInteger("20");
        integer.setAttribute("id", "1");
        OMInteger copy = (OMInteger) integer.copy();
        assertTrue(integer.getInteger().equals(copy.getInteger()) && integer.attributes != copy.attributes);
    }

    /**
     * Test of isSame method, of class nl.tue.win.riaca.openmath.lang.OMInteger. 
     */
    public void testIsSame() {
        OMInteger integer1 = new OMInteger("1");
        OMInteger integer2 = new OMInteger("1");
        assertTrue(integer1.isSame(integer2));
    }

    /**
     * Test isSame method.
     */
    public void testIsSame2() {
        OMInteger integer = new OMInteger("1");
        OMString string = new OMString("a");
        assertTrue(!integer.isSame(string));
    }

    /**
     * Test isValid method.
     */
    public void testIsValid() {
        OMInteger integer = new OMInteger("1");
        assertTrue(integer.isValid());
    }

    /**
     * Test isValid method.
     */
    public void testIsValid2() {
        OMInteger integer = new OMInteger();
        assertTrue(!integer.isValid());
    }
}
