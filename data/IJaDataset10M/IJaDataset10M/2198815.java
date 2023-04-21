package net.sourceforge.cruisecontrol.dashboard.utils.functors;

import java.util.Arrays;
import junit.framework.TestCase;

public class AlphabeticalDescOrderComparatorTest extends TestCase {

    public void testShouldOrderFilesAlphabetically() throws Exception {
        String[] files = new String[] { "a", "c", "b" };
        AlphabeticalDescOrderComparator comparator = new AlphabeticalDescOrderComparator();
        Arrays.sort(files, comparator);
        assertEquals("c", files[0]);
        assertEquals("b", files[1]);
        assertEquals("a", files[2]);
    }
}
