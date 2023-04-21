package shm.controller.col;

import shm.test.MyJDOControllerTestCase;

public class SelectControllerTest extends MyJDOControllerTestCase {

    public void testRun() throws Exception {
        login("aaa");
        start("/col/select");
        SelectController controller = getController();
        assertNotNull(controller);
        assertFalse(isRedirect());
        assertEquals("/col/select.jsp", getDestinationPath());
    }
}
