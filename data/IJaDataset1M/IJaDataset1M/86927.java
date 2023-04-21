package com.controltier.ctl.tasks;

import com.controltier.ctl.tools.BuildFileTest;
import com.controltier.ctl.utils.FileUtils;
import com.controltier.ctl.types.*;
import com.controltier.ctl.types.dispatch.DispatchContext;
import com.controltier.ctl.types.dispatch.IDispatchContext;
import com.controltier.ctl.types.dispatch.IDispatchContextCollection;
import com.controltier.ctl.types.controller.Arg;
import com.controltier.ctl.common.Depot;
import com.controltier.ctl.common.DepotObject;
import com.controltier.ctl.common.Deployments;
import com.controltier.ctl.tasks.controller.Controller;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import org.apache.tools.ant.BuildException;

public class TestDispatchCommand extends BuildFileTest {

    protected void setUp() {
        configureProject("src/test/com/controltier/ctl/tasks/dispatch-command.xml");
    }

    protected void tearDown() throws Exception {
        FileUtils.deleteDir(new File(getDepotsBase(), "TestDispatchCommand"));
    }

    public TestDispatchCommand(String name) {
        super(name);
    }

    public void testCollectionIterator() {
        DispatchCommand dc = new DispatchCommand();
        DispatchCommand.Contexts contexts = dc.createContexts();
        Context ctx1 = contexts.createContext("aTypeA", "TypeA", "TestDispatchCommand");
        Context ctx2 = contexts.createContext("aTypeB", "TypeB", "TestDispatchCommand");
        IDispatchContextCollection coll = contexts.select();
        assertTrue("IDispatchContextCollection did not return an Iterator", coll.iterator() instanceof Iterator);
        int i = 0;
        for (Iterator iter = coll.iterator(); iter.hasNext(); ) {
            Object o = iter.next();
            assertTrue("Expected an instance of IDispatchContext", o instanceof IDispatchContext);
            if (i == 0) {
                assertEquals("incorrect DispatchContext entityName", "aTypeA", ((IDispatchContext) o).getEntityName());
                assertEquals("incorrect DispatchContext entityType", "TypeA", ((IDispatchContext) o).getEntityType());
                assertEquals("incorrect DispatchContext depot", "TestDispatchCommand", ((IDispatchContext) o).getDepot());
            }
            if (i == 1) {
                assertEquals("incorrect DispatchContext entityName", "aTypeB", ((IDispatchContext) o).getEntityName());
                assertEquals("incorrect DispatchContext entityType", "TypeB", ((IDispatchContext) o).getEntityType());
                assertEquals("incorrect DispatchContext depot", "TestDispatchCommand", ((IDispatchContext) o).getDepot());
            }
            i++;
        }
        assertEquals("Expected to iterate over 2 objects", i, 2);
    }

    public void testCreateControllerTask() {
        DispatchCommand dc = new DispatchCommand();
        DispatchCommand.Contexts contexts = dc.createContexts();
        Context ctx1 = contexts.createContext("aTypeA", "TypeA", "TestDispatchCommand");
        Object task = dc.createController(new DispatchContext(ctx1), Command.create("Info", "Module"), Arg.create("-bar"));
        assertTrue(task instanceof Controller);
    }

    public void testBadSyntax() {
        expectBuildException("testBadSyntax1");
        expectBuildException("testBadSyntax2");
        expectBuildException("testBadSyntax3");
        expectBuildException("testBadSyntax4");
    }

