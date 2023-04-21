package org.vramework.commons.datatypes.test;

import junit.framework.Test;
import org.vramework.commons.junit.VTestSuite;

public class DataTypesTestSuite {

    public static Test suite() {
        VTestSuite suite = new VTestSuite(DataTypesTestSuite.class.getName());
        suite.addTestSuite(DataTypesUtilsTest.class);
        return suite;
    }
}
