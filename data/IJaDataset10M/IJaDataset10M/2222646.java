package net.sf.farmmanager.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.farmmanager.Client;
import net.sf.farmmanager.Farm;
import net.sf.farmmanager.FiltersCommand;
import net.sf.farmmanager.dao.FarmManagerDAO;
import net.sf.farmmanager.dao.HibernateDynaReport;
import org.easymock.MockControl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import junit.framework.TestCase;

public class MainControllerTest extends TestCase {

    private MainController mainController;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private FarmManagerDAO farmManagerDAO;

    private MockControl farmManagerDAOControl;

    private Client client;

    private HibernateDynaReport fieldDynaReport;

    private HibernateDynaReport farmDynaReport;

    protected void setUp() throws Exception {
        mainController = new MainController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        client = new Client(1);
        farmManagerDAOControl = MockControl.createControl(FarmManagerDAO.class);
        farmManagerDAO = (FarmManagerDAO) farmManagerDAOControl.getMock();
        mainController.setFarmManagerDAO(farmManagerDAO);
        fieldDynaReport = new HibernateDynaReport();
        fieldDynaReport.addColumnDBMapping("farm.id", "", Integer.class);
        farmDynaReport = new HibernateDynaReport();
    }

    public void testMainController() {
        assertNotNull(mainController);
    }

    public void testIndex() {
        ModelAndView mav = mainController.index(request, response);
        assertNotNull(mav);
        assertEquals("index", mav.getViewName());
    }

    public void testFarmsNoClient() {
        try {
            mainController.farms(request, response);
            fail("Should have thrown exception, since there is no client in the session.");
        } catch (Exception e) {
        }
    }

    public void testFarms() {
        List listOfFarms = new ArrayList();
        farmManagerDAO.getNewFarmDynaReport();
        farmManagerDAOControl.setReturnValue(farmDynaReport);
        farmManagerDAO.executeDynaReport(farmDynaReport);
        farmManagerDAOControl.setReturnValue(listOfFarms);
        farmManagerDAOControl.replay();
        request.getSession().setAttribute("client", client);
        ModelAndView mav = mainController.farms(request, response);
        assertNotNull(mav);
        assertEquals("farms", mav.getViewName());
        assertSame(listOfFarms, mav.getModel().get("farms"));
        assertNotNull(mav.getModel().get("command"));
        FiltersCommand command = (FiltersCommand) mav.getModel().get("command");
        farmManagerDAOControl.verify();
    }

    public void testFieldsNoClient() {
        try {
            mainController.fields(request, response);
            fail("Should have thrown exception, since there is no client in the session.");
        } catch (Exception e) {
        }
    }

    public void testFieldsWithNoFarmGiven() {
        List listOfFields = new ArrayList();
        farmManagerDAO.getNewFieldDynaReport();
        farmManagerDAOControl.setReturnValue(fieldDynaReport);
        farmManagerDAO.executeDynaReport(fieldDynaReport);
        farmManagerDAOControl.setReturnValue(listOfFields);
        farmManagerDAOControl.replay();
        request.getSession().setAttribute("client", client);
        ModelAndView mav = mainController.fields(request, response);
        assertNotNull(mav);
        assertEquals("fields", mav.getViewName());
        assertSame(listOfFields, mav.getModel().get("fields"));
        assertNotNull(mav.getModel().get("command"));
        farmManagerDAOControl.verify();
    }

    public void testFieldsWithFarmGiven() {
        request.addParameter("filters[farm.id]", "1");
        request.getSession().setAttribute("client", client);
        List listOfFields = new ArrayList();
        Farm farm = new Farm(1);
        farm.setClient(client);
        farmManagerDAO.getNewFieldDynaReport();
        farmManagerDAOControl.setReturnValue(fieldDynaReport);
        farmManagerDAO.executeDynaReport(fieldDynaReport);
        farmManagerDAOControl.setReturnValue(listOfFields);
        farmManagerDAOControl.replay();
        ModelAndView mav = mainController.fields(request, response);
        assertNotNull(mav);
        assertEquals("fields", mav.getViewName());
        assertSame(listOfFields, mav.getModel().get("fields"));
        assertNotNull(mav.getModel().get("command"));
        FiltersCommand command = (FiltersCommand) mav.getModel().get("command");
        assertEquals("1", command.getFilters().get("farm.id"));
        farmManagerDAOControl.verify();
    }

    public void testFieldsWithFarmGivenFromOtherClient() {
        request.addParameter("filters[farm.id]", "2");
        request.getSession().setAttribute("client", client);
        Farm farm = new Farm(1);
        farm.setClient(client);
        farmManagerDAO.getNewFieldDynaReport();
        farmManagerDAOControl.setReturnValue(fieldDynaReport);
        farmManagerDAO.executeDynaReport(fieldDynaReport);
        farmManagerDAOControl.setReturnValue(null);
        farmManagerDAOControl.replay();
        ModelAndView mav = mainController.fields(request, response);
        assertNotNull(mav);
        assertEquals("fields", mav.getViewName());
        farmManagerDAOControl.verify();
    }
}
