package net.sf.drawbridge.controller.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.drawbridge.controller.driver.ManageDriversController;
import net.sf.drawbridge.domain.DrawbridgeService;
import net.sf.drawbridge.vo.Driver;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.web.servlet.ModelAndView;

public class ManageDriversControllerTest extends MockObjectTestCase {

    private Mock mockDrawbridgeService = mock(DrawbridgeService.class);

    private ManageDriversController target;

    public void setUp() {
        target = new ManageDriversController();
        target.setDrawbridgeService((DrawbridgeService) mockDrawbridgeService.proxy());
    }

    public void testShouldReturnViewWithDrivers() throws Exception {
        List<Driver> driverList = new ArrayList<Driver>();
        driverList.add(new Driver(1, "name", "class"));
        mockDrawbridgeService.expects(once()).method("listDrivers").will(returnValue(driverList));
        ModelAndView mav = target.handleRequestInternal(null, null);
        assertEquals("driver/manage", mav.getViewName());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("driverList", driverList);
        assertEquals(model, mav.getModel());
    }
}
