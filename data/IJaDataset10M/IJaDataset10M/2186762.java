package net.sf.drawbridge.controller.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.drawbridge.controller.AbstractControllerTest;
import net.sf.drawbridge.controller.AbstractDrawbridgeController;
import net.sf.drawbridge.domain.DrawbridgeService;
import net.sf.drawbridge.vo.ExecutionContext;
import net.sf.drawbridge.vo.Status;
import net.sf.drawbridge.vo.StatusMessage;
import org.jmock.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class DeleteDatabaseSubmitControllerTest extends AbstractControllerTest {

    private Mock mockDrawbridgeService = mock(DrawbridgeService.class);

    private DeleteDatabaseSubmitController target;

    public void setUp() {
        target = new DeleteDatabaseSubmitController();
        target.setDrawbridgeService((DrawbridgeService) mockDrawbridgeService.proxy());
    }

    public void testShouldDeleteDatabaseAndReturnSuccessMessage() throws Exception {
        ExecutionContext context = new ExecutionContext(-1, 1, null);
        mockDrawbridgeService.expects(once()).method("deleteDatabase").with(eq(1), eq(context));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("databaseId", "1");
        login(mockRequest);
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("redirect:/ManageDatabases.do", mav.getViewName());
        assertEquals(new HashMap<String, Object>(), mav.getModel());
        List<StatusMessage> messageList = new ArrayList<StatusMessage>();
        messageList.add(new StatusMessage(Status.SUCCESS, "Successfully deleted database.", null));
        assertEquals(messageList, mockRequest.getSession().getAttribute(AbstractDrawbridgeController.MESSAGE_LIST));
    }

    public void testShouldDeleteDatabaseAndReturnFailureMessageOnException() throws Exception {
        ExecutionContext context = new ExecutionContext(-1, 1, null);
        RuntimeException ex = new RuntimeException("The Spanish Inquisition");
        mockDrawbridgeService.expects(once()).method("deleteDatabase").with(eq(1), eq(context)).will(throwException(ex));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("databaseId", "1");
        login(mockRequest);
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("redirect:/ManageDatabases.do", mav.getViewName());
        assertEquals(new HashMap<String, Object>(), mav.getModel());
        List<StatusMessage> messageList = new ArrayList<StatusMessage>();
        messageList.add(new StatusMessage(Status.ERROR, "Failed to delete database '1': The Spanish Inquisition", ex));
        assertEquals(messageList, mockRequest.getSession().getAttribute(AbstractDrawbridgeController.MESSAGE_LIST));
    }
}
