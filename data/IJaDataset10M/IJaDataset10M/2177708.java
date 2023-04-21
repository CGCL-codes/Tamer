package net.sf.staccatocommons.collections.stream;

import static junit.framework.Assert.*;
import static net.sf.staccatocommons.lang.number.NumberTypes.*;
import static net.sf.staccatocommons.lang.sequence.StopConditions.*;
import java.util.Arrays;
import net.sf.staccatocommons.defs.Applicable;
import net.sf.staccatocommons.defs.Evaluable;
import net.sf.staccatocommons.defs.Thunk;
import net.sf.staccatocommons.lang.sequence.Sequence;
import org.junit.Test;

/**
 * 
 * Test for {@link Streams}
 * 
 * @author flbulgarelli
 * 
 */
public class StreamsUnitTest {

    /**
   * Test method for
   * {@link Streams#from(java.lang.Object, Applicable, Evaluable)}.
   */
    @Test
    public void testFromSeq() {
        assertEquals(Streams.iterate(10, add(20), upTo(50)).toList(), Streams.from(Sequence.fromToBy(10, 50, 20)).toList());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), Streams.iterate(1, add(1)).take(10).toList());
    }

    /**
   * Test method for replicate with null argument
   * 
   * @throws Exception
   */
    @Test
    public void testRepeat() throws Exception {
        Stream<Object> repeat = Streams.repeat((Object) null).memorize();
        assertNull(repeat.first());
        assertNull(repeat.third());
    }

    /**
   * Test method for repeat(Thunk)
   * 
   * @throws Exception
   */
    @Test
    public void testRepeatThunk() throws Exception {
        Streams.repeat(new Thunk<Integer>() {

            int i = 0;

            public Integer value() {
                return i++;
            }
        }).take(4).equiv(0, 1, 2, 3);
    }
}
