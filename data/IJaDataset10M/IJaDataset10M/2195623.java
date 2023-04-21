package net.sf.eBus.condition;

import junit.framework.TestCase;
import net.sf.eBus.messages.type.DataType;

/**
 * JUnit test for the NotEquals condition.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public final class NotEqualsTest extends TestCase {

    public NotEqualsTest(final String name) {
        super(name);
    }

    public void testCtorInvalidType() {
        Exception caughtex = null;
        NotEquals condition = null;
        try {
            condition = new NotEquals("x", DataType.findType(Boolean.class), new String(), 0);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNotNull("no exception caught", caughtex);
        assertTrue("wrong exception", caughtex instanceof IllegalArgumentException);
        assertNull("condition not null", condition);
        return;
    }

    public void testNotEqualsNullFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Boolean.class), null, 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testNotEqualsNullTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Boolean.class), null, 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testBooleanNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Boolean.class), Boolean.TRUE, 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testBooleanNotEqualsNonBoolean() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Boolean.class), Boolean.TRUE, 0);
            retcode = condition.evaluate(new Integer(1));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testBooleanNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Boolean.class), Boolean.TRUE, 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testBooleanNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Boolean.class), Boolean.TRUE, 0);
            retcode = condition.evaluate(Boolean.FALSE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testCharNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Character.class), new Character('a'), 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testCharNotEqualsNonChar() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Character.class), new Character('a'), 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testCharNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Character.class), new Character('a'), 0);
            retcode = condition.evaluate(new Character('a'));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testCharNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Character.class), new Character('a'), 0);
            retcode = condition.evaluate(new Character('b'));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testByteNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Byte.class), new Byte(Byte.MAX_VALUE), 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testByteNotEqualsNonByte() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Byte.class), new Byte(Byte.MAX_VALUE), 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testByteNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Byte.class), new Byte(Byte.MAX_VALUE), 0);
            retcode = condition.evaluate(new Byte(Byte.MAX_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testByteNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Byte.class), new Byte(Byte.MAX_VALUE), 0);
            retcode = condition.evaluate(new Byte(Byte.MIN_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testShortNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Short.class), new Short(Short.MAX_VALUE), 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testShortNotEqualsNonShort() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Short.class), new Short(Short.MAX_VALUE), 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testShortNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Short.class), new Short(Short.MAX_VALUE), 0);
            retcode = condition.evaluate(new Short(Short.MAX_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testShortNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Short.class), new Short(Short.MAX_VALUE), 0);
            retcode = condition.evaluate(new Short(Short.MIN_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testIntNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Integer.class), new Integer(Integer.MAX_VALUE), 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testIntNotEqualsNonInt() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Integer.class), new Integer(Integer.MAX_VALUE), 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testIntNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Integer.class), new Integer(Integer.MAX_VALUE), 0);
            retcode = condition.evaluate(new Integer(Integer.MAX_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testIntNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Integer.class), new Integer(Integer.MAX_VALUE), 0);
            retcode = condition.evaluate(new Integer(Integer.MIN_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testLongNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Long.class), new Long(Long.MAX_VALUE), 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testLongNotEqualsNonLong() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Long.class), new Long(Long.MAX_VALUE), 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testLongNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Long.class), new Long(Long.MAX_VALUE), 0);
            retcode = condition.evaluate(new Long(Long.MAX_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testLongNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Long.class), new Long(Long.MAX_VALUE), 0);
            retcode = condition.evaluate(new Long(Long.MIN_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testFloatNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Float.class), new Float(Float.MAX_VALUE), 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testFloatNotEqualsNonFloat() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Float.class), new Float(Float.MAX_VALUE), 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testFloatNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Float.class), new Float(Float.MAX_VALUE), 0);
            retcode = condition.evaluate(new Float(Float.MAX_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testFloatNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Float.class), new Float(Float.MAX_VALUE), 0);
            retcode = condition.evaluate(new Float(Float.MIN_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testDoubleNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Double.class), new Double(Double.MAX_VALUE), 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testDoubleNotEqualsNonDouble() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Double.class), new Double(Double.MAX_VALUE), 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testDoubleNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(Double.class), new Double(Double.MAX_VALUE), 0);
            retcode = condition.evaluate(new Double(Double.MAX_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testDoubleNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(Double.class), new Double(Double.MAX_VALUE), 0);
            retcode = condition.evaluate(new Double(Double.MIN_VALUE));
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testStringNotEqualsNull() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(String.class), "foobar", 0);
            retcode = condition.evaluate((Comparable) null);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testStringNotEqualsNonString() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(String.class), "foobar", 0);
            retcode = condition.evaluate(Boolean.TRUE);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }

    public void testStringNotEqualsFalse() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = true;
        try {
            condition = new NotEquals("x", DataType.findType(String.class), "foobar", 0);
            retcode = condition.evaluate("foobar");
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertFalse("eval true", retcode);
        return;
    }

    public void testStringNotEqualsTrue() {
        Exception caughtex = null;
        NotEquals condition = null;
        boolean retcode = false;
        try {
            condition = new NotEquals("x", DataType.findType(String.class), "foobar", 0);
            retcode = condition.evaluate("foobaz");
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("condition null", condition);
        assertTrue("eval false", retcode);
        return;
    }
}
