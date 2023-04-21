package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;

public class TestSimpleForward extends MockStrutsTestCase {

    public TestSimpleForward(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testSimpleForward() {
        setRequestPathInfo("test", "/testSimpleForward");
        actionPerform();
    }
}
