package org.happy.commons.ver1x1.util;

import static org.junit.Assert.*;
import org.happy.commons.util.Pair_1x0;
import org.junit.Test;

public class Pair_1x0Test {

    @Test
    public void testPair_1x0() {
        Pair_1x0<String, Integer> pair = new Pair_1x0<String, Integer>("one", 1);
        assertEquals("one", pair.getFst());
        assertEquals((Integer) 1, pair.getSnd());
        pair.setFst("two");
        pair.setSnd(2);
        assertEquals("two", pair.getFst());
        assertEquals((Integer) 2, pair.getSnd());
    }
}
