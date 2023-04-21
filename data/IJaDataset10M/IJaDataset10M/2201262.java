package gnu.ojama.test.moduletest;

import gnu.ojama.attr.*;
import junit.framework.*;

/**
 * Tests for ObjectAttribute class
 * @author Markku Vuorenmaa
 */
public class ObjectAttributeTest extends TestCase {

    /**
     * Test case constructor with name.
     * @param name test case name
     */
    public ObjectAttributeTest(String name) {
        super(name);
    }

    /**
     * Test constructor with boolean.
     */
    public void testBooleanConstructor() {
        ObjectAttribute attr = new ObjectAttribute(true, true);
        assertTrue(attr.getValue().equals(Boolean.TRUE));
    }

    /**
     * Test constructor with byte.
     */
    public void testByteConstructor() {
        ObjectAttribute attr = new ObjectAttribute((byte) 1, true);
        assertTrue(attr.getValue().equals(new Byte((byte) 1)));
    }

    /**
     * Test constructor with char.
     */
    public void testCharConstructor() {
        ObjectAttribute attr = new ObjectAttribute('c', true);
        assertTrue(attr.getValue().equals(new Character('c')));
    }

    /**
     * Test constructor with double.
     */
    public void testDoubleConstructor() {
        ObjectAttribute attr = new ObjectAttribute(78.910, true);
        assertTrue(attr.getValue().equals(new Double(78.910)));
    }

    /**
     * Test constructor with float.
     */
    public void testFloatConstructor() {
        ObjectAttribute attr = new ObjectAttribute((float) 789.10, true);
        assertTrue(attr.getValue().equals(new Float((float) 789.10)));
    }

    /**
     * Test constructor with int.
     */
    public void testIntConstructor() {
        ObjectAttribute attr = new ObjectAttribute(78910, true);
        assertTrue(attr.getValue().equals(new Integer(78910)));
    }

    /**
     * Test constructor with Long.
     */
    public void testLongConstructor() {
        ObjectAttribute attr = new ObjectAttribute((long) 789104, true);
        assertTrue(attr.getValue().equals(new Long((long) 789104)));
    }

    /**
     * Test constructor with int.
     */
    public void testShortConstructor() {
        ObjectAttribute attr = new ObjectAttribute((short) 31000, true);
        assertTrue(attr.getValue().equals(new Short((short) 31000)));
    }

    /**
     *
     */
    protected void setUp() {
    }
}
