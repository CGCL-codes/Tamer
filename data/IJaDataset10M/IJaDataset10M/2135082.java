package org.apache.commons.beanutils.locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverterTestCase;

/**
 * <p>
 * Created a test suite so that new test cases can be added here without having to
 * edit the build.xml.
 * </p>
 *
 * @author  Robert Burrell Donkin
 * @version $Revision: 1.6 $ $Date: 2004/02/28 13:18:37 $
 */
public class LocaleConvertTestSuite {

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(LocaleConvertUtilsTestCase.class);
        testSuite.addTestSuite(DateLocaleConverterTestCase.class);
        testSuite.addTestSuite(LocaleBeanificationTestCase.class);
        return testSuite;
    }
}
