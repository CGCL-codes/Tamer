package com.kakao.jini.java.container;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ArrayTest {

    @Test
    public void testListSize() {
        List<String> list = new ArrayList<String>();
        list.add("one");
        list.add("two");
        assertEquals(list.size(), 2);
    }
}
