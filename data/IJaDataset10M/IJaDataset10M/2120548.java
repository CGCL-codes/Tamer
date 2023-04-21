package org.hsqldb.persist;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HsqlPropertiesTest extends TestCase {

    public void testHsqlProperties() {
        HsqlProperties props1 = HsqlProperties.delimitedArgPairsToProps("-dbname.0", "=", ";", "server");
        HsqlProperties props2 = HsqlProperties.delimitedArgPairsToProps("filename.cvs;a=123 ;  b=\\delta ;c= another; derrorkey;", "=", ";", "textdb");
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(HsqlPropertiesTest.class);
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
