package ssg.common.utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author ssg
 */
public class UtilsSuite extends TestCase {

    public UtilsSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("UtilsSuite");
        suite.addTest(FilteredCollectionTest.suite());
        suite.addTest(MetaHandlerTest.suite());
        suite.addTest(CloneHelperTest.suite());
        suite.addTest(ReflectiveAttributesTest.suite());
        suite.addTest(BasicSearchCriteriaTest.suite());
        suite.addTest(StringHelperTest.suite());
        suite.addTest(EVVTest.suite());
        suite.addTest(PairTest.suite());
        suite.addTest(UIDTest.suite());
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