    public void testContextTags() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testContextTags");
        expectErrorOutput("testContextTags", "OK");
    }

    public void testBogusContextCommand() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        expectBuildException("testBogusContextCommand");
    }

    public void testBogusContexts() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        expectBuildException("testBogusContexts");
    }

    public void testSelectObjects() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectObjects");
        expectErrorOutput("testSelectObjects", "OK");
    }

    public void testSelectObjects2threads() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectObjects2threads");
        expectErrorOutput("testSelectObjects2threads", "OK");
    }

    public void testSelectObjectsBogusCommand() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectObjectsBogusCommand");
        expectErrorOutput("testSelectObjectsBogusCommand", "OK");
    }

    public void testSelectObjectsBogusArgline() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectObjectsBogusArgline");
    }

    public void testSelectorNoMatches() throws IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        expectBuildException("testSelectObjectsNoMatches");
        expectBuildException("testSelectDependenciesNoMatches");
        expectBuildException("testSelectDeploymentsNoMatches");
    }

    public void testSelectDependencies() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectDependencies");
        expectErrorOutput("testSelectDependencies", "OK");
    }

    public void testSelectDependenciesUsingProximity() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectDependenciesUsingProximity");
        expectErrorOutput("testSelectDependenciesUsingProximity", "OK");
    }

    public void testSelectDependenciesUsingProximityAndOrrdReltype() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectDependenciesUsingProximityAndOrrdReltype");
        expectErrorOutput("testSelectDependenciesUsingProximityAndOrrdReltype", "OK");
    }

    public void testSelectDependenciesRelationtypeFiltered() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        setOutputResults("testSelectDependenciesRelationtypeFiltered");
        expectErrorOutput("testSelectDependenciesRelationtypeFiltered", "OK");
    }

    public void testSelectDeployments() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        File propFile = new File(getFrameworkInstance().getDepotResourceMgr().getDepot("TestDispatchCommand").getEtcDir(), Deployments.BASE_NAME);
        propFile.deleteOnExit();
        Properties p = new Properties();
        p.setProperty("object-deployment.TestDispatchCommand.Managed-Entity.me1", getFrameworkInstance().getFrameworkNodeHostname());
        p.setProperty("object-deployment.TestDispatchCommand.Managed-Entity.me2", getFrameworkInstance().getFrameworkNodeHostname());
        p.setProperty("object-deployment.TestDispatchCommand.Bogus.noexist", "hoho");
        try {
            p.store(new FileOutputStream(propFile), "deployment info");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        setOutputResults("testSelectDeployments");
    }

    public void testSelectDeployments2threads() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        File propFile = new File(getFrameworkInstance().getDepotResourceMgr().getDepot("TestDispatchCommand").getEtcDir(), Deployments.BASE_NAME);
        propFile.deleteOnExit();
        Properties p = new Properties();
        p.setProperty("object-deployment.TestDispatchCommand.Managed-Entity.me1", getFrameworkInstance().getFrameworkNodeHostname());
        p.setProperty("object-deployment.TestDispatchCommand.Managed-Entity.me2", getFrameworkInstance().getFrameworkNodeHostname());
        p.setProperty("object-deployment.TestDispatchCommand.Bogus.noexist", "hoho");
        try {
            p.store(new FileOutputStream(propFile), "deployment info");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        setOutputResults("testSelectDeployments2threads");
    }

    public void testSelectDeploymentsBogusCommand() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        File propFile = new File(getFrameworkInstance().getDepotResourceMgr().getDepot("TestDispatchCommand").getEtcDir(), Deployments.BASE_NAME);
        propFile.deleteOnExit();
        Properties p = new Properties();
        p.setProperty("object-deployment.TestDispatchCommand.Managed-Entity.me1", getFrameworkInstance().getFrameworkNodeHostname());
        p.setProperty("object-deployment.TestDispatchCommand.Managed-Entity.me2", getFrameworkInstance().getFrameworkNodeHostname());
        p.setProperty("object-deployment.TestDispatchCommand.Bogus.noexist", "hoho");
        try {
            p.store(new FileOutputStream(propFile), "deployment info");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        setOutputResults("testSelectDeploymentsBogusCommand");
    }

    public void testSelectObjectsWithPreDispatch() throws BuildException, IOException {
        createPopulatedMockDepot("TestDispatchCommand", "Managed-Entity");
        File propFile = new File(getFrameworkInstance().getDepotResourceMgr().getDepot("TestDispatchCommand").getEtcDir(), Deployments.BASE_NAME);
        propFile.deleteOnExit();
        Properties p = new Properties();
        p.setProperty("object-deployment.TestDispatchCommand.Deployment.d1", getFrameworkInstance().getFrameworkNodeHostname());
        try {
            p.store(new FileOutputStream(propFile), "deployment info");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        setOutputResults("testSelectObjectsWithPreDispatch");
        expectErrorOutput("testSelectObjectsWithPreDispatch", "OK");
    }

    private void createPopulatedMockDepot(final String depotName, final String objectType) throws IOException {
        Depot depot = generateMockDepot(depotName);
        depot.createType("Managed-Entity");
        DepotObject me1 = depot.getDepotType(objectType).createChild("me1", true);
        assertTrue("failed creating test directory", new File(me1.getBaseDir(), "var").mkdirs());
        assertTrue(me1.getPropertyFile().createNewFile());
        DepotObject me2 = depot.getDepotType(objectType).createChild("me2", true);
        assertTrue("failed creating test directory", new File(me2.getBaseDir(), "var").mkdirs());
        assertTrue(me2.getPropertyFile().createNewFile());
    }
}
