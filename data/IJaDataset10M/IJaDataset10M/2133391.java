package org.jsemantic.support.spring;

import org.jsemantic.core.context.external.ExternalContext;
import org.jsemantic.support.Config;
import org.jsemantic.support.annotations.Service;
import org.jservicerules.support.spring.SpringExternalContextImpl;
import junit.framework.TestCase;

public class TestSpringExternalContext extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test() {
        ExternalContext externalContext = new SpringExternalContextImpl(Config.ctxFile);
        super.assertNotNull(externalContext);
        super.assertNotNull(externalContext.getCtx());
        Service service = (Service) externalContext.getObject("testService");
        super.assertNotNull(service);
    }

    public void testCreationError() {
        ExternalContext externalContext;
        try {
            externalContext = new SpringExternalContextImpl(Config.errorRuleFile);
        } catch (RuntimeException e) {
            super.assertTrue(true);
            return;
        }
        super.fail();
    }

    public void testObjectNotFound() {
        ExternalContext externalContext = new SpringExternalContextImpl(Config.ctxFile);
        super.assertNotNull(externalContext);
        super.assertNotNull(externalContext.getCtx());
        Service service;
        try {
            service = (Service) externalContext.getObject("service");
        } catch (RuntimeException e) {
            super.assertTrue(true);
            return;
        }
        super.fail();
    }
}
