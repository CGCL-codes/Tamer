package net.sf.drawbridge.controller.runas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.drawbridge.controller.runas.AddOrEditRunAsController;
import net.sf.drawbridge.domain.DrawbridgeService;
import net.sf.drawbridge.vo.Database;
import net.sf.drawbridge.vo.RunAsAccount;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class AddOrEditRunAsControllerTest extends MockObjectTestCase {

    private Mock mockDrawbridgeService = mock(DrawbridgeService.class);

    private AddOrEditRunAsController target;

    public void setUp() {
        target = new AddOrEditRunAsController();
        target.setDrawbridgeService((DrawbridgeService) mockDrawbridgeService.proxy());
    }

    public void testShouldReturnViewWithRunAsWhenRunAsIdProvided() throws Exception {
        Database db = new Database(1, "name", "url", null);
        RunAsAccount runAs = new RunAsAccount(1, "userName", "password", db);
        List<Database> databaseList = new ArrayList<Database>();
        databaseList.add(db);
        mockDrawbridgeService.expects(once()).method("getRunAsAccount").with(eq(1)).will(returnValue(runAs));
        mockDrawbridgeService.expects(once()).method("listDatabases").will(returnValue(databaseList));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setParameter("runAsId", "1");
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("runAs/addOrEdit", mav.getViewName());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("runAs", runAs);
        model.put("databaseList", databaseList);
        assertEquals(model, mav.getModel());
    }

    public void testShouldReturnViewWithoutRunAsWhenRunAsIdNotProvided() throws Exception {
        List<Database> databaseList = new ArrayList<Database>();
        databaseList.add(new Database(1, "name", "url", null));
        mockDrawbridgeService.expects(once()).method("listDatabases").will(returnValue(databaseList));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        ModelAndView mav = target.handleRequestInternal(mockRequest, null);
        assertEquals("runAs/addOrEdit", mav.getViewName());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("databaseList", databaseList);
        assertEquals(model, mav.getModel());
    }
}
