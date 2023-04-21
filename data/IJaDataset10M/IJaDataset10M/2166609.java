package com.gever.log;

import junit.framework.*;
import com.gever.util.log.Log;

public class TestLog extends TestCase {

    private Log log = null;

    public TestLog(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        log = new Log(false);
    }

    protected void tearDown() throws Exception {
        log = null;
        super.tearDown();
    }

    public void testLog() {
        boolean debug = false;
        log = new Log(debug);
    }

    public void testLog1() {
        boolean debug = false;
        Class clazz = this.getClass();
        log = new Log(debug, clazz);
    }

    public void testGetDebug() {
        boolean expectedReturn = false;
        boolean actualReturn = log.getDebug();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetInstance1() {
        Log expectedReturn = null;
        Log actualReturn = log.getInstance(Test.class);
    }

    public void testInit() {
        boolean debug = false;
        Class clazz = null;
        String apropertiesFile = null;
    }

    public void testInit1() {
        boolean debug = true;
        Log.setUselog4j(true);
        String apropertiesFile = "testDir/resource/debug.properties";
        log.init(debug, true, apropertiesFile);
        log.showLog("-----9fdsafkjdsalkf-----------");
    }

    public void testInitLogger() {
        boolean debug = true;
        Log.setUselog4j(true);
        String apropertiesFile = "testDir/resource/debug.properties";
        log.init(debug, true, apropertiesFile);
        log.initLogger(TestLog.class);
        log.showLog("-----9fdsafkjdsalkf-----dddddd------");
    }
}
