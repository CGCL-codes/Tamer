package uk.ac.ed.rapid.value;

import junit.framework.TestCase;
import uk.ac.ed.rapid.value.impl.ArrayValue;

/**
 *
 * @author jos
 */
public class ArrayTest extends TestCase {

    public void testValidate() {
        ArrayValue array = new ArrayValue();
        assertEquals(true, array.validate());
        array = new ArrayValue(new String[] {});
        assertEquals(true, array.validate());
        array.put(new String[] { "5", "4.5" });
        array.setMin(-1.0);
        assertEquals(true, array.validate());
        array.setMax(6.0);
        assertEquals(true, array.validate());
        array.setMax(4.7);
        assertEquals(false, array.validate());
        array.setMax(null);
        array.setMin(4.6);
        assertEquals(false, array.validate());
        array.put(new String[] { "not a number", "3.0" });
        assertEquals(false, array.validate());
        array.setMin(null);
        array.put(new String[] { "ababab", "abab", "ab" });
        array.setRegExp("^(ab)*$");
        assertEquals(true, array.validate());
        array.setRegExp("wrong");
        assertEquals(false, array.validate());
        try {
            array.put(new String[] {});
            assertEquals("", array.get(0));
            assertEquals("", array.get(1));
            array.put(new String[] { "one element" });
            assertEquals("", array.get(-1));
            assertEquals("one element", array.get(0));
            assertEquals("one element", array.get(1));
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            fail("Error " + ex.getMessage());
        }
    }
}
