package com.genia.toolbox.basics.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.genia.toolbox.basics.bean.impl.IterableIteratorImpl;

/**
 * Testing {@link IterableIterator}.
 */
public class TestIterableIterator {

    /**
   * the test method.
   */
    @Test
    public void test() {
        List<Integer> l = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
        for (Iterator<Integer> it = new IterableIteratorImpl<Integer>(l.iterator()); it.hasNext(); ) {
            it.next();
            it.remove();
        }
        assertEquals(0, l.size());
        int i = 1;
        for (Integer j : new IterableIteratorImpl<Integer>(Arrays.asList(1, 2, 3, 4).iterator())) {
            assertEquals(i, j.intValue());
            i++;
        }
    }
}
