package junitx.framework;

import java.util.List;
import java.util.Vector;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.1 $ $Date: 2003/04/28 03:08:08 $
 * @author <a href="mailto:vbossica@users.sourceforge.net">Vladimir R. Bossicard</a>
 */
public class ListAssertTest extends TestCase {

    public ListAssertTest(String name) {
        super(name);
    }

    public void testEqualsSingleValue() {
        List expected = new Vector();
        expected.add(new Integer(1));
        List actual = new Vector();
        actual.add(new Integer(1));
        ListAssert.assertEquals(expected, actual);
    }

    public void testEqualsMultipleValues() {
        List expected = new Vector();
        expected.add(new Integer(1));
        expected.add(new Integer(2));
        List actual = new Vector();
        actual.add(new Integer(2));
        actual.add(new Integer(1));
        ListAssert.assertEquals(expected, actual);
    }

    public void testFailEqualsDifferentLength() {
        List expected = new Vector();
        expected.add(new Integer(1));
        expected.add(new Integer(2));
        List actual = new Vector();
        actual.add(new Integer(1));
        try {
            ListAssert.assertEquals(expected, actual);
        } catch (AssertionFailedError e) {
            assertEquals("expecting <2> in <1>", e.getMessage());
            return;
        }
        fail();
    }

    public void testFailEqualsDifferentLength_2() {
        List expected = new Vector();
        expected.add(new Integer(2));
        List actual = new Vector();
        actual.add(new Integer(2));
        actual.add(new Integer(1));
        try {
            ListAssert.assertEquals(expected, actual);
        } catch (AssertionFailedError e) {
            assertEquals("[length] expected:<1> but was:<2>", e.getMessage());
            return;
        }
        fail();
    }

    public void testContains() {
        List expected = new Vector();
        expected.add(new Integer(1));
        expected.add(new Integer(2));
        ListAssert.assertContains(expected, new Integer(1));
    }

    public void testFailContains() {
        List expected = new Vector();
        expected.add(new Integer(1));
        expected.add(new Integer(2));
        try {
            ListAssert.assertContains(expected, new Integer(3));
        } catch (AssertionFailedError e) {
            assertEquals("expecting <3> in <1, 2>", e.getMessage());
            return;
        }
        fail();
    }
}
