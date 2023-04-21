package it.aco.mandragora.bd.impl.pojo;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import it.aco.mandragora.bd.BD;
import it.aco.mandragora.bd.BDTest;
import it.aco.mandragora.bd.concreteFactory.ProxyBDHandlerFactory;
import it.aco.mandragora.common.ServiceLocator;
import it.aco.mandragora.vo.*;
import java.util.*;
import static junit.framework.Assert.assertNull;

public class UserExampleForProxyServiceFacadeBDTest extends BDTest {

    protected BD getBD() throws Exception {
        return ServiceLocator.getInstance().getManagerBD("UserExampleForProxyServiceFacadeBDTest.BDFactoryClass", "UserExampleForProxyServiceFacadeBDTest.BDClass");
    }

    protected String getThisClassName() {
        return UserExampleForProxyServiceFacadeBDTest.class.getName();
    }

    protected org.apache.log4j.Category getLog() {
        return org.apache.log4j.Logger.getLogger(UserExampleForProxyServiceFacadeBDTest.class.getName());
    }

    private org.apache.log4j.Category log = getLog();
}
