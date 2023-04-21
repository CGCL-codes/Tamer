package com.telstra.ess.test.junit;

import org.apache.log4j.*;
import com.telstra.ess.*;
import com.telstra.ess.logging.*;
import junit.framework.TestCase;

/**
 * @author c957258
 */
public class EssLoggingTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EssLoggingTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Constructor for EssLoggingTest.
	 * @param arg0
	 */
    public EssLoggingTest(String arg0) {
        super(arg0);
    }

    public void testTwoLoggerClients() {
        EssComponent clientComp = new EssComponent("LoggingTest");
        EssComponent anotherClientComp = new EssComponent("AnotherLoggingTest");
        LoggingManager.getEssLoggerInstance(clientComp).warn("EssLogger Message - Hello -1");
        PropertyConfigurator.configure("log4j.properties");
        PropertyConfigurator.configure("log4j.properties");
        LogManager.getLogger("console").debug("First message for console");
        try {
            LogManager.getLogger("console").fatal("Hello 1");
            PropertyConfigurator.configure("log4j.properties");
            PropertyConfigurator.configure("log4j.properties");
            LoggingManager.getEssLoggerInstance(clientComp).fatal("EssLogger Message - Hello 0");
            LogManager.getLogger("console").debug("Hello 2");
            LogManager.getLogger("console").debug("Hello 3");
            PropertyConfigurator.configure("log4j.properties");
            LogManager.getLogger("console").fatal("Hello 4");
            LoggingManager.getEssLoggerInstance(clientComp).debug("EssLogger message - Hello 1");
            PropertyConfigurator.configure("log4j.properties");
            LogManager.getLogger("console").fatal("Hello 5");
        } catch (Exception e) {
            fail("Could not configure logger " + e.getMessage());
        }
    }
}
