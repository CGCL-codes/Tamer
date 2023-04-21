package uk.org.ogsadai.activity.astro;

import uk.org.ogsadai.activity.astro.votable.StarTableTypeToTupleTypeMapper;
import uk.org.ogsadai.tuple.TupleTypes;
import junit.framework.TestCase;

/**
 * Test for StarTableTypeToTupleTypeMapper.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class StarTableTypeToTupleTypeMapperTest extends TestCase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010";

    /**
     * Test getType() method.
     */
    public void testGetType() {
        Character ch = new Character('a');
        Class cs = ch.getClass();
        assertEquals(TupleTypes._OBJECT, StarTableTypeToTupleTypeMapper.getType(cs, true));
        Object[] input = { new Boolean(true), new Byte("0"), new Short("0"), new Integer(0), new Long(1), new Character('a'), new Float(0.0), new Double(0.0), new String(""), new Object() };
        int[] expected = { TupleTypes._BOOLEAN, TupleTypes._INT, TupleTypes._SHORT, TupleTypes._INT, TupleTypes._LONG, TupleTypes._CHAR, TupleTypes._FLOAT, TupleTypes._DOUBLE, TupleTypes._STRING, TupleTypes._OBJECT };
        assertTrue("Number of inputs and outputs do not match", input.length == expected.length);
        for (int i = 0; i < input.length; i++) {
            Class c = input[i].getClass();
            assertEquals(expected[i], StarTableTypeToTupleTypeMapper.getType(c, false));
        }
    }
}
