package org.slasoi.gslam.monitoring.manager.tests;

import java.io.File;
import junit.framework.TestCase;
import org.slasoi.gslam.monitoring.manager.demos.features.SLACoverageFeatures;

/**
 * HelloWorld example for the MonitoringManager.
 * 
 * @see org.slasoi.gslam.monitoring.manager.tests.GenericMMTest
 **/
final class HelloWorld extends TestCase {

    /**
     * Constructor.
     * 
     * @param name
     *        name of testcase
     */
    private HelloWorld(final String name) {
        super(name);
    }

    /***
     * testHelloWorld.
     * 
     * <p>
     * Test method to run MonitoringManager with an example SLA and feature set.
     * 
     **/
    public void testHelloWorld() {
        String filepath = System.getenv("SLASOI_HOME") + File.separator + "..\\..\\generic-slamanager\\monitoring-manager\\src\\test\\java\\org\\slasoi\\gslam" + "\\monitoring\\manager\\demos\\xml\\TEST_SLA_1_KM.xml";
        GenericRun tester = new GenericRun();
        tester.setSLAPath(filepath);
        SLACoverageFeatures features = new SLACoverageFeatures();
        tester.setFeatures(features.buildTest());
        tester.setParentName("org.slasoi.gslam.monitoring.manager.tests.HelloWorld");
        tester.setPrintConfig(false);
        tester.run();
    }

    /**
     * @param args
     *            main arguments
     */
    public static void main(final String[] args) {
        HelloWorld hw = new HelloWorld("HelloWorld Test");
        hw.testHelloWorld();
    }
}
