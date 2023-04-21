package org.antdepo.common;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.antdepo.tools.AntdepoTest;
import java.io.File;

public class TestModuleMgr extends AntdepoTest {

    ModuleMgr moduleMgr;

    public TestModuleMgr(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestModuleMgr.class);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    public void testConstruction() {
        moduleMgr = new ModuleMgr("test", new File(getModulesBase()), getFrameworkInstance().getDepotResourceMgr(), false, true);
        assertEquals("test", moduleMgr.getName());
    }

    public void testListChildNames() {
        moduleMgr = new ModuleMgr("test", new File(getModulesBase()), getFrameworkInstance().getDepotResourceMgr(), false, true);
        assertTrue(moduleMgr.listChildNames().size() > 0);
        assertTrue(moduleMgr.listChildNames().contains("Managed-Entity"));
    }

    public void testLoadChild() {
        moduleMgr = new ModuleMgr("test", new File(getModulesBase()), getFrameworkInstance().getDepotResourceMgr(), false, true);
        try {
            IFrameworkResource res = moduleMgr.loadChild("Managed-Entity");
            assertNotNull(res);
            assertTrue(res instanceof CmdModule);
            assertEquals("Managed-Entity", res.getName());
            CmdModule mod = (CmdModule) res;
            assertNull(mod.getDepot());
        } catch (FrameworkResourceException e) {
            fail("loadChild failed: " + e.getMessage());
        }
        try {
            IFrameworkResource res = moduleMgr.loadChild("DoesNotExist");
            assertNull(res);
        } catch (FrameworkResourceException e) {
            assertNotNull(e);
        }
        moduleMgr = new ModuleMgr("TestModuleMgr", new File(getModulesBase()), getFrameworkInstance().getDepotResourceMgr(), false, false);
        try {
            IFrameworkResource res = moduleMgr.loadChild("Managed-Entity");
            assertNotNull(res);
            assertTrue(res instanceof CmdModule);
            assertEquals("Managed-Entity", res.getName());
            CmdModule mod = (CmdModule) res;
            assertEquals("TestModuleMgr", mod.getDepot());
        } catch (FrameworkResourceException e) {
            fail("loadChild failed: " + e.getMessage());
        }
        try {
            IFrameworkResource res = moduleMgr.loadChild("DoesNotExist");
            assertNull(res);
        } catch (FrameworkResourceException e) {
            assertNotNull(e);
        }
    }

    public void testGetChild() {
        moduleMgr = new ModuleMgr("test", new File(getModulesBase()), getFrameworkInstance().getDepotResourceMgr(), false, true);
        try {
            IFrameworkResource res = moduleMgr.getChild("Managed-Entity");
            assertNotNull(res);
            assertTrue(res instanceof CmdModule);
            assertEquals("Managed-Entity", res.getName());
            CmdModule mod = (CmdModule) res;
            assertNull(mod.getDepot());
        } catch (FrameworkResourceException e) {
            fail("getChild failed: " + e.getMessage());
        }
        try {
            IFrameworkResource res = moduleMgr.getChild("DoesNotExist");
            fail("getChild should throw exception");
        } catch (FrameworkResourceException e) {
            assertNotNull(e);
        }
        moduleMgr = new ModuleMgr("TestModuleMgr", new File(getModulesBase()), getFrameworkInstance().getDepotResourceMgr(), false, false);
        try {
            IFrameworkResource res = moduleMgr.getChild("Managed-Entity");
            assertNotNull(res);
            assertTrue(res instanceof CmdModule);
            assertEquals("Managed-Entity", res.getName());
            CmdModule mod = (CmdModule) res;
            assertEquals("TestModuleMgr", mod.getDepot());
        } catch (FrameworkResourceException e) {
            fail("getChild failed: " + e.getMessage());
        }
        try {
            IFrameworkResource res = moduleMgr.getChild("DoesNotExist");
            fail("getChild should throw exception");
        } catch (FrameworkResourceException e) {
            assertNotNull(e);
        }
    }

    public void testCreateRemoveModule() {
        moduleMgr = new ModuleMgr("test", new File(getModulesBase()), getFrameworkInstance().getDepotResourceMgr(), false, true);
        final File moddir = new File(getModulesBase(), "TestModule");
        CmdModule mod = null;
        try {
            mod = moduleMgr.createCmdModule("TestModule");
            assertNotNull(mod);
        } catch (FrameworkResourceException e) {
            fail("addModule failed: " + e.getMessage());
        }
        assertEquals("TestModule", mod.getName());
        assertEquals(moduleMgr, mod.getParent());
        assertTrue(moddir.exists());
        assertEquals(moddir, mod.getBaseDir());
        moduleMgr.remove("TestModule");
        assertFalse(moduleMgr.listChildNames().contains("TestModule"));
        assertFalse(moduleMgr.getChildren().containsKey("TestModule"));
        assertFalse(moddir.exists());
    }
}
