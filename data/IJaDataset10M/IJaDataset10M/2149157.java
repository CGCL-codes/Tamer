package ch.netcetera.wikisearch.lookup;

import junit.framework.Test;
import junit.framework.TestSuite;

public class LookupSuite {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("ch.netcetera.wikisearch.lookup");
        suite.addTest(DefaultFilterMatcherTest.suite());
        suite.addTest(DefaultLabelProviderTest.suite());
        suite.addTest(FileReaderDataSourceTest.suite());
        suite.addTest(FilteredListTest.suite());
        suite.addTest(LookupBaseTest.suite());
        return suite;
    }
}
