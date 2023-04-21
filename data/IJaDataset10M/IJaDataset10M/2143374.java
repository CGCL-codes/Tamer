package org.opencms.file;

import org.opencms.file.types.CmsResourceTypeJsp;
import org.opencms.file.types.CmsResourceTypePlain;
import org.opencms.lock.CmsLockType;
import org.opencms.security.CmsSecurityException;
import org.opencms.test.OpenCmsTestCase;
import org.opencms.test.OpenCmsTestProperties;
import org.opencms.test.OpenCmsTestResourceFilter;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit tests for replace operation.<p>
 * 
 * @author Carsten Weinholz 
 * 
 * @version $Revision: 1.13 $
 */
public class TestReplace extends OpenCmsTestCase {

    /**
     * Default JUnit constructor.<p>
     * 
     * @param arg0 JUnit parameters
     */
    public TestReplace(String arg0) {
        super(arg0);
    }

    /**
     * Test suite for this test class.<p>
     * 
     * @return the test suite
     */
    public static Test suite() {
        OpenCmsTestProperties.initialize(org.opencms.test.AllTests.TEST_PROPERTIES_PATH);
        TestSuite suite = new TestSuite();
        suite.setName(TestReplace.class.getName());
        suite.addTest(new TestReplace("testReplaceResourceContent"));
        suite.addTest(new TestReplace("testReplaceResourceJsp"));
        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() {
                setupOpenCms("simpletest", "/sites/default/");
            }

            protected void tearDown() {
                removeOpenCms();
            }
        };
        return wrapper;
    }

    /**
     * Tests the "replace resource" operation.<p>
     * 
     * @throws Throwable if something goes wrong
     */
    public void testReplaceResourceContent() throws Throwable {
        CmsObject cms = getCmsObject();
        echo("Testing replacement of file content");
        String path = "/types/text.txt";
        String contentStr = "Hello this is the new content";
        long timestamp = System.currentTimeMillis();
        storeResources(cms, path);
        cms.lockResource(path);
        cms.replaceResource(path, CmsResourceTypePlain.getStaticTypeId(), contentStr.getBytes(), null);
        assertProject(cms, path, cms.getRequestContext().currentProject());
        assertState(cms, path, CmsResource.STATE_CHANGED);
        assertDateLastModifiedAfter(cms, path, timestamp);
        assertUserLastModified(cms, path, cms.getRequestContext().currentUser());
        assertLock(cms, path, CmsLockType.EXCLUSIVE);
        assertContent(cms, path, contentStr.getBytes());
        assertFilter(cms, path, OpenCmsTestResourceFilter.FILTER_REPLACERESOURCE);
    }

    /**
     * Tests the "replace resource" operation for jsp without permissions.<p>
     * 
     * @throws Throwable if something goes wrong
     */
    public void testReplaceResourceJsp() throws Throwable {
        CmsObject cms = getCmsObject();
        echo("Testing replacement of file for jsp without permissions");
        CmsProject offlineProject = cms.getRequestContext().currentProject();
        String path = "/types/text.txt";
        String contentStr = "Hello this is the new content";
        cms.replaceResource(path, CmsResourceTypeJsp.getStaticTypeId(), contentStr.getBytes(), null);
        cms.unlockResource(path);
        cms.loginUser("test1", "test1");
        cms.getRequestContext().setCurrentProject(offlineProject);
        try {
            cms.lockResource(path);
            cms.replaceResource(path, CmsResourceTypePlain.getStaticTypeId(), contentStr.getBytes(), null);
            fail("replaceResource from jsp without permissions should fail");
        } catch (CmsSecurityException e) {
        }
        cms = getCmsObject();
        cms.lockResource(path);
        cms.replaceResource(path, CmsResourceTypePlain.getStaticTypeId(), contentStr.getBytes(), null);
        cms.unlockResource(path);
        cms.loginUser("test1", "test1");
        cms.getRequestContext().setCurrentProject(offlineProject);
        try {
            cms.lockResource(path);
            cms.replaceResource(path, CmsResourceTypeJsp.getStaticTypeId(), contentStr.getBytes(), null);
            fail("replaceResource to jsp without permissions should fail");
        } catch (CmsSecurityException e) {
        }
    }
}
