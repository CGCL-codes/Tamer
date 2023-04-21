package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;

public class TestMessageResourceAction extends MockStrutsTestCase {

    public TestMessageResourceAction(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testGetResources() {
        setRequestPathInfo("test", "/testMessage");
        actionPerform();
        verifyForward("success");
        verifyNoActionErrors();
    }
}
