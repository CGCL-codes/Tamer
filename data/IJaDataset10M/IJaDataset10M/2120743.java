package com.controltier.ctl.tasks;

import com.controltier.ctl.common.Deployments;
import com.controltier.ctl.common.Depot;
import com.controltier.ctl.tools.BuildFileTest;
import com.controltier.ctl.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * TestDeploymentsLookupObject tests the deployments-lookup-object ant task.
 *
 * @author Alex Honor <a href="mailto:alex@controltier.com">alex@controltier.com</a>
 * @version $Revision: 1081 $
 */
public class TestDeploymentsLookupObject extends BuildFileTest {

    static final String ENTITY_DEPOT = "TestDeploymentsLookupObject";

    static final String NODE_LIST1 = "bozo,hoho";

    static final String NODE_LIST2 = "bozo";

    static final String NODE_LIST3 = "hoho";

    private File propFile;

    public TestDeploymentsLookupObject(String name) throws IOException {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        final Depot depot = getFrameworkInstance().getDepotResourceMgr().createDepot(ENTITY_DEPOT);
        propFile = new File(depot.getEtcDir(), Deployments.BASE_NAME);
        propFile.deleteOnExit();
        final Properties p = new Properties();
        p.setProperty("module-deployment.P.moduleY", NODE_LIST1);
        p.setProperty("module-deployment.P.moduleX", NODE_LIST2);
        p.setProperty("object-deployment.P.X.x", NODE_LIST3);
        p.setProperty("object-deployment.P.X.y", NODE_LIST1);
        p.setProperty("object-deployment.P-roj.Type-X.y.1.2.3", NODE_LIST3);
        try {
            p.store(new FileOutputStream(propFile), "deployment info");
        } catch (IOException e) {
            fail(e.getMessage());
        }
        configureProject("src/test/com/controltier/ctl/tasks/deployments-lookup-object.xml");
        getProject().setProperty("depot.deployments.file", propFile.getAbsolutePath());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        FileUtils.deleteDir(new File(getDepotsBase(), ENTITY_DEPOT));
    }

    public void test1() {
        setOutputResults("test1");
        expectInfoOutput("test1", NODE_LIST1);
    }

    public void test2() {
        setOutputResults("test2");
        expectInfoOutput("test2", NODE_LIST3);
    }

    public void test3() {
        setOutputResults("test3");
        expectInfoOutput("test3", "${result}");
    }

    public void test4() {
        setOutputResults("test4");
        expectInfoOutput("test4", "SUCCESS");
    }

    public void test5() {
        setOutputResults("test5");
        expectInfoOutput("test5", "bozo:hoho");
    }
}
