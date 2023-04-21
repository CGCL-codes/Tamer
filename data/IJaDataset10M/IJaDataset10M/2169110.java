package net.sf.vorg.routecalculator.models;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.vorg.routecalculator.internals.TestGeoLocation;
import net.sf.vorg.routecalculator.internals.TestPolars;

public class RouteCalculatorTestSuite {

    public RouteCalculatorTestSuite() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sf.vorg.routecalculator.models");
        suite.addTestSuite(TestGeoLocation.class);
        suite.addTestSuite(TestPolars.class);
        return suite;
    }
}
