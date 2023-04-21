package com.rapidminer.test;

import static junit.framework.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;
import com.rapidminer.tools.IterationArrayList;

/**
 * A test for the  {@link IterationArrayList}.
 * 
 * @author Michael Wurst
 */
public class IterationArrayListTest {

    @Test
    public void testAccess() {
        ArrayList<String> l = new ArrayList<String>();
        l.add("a");
        l.add("b");
        l.add("c");
        ArrayList<String> l2 = new IterationArrayList<String>(l.iterator());
        for (int i = 0; i < l.size(); i++) assertEquals(l2.get(i), l.get(i));
    }
}
