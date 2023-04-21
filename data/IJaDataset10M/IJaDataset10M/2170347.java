package net.sf.portletunit.test;

import java.io.File;
import junit.framework.TestCase;
import net.sf.portletunit.PortletRunner;
import net.sf.portletunit.portlet.helloworld.HelloWorldPortlet2;
import com.meterware.httpunit.WebResponse;

public class TestHelloWorldPortlet2 extends TestCase {

    public void testNewHelloWorld() throws Exception {
        Class portletClass = HelloWorldPortlet2.class;
        File webInfDir = new File("portlets/examples");
        PortletRunner runner = PortletRunner.createPortletRunner(portletClass, webInfDir, "helloworld2");
        assertNotNull(runner);
        WebResponse helloResponse = runner.getResponse();
        assertNotNull(helloResponse);
    }
}
