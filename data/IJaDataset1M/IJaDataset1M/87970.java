package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;
import java.io.File;

/**
 *
 * <p>Title: TestNoRequestPathInfo</p>
 * <p>Description: Confirms correct behavior if
 * actionPerfoem() is called prior to calling
 * setRequestPathInfo().</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Sean Pritchard
 * @version 1.0
 */
public class TestNoRequestPathInfo extends MockStrutsTestCase {

    public TestNoRequestPathInfo() {
    }

    public void setUp() throws Exception {
        super.setUp();
        this.setContextDirectory(new File(System.getProperty("basedir") + "/src/examples"));
        setConfigFile("/WEB-INF/struts-config-test.xml");
    }

    /**
     * this test assumes that web.xml and struts-config.xml
     * can be found.  If these files are found,
     * but no request path is set prior to calling
     * actionPerform(), we expect an IllegalStateException
     */
    public void testNoRequestPathInfo() {
        try {
            actionPerform();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        }
    }
}
