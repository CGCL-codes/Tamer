package net.sf.drawbridge.controller.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.drawbridge.controller.AbstractControllerTest;
import net.sf.drawbridge.controller.AbstractDrawbridgeController;
import net.sf.drawbridge.domain.DrawbridgeService;
import net.sf.drawbridge.vo.Database;
import net.sf.drawbridge.vo.Driver;
import net.sf.drawbridge.vo.ExecutionContext;
import net.sf.drawbridge.vo.Status;
import net.sf.drawbridge.vo.StatusMessage;
import org.jmock.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class AddOrEditDatabaseSubmitControllerTest extends AbstractControllerTest {

    private Mock mockDrawbridgeService = mock(DrawbridgeService.class);

    private AddOrEditDatabaseSubmitController target;

    private Driver driver = new Driver(2, "name", "class");

    public void setUp() {
        target = new AddOrEditDatabaseSubmitController();
        target.setDrawbridgeService((DrawbridgeService) mockDrawbridgeService.proxy());
    }

    public void testShouldAddDatabaseAndReturnSuccessMessage() throws Exception {
        ExecutionContext context = new ExecutionContext(-1, null, null);
        Database database = new Database(null, "name", "url", driver);
        mockDrawbridgeService.expects(once()).method("addDatabase").with(eq(database), eq(context));
        mockDrawbridgeService.expects(once()).method("getDriver").with(eq(new Integer(2))).will(returnValue(driver));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("name", "name");
        mockRequest.setParameter("jdbcUrl", "url");
        mockRequest.setParameter("driverId", "2");
        login(mockRequest);
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("redirect:/ManageDatabases.do", mav.getViewName());
        assertEquals(new HashMap<String, Object>(), mav.getModel());
        List<StatusMessage> messageList = new ArrayList<StatusMessage>();
        messageList.add(new StatusMessage(Status.SUCCESS, "Successfully created database: name", null));
        assertEquals(messageList, mockRequest.getSession().getAttribute(AbstractDrawbridgeController.MESSAGE_LIST));
    }

    public void testShouldEditDatabaseAndReturnSuccessMessage() throws Exception {
        ExecutionContext context = new ExecutionContext(-1, 1, null);
        Database database = new Database(1, "name", "url", driver);
        mockDrawbridgeService.expects(once()).method("getDriver").with(eq(new Integer(2))).will(returnValue(driver));
        mockDrawbridgeService.expects(once()).method("updateDatabase").with(eq(database), eq(context));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("databaseId", "1");
        mockRequest.setParameter("name", "name");
        mockRequest.setParameter("jdbcUrl", "url");
        mockRequest.setParameter("driverId", "2");
        login(mockRequest);
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("redirect:/ManageDatabases.do", mav.getViewName());
        assertEquals(new HashMap<String, Object>(), mav.getModel());
        List<StatusMessage> messageList = new ArrayList<StatusMessage>();
        messageList.add(new StatusMessage(Status.SUCCESS, "Successfully updated database: name", null));
        assertEquals(messageList, mockRequest.getSession().getAttribute(AbstractDrawbridgeController.MESSAGE_LIST));
    }

    public void testShouldAddDatabaseAndReturnFailureMessageOnException() throws Exception {
        ExecutionContext context = new ExecutionContext(-1, null, null);
        Database database = new Database(null, "name", "url", driver);
        RuntimeException ex = new RuntimeException("The Spanish Inquisition");
        mockDrawbridgeService.expects(once()).method("getDriver").with(eq(new Integer(2))).will(returnValue(driver));
        mockDrawbridgeService.expects(once()).method("addDatabase").with(eq(database), eq(context)).will(throwException(ex));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("name", "name");
        mockRequest.setParameter("jdbcUrl", "url");
        mockRequest.setParameter("driverId", "2");
        login(mockRequest);
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("redirect:/ManageDatabases.do", mav.getViewName());
        assertEquals(new HashMap<String, Object>(), mav.getModel());
        List<StatusMessage> messageList = new ArrayList<StatusMessage>();
        messageList.add(new StatusMessage(Status.ERROR, "Failed to create database " + database + ": The Spanish Inquisition", ex));
        assertEquals(messageList, mockRequest.getSession().getAttribute(AbstractDrawbridgeController.MESSAGE_LIST));
    }

    public void testShouldEditDatabaseAndReturnFailureMessageOnException() throws Exception {
        ExecutionContext context = new ExecutionContext(-1, 1, null);
        Database database = new Database(1, "name", "url", driver);
        RuntimeException ex = new RuntimeException("The Spanish Inquisition");
        mockDrawbridgeService.expects(once()).method("getDriver").with(eq(new Integer(2))).will(returnValue(driver));
        mockDrawbridgeService.expects(once()).method("updateDatabase").with(eq(database), eq(context)).will(throwException(ex));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("databaseId", "1");
        mockRequest.setParameter("name", "name");
        mockRequest.setParameter("jdbcUrl", "url");
        mockRequest.setParameter("driverId", "2");
        login(mockRequest);
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("redirect:/ManageDatabases.do", mav.getViewName());
        assertEquals(new HashMap<String, Object>(), mav.getModel());
        List<StatusMessage> messageList = new ArrayList<StatusMessage>();
        messageList.add(new StatusMessage(Status.ERROR, "Failed to update database " + database + ": The Spanish Inquisition", ex));
        assertEquals(messageList, mockRequest.getSession().getAttribute(AbstractDrawbridgeController.MESSAGE_LIST));
    }
}
