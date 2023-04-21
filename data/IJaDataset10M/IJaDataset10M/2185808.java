package tests.com.ivis.xprocess.workflowserver;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import com.ivis.xprocess.workflowserver.WorkflowController;
import com.ivis.xprocess.workflowserver.monitors.flyspray.FlysprayMailHandler;

public class TestFlysprayMailHandler extends AbstractDependencyInjectionSpringContextTests {

    private static final String FLYSPRAY_MONITOR = "FlysprayMonitor";

    private static final String WORKFLOW_CONTROLLER = "WorkflowController";

    public void testFlySpraySetUp() throws Exception {
        FlysprayMailHandler monitor = (FlysprayMailHandler) applicationContext.getBean(FLYSPRAY_MONITOR);
        assertNotNull(monitor);
        assertEquals("http://issues.ivis.com/flyspray/?do=details&id=", monitor.getDetailsURL());
        assertEquals("FlysprayEvent", monitor.getEventNames()[0]);
        assertTrue(monitor.getPatternNames().contains("Defect"));
        assertTrue(monitor.getPatternNames().contains("Enhancement"));
        assertTrue(monitor.getPatternNames().contains("Web Issue"));
        assertEquals("pop.gmail.com", monitor.getPop3Server().getHost());
        assertEquals("ivis.uk.demo@googlemail.com", monitor.getPop3Server().getUser());
        assertEquals("scorpion", monitor.getPop3Server().getPassword());
        assertEquals("issues@ivis.com", monitor.getSenderOfInterest());
        assertEquals("Flyspray", monitor.getSystemName());
        assertEquals(Integer.MAX_VALUE, monitor.getMaximumNumberOfMails());
        WorkflowController controller = (WorkflowController) applicationContext.getBean(WORKFLOW_CONTROLLER);
        while (true) {
            controller.request();
            Thread.sleep(20000);
        }
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "file:deploy/testMail.xml" };
    }
}
