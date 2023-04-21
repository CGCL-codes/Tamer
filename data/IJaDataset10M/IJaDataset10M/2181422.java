package org.jsemantic.services.logging.factory;

import org.apache.commons.logging.Log;
import org.jsemantic.services.logging.exception.LoggingServiceException;
import org.jsemantic.services.logging.factory.LoggingFactory;
import junit.framework.TestCase;

public class LoggingFactoryTest extends TestCase {

    private LoggingFactory factory = null;

    protected void setUp() throws Exception {
        super.setUp();
        this.factory = new LoggingFactory();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        this.factory.dispose();
    }

    public void testLog4j() {
        this.factory.setLogType(LoggingFactory.LOG_PROVIDER_LOG4J);
        Log log4j = this.factory.getLogInstance();
        super.assertNotNull(log4j);
    }

    public void testSimpleLog() {
        Log log4j = this.factory.getLogInstance();
        super.assertNotNull(log4j);
    }

    public void testError() {
        Log log4j = null;
        try {
            this.factory.setLogType("org.common");
            log4j = this.factory.getLogInstance();
            log4j.getClass();
        } catch (LoggingServiceException e) {
            super.assertTrue(true);
        }
    }
}
