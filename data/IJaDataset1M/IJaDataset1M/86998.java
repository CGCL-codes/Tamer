package com.google.gwt.jsonp;

import com.google.gwt.jsonp.client.JsonpRequestTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Suite for JSONP tests.
 */
public class JsonpRequestSuite extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(JsonpRequestTest.class);
        return suite;
    }
}
