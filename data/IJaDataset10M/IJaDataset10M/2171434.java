package org.ztemplates.test.actions.urlhandler.constructor;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.ztemplates.actions.urlhandler.ZIUrlHandler;
import org.ztemplates.test.ZTestUrlHandlerFactory;

public class ConstructorTest extends TestCase {

    private static Logger log = Logger.getLogger(ConstructorTest.class);

    ZIUrlHandler up;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        up = ZTestUrlHandlerFactory.create(ConstructorTest.class.getPackage().getName(), ZTestUrlHandlerFactory.defaultSecurityService, "utf-8");
    }

    @Override
    protected void tearDown() throws Exception {
        up = null;
        super.tearDown();
    }

    public void testConstructor() throws Exception {
        ConstructorHandler obj = (ConstructorHandler) up.process("/test-constructor");
        Assert.assertNotNull(obj);
    }
}
