package test.model.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:51 $
 */
public final class SuiteModelUtil extends TestCase {

    /**
     * Constructor for the SuiteModelUtil object
     *
     * @param inName TODO: Describe the Parameter
     */
    public SuiteModelUtil(String inName) {
        super(inName);
    }

    /**
     * A unit test suite for JUnit
     *
     * @return The test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(TestArraySelectorIterator.class));
        suite.addTest(new TestSuite(TestIntIndexSelectorIterator.class));
        suite.addTest(new TestSuite(TestCompoundSelectorIterator.class));
        return suite;
    }
}
