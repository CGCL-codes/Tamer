package de.jrpgcore.rpg.utils;

import junit.framework.TestCase;
import java.util.Comparator;

/**
 * @author wolfenlord
 * @since Oct 20, 2010 11:03:32 PM
 */
public class TestHashOfPriorityQueues extends TestCase {

    public static final Integer integers[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    public static enum KeyEnum {

        k1, k2, k3
    }

    ;

    HashOfPriorityQueues<KeyEnum, Integer> hash;

    @Override
    protected void setUp() throws Exception {
        hash = new HashOfPriorityQueues<KeyEnum, Integer>();
    }

    public void testSimpleAdd() throws Exception {
        hash.add(KeyEnum.k1, integers[4]);
        hash.add(KeyEnum.k2, integers[5]);
        hash.add(KeyEnum.k3, integers[6]);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[4]);
        assertEquals(hash.peekFirst(KeyEnum.k2), integers[5]);
        assertEquals(hash.peekFirst(KeyEnum.k3), integers[6]);
    }

    public void testMultipleAdd() {
        hash.add(KeyEnum.k1, integers[4]);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[4]);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[4]);
        hash.add(KeyEnum.k1, 1);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[1]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[1]);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[4]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[4]);
        assertNull(hash.peekFirst(KeyEnum.k1));
    }

    public void testMoreMultipleAdd() throws Exception {
        hash.add(KeyEnum.k1, 4);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[4]);
        hash.add(KeyEnum.k1, 1);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[1]);
        hash.add(KeyEnum.k1, 6);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[1]);
        hash.add(KeyEnum.k1, 3);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[1]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[1]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[3]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[4]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[6]);
        assertNull(hash.pollFirst(KeyEnum.k1));
    }

    public void testWithInversionComparator() {
        hash = new HashOfPriorityQueues<KeyEnum, Integer>(new InversionComparator());
        hash.add(KeyEnum.k1, 4);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[4]);
        hash.add(KeyEnum.k1, 1);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[4]);
        hash.add(KeyEnum.k1, 6);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[6]);
        hash.add(KeyEnum.k1, 3);
        assertEquals(hash.peekFirst(KeyEnum.k1), integers[6]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[6]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[4]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[3]);
        assertEquals(hash.pollFirst(KeyEnum.k1), integers[1]);
        assertNull(hash.pollFirst(KeyEnum.k1));
    }

    public static class InversionComparator implements Comparator<Integer> {

        public int compare(Integer o1, Integer o2) {
            if (o1 != null && o2 != null) {
                return o2.intValue() - o1.intValue();
            }
            return 0;
        }
    }
}
