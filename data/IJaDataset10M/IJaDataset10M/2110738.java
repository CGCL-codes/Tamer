package com.apelon.apelonserver.server;

import com.apelon.common.log4j.LogConfigLoader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Hashtable;

public class TestApelonServerPropsHandler extends TestCase {

    private String log_config_file = "apelonserverlog.xml";

    ApelonServerPropsHandler handler;

    private LogConfigLoader logCfgLoader() {
        return new LogConfigLoader(".", log_config_file, ApelonServer.class);
    }

    public static Test suite() {
        return new TestSuite(TestApelonServerPropsHandler.class);
    }

    public TestApelonServerPropsHandler(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        logCfgLoader().loadDefault();
    }

    public void tearDown() throws Exception {
    }

    public void testApelonServerProps() {
        try {
            handler = new ApelonServerPropsHandler("apelonserverprops.xml");
            String[] ps = handler.getServerProps();
            assertTrue(ps.length == 8);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testApelonQsProps() {
        try {
            handler = new ApelonServerPropsHandler("apelonserverprops.xml");
            Hashtable ht = handler.getQueryServerProps();
            assertTrue(ht.size() == 3);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testDefaultConn() {
        try {
            handler = new ApelonServerPropsHandler("TestASPdefaultConn.xml");
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testBadQueryServer() {
        try {
            handler = new ApelonServerPropsHandler("TestASPbadQueryServer.xml");
        } catch (Exception e) {
            assertTrue(e.getMessage().equalsIgnoreCase("No connection for QueryServer"));
        }
    }
}
