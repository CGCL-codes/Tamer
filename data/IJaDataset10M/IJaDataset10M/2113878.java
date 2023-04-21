package org.limaloa.mapping.object;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the <code>SimpleObjectMapper</code> class.
 * 
 * @author Chris Nappin
 */
public class SimpleObjectMapperTest {

    /** The class under test (should be stateless) */
    private static ObjectMapper mapper = new SimpleObjectMapper();

    /**
	 * Test mapping of same type.
	 */
    @Test
    public void mapObjectSameType() {
        TestBean testBean = new TestBean();
        ObjectMappingContext context = new ObjectMappingContext(TestBean.class, TestBean.class);
        Object result = mapper.mapObject(testBean, context);
        assertTrue("Should return same object", testBean == result);
    }

    /**
	 * Test mapping of String to Boolean.
	 */
    @Test
    public void mapObjectStringToBoolean() {
        ObjectMappingContext context = new ObjectMappingContext(String.class, Boolean.class);
        assertEquals("Should map boolean TRUE", Boolean.TRUE, mapper.mapObject("true", context));
        assertEquals("Should map boolean FALSE", Boolean.FALSE, mapper.mapObject("false", context));
    }

    /**
	 * Test mapping of Integer to Boolean is not handled.
	 */
    @Test(expected = UnsupportedObjectMappingException.class)
    public void mapObjectIntegerToBooleanFails() {
        ObjectMappingContext context = new ObjectMappingContext(Integer.class, Boolean.class);
        mapper.mapObject(new Integer(1), context);
    }

    /**
	 * Test mapping to Byte.
	 */
    @Test
    public void mapObjectToByte() {
        assertEquals("Should map String to Byte", new Byte((byte) 123), mapper.mapObject("123", new ObjectMappingContext(String.class, Byte.class)));
        assertEquals("Should map Character (in range) to Byte", new Byte((byte) 65), mapper.mapObject(new Character('A'), new ObjectMappingContext(Character.class, Byte.class)));
        assertEquals("Should map Short (in range) to Byte", new Byte(Byte.MAX_VALUE), mapper.mapObject(new Short((short) Byte.MAX_VALUE), new ObjectMappingContext(Short.class, Byte.class)));
        assertEquals("Should map Integer (in range) to Byte", new Byte(Byte.MIN_VALUE), mapper.mapObject(new Integer((int) Byte.MIN_VALUE), new ObjectMappingContext(Integer.class, Byte.class)));
        assertEquals("Should map Long (in range) to Byte", new Byte(Byte.MAX_VALUE), mapper.mapObject(new Long((long) Byte.MAX_VALUE), new ObjectMappingContext(Long.class, Byte.class)));
    }

    /**
	 * Test mapping of Short out of range to Byte is not handled.
	 */
    @Test(expected = InvalidNumericMappingException.class)
    public void mapObjectShortOutOfRangeToByteFails1() {
        mapper.mapObject(new Short((short) (Byte.MAX_VALUE + 1)), new ObjectMappingContext(Short.class, Byte.class));
    }

    /**
	 * Test mapping of Short out of range to Byte is not handled.
	 */
    @Test(expected = InvalidNumericMappingException.class)
    public void mapObjectShortOutOfRangeToByteFails2() {
        mapper.mapObject(new Short((short) (Byte.MIN_VALUE - 1)), new ObjectMappingContext(Short.class, Byte.class));
    }

    /**
	 * Test mapping of Integer to Byte is not handled.
	 */
    @Test(expected = UnsupportedObjectMappingException.class)
    public void mapObjectIntegerOutOfRangeToByteFails() {
        mapper.mapObject(new Integer(Integer.MAX_VALUE), new ObjectMappingContext(Integer.class, Byte.class));
    }

    /**
	 * Test mapping to Short.
	 */
    @Test
    public void mapObjectToShort() {
        assertEquals("Should map String to Short", new Short((short) 4321), mapper.mapObject("4321", new ObjectMappingContext(String.class, Short.class)));
        assertEquals("Should map Character to Short", new Short((short) 65), mapper.mapObject(new Character('A'), new ObjectMappingContext(Character.class, Short.class)));
        assertEquals("Should map Byte to Short", new Short(Byte.MAX_VALUE), mapper.mapObject(new Byte(Byte.MAX_VALUE), new ObjectMappingContext(Byte.class, Short.class)));
        assertEquals("Should map Integer in range to Short", new Short(Short.MAX_VALUE), mapper.mapObject(new Integer(Short.MAX_VALUE), new ObjectMappingContext(Integer.class, Short.class)));
        assertEquals("Should map Long in range to Short", new Short(Short.MIN_VALUE), mapper.mapObject(new Long(Short.MIN_VALUE), new ObjectMappingContext(Long.class, Short.class)));
    }

