package cn.com.once.deploytool.deployer.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import cn.com.once.deploytool.model.unit.DeployUnit;
import cn.com.once.deploytool.model.unit.UnitFactory;

public class OnceBPELDeployerTest {

    private OnceBPELDeployer deployer;

    private DeployUnit du;

    @Before
    public void setUp() throws Exception {
        deployer = new OnceBPELDeployer("http://localhost:8080/OnceBpel/");
        du = UnitFactory.eINSTANCE.createDeployUnit();
    }

    @Test
    @Ignore
    public void testDeploy() {
        du.setLocalTempUrl("D:\\ATM.zip");
        du.setUrl("http://test/ATP.zip");
        du.setName("ATM");
        deployer.deploy(du);
    }

    @Test
    @Ignore
    public void testIsDeployedDeployUnit() {
        du.setLocalTempUrl("D:\\ATM.zip");
        du.setUrl("http://test/ATP.zip");
        du.setName("ATM");
        boolean b = deployer.isDeployed(du);
        System.out.println(b);
    }

    @Test
    @Ignore
    public void testRedeploy() {
        du.setLocalTempUrl("D:\\ATM.zip");
        du.setUrl("http://test/ATP.zip");
        du.setName("ATM");
        if (deployer.isDeployed(du)) {
            deployer.redeploy(du);
        }
    }

    @Test
    @Ignore
    public void testUndeploy() {
        du.setLocalTempUrl("D:\\ATM.zip");
        du.setUrl("http://test/ATP.zip");
        du.setName("ATM");
        deployer.undeploy(du);
    }
}
