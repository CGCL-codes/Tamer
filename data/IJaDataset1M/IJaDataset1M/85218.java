package net.sf.kerner.utils.collections.impl;

import static org.junit.Assert.assertTrue;
import java.util.Comparator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestComparatorInverter {

    private ComparatorInverter c;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testCompare01() {
        c = new ComparatorInverter<Integer>(new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        final int i = c.compare(Integer.valueOf(1), Integer.valueOf(2));
        final boolean b = (i > 0);
        assertTrue(b);
    }

    @Test
    public final void testCompare02() {
        c = new ComparatorInverter<Integer>(new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        final int i = c.compare(Integer.valueOf(2), Integer.valueOf(1));
        final boolean b = (i < 0);
        assertTrue(b);
    }

    @Test
    public final void testCompare03() {
        c = new ComparatorInverter<Integer>(new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        final int i = c.compare(Integer.valueOf(1), Integer.valueOf(1));
        final boolean b = (i == 0);
        assertTrue(b);
    }
}
