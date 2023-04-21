package org.exist.xquery.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.exist.storage.DBBroker;

public class RemoteTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.exist.xquery.test");
        XPathQueryTest.setURI("xmldb:exist://localhost:8088/xmlrpc" + DBBroker.ROOT_COLLECTION);
        suite.addTestSuite(XPathQueryTest.class);
        return suite;
    }
}
