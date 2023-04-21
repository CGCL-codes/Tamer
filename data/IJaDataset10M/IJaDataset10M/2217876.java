package com.nokia.ats4.appmodel.model.domain.testset.impl;

import com.nokia.ats4.appmodel.model.domain.testset.impl.TestSetItemImpl;
import com.nokia.ats4.appmodel.model.domain.testset.impl.TestSetImpl;
import com.nokia.ats4.appmodel.MainApplication;
import com.nokia.ats4.appmodel.exception.KendoException;
import com.nokia.ats4.appmodel.model.KendoApplicationModel;
import com.nokia.ats4.appmodel.model.KendoProject;
import com.nokia.ats4.appmodel.util.DummyProjectBuilder;
import java.util.Observable;
import junit.framework.*;
import com.nokia.ats4.appmodel.model.domain.testset.TestSet;
import com.nokia.ats4.appmodel.model.domain.testset.TestSetItem;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCase;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCasePath;
import com.nokia.ats4.appmodel.util.Settings;
import java.util.List;
import java.util.Observer;

/**
 * TestSetImplTest
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class TestSetImplTest extends TestCase {

    private static final String TEST_SET_NAME = "Test Set";

    private TestSet instance = null;

    private UseCase useCaseItem = null;

    private UseCasePath useCasePathItem = null;

    private KendoApplicationModel appModelItem = null;

    public TestSetImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        Settings.load(MainApplication.FILE_PROPERTIES);
        this.instance = new TestSetImpl(TEST_SET_NAME);
        KendoProject prj = DummyProjectBuilder.createProjectWithUseCasePaths();
        useCaseItem = prj.getUseCaseModel().getUseCases().get(0);
        useCasePathItem = useCaseItem.getUseCasePath(0);
        appModelItem = prj.getDefaultModel().getApplicationModels().next();
        TestSetItem item1 = instance.createTestSetItem(useCaseItem);
        assertNotNull(item1);
        TestSetItem item2 = instance.createTestSetItem(useCasePathItem);
        assertNotNull(item2);
        TestSetItem item3 = instance.createTestSetItem(appModelItem);
        assertNotNull(item3);
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of constructor, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testConstructor() {
        try {
            TestSet item = new TestSetImpl("My Test Set");
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try {
            TestSet item = new TestSetImpl("");
            fail("Constructor did not throw exeception as expected");
        } catch (Exception e) {
        }
        try {
            TestSet item = new TestSetImpl(null);
            fail("Constructor did not throw exeception as expected");
        } catch (Exception e) {
        }
    }

    /**
     * Test of setName method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testSetName() {
        String name = "NewTestSetName";
        assertFalse(name.equals(instance.getName()));
        instance.setName(name);
        assertEquals(name, instance.getName());
        try {
            instance.setName("");
            fail("Did not throw exception as expected");
        } catch (IllegalArgumentException e) {
            assertEquals(name, instance.getName());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        try {
            instance.setName(null);
            fail("Did not throw exception as expected");
        } catch (IllegalArgumentException e) {
            assertEquals(name, instance.getName());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * Test of setDescription method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testSetDescription() {
        String description = "NewTestSetName";
        assertFalse(description.equals(instance.getDescription()));
        instance.setDescription(description);
        assertEquals(description, instance.getDescription());
        instance.setDescription(null);
        assertEquals("", instance.getDescription());
    }

    /**
     * Test of addTargetPlatform method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testAddTargetPlatform() {
        String[] platforms = { "Platform1", "Platform2", "Platform3", "Platform4", "Platform5" };
        for (int i = 0; i < platforms.length; i++) {
            instance.addTargetPlatform(platforms[i]);
            assertTrue(instance.getTargetPlatforms().size() == (i + 1));
            assertEquals(platforms[i], instance.getTargetPlatforms().get(i));
        }
        assertTrue(instance.getTargetPlatforms().size() == platforms.length);
        instance.addTargetPlatform(platforms[0]);
        instance.addTargetPlatform(platforms[0]);
        assertTrue(instance.getTargetPlatforms().size() == platforms.length);
        int count = 0;
        for (String platform : instance.getTargetPlatforms()) {
            if (platform.equals(platforms[0])) {
                count++;
            }
        }
        assertEquals(1, count);
        try {
            instance.addTargetPlatform("");
            fail("Did not throw exception as expected");
        } catch (IllegalArgumentException e) {
            assertTrue(instance.getTargetPlatforms().size() == platforms.length);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        try {
            instance.addTargetPlatform(null);
            fail("Did not throw exception as expected");
        } catch (IllegalArgumentException e) {
            assertTrue(instance.getTargetPlatforms().size() == platforms.length);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * Test of removeTargetPlatform method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testRemoveTargetPlatform() {
        String[] platforms = { "Platform1", "Platform2", "Platform3", "Platform4", "Platform5" };
        for (int i = 0; i < platforms.length; i++) {
            instance.addTargetPlatform(platforms[i]);
            assertTrue(instance.getTargetPlatforms().size() == (i + 1));
            assertEquals(platforms[i], instance.getTargetPlatforms().get(i));
        }
        assertTrue(instance.getTargetPlatforms().size() == platforms.length);
        assertTrue(instance.removeTargetPlatform(platforms[0]));
        assertTrue(instance.getTargetPlatforms().size() == (platforms.length - 1));
        assertFalse(instance.getTargetPlatforms().contains(platforms[0]));
        assertTrue(instance.removeTargetPlatform(platforms[3]));
        assertTrue(instance.getTargetPlatforms().size() == (platforms.length - 2));
        assertFalse(instance.getTargetPlatforms().contains(platforms[3]));
    }

    /**
     * Test of setMode method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testSetMode() {
        instance.setMode(TestSet.Mode.ALL_PATHS);
        assertEquals(TestSet.Mode.ALL_PATHS, instance.getMode());
        instance.setMode(TestSet.Mode.PRIORITY_BASED);
        assertEquals(TestSet.Mode.PRIORITY_BASED, instance.getMode());
        try {
            instance.setMode(null);
            fail("Did not throw exception as expected");
        } catch (IllegalArgumentException e) {
            assertEquals(TestSet.Mode.PRIORITY_BASED, instance.getMode());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * Test of createTestSetItem method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testCreateTestSetItem() {
        while (instance.getItemCount() > 0) {
            instance.removeItem(0);
        }
        assertTrue(instance.getItemCount() == 0);
        try {
            TestSetItem a = instance.createTestSetItem(null);
            fail("Did not throw exception as expected");
        } catch (KendoException e) {
            assertTrue(instance.isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        try {
            TestSetItem b = instance.createTestSetItem("illegal object");
            fail("Did not throw exception as expected");
        } catch (KendoException e) {
            assertTrue(instance.isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        TestSetItem item = instance.createTestSetItem(this.appModelItem);
        assertNotNull(item);
        assertTrue(instance.getItems().size() == 1);
        assertEquals(item, instance.getItem(0));
        assertEquals(this.appModelItem.getId(), item.getItemId());
    }

    /**
     * Test of addItem method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testAddItem() {
        while (instance.getItemCount() > 0) {
            instance.removeItem(0);
        }
        assertTrue(instance.getItemCount() == 0);
        TestSetItem item1 = new TestSetItemImpl(this.appModelItem);
        assertTrue(instance.addItem(item1));
        assertTrue(instance.getItemCount() == 1);
        assertTrue(instance.getItems().contains(item1));
        assertEquals(this.appModelItem.getId(), item1.getItemId());
        TestSetItem item2 = new TestSetItemImpl(this.useCaseItem);
        assertTrue(instance.addItem(item2));
        assertTrue(instance.getItemCount() == 2);
        assertTrue(instance.getItems().contains(item2));
        assertEquals((Long) this.useCaseItem.getId(), item2.getItemId());
        TestSetItem item3 = new TestSetItemImpl(this.useCasePathItem);
        instance.addItem(1, item3);
        assertTrue(instance.getItemCount() == 3);
        assertTrue(instance.getItems().contains(item2));
        assertEquals((Long) this.useCasePathItem.getId(), item3.getItemId());
        assertEquals(item1, instance.getItem(0));
        assertEquals(item2, instance.getItem(2));
        assertEquals(item3, instance.getItem(1));
        assertFalse(instance.addItem(null));
        assertTrue(instance.getItemCount() == 3);
    }

    /**
     * Test of getItems method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testGetItems() {
        while (instance.getItemCount() > 0) {
            instance.removeItem(0);
        }
        assertNotNull(instance.getItems());
        assertTrue(instance.getItems().size() == 0);
        instance.createTestSetItem(this.appModelItem);
        assertNotNull(instance.getItems());
        assertTrue(instance.getItems().size() == 1);
        instance.createTestSetItem(this.useCaseItem);
        assertNotNull(instance.getItems());
        assertTrue(instance.getItems().size() == 2);
        assertTrue(instance.getItems().size() == instance.getItemCount());
        instance.createTestSetItem(this.useCasePathItem);
        assertNotNull(instance.getItems());
        assertTrue(instance.getItems().size() == 3);
        assertTrue(instance.getItems().size() == instance.getItemCount());
    }

    /**
     * Test of getItem method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testGetItem() {
        for (int i = 0; i < instance.getItemCount(); i++) {
            TestSetItem item = instance.getItem(i);
            assertNotNull(item);
        }
        try {
            instance.getItem(-1);
            fail("Did not throw exception as expected");
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        try {
            instance.getItem(instance.getItemCount());
            fail("Did not throw exception as expected");
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * Test of removeItem method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testRemoveItem() {
        int itemCount = instance.getItemCount();
        TestSetItem removed = instance.removeItem(0);
        assertNotNull(removed);
        assertTrue(instance.getItemCount() == (itemCount - 1));
        assertFalse(instance.getItems().contains(removed));
        instance.addItem(removed);
        assertTrue(instance.getItems().contains(removed));
        assertTrue(instance.removeItem(removed));
        assertFalse(instance.getItems().contains(removed));
        itemCount = instance.getItemCount();
        try {
            TestSetItem item = instance.removeItem(-1);
            fail("Did not throw exception as expected");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(itemCount, instance.getItemCount());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        try {
            TestSetItem item = instance.removeItem(instance.getItemCount());
            fail("Did not throw exception as expected");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(itemCount, instance.getItemCount());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    /**
     * Test of isEmpty method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testIsEmpty() {
        while (instance.getItemCount() > 0) {
            instance.removeItem(0);
        }
        assertTrue(instance.isEmpty());
        assertNotNull(instance.createTestSetItem(appModelItem));
        assertFalse(instance.isEmpty());
    }

    /**
     * Test of update method, of class com.nokia.kendo.model.domain.testset.impl.TestSetImpl.
     */
    public void testUpdate() {
        TestObserver obs = new TestObserver(this.instance);
        ((Observable) instance).addObserver(obs);
        obs.reset();
        instance.resetModified();
        assertFalse(obs.validate());
        assertFalse(instance.isModified());
        instance.setName("setting the name triggers an update");
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        instance.setDescription("setting the description triggers an update");
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        instance.setMode(TestSet.Mode.ALL_PATHS);
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        String platform = "Platform1";
        instance.addTargetPlatform(platform);
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        instance.removeTargetPlatform(platform);
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        TestSetItem item = instance.createTestSetItem(this.appModelItem);
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        item.setRepeats(10);
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        assertTrue(instance.removeItem(item));
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        instance.addItem(item);
        assertTrue(obs.validate());
        assertTrue(instance.removeItem(item));
        assertTrue(instance.isModified());
        int index = instance.getItemCount() - 2;
        obs.reset();
        instance.resetModified();
        instance.addItem(index, item);
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
        obs.reset();
        instance.resetModified();
        assertNotNull(instance.removeItem(index));
        assertTrue(obs.validate());
        assertTrue(instance.isModified());
    }

    public void testClone() {
        TestSet clone = instance.clone();
        assertEquals(instance.getDescription(), clone.getDescription());
        assertEquals(instance.getItemCount(), clone.getItemCount());
        assertEquals(instance.getMode(), clone.getMode());
        assertEquals(instance.getName(), clone.getName());
        List<String> iTargets = instance.getTargetPlatforms();
        List<String> cTargets = clone.getTargetPlatforms();
        assertTrue(iTargets.equals(cTargets));
        assertNotNull(iTargets);
        assertNotNull(cTargets);
        assertEquals(iTargets.size(), cTargets.size());
    }

    /**
     * Simple observer for testing observer/observable functionality.
     */
    public static class TestObserver implements Observer {

        private boolean updated = false;

        private boolean correctObservable = false;

        private Object observable = null;

        /**
         * Creates a new instance of TestObserver
         * @param observable Object being observed by this observer
         */
        public TestObserver(Object observable) {
            this.observable = observable;
        }

        /**
         * Observer impl
         */
        public void update(Observable o, Object arg) {
            updated = true;
            correctObservable = (o == this.observable);
        }

        /**
         * Reset status to "not updated"
         */
        public void reset() {
            this.updated = false;
            this.correctObservable = false;
        }

        /**
         * Tells if the update() method has been invoked by the observed object.
         */
        public boolean validate() {
            return (updated && correctObservable);
        }
    }
}
