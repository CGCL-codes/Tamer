package com.meterware.website;

import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.util.ArrayList;

/**
 * @author <a href="mailto:russgold@gmail.com">Russell Gold</a>
 */
public class WebSiteSuite {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static TestSuite suite() {
        TestSuite aSuite = new TestSuite();
        aSuite.addTest(PageFragmentTest.suite());
        aSuite.addTest(WebPageTest.suite());
        return aSuite;
    }
}
