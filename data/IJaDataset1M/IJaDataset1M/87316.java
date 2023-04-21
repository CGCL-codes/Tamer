package tests.com.ivis.xprocess.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;
import com.ivis.xprocess.ui.util.RenumberUtil;

public class TestRenumbering extends TestCase {

    public void testRenumberingMoveToBottom() {
        List<Integer> testSet = new ArrayList<Integer>();
        for (int i = 1; i < 5; i++) {
            testSet.add(new Integer(i));
        }
        Collections.sort(testSet, new Comparator<Integer>() {

            public int compare(Integer a1, Integer a2) {
                if (a1 > a2) {
                    return 1;
                }
                return 0;
            }
        });
        ArrayList<Object> orderedObjects = RenumberUtil.renumber(testSet, testSet.toArray()[1], 3);
        assertEquals(4, orderedObjects.size());
        Integer integerToTest = (Integer) orderedObjects.get(3);
        assertEquals(2, integerToTest.intValue());
        integerToTest = (Integer) orderedObjects.get(2);
        assertEquals(4, integerToTest.intValue());
    }

    public void testRenumbering() {
        List<Integer> testSet = new ArrayList<Integer>();
        for (int i = 1; i < 5; i++) {
            testSet.add(new Integer(i));
        }
        Collections.sort(testSet, new Comparator<Integer>() {

            public int compare(Integer a1, Integer a2) {
                if (a1 > a2) {
                    return 1;
                }
                return 0;
            }
        });
        ArrayList<Object> orderedObjects = RenumberUtil.renumber(testSet, testSet.toArray()[1], 2);
        assertEquals(4, orderedObjects.size());
        Integer integerToTest = (Integer) orderedObjects.get(2);
        assertEquals(2, integerToTest.intValue());
        integerToTest = (Integer) orderedObjects.get(1);
        assertEquals(3, integerToTest.intValue());
    }

    public void testRenumberingMoveToTop() {
        List<Integer> testSet = new ArrayList<Integer>();
        for (int i = 1; i < 5; i++) {
            testSet.add(new Integer(i));
        }
        Collections.sort(testSet, new Comparator<Integer>() {

            public int compare(Integer a1, Integer a2) {
                if (a1 > a2) {
                    return 1;
                }
                return 0;
            }
        });
        ArrayList<Object> orderedObjects = RenumberUtil.renumber(testSet, testSet.toArray()[1], 0);
        assertEquals(4, orderedObjects.size());
        Integer integerToTest = (Integer) orderedObjects.get(0);
        assertEquals(2, integerToTest.intValue());
        integerToTest = (Integer) orderedObjects.get(1);
        assertEquals(1, integerToTest.intValue());
    }

    public void testRenumberingScenario2() {
        List<Integer> testSet = new ArrayList<Integer>();
        for (int i = 1; i < 3; i++) {
            testSet.add(new Integer(i));
        }
        Collections.sort(testSet, new Comparator<Integer>() {

            public int compare(Integer a1, Integer a2) {
                if (a1 > a2) {
                    return 1;
                }
                return 0;
            }
        });
        ArrayList<Object> orderedObjects = RenumberUtil.renumber(testSet, testSet.toArray()[1], 0);
        assertEquals(2, orderedObjects.size());
        Integer integerToTest = (Integer) orderedObjects.get(0);
        assertEquals(2, integerToTest.intValue());
        integerToTest = (Integer) orderedObjects.get(1);
        assertEquals(1, integerToTest.intValue());
    }
}
