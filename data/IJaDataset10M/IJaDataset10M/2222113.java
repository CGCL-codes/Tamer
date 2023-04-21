package net.sf.jfpl.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;
import net.sf.jfpl.core.Fun1;

/**
 * 
 * @author <a href="mailto:guilin.sun@gmail.com">Guile</a>
 */
public class ForEachTest {

    private List<AtomicInteger> list;

    private Fun1<AtomicInteger, ?> fun = new Fun1<AtomicInteger, Void>() {

        @Override
        public Void call(AtomicInteger arg) {
            arg.incrementAndGet();
            return null;
        }
    };

    @Before
    public void init() {
        list = Arrays.asList(new AtomicInteger(0), new AtomicInteger(2), new AtomicInteger(4), new AtomicInteger(6));
    }

    @Test
    public void testCall() {
        ForEach<AtomicInteger> forEach = ForEach.forEach(AtomicInteger.class);
        forEach.call(list.iterator(), fun);
    }

    @Test
    public void testForEach() {
        ForEach.forEach(list.iterator(), fun);
        test();
    }

    /**
	 * 
	 */
    private void test() {
        Iterator<AtomicInteger> itor = list.iterator();
        assertTrue(itor.hasNext());
        assertEquals(1, itor.next().get());
        assertTrue(itor.hasNext());
        assertEquals(3, itor.next().get());
        assertTrue(itor.hasNext());
        assertEquals(5, itor.next().get());
        assertTrue(itor.hasNext());
        assertEquals(7, itor.next().get());
        assertFalse(itor.hasNext());
    }
}
