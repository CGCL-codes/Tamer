package org.opencms.util;

import org.opencms.test.OpenCmsTestProperties;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Main test suite for the package <code>{@link org.opencms.util}</code>.<p>
 * 
 * @author Alexander Kandzior 
 * @version $Revision: 1.23 $
 * 
 * @since 6.0
 */
public final class AllTests {

    /**
     * Hide constructor to prevent generation of class instances.<p>
     */
    private AllTests() {
    }

    /**
     * Returns the JUnit test suite for this package.<p>
     * 
     * @return the JUnit test suite for this package
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for package " + AllTests.class.getPackage().getName());
        OpenCmsTestProperties.initialize(org.opencms.test.AllTests.TEST_PROPERTIES_PATH);
        suite.addTest(new TestSuite(TestCmsBrowserMatcher.class));
        suite.addTest(new TestSuite(TestCmsDateUtil.class));
        suite.addTest(new TestSuite(TestCmsExportFolderMatcher.class));
        suite.addTest(new TestSuite(TestCmsFileUtil.class));
        suite.addTest(new TestSuite(TestCmsHtml2TextConverter.class));
        suite.addTest(new TestSuite(TestCmsHtmlConverter.class));
        suite.addTest(new TestSuite(TestCmsHtmlExtractor.class));
        suite.addTest(new TestSuite(TestCmsHtmlParser.class));
        suite.addTest(new TestSuite(TestCmsHtmlStripper.class));
        suite.addTest(new TestSuite(TestCmsMacroResolver.class));
        suite.addTest(new TestSuite(TestCmsResourceTranslator.class));
        suite.addTest(new TestSuite(TestCmsStringUtil.class));
        suite.addTest(new TestSuite(TestCmsUriSplitter.class));
        suite.addTest(new TestSuite(TestCmsUUID.class));
        suite.addTest(new TestSuite(TestCmsXmlSaxWriter.class));
        suite.addTest(new TestSuite(TestValidFilename.class));
        return suite;
    }
}
