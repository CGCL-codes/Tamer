package it.aco.mandragora.bd.impl.proxy;

import it.aco.mandragora.bd.BD;
import it.aco.mandragora.bd.BDTest;
import javax.annotation.Resource;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/AOPProxyBDHandlerTest-context.xml")
public class AOPProxyBDHandlerTest extends BDTest {

    protected BD bD;

    public BD getBD() {
        return bD;
    }

    @Resource(name = "aopProxyManagerBD")
    public void setBD(BD bD) {
        this.bD = bD;
    }

    protected String getThisClassName() {
        return AOPProxyBDHandlerTest.class.getName();
    }

    protected org.apache.log4j.Category getLog() {
        return org.apache.log4j.Logger.getLogger(AOPProxyBDHandlerTest.class.getName());
    }

    private org.apache.log4j.Category log = getLog();
}
