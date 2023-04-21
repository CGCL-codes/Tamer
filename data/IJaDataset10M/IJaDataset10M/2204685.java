package com.google.gwt.sample.stockwatcher.client;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * 
 * @author ggregory
 * 
 */
public class AllTests extends GWTTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("com.google.gwt.sample.stockwatcher.client.AllTests");
        suite.addTestSuite(StockPriceTest.class);
        suite.addTestSuite(StockWatcherTest.class);
        return suite;
    }
}
