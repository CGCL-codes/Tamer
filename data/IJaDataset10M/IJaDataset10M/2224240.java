package com.jcrawler.test;

import com.jcrawler.util.TestHttpUtil;
import com.jcrawler.util.TestBoundedFIFO;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.jcrawler.TestUrlFetcher;
import com.jcrawler.util.TestCrawler;

public class AllTests extends TestCase {

    public AllTests(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(TestHttpUtil.class);
        suite.addTestSuite(TestBoundedFIFO.class);
        suite.addTestSuite(TestUrlFetcher.class);
        suite.addTestSuite(TestCrawler.class);
        return suite;
    }
}
