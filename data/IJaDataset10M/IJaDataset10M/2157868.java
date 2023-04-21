package com.manning.junitbook.ch16.test;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.osgi.framework.BundleContext;

/**
 * A test-suite for the client application.
 * 
 * @version $Id: TestClientActivatorSuite.java 534 2009-08-17 09:04:59Z paranoid12 $
 */
public class TestClientActivatorSuite {

    public static Test suite(BundleContext bc) {
        OSGiTestSuite suite = new OSGiTestSuite("Client activator tests", bc);
        suite.addTestSuite(TestClientActivator.class);
        return suite;
    }
}
