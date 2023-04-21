package org.apache.roller.business;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.TestUtils;
import org.apache.roller.model.RollerFactory;
import org.apache.roller.model.UserManager;
import org.apache.roller.pojos.UserData;
import org.apache.roller.pojos.WeblogTemplate;
import org.apache.roller.pojos.WebsiteData;

/**
 * Test Weblog Page related business operations.
 */
public class WeblogPageTest extends TestCase {

    public static Log log = LogFactory.getLog(WeblogPageTest.class);

    UserData testUser = null;

    WebsiteData testWeblog = null;

    WeblogTemplate testPage = null;

    public WeblogPageTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(WeblogPageTest.class);
    }

    /**
     * All tests in this suite require a user and a weblog.
     */
    public void setUp() throws Exception {
        try {
            testUser = TestUtils.setupUser("wtTestUser");
            testWeblog = TestUtils.setupWeblog("wtTestWeblog", testUser);
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error(ex);
            throw new Exception("Test setup failed", ex);
        }
        testPage = new WeblogTemplate();
        testPage.setName("testTemplate");
        testPage.setDescription("Test Weblog Template");
        testPage.setLink("testTemp");
        testPage.setContents("a test weblog template.");
        testPage.setLastModified(new java.util.Date());
        testPage.setWebsite(testWeblog);
    }

    public void tearDown() throws Exception {
        try {
            TestUtils.teardownWeblog(testWeblog.getId());
            TestUtils.teardownUser(testUser.getId());
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error(ex);
            throw new Exception("Test teardown failed", ex);
        }
        testPage = null;
    }

    /**
     * Test basic persistence operations ... Create, Update, Delete
     */
    public void testTemplateCRUD() throws Exception {
        UserManager mgr = RollerFactory.getRoller().getUserManager();
        WeblogTemplate template = null;
        mgr.savePage(testPage);
        TestUtils.endSession(true);
        template = null;
        template = mgr.getPageByName(testWeblog, testPage.getName());
        assertNotNull(template);
        assertEquals(testPage.getContents(), template.getContents());
        template.setName("testtesttest");
        mgr.savePage(template);
        TestUtils.endSession(true);
        template = null;
        template = mgr.getPageByName(testWeblog, "testtesttest");
        assertNotNull(template);
        assertEquals(testPage.getContents(), template.getContents());
        mgr.removePage(template);
        TestUtils.endSession(true);
        template = null;
        template = mgr.getPageByName(testWeblog, testPage.getName());
        assertNull(template);
    }

    /**
     * Test lookup mechanisms ... id, name, link, weblog
     */
    public void testPermissionsLookups() throws Exception {
        UserManager mgr = RollerFactory.getRoller().getUserManager();
        WeblogTemplate page = null;
        mgr.savePage(testPage);
        String id = testPage.getId();
        TestUtils.endSession(true);
        page = mgr.getPage(id);
        assertNotNull(page);
        assertEquals(testPage.getContents(), page.getContents());
        page = null;
        page = mgr.getPageByName(testWeblog, testPage.getName());
        assertNotNull(page);
        assertEquals(testPage.getContents(), page.getContents());
        page = null;
        page = mgr.getPageByLink(testWeblog, testPage.getLink());
        assertNotNull(page);
        assertEquals(testPage.getContents(), page.getContents());
        List pages = mgr.getPages(testWeblog);
        assertNotNull(pages);
        assertEquals(1, pages.size());
        mgr.removePage(page);
        TestUtils.endSession(true);
    }
}
