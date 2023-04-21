package net.sf.strutstestcase;

import net.sf.strutstestcase.MockStrutsTestCase;
import servletunit.ServletContextSimulator;
import java.io.File;

public class TestAbsolutePath extends MockStrutsTestCase {

    String rootPath;

    public TestAbsolutePath(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        rootPath = System.getProperty("basedir");
        setServletConfigFile(rootPath + "/src/test/resources/WEB-INF/web.xml");
    }

    public void testSuccessfulLogin() {
        setConfigFile(rootPath + "/src/test/resources/WEB-INF/struts-config.xml");
        addRequestParameter("username", "deryl");
        addRequestParameter("password", "radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        assertEquals("deryl", getSession().getAttribute("authentication"));
        verifyNoActionErrors();
    }

    public void testContextDirectory() {
        this.setContextDirectory(new File(rootPath + "/src/test/resources"));
        setConfigFile("/WEB-INF/struts-config.xml");
        addRequestParameter("username", "deryl");
        addRequestParameter("password", "radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        assertEquals("deryl", getSession().getAttribute("authentication"));
        verifyNoActionErrors();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestAbsolutePath.class);
    }
}
