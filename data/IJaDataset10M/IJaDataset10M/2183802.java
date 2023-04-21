package de.humanfork.treemerge.indentation;

import de.humanfork.treemerge.indentation.tree.AllTreeTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllIndentationTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for de.humanfork.treemerge.indentation");
        suite.addTest(AllTreeTests.suite());
        suite.addTestSuite(ParserTest.class);
        return suite;
    }
}
