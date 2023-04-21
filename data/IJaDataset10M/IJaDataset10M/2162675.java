package junitx.framework;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.2 $ $Date: 2003/01/20 10:44:50 $
 * @author <a href="mailto:vbossica@users.sourceforge.net">Vladimir R. Bossicard</a>
 */
public class ComparableAssertTest extends TestCase {

    public ComparableAssertTest(String name) {
        super(name);
    }

    public void testSuccessLesser() {
        ComparableAssert.assertLesser(new Integer(3), new Integer(2));
    }

    public void testFailLesser() {
        try {
            ComparableAssert.assertLesser(new Integer(2), new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailLesserEquals() {
        try {
            ComparableAssert.assertLesser(new Integer(3), new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailLesserNullLimit() {
        try {
            ComparableAssert.assertLesser(null, new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailLesserNullActual() {
        try {
            ComparableAssert.assertLesser(new Integer(2), null);
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testSuccessGreater() {
        ComparableAssert.assertGreater(new Integer(1), new Integer(3));
    }

    public void testFailGreater() {
        try {
            ComparableAssert.assertGreater(new Integer(5), new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailGreaterEquals() {
        try {
            ComparableAssert.assertGreater(new Integer(3), new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailGreaterNullLimit() {
        try {
            ComparableAssert.assertGreater(null, new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailGreaterNullActual() {
        try {
            ComparableAssert.assertGreater(new Integer(3), null);
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testSuccessNotLesser() {
        ComparableAssert.assertNotLesser(new Integer(2), new Integer(5));
    }

    public void testSuccessNotLesserEquals() {
        ComparableAssert.assertNotLesser(new Integer(5), new Integer(5));
    }

    public void testFailNotLesser() {
        try {
            ComparableAssert.assertNotLesser(new Integer(2), new Integer(1));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailNotLesserNullLimit() {
        try {
            ComparableAssert.assertNotLesser(null, new Integer(1));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailNotLesserNullActual() {
        try {
            ComparableAssert.assertNotLesser(new Integer(1), null);
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testSuccessNotGreater() {
        ComparableAssert.assertNotGreater(new Integer(3), new Integer(2));
    }

    public void testSuccessNotGreaterEquals() {
        ComparableAssert.assertNotGreater(new Integer(5), new Integer(5));
    }

    public void testFailNotGreater() {
        try {
            ComparableAssert.assertNotGreater(new Integer(2), new Integer(5));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailNotGreaterNullLimit() {
        try {
            ComparableAssert.assertNotGreater(null, new Integer(5));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testFailNotGreaterNullActual() {
        try {
            ComparableAssert.assertNotGreater(new Integer(2), null);
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testSuccessEquals() {
        ComparableAssert.assertEquals(new Integer(3), new Integer(3));
    }

    public void testFailEquals() {
        try {
            ComparableAssert.assertEquals(new Integer(2), new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testSuccessNotEquals() {
        ComparableAssert.assertNotEquals(new Integer(2), new Integer(3));
    }

    public void testFailNotEquals() {
        try {
            ComparableAssert.assertNotEquals(new Integer(3), new Integer(3));
        } catch (AssertionFailedError e) {
            return;
        }
        fail();
    }
}
