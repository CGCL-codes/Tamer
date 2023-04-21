package sample.controller.todo;

import org.slim3.tester.JDOControllerTestCase;

public class UpdateControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        start("/todo/update");
        UpdateController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/todo/update.jsp", getDestinationPath());
    }
}
