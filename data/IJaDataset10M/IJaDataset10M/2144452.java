package net.sf.jfpl.tuple;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:guilin.sun@gmail.com">Guile</a>
 */
public class PairTest {

    @Test
    public void testPair() {
        Pair<String, String> p1 = new Pair<String, String>("a", "b");
        assertEquals("a", p1.car());
        assertEquals("b", p1.cdr());
        Pair<Integer, Pair<String, String>> p2 = new Pair<Integer, Pair<String, String>>(1, p1);
        assertEquals(1, p2.car().intValue());
        assertEquals(p1, p2.cdr());
    }
}
