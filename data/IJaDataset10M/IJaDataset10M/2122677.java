package com.nokia.ats4.appmodel.model.domain.testset.impl;

import com.nokia.ats4.appmodel.model.domain.testset.impl.TestSetModelImpl;
import com.nokia.ats4.appmodel.model.domain.testset.impl.TestSetImpl;
import com.nokia.ats4.appmodel.model.domain.testset.TestSet;
import junit.framework.*;
import com.nokia.ats4.appmodel.model.domain.testset.TestSetModel;
import java.util.List;
import java.util.Observable;

/**
 * TestSetModelImplTest
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class TestSetModelImplTest extends TestCase {

    private static final String NAME_PATTERN = "TestSet%s";

    private static final int NUMBER_OF_TEST_SETS = 10;

    private static final String[] PLATFORMS = { "Platform1", "Platform2" };

    private TestSetModel instance = null;

    public TestSetModelImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.instance = new TestSetModelImpl();
        for (int i = 0; i < NUMBER_OF_TEST_SETS; i++) {
            TestSet set = instance.createTestSet(String.format(NAME_PATTERN, i));
            if (i % 2 == 0) {
                set.addTargetPlatform(PLATFORMS[0]);
            } else {
                set.addTargetPlatform(PLATFORMS[1]);
            }
        }
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of addTestSet method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testAddTestSet() {
        String name = "Test Set to be added";
        assertFalse(instance.isDuplicateName(name));
        TestSet myTestSet = new TestSetImpl(name);
        assertTrue(instance.addTestSet(myTestSet));
        assertTrue(instance.getTestSets().contains(myTestSet));
        assertFalse(instance.addTestSet(null));
    }

    /**
     * Test of createTestSet method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testCreateTestSet() {
        String name = "Test Set to be created";
        assertFalse(instance.isDuplicateName(name));
        TestSet setA = instance.createTestSet(name);
        assertNotNull(setA);
        assertTrue(instance.getTestSets().contains(setA));
        TestSet setB = instance.createTestSet(name);
        assertNull(setB);
        try {
            TestSet fail = instance.createTestSet("");
            fail("Did not throw exception as expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        try {
            TestSet fail = instance.createTestSet(null);
            fail("Did not throw exception as expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * Test of getTestSets method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testGetTestSets() {
        List<TestSet> sets = instance.getTestSets();
        assertNotNull(sets);
        assertTrue(sets.size() == NUMBER_OF_TEST_SETS);
        assertEquals(instance.getTestSetCount(), sets.size());
        List<TestSet> setsA = instance.getTestSets(PLATFORMS[0]);
        assertNotNull(setsA);
        assertTrue(setsA.size() == 5);
        for (TestSet set : setsA) {
            assertTrue(set.getTargetPlatforms().contains(PLATFORMS[0]));
        }
        List<TestSet> setsB = instance.getTestSets(PLATFORMS[1]);
        assertNotNull(setsB);
        assertTrue(setsB.size() == 5);
        for (TestSet set : setsB) {
            assertTrue(set.getTargetPlatforms().contains(PLATFORMS[1]));
        }
        List<TestSet> nullSet = instance.getTestSets(null);
        assertNotNull(nullSet);
        assertTrue(nullSet.size() == 0);
    }

    /**
     * Test of getTestSet method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testGetTestSet() {
        for (int i = 0; i < NUMBER_OF_TEST_SETS; i++) {
            String name = String.format(NAME_PATTERN, i);
            TestSet setByIndex = instance.getTestSet(i);
            assertNotNull(setByIndex);
            assertEquals(name, setByIndex.getName());
            TestSet setByName = instance.getTestSet(name);
            assertNotNull(setByName);
            assertEquals(name, setByName.getName());
        }
        TestSet nonExistentSet = instance.getTestSet("foobar");
        assertNull(nonExistentSet);
    }

    /**
     * Test of removeTestSet method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testRemoveTestSet() {
        String name = String.format(NAME_PATTERN, 5);
        TestSet removedByName = instance.removeTestSet(name);
        assertNotNull(removedByName);
        assertEquals(name, removedByName.getName());
        assertTrue(instance.getTestSetCount() == (NUMBER_OF_TEST_SETS - 1));
        assertTrue(instance.getTestSets().size() == (NUMBER_OF_TEST_SETS - 1));
        assertFalse(instance.getTestSets().contains(removedByName));
        name = String.format(NAME_PATTERN, 1);
        TestSet removedByIndex = instance.removeTestSet(1);
        assertNotNull(removedByIndex);
        assertEquals(name, removedByIndex.getName());
        assertTrue(instance.getTestSetCount() == (NUMBER_OF_TEST_SETS - 2));
        assertTrue(instance.getTestSets().size() == (NUMBER_OF_TEST_SETS - 2));
        assertFalse(instance.getTestSets().contains(removedByIndex));
        TestSet foobarSet = instance.removeTestSet("foobar");
        assertNull(foobarSet);
        TestSet emptySet = instance.removeTestSet("");
        assertNull(emptySet);
        TestSet nullSet = instance.removeTestSet(null);
        assertNull(nullSet);
        try {
            TestSet negative = instance.removeTestSet(-1);
            fail("Did not throw exception as expected");
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        try {
            TestSet negative = instance.removeTestSet(instance.getTestSetCount());
            fail("Did not throw exception as expected");
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * Test of getTestSetCount method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testGetTestSetCount() {
        assertTrue(instance.getTestSetCount() == NUMBER_OF_TEST_SETS);
        int i = 1;
        while (!instance.isEmpty()) {
            instance.removeTestSet(0);
            assertTrue(instance.getTestSetCount() == (NUMBER_OF_TEST_SETS - i));
            i++;
        }
    }

    /**
     * Test of isDuplicateName method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testIsDuplicateName() {
        assertFalse(instance.isDuplicateName(""));
        assertFalse(instance.isDuplicateName(null));
        assertFalse(instance.isDuplicateName("Not a duplicate name"));
        for (int i = 0; i < NUMBER_OF_TEST_SETS; i++) {
            assertTrue(instance.isDuplicateName(String.format(NAME_PATTERN, i)));
        }
    }

    /**
     * Test of reset method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testReset() {
        assertFalse(instance.isEmpty());
        assertTrue(instance.getTestSets().size() > 0);
        assertTrue(instance.getTestSetCount() > 0);
        instance.reset();
        assertTrue(instance.isEmpty());
        assertTrue(instance.getTestSetCount() == 0);
        assertTrue(instance.getTestSets().size() == 0);
    }

    /**
     * Test of update method, of class com.nokia.kendo.model.domain.testset.impl.TestSetModelImpl.
     */
    public void testUpdate() {
        TestSetImplTest.TestObserver obs = new TestSetImplTest.TestObserver(instance);
        ((Observable) instance).addObserver(obs);
        obs.reset();
        assertFalse(obs.validate());
        TestSet set = instance.createTestSet("NewTestSet");
        assertTrue(obs.validate());
        obs.reset();
        instance.removeTestSet("NewTestSet");
        assertTrue(obs.validate());
        obs.reset();
        instance.addTestSet(set);
        assertTrue(obs.validate());
        obs.reset();
        instance.removeTestSet(0);
        assertTrue(obs.validate());
        obs.reset();
        instance.reset();
        assertTrue(obs.validate());
    }
}
