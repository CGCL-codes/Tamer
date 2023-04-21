package org.argouml.moduleloader;

import java.util.Collection;
import java.util.List;
import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

/**
 * Tests for the new module loader.
 */
public class GUITestModuleLoader2 extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }

    public void testGetInstance() {
        assertNotNull(ModuleLoader2.getInstance());
    }

    public void testDoLoad() {
        ModuleLoader2.doLoad(false);
        ModuleLoader2.doLoad(true);
    }

    public void testIsEnabled() {
        assertFalse(ModuleLoader2.isEnabled("some-nonexisting-module"));
    }

    public void testAllModules() {
        Collection<String> all = ModuleLoader2.allModules();
        assertNotNull(all);
        for (String module : all) {
            assertNotNull(module);
        }
    }

    public void testIsSelected() {
        assertFalse(ModuleLoader2.isSelected("some-module-that-do-not-exist"));
    }

    public void testSetSelected() {
        ModuleLoader2.setSelected("nonexisting-module", true);
        ModuleLoader2.setSelected("nonexisting-module", false);
    }

    public void testGetDescription() {
        try {
            ModuleLoader2.getDescription("nonexisting-module");
            fail("No exception was thrown.");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGetExtensionLocations() {
        List<String> extensionLocations = ModuleLoader2.getInstance().getExtensionLocations();
        assertNotNull(extensionLocations);
        for (String extensionLocation : extensionLocations) {
            assertNotNull(extensionLocation);
        }
    }

    public void testAddClassNegative() {
        try {
            ModuleLoader2.addClass("a.class.that.do.not.exist");
            fail("No exception was thrown!");
        } catch (ClassNotFoundException e) {
        }
        try {
            ModuleLoader2.addClass(this.getClass().getName());
        } catch (ClassNotFoundException e) {
            fail("Exception thrown unexpectedly.");
        }
    }

    private static Object created;

    public static void interfaceCreatedForTesting(Object testing1) {
        created = testing1;
    }

    public void testAddClass() {
        created = null;
        try {
            ModuleLoader2.addClass(ModuleInterfaceForTesting1.class.getName());
        } catch (ClassNotFoundException e) {
            fail("Exception thrown unexpectedly.");
        }
        assertNotNull(created);
        ModuleInterfaceForTesting1.setReadyToBeEnabled(false);
        ModuleLoader2.setSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME, false);
        assertNotNull(ModuleLoader2.getDescription(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertFalse(ModuleLoader2.isEnabled(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertFalse(ModuleLoader2.isSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        ModuleLoader2.doLoad(false);
        assertFalse(ModuleLoader2.isEnabled(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertFalse(ModuleLoader2.isSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        ModuleLoader2.setSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME, true);
        assertFalse(ModuleLoader2.isEnabled(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertTrue(ModuleLoader2.isSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        ModuleLoader2.setSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME, false);
        assertFalse(ModuleLoader2.isEnabled(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertFalse(ModuleLoader2.isSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        ModuleLoader2.setSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME, true);
        assertFalse(ModuleLoader2.isEnabled(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertTrue(ModuleLoader2.isSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        ModuleLoader2.doLoad(false);
        assertFalse(ModuleLoader2.isEnabled(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertTrue(ModuleLoader2.isSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        ModuleInterfaceForTesting1.setReadyToBeEnabled(true);
        ModuleLoader2.doLoad(false);
        assertTrue(ModuleLoader2.isEnabled(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
        assertTrue(ModuleLoader2.isSelected(ModuleInterfaceForTesting1.TEST_MODULE_NAME));
    }
}