    /**
	 * Test mapping of Long out of range to Short is not handled.
	 */
    @Test(expected = InvalidNumericMappingException.class)
    public void mapObjectLongOutOfRangeToShortFails() {
        mapper.mapObject(new Long(Long.MIN_VALUE), new ObjectMappingContext(Long.class, Short.class));
    }

    /**
	 * Test mapping to Integer.
	 */
    @Test
    public void mapObjectToInteger() {
        assertEquals("Should map String to Integer", new Integer(54321), mapper.mapObject("54321", new ObjectMappingContext(String.class, Integer.class)));
        assertEquals("Should map Character to Integer", new Integer(65), mapper.mapObject(new Character('A'), new ObjectMappingContext(Character.class, Integer.class)));
        assertEquals("Should map Byte to Integer", new Integer(Byte.MAX_VALUE), mapper.mapObject(new Byte(Byte.MAX_VALUE), new ObjectMappingContext(Byte.class, Integer.class)));
        assertEquals("Should map Short to Integer", new Integer(Short.MAX_VALUE), mapper.mapObject(new Short(Short.MAX_VALUE), new ObjectMappingContext(Short.class, Integer.class)));
        assertEquals("Should map Long in range to Integer", new Integer(Integer.MIN_VALUE), mapper.mapObject(new Long(Integer.MIN_VALUE), new ObjectMappingContext(Long.class, Integer.class)));
    }

    /**
	 * Test mapping of Long out of range to Integer is not handled.
	 */
    @Test(expected = InvalidNumericMappingException.class)
    public void mapObjectLongToIntegerFails() {
        mapper.mapObject(new Long(((long) Integer.MIN_VALUE) - 1L), new ObjectMappingContext(Long.class, Integer.class));
    }

    /**
	 * Test mapping to Long.
	 */
    @Test
    public void mapObjectToLong() {
        assertEquals("Should map String to Long", new Long(54321), mapper.mapObject("54321", new ObjectMappingContext(String.class, Long.class)));
        assertEquals("Should map Character to Long", new Long(65), mapper.mapObject(new Character('A'), new ObjectMappingContext(Character.class, Long.class)));
        assertEquals("Should map Byte to Long", new Long(123), mapper.mapObject(new Byte((byte) 123), new ObjectMappingContext(Byte.class, Long.class)));
        assertEquals("Should map Short to Long", new Long(1234), mapper.mapObject(new Short((short) 1234), new ObjectMappingContext(Short.class, Long.class)));
        assertEquals("Should map Integer to Long", new Long(12345), mapper.mapObject(new Integer(12345), new ObjectMappingContext(Integer.class, Long.class)));
    }

    /**
	 * Test mapping of Double to Long is not handled.
	 */
    @Test(expected = UnsupportedObjectMappingException.class)
    public void mapObjectDoubleToLongFails() {
        mapper.mapObject(new Double(1.0D), new ObjectMappingContext(Double.class, Long.class));
    }

    /**
	 * Test mapping to Float.
	 */
    @Test
    public void mapObjectToFloat() {
        assertEquals("Should map String to Float", new Float(54321.5f).floatValue(), ((Float) mapper.mapObject("54321.5", new ObjectMappingContext(String.class, Float.class))).floatValue(), 0.01);
        assertEquals("Should map Character to Float", new Float(65).floatValue(), ((Float) mapper.mapObject(new Character('A'), new ObjectMappingContext(Character.class, Float.class))).floatValue(), 0.01);
        assertEquals("Should map Byte to Float", new Float(123).floatValue(), ((Float) mapper.mapObject(new Byte((byte) 123), new ObjectMappingContext(Byte.class, Float.class))).floatValue(), 0.01);
        assertEquals("Should map Short to Float", new Float(1234).floatValue(), ((Float) mapper.mapObject(new Short((short) 1234), new ObjectMappingContext(Short.class, Float.class))).floatValue(), 0.01);
        assertEquals("Should map Integer to Float", new Float(12345).floatValue(), ((Float) mapper.mapObject(new Integer(12345), new ObjectMappingContext(Integer.class, Float.class))).floatValue(), 0.01);
        assertEquals("Should map Long to Float", new Float(123456).floatValue(), ((Float) mapper.mapObject(new Long(123456), new ObjectMappingContext(Long.class, Float.class))).floatValue(), 0.01);
    }

    /**
	 * Test mapping of Double to Float is not handled.
	 */
    @Test(expected = UnsupportedObjectMappingException.class)
    public void mapObjectDoubleToFloatFails() {
        mapper.mapObject(new Double(1.0D), new ObjectMappingContext(Double.class, Float.class));
    }

