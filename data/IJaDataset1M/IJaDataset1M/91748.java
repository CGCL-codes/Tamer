package org.opencms.staticexport;

import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.types.CmsResourceTypeFolder;
import org.opencms.file.types.CmsResourceTypeXmlPage;
import org.opencms.i18n.CmsEncoder;
import org.opencms.main.OpenCms;
import org.opencms.test.OpenCmsTestCase;
import org.opencms.test.OpenCmsTestProperties;
import org.opencms.xml.page.CmsXmlPage;
import java.util.Locale;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/** 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.15 $
 * 
 * @since 6.0.0
 */
public class TestCmsLinkManager extends OpenCmsTestCase {

    /**
     * Default JUnit constructor.<p>
     * 
     * @param arg0 JUnit parameters
     */
    public TestCmsLinkManager(String arg0) {
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
        suite.setName(TestCmsLinkManager.class.getName());
        suite.addTest(new TestCmsLinkManager("testToAbsolute"));
        suite.addTest(new TestCmsLinkManager("testLinkSubstitution"));
        suite.addTest(new TestCmsLinkManager("testSymmetricSubstitution"));
        suite.addTest(new TestCmsLinkManager("testCustomLinkHandler"));
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
     * Tests the method getAbsoluteUri.<p>
     */
    public void testToAbsolute() {
        String test;
        test = CmsLinkManager.getRelativeUri("/dir1/dir2/index.html", "/dir1/dirB/index.html");
        System.out.println(test);
        assertEquals(test, "../dirB/index.html");
        test = CmsLinkManager.getRelativeUri("/exp/en/test/index.html", "/exp/de/test/index.html");
        System.out.println(test);
        assertEquals(test, "../../de/test/index.html");
        test = CmsLinkManager.getAbsoluteUri("../../index.html", "/dir1/dir2/dir3/");
        System.out.println(test);
        assertEquals(test, "/dir1/index.html");
        test = CmsLinkManager.getAbsoluteUri("./../././.././dir2/./../index.html", "/dir1/dir2/dir3/");
        System.out.println(test);
        assertEquals(test, "/dir1/index.html");
        test = CmsLinkManager.getAbsoluteUri("/dirA/index.html", "/dir1/dir2/dir3/");
        System.out.println(test);
        assertEquals(test, "/dirA/index.html");
    }

    /**
     * Tests symmetric link / root path substitution.<p>
     * 
     * @throws Exception if test fails
     */
    public void testSymmetricSubstitution() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing symmetric link / root path substitution substitution");
        CmsResource res = cms.readResource("/xmlcontent/article_0001.html");
        CmsLinkManager lm = OpenCms.getLinkManager();
        String link = lm.substituteLinkForRootPath(cms, res.getRootPath());
        String rootPath = lm.getRootPath(cms, link);
        assertEquals(res.getRootPath(), rootPath);
        link = lm.getServerLink(cms, res.getRootPath());
        rootPath = lm.getRootPath(cms, link);
        assertEquals(res.getRootPath(), rootPath);
    }

    /** Content for a simple test page. */
    private static final String PAGE_01 = "<html><body><a href=\"/system/news/test.html?__locale=de\">test</a></body></html>";

    /**
     * Tests symmetric link / root path substitution with a custom link handler.<p>
     * 
     * @throws Exception if test fails
     */
    public void testCustomLinkHandler() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing symmetric link / root path substitution with a custom link handler");
        CmsLinkManager lm = OpenCms.getLinkManager();
        I_CmsLinkSubstitutionHandler lh = new CmsTestLinkSubstitutionHandler();
        lm.setLinkSubstitutionHandler(cms, lh);
        cms.createResource("/system/news", CmsResourceTypeFolder.getStaticTypeId());
        String resName = "/system/news/test.html";
        cms.createResource(resName, CmsResourceTypeXmlPage.getStaticTypeId());
        OpenCms.getPublishManager().publishResource(cms, "/system/news");
        OpenCms.getPublishManager().waitWhileRunning();
        CmsResource res = cms.readResource(resName);
        testCustomLinkHandler(cms, res.getRootPath());
        OpenCms.getStaticExportManager().setVfsPrefix("");
        OpenCms.getStaticExportManager().initialize(cms);
        testCustomLinkHandler(cms, res.getRootPath());
        cms.getRequestContext().setUri(resName);
        cms.getRequestContext().setSiteRoot("");
        testCustomLinkHandler(cms, res.getRootPath());
        testCustomLinkHandler(cms, res.getRootPath() + "?__locale=de");
        CmsXmlPage page = new CmsXmlPage(Locale.ENGLISH, CmsEncoder.ENCODING_UTF_8);
        page.addValue("body", Locale.ENGLISH);
        page.setStringValue(cms, "body", Locale.ENGLISH, PAGE_01);
        cms.lockResource(resName);
        CmsFile file = cms.readFile(res);
        file.setContents(page.marshal());
        cms.writeFile(file);
    }

    /**
     * Internal test method for custom link test.<p>
     * 
     * @param cms the current OpenCms context
     * @param path the resource path in the VFS to check the links for
     * 
     * @throws Exception in case the test fails
     */
    private void testCustomLinkHandler(CmsObject cms, String path) throws Exception {
        CmsLinkManager lm = OpenCms.getLinkManager();
        String link = lm.substituteLinkForRootPath(cms, path);
        String rootPath = lm.getRootPath(cms, link);
        assertEquals(path, rootPath);
        link = lm.getServerLink(cms, path);
        rootPath = lm.getRootPath(cms, link);
        assertEquals(path, rootPath);
    }

    /**
     * Tests the link substitution.<p>
     * 
     * @throws Exception if test fails
     */
    public void testLinkSubstitution() throws Exception {
        String test;
        CmsObject cms = getCmsObject();
        echo("Testing link substitution");
        cms.getRequestContext().setCurrentProject(cms.readProject("Online"));
        CmsLinkManager linkManager = OpenCms.getLinkManager();
        test = linkManager.substituteLink(cms, "/folder1/index.html?additionalParam", "/sites/default");
        System.out.println(test);
        assertEquals("/data/opencms/folder1/index.html?additionalParam", test);
        test = linkManager.substituteLink(cms, CmsLinkManager.getAbsoluteUri("/", "/folder1/index.html"), "/sites/default");
        System.out.println(test);
        assertEquals("/data/opencms/", test);
        test = linkManager.substituteLink(cms, CmsLinkManager.getAbsoluteUri("./", "/folder1/index.html"), "/sites/default");
        System.out.println(test);
        assertEquals("/data/opencms/folder1/", test);
        test = CmsLinkManager.getRelativeUri("/index.html", "/index.html");
        System.out.println(test);
        assertEquals("index.html", test);
        test = CmsLinkManager.getRelativeUri("/folder1/index.html", "/folder1/");
        System.out.println(test);
        assertEquals("./", test);
        test = CmsLinkManager.getRelativeUri("/index.html", "/");
        System.out.println(test);
        assertEquals("./", test);
        test = CmsLinkManager.getRelativeUri("/index.html", "./");
        System.out.println(test);
        assertEquals("./", test);
        test = CmsLinkManager.getRelativeUri("/", "/");
        System.out.println(test);
        assertEquals("./", test);
    }
}
