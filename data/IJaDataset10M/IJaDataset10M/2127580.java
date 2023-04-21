package net.sourceforge.cruisecontrol.webtest;

import net.sourceforge.jwebunit.WebTestCase;
import net.sourceforge.cruisecontrol.Configuration;

public class BootstrapperDetailsWebTest extends WebTestCase {

    private static final String BASE = "/cruisecontrol/load-details.jspa?" + "project=connectfour&pluginType=bootstrapper";

    private static final String CVS_URL = BASE + "&pluginName=cvsbootstrapper";

    private static final String SVN_URL = BASE + "&pluginName=svnbootstrapper";

    private Configuration configuration;

    private String contents;

    protected void setUp() throws Exception {
        super.setUp();
        getTestContext().setBaseUrl("http://localhost:7854");
        configuration = new Configuration("localhost", 7856);
        contents = configuration.getConfiguration();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        configuration.setConfiguration(contents);
    }

    public void testShouldBeAccessibleFromBootstrappersPage() {
        String pluginsUrl = "/cruisecontrol/plugins.jspa?project=connectfour&pluginType=bootstrapper";
        beginAt(pluginsUrl);
        assertLinkPresentWithText("cvsbootstrapper");
    }

    public void testShouldLoadCVSBootstrapperConfiguration() {
        beginAt(CVS_URL);
        assertFormPresent("cvsbootstrapper-details");
        assertFormElementPresent("cvsroot");
        assertFormElementPresent("file");
        assertFormElementPresent("localWorkingCopy");
        assertFormElementPresent("overwriteChanges");
        assertFormElementPresent("resetStickyTags");
    }

    public void testShouldLoadSVNBootstrapperConfiguration() {
        beginAt(SVN_URL);
        assertFormPresent("svnbootstrapper-details");
        assertFormElementPresent("file");
        assertFormElementPresent("localWorkingCopy");
        assertFormElementPresent("password");
        assertFormElementPresent("username");
    }

    public void testShouldSaveCVSBootstrapperConfiguration() {
        beginAt(CVS_URL);
        setWorkingForm("cvsbootstrapper-details");
        setFormElement("localWorkingCopy", "projects/jakarta-commons/cli");
        submit();
        assertTextPresent("Updated configuration.");
        assertFormPresent("cvsbootstrapper-details");
        assertFormElementPresent("localWorkingCopy");
        assertTextPresent("projects/jakarta-commons/cli");
    }

    public void testShouldSaveSVNBootstrapperConfiguration() {
        beginAt(SVN_URL);
        setWorkingForm("svnbootstrapper-details");
        setFormElement("localWorkingCopy", "repos/trunk/foobar");
        submit();
        assertTextPresent("Updated configuration.");
        assertFormPresent("svnbootstrapper-details");
        assertFormElementPresent("localWorkingCopy");
        assertTextPresent("repos/trunk/foobar");
    }

    public void testShouldAllowUsersToClearCVSBootstrapperAttributes() {
        String cvsroot = "/cvs/foo";
        beginAt(CVS_URL);
        setWorkingForm("cvsbootstrapper-details");
        setFormElement("cvsroot", cvsroot);
        submit();
        assertTextPresent("Updated configuration.");
        assertTextPresent(cvsroot);
        gotoPage(CVS_URL);
        assertTextPresent(cvsroot);
        setWorkingForm("cvsbootstrapper-details");
        setFormElement("cvsroot", "");
        submit();
        assertTextPresent("Updated configuration.");
        assertTextNotPresent(cvsroot);
        gotoPage(CVS_URL);
        assertTextNotPresent(cvsroot);
    }

    public void testShouldAllowUsersToClearSVNBootstrapperAttributes() {
        String localWorkingCopy = "/cvs/foo";
        beginAt(SVN_URL);
        setWorkingForm("svnbootstrapper-details");
        setFormElement("localWorkingCopy", localWorkingCopy);
        submit();
        assertTextPresent("Updated configuration.");
        assertTextPresent(localWorkingCopy);
        gotoPage(SVN_URL);
        assertTextPresent(localWorkingCopy);
        setWorkingForm("svnbootstrapper-details");
        setFormElement("localWorkingCopy", "");
        submit();
        assertTextPresent("Updated configuration.");
        assertTextNotPresent(localWorkingCopy);
        gotoPage(SVN_URL);
        assertTextNotPresent(localWorkingCopy);
    }
}
