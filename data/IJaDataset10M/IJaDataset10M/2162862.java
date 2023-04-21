package uk.org.ogsadai.tuple;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import junit.framework.TestCase;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeConverter;

/**
 * Tests for type conversion helper class.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class TypeConverterTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Constructor.
     * 
     * @param name
     */
    public TypeConverterTest(String name) {
        super(name);
    }

    /**
     * Test conversion between numeric values.
     * 
     * @throws Exception
     */
    public void testNumeric() throws Exception {
        Object[] values = new Object[] { new Short((short) 100), new Integer(100), new Long(100), new Float(100), new Double(100), new BigDecimal(100) };
        int[] types = new int[] { TupleTypes._SHORT, TupleTypes._INT, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL };
        for (int fromTypeIdx = 0; fromTypeIdx < values.length; fromTypeIdx++) {
            for (int toTypeIdx = fromTypeIdx; toTypeIdx < values.length; toTypeIdx++) {
                assertTrue(TypeConverter.convertObject(types[fromTypeIdx], types[toTypeIdx], values[fromTypeIdx]).getClass() == values[toTypeIdx].getClass());
            }
        }
    }

    /**
     * Tests conversion from string type.
     * 
     * @throws Exception
     */
    public void testFromString() throws Exception {
        int[] types = new int[] { TupleTypes._SHORT, TupleTypes._INT, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL, TupleTypes._DATE, TupleTypes._TIME, TupleTypes._TIMESTAMP, TupleTypes._CHAR, TupleTypes._STRING };
        for (int toTypeIdx = 0; toTypeIdx < types.length; toTypeIdx++) {
            assertEquals(getExpectedClass(types[toTypeIdx]), TypeConverter.convertObject(TupleTypes._STRING, types[toTypeIdx], getTestStringValue(types[toTypeIdx])).getClass());
        }
    }

    /**
     * Tests conversion between dates.
     * 
     * @throws Exception
     */
    public void testBetweenDates() throws Exception {
        int[] types = new int[] { TupleTypes._DATE, TupleTypes._TIME, TupleTypes._TIMESTAMP };
        for (int fromTypeIdx = 0; fromTypeIdx < types.length; fromTypeIdx++) {
            for (int toTypeIdx = 0; toTypeIdx < types.length; toTypeIdx++) {
                int fromType = types[fromTypeIdx];
                int toType = types[toTypeIdx];
                assertEquals(getExpectedClass(toType), TypeConverter.convertObject(fromType, toType, TypeConverter.convertObject(TupleTypes._STRING, fromType, getTestStringValue(fromType))).getClass());
            }
        }
    }

    private String getTestStringValue(int type) {
        switch(type) {
            case TupleTypes._SHORT:
            case TupleTypes._INT:
            case TupleTypes._LONG:
            case TupleTypes._FLOAT:
            case TupleTypes._DOUBLE:
            case TupleTypes._BIGDECIMAL:
                return "100";
            case TupleTypes._DATE:
                return "2010-10-10";
            case TupleTypes._TIME:
                return "10:10";
            case TupleTypes._TIMESTAMP:
                return "2010-10-10 10:10:10";
            case TupleTypes._CHAR:
                return "A";
            case TupleTypes._STRING:
                return "Any String";
            default:
                throw new IllegalArgumentException("Type " + type + " is illegal.");
        }
    }

    private Class getExpectedClass(int type) {
        switch(type) {
            case TupleTypes._SHORT:
                return Short.class;
            case TupleTypes._INT:
                return Integer.class;
            case TupleTypes._LONG:
                return Long.class;
            case TupleTypes._FLOAT:
                return Float.class;
            case TupleTypes._DOUBLE:
                return Double.class;
            case TupleTypes._BIGDECIMAL:
                return BigDecimal.class;
            case TupleTypes._DATE:
                return Date.class;
            case TupleTypes._TIME:
                return Time.class;
            case TupleTypes._TIMESTAMP:
                return Timestamp.class;
            case TupleTypes._CHAR:
                return Character.class;
            case TupleTypes._STRING:
                return String.class;
            default:
                throw new IllegalArgumentException("Type " + type + " is illegal.");
        }
    }
}
