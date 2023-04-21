package com.mainatom.utils;

import junit.framework.*;

public class StringListTest extends TestCase {

    public void testJoin() throws Exception {
        StringList lst = new StringList();
        lst.add("q");
        assertEquals(lst.join(",", "'"), "'q'");
        lst.add("q1");
        assertEquals(lst.join(",", "'"), "'q','q1'");
        lst.add("");
        assertEquals(lst.join(",", "'"), "'q','q1',''");
    }
}
