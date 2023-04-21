package test.com.ivis.xprocess.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import junit.framework.TestCase;
import com.ivis.xprocess.util.ScoredList;

public class TestScoredList extends TestCase {

    final Integer three = new Integer(3);

    final Integer two = new Integer(2);

    final Integer one = new Integer(1);

    final Integer zero = new Integer(0);

    ScoredList<Integer> list1;

    public void testConstructors() {
        assertEquals(2, list1.size());
        assertEquals(new BigDecimal(1), list1.getScore(0));
        assertEquals(new BigDecimal(0), list1.getScore(1));
        ScoredList<Object> list2 = new ScoredList<Object>(list1);
        assertNotNull(list2);
        assertEquals(2, list2.size());
        assertNotNull(list2.get(0));
        assertEquals("1", list2.getScore(0).toString());
        assertEquals("0", list2.getScore(1).toString());
        assertTrue(list1.containsAll(list2));
        assertTrue(list2.containsAll(list1));
        assertTrue(list1.size() == list2.size());
        for (int i = 0; i < list1.size(); i++) {
            assertEquals("index: " + i + "\n", list1.get(i), list2.get(i));
            assertEquals("index: " + i + "\n", list1.getScore(i), list2.getScore(i));
        }
    }

    public void testInsertAll() {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < 10; i++) {
            set.add(new Integer(i));
        }
        list1.insertAll(set, new BigDecimal(0));
    }

    public void testAdd() {
        list1.add(1, three);
        assertTrue(list1.toString(), Arrays.equals(new Integer[] { one, three, zero }, list1.toArray(new Integer[3])));
        list1.add(2, two);
        assertTrue(Arrays.equals(new Integer[] { one, three, two, zero }, list1.toArray(new Integer[4])));
        assertScoresInOrder(list1);
        ArrayList<Integer> additions = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            additions.add(one);
            list1.add(i + 2, two);
        }
        assertDifferentScoresInOrder(list1);
        assertEquals("0.3", list1.getScore(102).toString());
        assertDifferentScoresInOrder(list1);
        assertEquals("0.35", list1.getScore(3).toString());
        assertEquals("0.30000000000000000000000002", list1.getScore(101).toString());
        assertEquals(26, list1.getScore(101).scale());
        list1.addAll(additions);
        assertDifferentScoresInOrder(list1);
        assertEquals(new BigDecimal(-100), list1.getScore(203));
        list1.addAll(1, additions);
        assertDifferentScoresInOrder(list1);
        assertEquals(new BigDecimal(0.5), list1.getScore(101));
        assertDifferentScoresInOrder(list1);
        assertEquals("0.600", list1.getScore(100).toString());
        assertEquals(3, list1.getScore(77).scale());
        list1.resetScores();
        assertEquals("304", list1.getScore(0).toString());
        assertEquals("1", list1.getScore(list1.size() - 1).toString());
        for (int i = 0; i < 100; i++) {
            additions.add(one);
            list1.add(2, two);
        }
        assertDifferentScoresInOrder(list1);
        assertEquals("303", list1.getScore(1).toString());
        assertEquals("302.9999999999999999999999999999999995", list1.getScore(2).toString());
        assertEquals(34, list1.getScore(2).scale());
    }

    private <T> void assertScoresInOrder(ScoredList<T> list) {
        BigDecimal score = list.getScore(0);
        BigDecimal thisScore;
        for (int i = 0; i < list.size(); i++) {
            thisScore = list.getScore(i);
            assertTrue("Index: " + i + ", Score: " + score + "\n" + list, score.compareTo(thisScore) >= 0);
            score = thisScore;
        }
    }

    private <T> void assertDifferentScoresInOrder(ScoredList<T> list) {
        BigDecimal score = list.getScore(0).add(new BigDecimal(1));
        BigDecimal thisScore;
        for (int i = 0; i < list.size(); i++) {
            thisScore = list.getScore(i);
            assertTrue("Index: " + i + ", Score: " + score + "\n" + list, score.compareTo(thisScore) > 0);
            score = thisScore;
        }
    }

    protected void setUp() {
        list1 = new ScoredList<Integer>();
        list1.add(one);
        list1.add(zero);
    }
}
