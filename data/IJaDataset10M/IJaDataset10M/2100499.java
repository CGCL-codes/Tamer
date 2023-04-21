package org.enerj.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test suite which runs all the test cases application to beans in collections.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Revision: 1.2 $
 */
public class BeanCollectionsTestSuite extends TestCase {

    public BeanCollectionsTestSuite(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(BeanComparatorTestCase.class);
        suite.addTestSuite(BeanToPropertyValueTransformerTest.class);
        suite.addTestSuite(BeanPropertyValueEqualsPredicateTest.class);
        suite.addTestSuite(BeanPropertyValueChangeClosureTest.class);
        suite.addTestSuite(TestBeanMap.class);
        return suite;
    }
}
