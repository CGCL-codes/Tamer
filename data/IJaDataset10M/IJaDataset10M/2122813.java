package org.antdepo.tasks.controller;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.antdepo.cli.ADDepotSetupMain;
import org.antdepo.cli.ADGenMain;
import org.antdepo.common.Depot;
import org.antdepo.common.DepotObject;
import org.antdepo.common.DepotType;
import org.antdepo.common.FrameworkResource;
import org.antdepo.common.context.DepotContext;
import org.antdepo.common.context.ObjectContext;
import org.antdepo.tools.AntdepoTest;
import org.antdepo.types.Command;
import org.antdepo.utils.FileUtils;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 26, 2005
 * Time: 8:56:01 PM
 */
public class TestListDispatchAction extends AntdepoTest {

    private static final String ENTITY_DEPOT = "TestListDispatchAction";

    private static final String ENTITY_TYPE1 = "Managed-Entity";

    private static final String ENTITY_TYPE2 = "NewModuleType";

    private static final String ENTITY_NAME = "aHaha";

    private static final String COMMAND_NAME = "Heehee";

    private static final int AOME_CMD_CNT = 9;

    public TestListDispatchAction(String name) throws Throwable {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        createDepot();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        FileUtils.deleteDir(new File(getDepotsBase(), ENTITY_DEPOT));
    }

    public void createDepot() {
        final File depotDir = new File(getDepotsBase(), ENTITY_DEPOT);
        try {
            Depot.createDepotStructure(depotDir, true);
            final ADDepotSetupMain setup = new ADDepotSetupMain();
            final String[] args = new String[] { "-p", ENTITY_DEPOT, "-b", "src/ant/controllers/ad/depotsetupCmd.xml", "-o", "-v" };
            setup.parseArgs(args);
            setup.executeAction();
            final File templateBasedir = new File("src/templates/ant");
            final File modulesDir = new File(new File(new File(depotDir, "lib"), "ant"), "modules");
            assertTrue("depot modules lib dir does not exist", modulesDir.exists());
            final ADGenMain creator = ADGenMain.create(templateBasedir, modulesDir);
            creator.executeAction("module", "add", new String[] { "-m", ENTITY_TYPE2 });
            assertTrue("module dir not created: " + new File(modulesDir, ENTITY_TYPE2).getAbsolutePath(), new File(modulesDir, ENTITY_TYPE2).exists());
            final Depot depot = getFrameworkInstance().getDepotResourceMgr().createDepot(ENTITY_DEPOT);
            final DepotType type = depot.createType(ENTITY_TYPE2);
            final DepotObject obj = type.createObject(ENTITY_NAME, true);
            assertEquals(modulesDir.getAbsolutePath(), depot.getModulesDir().getAbsolutePath());
            assertEquals(ENTITY_NAME, obj.getName());
            final DepotType mType = depot.createType(ENTITY_TYPE1);
            final DepotObject mObj = mType.createObject(ENTITY_NAME, true);
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestListDispatchAction.class);
    }

