package de.javatt.data.scenario;

import java.util.Vector;
import de.javatt.id.Identifier;
import junit.framework.TestCase;

public class TestScenarioImpl extends TestCase {

    public void testScenarioNotInitialized() {
        ScenarioImpl underTest = new ScenarioImpl();
        assertEquals("no name", underTest.getName());
        assertNull(underTest.getContext());
        assertNull(underTest.getID());
        assertNull(underTest.getPreCondition());
        assertNotNull(underTest.getItems());
        assertTrue(underTest.getItems().size() == 0);
        assertTrue(underTest.getTimeOut() == 0);
        assertEquals("Scenario", underTest.getTypeName());
    }

    public void testScenario() {
        ScenarioImpl underTest = new ScenarioImpl();
        underTest.setName("TestScenario");
        Identifier id = new Identifier(10);
        underTest.setID(id);
        underTest.setPreCondition("Precondition");
        underTest.setTimeOut(30);
        assertEquals("TestScenario", underTest.getName());
        assertEquals(id, underTest.getID());
        assertEquals("Precondition", underTest.getPreCondition());
        assertTrue(underTest.getTimeOut() == 30);
    }

    public void testScenarioAddItem() {
        Scenario target = new ScenarioImpl();
        Sequence seqToAdd = new SequenceImpl();
        TestItemNotSupportedException ex = null;
        try {
            target.addItem(seqToAdd);
        } catch (TestItemNotSupportedException wrongItem) {
            ex = wrongItem;
        }
        assertNull("Got TestItemNotSupportedException.", ex);
        Vector<Item> children = target.getItems();
        assertTrue("TestItem has wrong number of children", children.size() == 1);
    }

    public void testScenarioAddNullItem() {
        Scenario target = new ScenarioImpl();
        TestItemNotSupportedException ex = null;
        NullPointerException nullEx = null;
        try {
            target.addItem(null);
        } catch (TestItemNotSupportedException wrongItem) {
            ex = wrongItem;
        } catch (NullPointerException nullPex) {
            nullEx = nullPex;
        }
        assertNull(ex);
        assertNotNull(nullEx);
        Vector<Item> children = target.getItems();
        assertTrue(children.size() == 0);
    }

    public void testScenarioAddInvalidItem() {
        Scenario target = new ScenarioImpl();
        Scenario toAdd = new ScenarioImpl();
        TestItemNotSupportedException ex = null;
        NullPointerException nullEx = null;
        try {
            target.addItem(toAdd);
        } catch (TestItemNotSupportedException wrongItem) {
            ex = wrongItem;
        } catch (NullPointerException nullPex) {
            nullEx = nullPex;
        }
        assertNotNull(ex);
        assertNull(nullEx);
        Vector<Item> children = target.getItems();
        assertTrue(children.size() == 0);
    }
}
