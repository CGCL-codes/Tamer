package toxTree.test.tree.rules;

import java.util.HashSet;
import junit.framework.TestCase;
import toxTree.core.SmartElementsList;

public class TestSmartElementsList extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestSmartElementsList.class);
    }

    public void testContainsObject() {
        SmartElementsList list = new SmartElementsList();
        list.add("C");
        assertTrue(list.contains("C"));
        list.add("Cl");
        assertTrue(list.contains("Cl"));
        assertTrue(list.contains("X"));
    }

    public void testEquals() {
        SmartElementsList list = new SmartElementsList();
        list.add("C");
        list.add("X");
        SmartElementsList list1 = new SmartElementsList();
        list1.add("X");
        list1.add("C");
        assertEquals(list, list1);
    }

    public void testGetHalogens() {
        SmartElementsList list = new SmartElementsList();
        HashSet<String> h = list.getHalogens();
        HashSet<String> hal = new HashSet<String>();
        hal.add("F");
        hal.add("Cl");
        hal.add("Br");
        hal.add("I");
        assertEquals(hal, h);
    }

    public void testSetHalogens() {
        HashSet<String> hal = new HashSet<String>();
        hal.add("F");
        hal.add("Cl");
        hal.add("Br");
        SmartElementsList list = new SmartElementsList();
        list.add("X");
        list.setHalogens(hal);
        HashSet<String> h = list.getHalogens();
        assertEquals(hal, h);
        assertTrue(list.contains("Cl"));
        assertFalse(list.contains("I"));
    }
}
