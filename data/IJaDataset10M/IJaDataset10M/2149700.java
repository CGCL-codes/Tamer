package org.antdepo.common;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.antdepo.cli.ADGenMain;
import org.antdepo.tools.AntdepoTest;
import org.antdepo.utils.FileUtils;
import java.io.File;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 22, 2005
 * Time: 12:18:00 PM
 */
public class TestDepotObject extends AntdepoTest {

    private File depotBasedir;

    private File depotsDir;

    private static final String ENTITY_DEPOT = "TestDepotObject";

    private static final String ENTITY_TYPE = "HahaType";

    private static final String ENTITY_NAME = "aHaha";

    public TestDepotObject(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestDepotObject.class);
    }

    protected void setUp() {
        super.setUp();
        depotsDir = new File(getDepotsBase());
        depotBasedir = new File(depotsDir, ENTITY_DEPOT);
        File t = new File(depotsDir, ENTITY_DEPOT + "/deployments/" + ENTITY_TYPE + "/" + ENTITY_NAME);
        if (t.exists()) {
            FileUtils.deleteDir(t);
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        File depotDir = new File(getDepotsBase(), ENTITY_DEPOT);
        FileUtils.deleteDir(depotDir);
    }

    public void testConstruction() {
        if (depotBasedir.exists()) depotBasedir.delete();
        final Depot depot = Depot.create(ENTITY_DEPOT, depotsDir, getFrameworkInstance().getDepotResourceMgr());
        final DepotType type = depot.createType(ENTITY_TYPE);
        final DepotObject object = type.createChild(ENTITY_NAME, false);
        assertNotNull("object was null after type.createChild called", object);
        assertEquals("object getName() did not equal " + ENTITY_NAME, object.getName(), ENTITY_NAME);
        assertEquals("object getType did not equal expected", object.getType(), type);
        assertEquals("object getParent did not equal expected type", object.getParent().getName(), ENTITY_TYPE);
        assertTrue("object's basedir didn't exist when it should: " + object.getBaseDir(), object.getBaseDir().exists());
        assertEquals("exepcted " + ENTITY_TYPE + " for getControllerName(), got: " + object.getControllerName(), ENTITY_TYPE, object.getControllerName());
        assertEquals("object basedir did not match expected", object.getBaseDir(), new File(type.getBaseDir(), object.getName()));
    }

    public void testExistsCmdHandler() {
        if (depotBasedir.exists()) depotBasedir.delete();
        final Depot depot = Depot.create(ENTITY_DEPOT, depotsDir, getFrameworkInstance().getDepotResourceMgr());
        final DepotType type = depot.createType("Managed-Entity");
        final DepotObject object = type.createChild(ENTITY_NAME, false);
        assertTrue("Properties did not exist", object.existsCmdHandler("Properties"));
    }

    public void testGetCmdModule() throws Throwable {
        Depot.createDepotStructure(depotBasedir, true);
        final File depotModsDir = new File(new File(new File(depotBasedir, "lib"), "ant"), "modules");
        final File templateBasedir = new File("src/templates/ant");
        final ADGenMain creator = ADGenMain.create(templateBasedir, depotModsDir);
        creator.executeAction("module", "add", new String[] { "-m", "NewModule1" });
        assertTrue(depotModsDir.exists());
        final Depot depot = getFrameworkInstance().getDepotResourceMgr().createDepot(ENTITY_DEPOT);
        final DepotType type = depot.createType("NewModule1");
        final DepotObject object = type.createObject(ENTITY_NAME, true);
        assertEquals("NewModule1", object.getControllerName());
        assertTrue("existsCmdModule failed when it should have found NewModule1", object.existsCmdModule());
        final CmdModule module = object.getCmdModule();
        assertEquals("NewModule1", module.getName());
    }
}