    private boolean hasResourceNamed(final Collection c, final String name) {
        for (Iterator iter = c.iterator(); iter.hasNext(); ) {
            final FrameworkResource r = (FrameworkResource) iter.next();
            if (name.equals(r.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasStringNamed(final Collection c, final String name) {
        for (Iterator iter = c.iterator(); iter.hasNext(); ) {
            final String s = (String) iter.next();
            if (name.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void testConstruction() {
        final DispatchAction action = new ListDispatchAction(ObjectContext.create(null, null, DepotContext.create(null)), getFrameworkInstance().getDepotResourceMgr());
        assertNotNull("action was null after construction", action);
    }

    public void testListEmptyContext() {
        final ListDispatchAction action = new ListDispatchAction(ObjectContext.create(null, null, DepotContext.create(null)), getFrameworkInstance().getDepotResourceMgr());
        final Collection c = action.listEmptyContext();
        assertTrue(c.size() > 0);
        assertTrue(hasStringNamed(c, ENTITY_DEPOT));
        assertFalse(hasStringNamed(c, "Bogus"));
    }

    public void testListDepotContext() {
        final ListDispatchAction action = new ListDispatchAction(ObjectContext.create(null, null, DepotContext.create(ENTITY_DEPOT)), getFrameworkInstance().getDepotResourceMgr());
        final Collection c = action.listDepotContext();
        assertTrue(c.size() == 2);
        assertTrue(hasStringNamed(c, ENTITY_TYPE1));
        assertTrue(hasStringNamed(c, ENTITY_TYPE2));
        assertFalse(hasStringNamed(c, "Bogus"));
    }

    public void testListTypeContext() {
        final ListDispatchAction action = new ListDispatchAction(ObjectContext.create(null, ENTITY_TYPE1, DepotContext.create(ENTITY_DEPOT)), getFrameworkInstance().getDepotResourceMgr());
        final Collection c = action.listTypeContext();
        assertEquals(1, c.size());
        assertTrue(hasStringNamed(c, ENTITY_NAME));
        assertFalse(hasStringNamed(c, "Bogus"));
    }

    public void testListObjectContext() {
        final ListDispatchAction action = new ListDispatchAction(ObjectContext.create(ENTITY_NAME, ENTITY_TYPE1, DepotContext.create(ENTITY_DEPOT)), getFrameworkInstance().getDepotResourceMgr());
        final Collection c = action.listObjectContext();
        assertEquals("unexpected number of commands (" + c + ")", AOME_CMD_CNT, c.size());
        assertTrue(hasStringNamed(c, "Get-Properties"));
        assertTrue(hasStringNamed(c, "Get-Supers"));
        assertTrue(hasStringNamed(c, "Install-Module"));
        assertTrue(hasStringNamed(c, "Install"));
        assertTrue(hasStringNamed(c, "Properties"));
        assertTrue(hasStringNamed(c, "Purge"));
        assertTrue(hasStringNamed(c, "Remove-Properties"));
        assertTrue(hasStringNamed(c, "Update-Module"));
        assertTrue(hasStringNamed(c, "Update-Properties"));
        assertFalse(hasStringNamed(c, "Bogus"));
    }

    public void testListModuleContext() {
        ListDispatchAction action = new ListDispatchAction(Command.create(null, ENTITY_TYPE1, ENTITY_DEPOT), getFrameworkInstance().getDepotResourceMgr());
        Collection c = action.listModuleContext();
        assertEquals("unexepcted number of commands(" + c + ")", AOME_CMD_CNT, c.size());
        action = new ListDispatchAction(Command.create(null, ENTITY_TYPE1, ENTITY_DEPOT), getFrameworkInstance().getDepotResourceMgr());
        c = action.listModuleContext();
        assertEquals("unexepcted number of commands(" + c + ")", AOME_CMD_CNT, c.size());
        action = new ListDispatchAction(Command.create(null, ENTITY_TYPE1, null), getFrameworkInstance().getDepotResourceMgr());
        c = action.listModuleContext();
        assertEquals("unexepcted number of commands(" + c + ")", AOME_CMD_CNT, c.size());
    }

    public void testObjContextPerform() {
        DispatchAction action = new ListDispatchAction(ObjectContext.create(null, null, DepotContext.create(null)), getFrameworkInstance().getDepotResourceMgr());
        ActionResult result = action.perform();
        assertNotNull("null action result", result);
        assertTrue("action result was unsuccesful", result.isSuccessful());
        String[] output = result.getOutputString().split("\\s");
        assertTrue("empty context listing did not include" + ". It included: " + result.getOutputString(), Arrays.binarySearch(output, ENTITY_DEPOT) >= 0);
        action = new ListDispatchAction(ObjectContext.create(null, null, DepotContext.create(ENTITY_DEPOT)), getFrameworkInstance().getDepotResourceMgr());
        result = action.perform();
        output = result.getOutputString().split("\\s");
        assertNotNull("null action result", result);
        assertTrue("action result was unsuccesful", result.isSuccessful());
        assertTrue("entity type listed did not include: " + ENTITY_TYPE1 + ". It included: " + result.getOutputString(), Arrays.binarySearch(output, ENTITY_TYPE1) >= 0);
        action = new ListDispatchAction(ObjectContext.create(null, ENTITY_TYPE1, DepotContext.create(ENTITY_DEPOT)), getFrameworkInstance().getDepotResourceMgr());
        result = action.perform();
        output = result.getOutputString().split("\\s");
        assertNotNull("null action result", result);
        assertTrue("action result was unsuccesful", result.isSuccessful());
        assertTrue("entity type listed did not include: " + ENTITY_NAME + ". It included: " + result.getOutputString(), Arrays.binarySearch(output, ENTITY_NAME) >= 0);
    }
}