    /**
	 * Test mapping to Double.
	 */
    @Test
    public void mapObjectToDouble() {
        assertEquals("Should map String to Double", new Double(54321.5d).doubleValue(), ((Double) mapper.mapObject("54321.5", new ObjectMappingContext(String.class, Double.class))).doubleValue(), 0.01);
        assertEquals("Should map Character to Double", new Double(65).doubleValue(), ((Double) mapper.mapObject(new Character('A'), new ObjectMappingContext(Character.class, Double.class))).doubleValue(), 0.01);
        assertEquals("Should map Byte to Double", new Double(123).doubleValue(), ((Double) mapper.mapObject(new Byte((byte) 123), new ObjectMappingContext(Byte.class, Double.class))).doubleValue(), 0.01);
        assertEquals("Should map Short to Double", new Double(1234).doubleValue(), ((Double) mapper.mapObject(new Short((short) 1234), new ObjectMappingContext(Short.class, Double.class))).doubleValue(), 0.01);
        assertEquals("Should map Integer to Double", new Double(12345).doubleValue(), ((Double) mapper.mapObject(new Integer(12345), new ObjectMappingContext(Integer.class, Double.class))).doubleValue(), 0.01);
        assertEquals("Should map Long to Double", new Double(123456).doubleValue(), ((Double) mapper.mapObject(new Long(123456), new ObjectMappingContext(Long.class, Double.class))).doubleValue(), 0.01);
    }

    /**
	 * Test mapping to Character.
	 */
    @Test
    public void mapObjectToCharacter() {
        assertEquals("Should map String to Character", new Character('A'), mapper.mapObject("A", new ObjectMappingContext(String.class, Character.class)));
        assertEquals("Should map Byte to Character", new Character('A'), mapper.mapObject(new Byte((byte) 65), new ObjectMappingContext(Byte.class, Character.class)));
        assertEquals("Should map Short to Character", new Character((char) 123), mapper.mapObject(new Short((short) 123), new ObjectMappingContext(Short.class, Character.class)));
        assertEquals("Should map Integer in range to Character", new Character(Character.MAX_VALUE), mapper.mapObject(new Integer(Character.MAX_VALUE), new ObjectMappingContext(Integer.class, Character.class)));
        assertEquals("Should map Long in range to Character", new Character(Character.MIN_VALUE), mapper.mapObject(new Long(Character.MIN_VALUE), new ObjectMappingContext(Long.class, Character.class)));
    }

    /**
	 * Test mapping of Integer out of range to Character is not handled.
	 */
    @Test(expected = InvalidNumericMappingException.class)
    public void mapObjectIntegerOutOfRangeToCharacterFails() {
        mapper.mapObject(new Integer(Character.MAX_VALUE + 1), new ObjectMappingContext(Integer.class, Character.class));
    }

    /**
	 * Test mapping of multi-character String to Character is not handled.
	 */
    @Test(expected = UnsupportedObjectMappingException.class)
    public void mapObjectMultiCharStringToCharacterFails() {
        mapper.mapObject("ABC", new ObjectMappingContext(String.class, Character.class));
    }

    /**
	 * Test mapping to String.
	 */
    @Test
    public void mapObjectToString() {
        assertEquals("Should map Character to String", "A", mapper.mapObject(new Character('A'), new ObjectMappingContext(Character.class, String.class)));
        assertEquals("Should map Boolean to String", "true", mapper.mapObject(Boolean.TRUE, new ObjectMappingContext(Boolean.class, String.class)));
        assertEquals("Should map Byte to String", "123", mapper.mapObject(new Byte((byte) 123), new ObjectMappingContext(Byte.class, String.class)));
        assertEquals("Should map Short to String", "1234", mapper.mapObject(new Short((short) 1234), new ObjectMappingContext(Short.class, String.class)));
        assertEquals("Should map Integer to String", "12345", mapper.mapObject(new Integer(12345), new ObjectMappingContext(Integer.class, String.class)));
        assertEquals("Should map Long to String", "123456", mapper.mapObject(new Long(123456), new ObjectMappingContext(Long.class, String.class)));
        assertEquals("Should map Float to String", "12345.678", mapper.mapObject(new Float(12345.678f), new ObjectMappingContext(Float.class, String.class)));
        assertEquals("Should map Double to String", "1.23456789123456E8", mapper.mapObject(new Double(123456789.123456d), new ObjectMappingContext(Double.class, String.class)));
    }

    /**
	 * Used in tests above.
	 * @author NappinC
	 */
    public class TestBean {
    }
}
