package org.acs.elated.test.lucene;

import junit.framework.*;

public class AllLuceneTests extends TestCase {

    public AllLuceneTests(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(TestIndexItems.class);
        suite.addTestSuite(TestLuceneInitializer.class);
        suite.addTestSuite(TestLuceneSearch.class);
        suite.addTestSuite(TestLuceneDelete.class);
        return suite;
    }
}
