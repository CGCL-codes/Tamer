package com.razie.pub.http.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * suite to run all pub.hframe tests
 * 
 * @author razvanc99
 */
public class SuitePubHttp extends TestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite(SuitePubHttp.class.getName());
        result.addTestSuite(TestLightServer.class);
        return result;
    }
}
