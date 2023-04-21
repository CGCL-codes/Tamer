package org.happy.collections.sets;

import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections.set.AbstractTestSortedSet;
import org.happy.commons.util.comparators.Comparator_1x0;
import org.junit.Test;

/**
 * tests the TreeSet implementation based on the Tree
 * @author Andreas Hollmann
 *
 */
public class TreeSet_1x0Test extends AbstractTestSortedSet {

    public TreeSet_1x0Test() {
        super("TreeSet_1x0Test");
    }

    public TreeSet_1x0Test(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set makeEmptySet() {
        return new TreeSet_1x0(new Comparator_1x0());
    }

    @Test
    public void testITeration() {
        TreeSet_1x0<Integer> set = TreeSet_1x0.of(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        Iterator<Integer> it = set.iterator();
        Integer i = 0;
        while (it.hasNext()) {
            Integer elem = (Integer) it.next();
            System.out.print(elem + ", ");
            assertEquals(i, elem);
            i++;
        }
        assertEquals(new Integer(i - 1), new Integer(9));
    }
}
