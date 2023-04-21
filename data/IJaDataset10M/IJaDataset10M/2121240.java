package org.shapelogic.streams;

import org.shapelogic.calculation.RootMap;
import org.shapelogic.mathematics.NaturalNumberStream;
import junit.framework.TestCase;

/** Test NamedIndexStream0.
 * <br />
 * 
 * @author Sami Badawi
 *
 */
@Deprecated
public class NamedNumberedStreamTest extends TestCase {

    @Override
    public void setUp() {
        NumberedStream<Integer> naturalNumbersTo3 = new NaturalNumberStream(3);
        RootMap.put("naturalNumbersTo3", naturalNumbersTo3);
    }

    public void testNext() {
        NumberedStream<Integer> stream = new NamedNumberedStream("naturalNumbersTo3");
        assertEquals(new Integer(0), stream.next());
        assertEquals(new Integer(1), stream.next());
        assertEquals(new Integer(2), stream.next());
        assertEquals(new Integer(3), stream.next());
        assertEquals(null, stream.next());
    }

    public void testGet() {
        NumberedStream<Integer> stream = new NamedNumberedStream("naturalNumbersTo3");
        assertEquals(new Integer(0), stream.get(0));
        assertEquals(new Integer(1), stream.get(1));
        assertEquals(new Integer(2), stream.get(2));
        assertEquals(new Integer(3), stream.get(3));
        assertEquals(null, stream.get(4));
    }
}
