package nl.tue.win.riaca.openmath.lang;

import junit.framework.*;

/**
 * A JUnit test for an OpenMath root object. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.4 $
 */
public class OMRootTest extends TestCase {

    /**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMRootTest(String testName) {
        super(testName);
    }

    /**
     * Get the test suite. <p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMRootTest.class);
        return suite;
    }

    /**
     * Test of clone method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testClone() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);
        OMRoot clone = (OMRoot) root.clone();
        assertTrue(root.getObject() == clone.getObject() && root.attributes == clone.attributes);
    }

    /**
     * Test of copy method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testCopy() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);
        OMRoot copy = (OMRoot) root.copy();
        assertTrue(root.getObject() != copy.getObject());
    }

    /**
     * Test copy method.
     */
    public void testCopy2() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);
        root.setAttribute("id", "1");
        OMRoot copy = (OMRoot) root.copy();
        assertTrue(root.object != copy.object && root.object.isSame(copy.object) && root.attributes != copy.attributes);
    }

    /**
     * Test of getType method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testGetType() {
        OMRoot root = new OMRoot();
        assertTrue(root.getType().equals("OMOBJ"));
    }

    /**
     * Test of isAtom method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testIsAtom() {
        OMRoot root = new OMRoot();
        assertTrue(!root.isAtom());
    }

    /**
     * Test of isComposite method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testIsComposite() {
        OMRoot root = new OMRoot();
        assertTrue(root.isComposite());
    }

    /**
     * Test of isSame method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testIsSame() {
        OMRoot root1 = new OMRoot();
        root1.setObject(new OMInteger("1"));
        OMRoot root2 = new OMRoot();
        root2.setObject(new OMInteger("1"));
        assertTrue(root1.isSame(root2));
    }

    /**
     * Test isSame method.
     */
    public void testIsSame2() {
        OMRoot root = new OMRoot();
        OMObject object = new OMInteger("2");
        root.setObject(new OMInteger("1"));
        assertTrue(!root.isSame(object));
    }

    /**
     * Test of isValid method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testIsValid() {
        OMRoot root = new OMRoot();
        root.setObject(new OMInteger("1"));
        assertTrue(root.isValid());
    }

    /**
     * Test isValid method.
     */
    public void testIsValid2() {
        OMRoot root = new OMRoot();
        assertTrue(!root.isValid());
    }

    /**
     * Test of toString method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testToString() {
        OMRoot root = new OMRoot();
        root.setObject(new OMInteger("1"));
        assertTrue(root.toString().equals("<OMOBJ><OMI>1</OMI></OMOBJ>"));
    }

    /**
     * Test of setObject method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testSetObject() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);
        assertTrue(root.getObject() == integer);
    }

    /**
     * Test of getObject method, of class nl.tue.win.riaca.openmath.lang.OMRoot.
     */
    public void testGetObject() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);
        assertTrue(root.getObject() == integer);
    }
}
