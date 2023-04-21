package edu.kit.pse.ass.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.support.BindingAwareModelMap;
import edu.kit.pse.ass.entity.Building;
import edu.kit.pse.ass.entity.Facility;
import edu.kit.pse.ass.entity.Property;
import edu.kit.pse.ass.entity.Reservation;
import edu.kit.pse.ass.entity.Room;
import edu.kit.pse.ass.entity.Workplace;
import edu.kit.pse.ass.facility.management.FacilityManagement;
import edu.kit.pse.ass.gui.controller.ReservationController;
import edu.kit.pse.ass.gui.model.ReservationModel;
import edu.kit.pse.ass.realdata.DataHelper;

/**
 * Tests for the Reservation Controller
 * 
 * @author Jannis Koch
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext/applicationContext-*.xml" })
@Transactional
public class ReservationControllerTest {

    /** the reservation controller */
    @Autowired
    ReservationController reservationController;

    /** the facility management */
    @Autowired
    FacilityManagement facilityManagement;

    /** data helper for creating test data */
    @Autowired
    private DataHelper dataHelper;

    /** the user id of user 1 */
    private static final String USERID1 = "ubbbb@student.kit.edu";

    /** the user id of user 2 */
    private static final String USERID2 = "ucccc@student.kit.edu";

    /** a room */
    private Room room;

    /** a reservation of user 1 */
    private Reservation resUser1;

    /** a reservation of user 2 */
    private Reservation resUser2;

    /**
	 * creates the data for the tests
	 */
    @Before
    public void createTestData() {
        createUser();
        createFacilities();
        createReservations();
        setSecurityContext();
    }

    /**
	 * creates a persisted user for the tests
	 */
    private void createUser() {
        dataHelper.createPersistedUser(USERID1, "bbbbbb", new HashSet<String>());
    }

    /**
	 * creates persisted facilities for the tests
	 */
    private void createFacilities() {
        Property propertyWLAN = new Property("WLAN");
        Property propertyLAN = new Property("LAN");
        Property propertySteckdose = new Property("Steckdose");
        Building building = dataHelper.createPersistedBuilding("50.20", "Informatik", new ArrayList<Property>());
        room = dataHelper.createPersistedRoom("Seminarraum", "-101", -1, Arrays.asList(propertyWLAN, propertySteckdose, propertyLAN));
        Workplace wp1 = dataHelper.createPersistedWorkplace("Workplace 1", Arrays.asList(propertyLAN));
        Workplace wp2 = dataHelper.createPersistedWorkplace("Workplace 2", Arrays.asList(propertyLAN));
        Workplace wp3 = dataHelper.createPersistedWorkplace("Workplace 3", Arrays.asList(propertyLAN));
        room.addContainedFacility(wp1);
        room.addContainedFacility(wp2);
        building.addContainedFacility(room);
    }

    /**
	 * creates persisted reservations for the tests
	 */
    private void createReservations() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 1);
        Date start = calendar.getTime();
        calendar.add(Calendar.HOUR, 2);
        Date end = calendar.getTime();
        ArrayList<String> facilities = new ArrayList<String>();
        for (Facility f : room.getContainedFacilities()) {
            facilities.add(f.getId());
        }
        resUser1 = dataHelper.createPersistedReservation(USERID1, facilities, start, end);
        resUser2 = dataHelper.createPersistedReservation(USERID2, facilities, start, end);
        calendar.add(Calendar.MONTH, -2);
        start = calendar.getTime();
        calendar.add(Calendar.HOUR, 2);
        end = calendar.getTime();
        dataHelper.createPersistedReservation(USERID1, facilities, start, end);
    }

    /**
	 * sets the username in the security context
	 */
    private void setSecurityContext() {
        Authentication auth = new UsernamePasswordAuthenticationToken(USERID1, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
	 * tests the listReservations() method
	 */
    @Test
    public void testListReservations() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Model model = new BindingAwareModelMap();
        String view = reservationController.listReservations(model, request);
        assertEquals("reservation/list", view);
        Map<String, Object> modelAttributes = model.asMap();
        checkReservationCollection(modelAttributes, "reservations");
        checkReservationCollection(modelAttributes, "pastReservations");
        assertTrue(modelAttributes.get("deleteNotification") == null);
    }

    /**
	 * Tests the listReservations() method with delete notification
	 */
    @Test
    public void testListReservationsDeleteNotification() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("deleteNotification", "true");
        Model model = new BindingAwareModelMap();
        String view = reservationController.listReservations(model, request);
        assertEquals("reservation/list", view);
        Map<String, Object> modelAttributes = model.asMap();
        checkReservationCollection(modelAttributes, "reservations");
        checkReservationCollection(modelAttributes, "pastReservations");
        assertTrue(modelAttributes.get("deleteNotification") instanceof String);
        String deleteNotification = (String) modelAttributes.get("deleteNotification");
        assertEquals("true", deleteNotification);
    }

    /**
	 * tests the showReservationDetails() method
	 */
    @Test
    public void testShowReservationDetails() {
        Model model = new BindingAwareModelMap();
        String view = reservationController.showReservationDetails(model, resUser1.getId());
        assertEquals("reservation/details", view);
        Map<String, Object> modelAttributes = model.asMap();
        assertTrue(modelAttributes.get("reservation") instanceof ReservationModel);
        ReservationModel resModel = (ReservationModel) modelAttributes.get("reservation");
        assertEquals(resModel.getId(), resUser1.getId());
        assertEquals(resModel.getRoomName(), room.getName());
        assertEquals(resModel.getBuildingName(), room.getParentFacility().getName());
        assertEquals(resModel.getRoom().getId(), room.getId());
        assertEquals(resModel.getStartTime(), resUser1.getStartTime());
        assertEquals(resModel.getEndTime(), resUser1.getEndTime());
        assertEquals(resModel.getWorkplaceCount(), resUser1.getBookedFacilityIds().size());
    }

    /**
	 * tests the showReservationDetails() method using an invalid reservation id
	 */
    @Test
    public void testShowReservationDetailsInvalidReservation() {
        Model model = new BindingAwareModelMap();
        String wrongID = "xxx";
        String view = reservationController.showReservationDetails(model, wrongID);
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("errorReservationNotFound"));
    }

    /**
	 * tests the showReservationDetails() method using an empty reservation id
	 */
    @Test
    public void testShowReservationDetailsEmptyID() {
        Model model = new BindingAwareModelMap();
        String emptyID = "";
        String view = reservationController.showReservationDetails(model, emptyID);
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("errorReservationNotFound"));
    }

    /**
	 * tests the showReservationDetails() method using an reservation id belonging to another user's reservation
	 */
    @Test
    public void testShowReservationDetailsWrongUser() {
        Model model = new BindingAwareModelMap();
        String view = reservationController.showReservationDetails(model, resUser2.getId());
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("errorReservationNotFound"));
    }

    /**
	 * tests the deleteReservation method
	 */
    @Test
    public void testDeleteReservation() {
        String resId = testDeleteAddReservation();
        Model model = new BindingAwareModelMap();
        String view = reservationController.deleteReservations(model, resId);
        assertEquals("redirect:/reservation/list.html", view);
        assertTrue(model.containsAttribute("deleteNotification"));
        testListReservations();
    }

    /**
	 * tests the deleteReservation method with a wrong id
	 */
    @Test
    public void testDeleteReservationWrongId() {
        String wrongID = "xxx";
        Model model = new BindingAwareModelMap();
        String view = reservationController.deleteReservations(model, wrongID);
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("errorReservationNotFound"));
        assertFalse(model.containsAttribute("deleteNotification"));
    }

    /**
	 * tests the deleteReservation method with an empty id
	 */
    @Test
    public void testDeleteReservationEmptyId() {
        String emptyID = "";
        Model model = new BindingAwareModelMap();
        String view = reservationController.deleteReservations(model, emptyID);
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("errorReservationNotFound"));
        assertFalse(model.containsAttribute("deleteNotification"));
    }

    /**
	 * tests the deleteReservation method with another user's reservation id
	 */
    @Test
    public void testDeleteReservationWrongUser() {
        Model model = new BindingAwareModelMap();
        String view = reservationController.deleteReservations(model, resUser2.getId());
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("errorReservationNotFound"));
        assertFalse(model.containsAttribute("deleteNotification"));
    }

    /**
	 * adds a reservation for the delete test
	 * 
	 * @return the id of the new reservation
	 */
    private String testDeleteAddReservation() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 1);
        Date start = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date end = calendar.getTime();
        ArrayList<String> facilities = new ArrayList<String>();
        facilities.add(room.getId());
        Reservation reservation = dataHelper.createPersistedReservation(USERID1, facilities, start, end);
        return reservation.getId();
    }

    /**
	 * checks the reservation collection with the given attribute name in the model attributes
	 * 
	 * @param modelAttributes
	 *            the model attributes containing the collection
	 * @param attributeName
	 *            the attribute name of the collection to check
	 */
    private void checkReservationCollection(Map<String, Object> modelAttributes, String attributeName) {
        assertTrue("model does not contain " + attributeName, modelAttributes.containsKey(attributeName));
        assertTrue(attributeName + " is not a collection", modelAttributes.get(attributeName) instanceof Collection<?>);
        Collection<?> reservations = (Collection<?>) modelAttributes.get(attributeName);
        for (Object o : reservations) {
            assertTrue(attributeName + " contains wrong type", o instanceof ReservationModel);
        }
        assertEquals(1, reservations.size());
    }

    /**
	 * updated the reservation with the given id using the given values and returns the reservation model of the updated
	 * reservation
	 * 
	 * @param reservationID
	 *            the id of the reservation to update
	 * @param endTime
	 *            the updated end time
	 * @param wpCount
	 *            the updated workplace count
	 * @return the Reservation Model
	 */
    private ReservationModel updateReservation(String reservationID, Date endTime, int wpCount) {
        Model model = new BindingAwareModelMap();
        ReservationModel updatedReservation = new ReservationModel();
        updatedReservation.setEndTime(endTime);
        updatedReservation.setWorkplaceCount(wpCount);
        BindingResult updatedReservationResult = new BeanPropertyBindingResult(updatedReservation, "updatedReservation");
        String view = reservationController.updateReservation(model, reservationID, updatedReservation, updatedReservationResult);
        assertEquals("reservation/details", view);
        assertFalse(model.containsAttribute("formErrors"));
        Map<String, Object> modelAttributes = model.asMap();
        assertTrue(modelAttributes.get("reservation") instanceof ReservationModel);
        ReservationModel resModel = (ReservationModel) modelAttributes.get("reservation");
        assertEquals(room.getName(), resModel.getRoomName());
        assertEquals(room.getParentFacility().getName(), resModel.getBuildingName());
        assertEquals(room.getId(), resModel.getRoom().getId());
        assertEquals(reservationID, resModel.getId());
        assertEquals(endTime, resModel.getEndTime());
        return resModel;
    }

    /**
	 * Tests the updatedReservationTest updating the workplace count
	 */
    @Test
    public void testUpdateWpCount() {
        int wpCountBefore = resUser1.getBookedFacilityIds().size();
        ReservationModel resModel = updateReservation(resUser1.getId(), resUser1.getEndTime(), wpCountBefore - 1);
        assertEquals(wpCountBefore - 1, resModel.getWorkplaceCount());
        assertEquals(resUser1.getStartTime(), resModel.getStartTime());
    }

    /**
	 * Tests the updatedReservationTest updating the end time
	 */
    @Test
    public void testUpdateEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(resUser1.getEndTime());
        calendar.add(Calendar.MINUTE, -30);
        Date newEndTime = calendar.getTime();
        ReservationModel resModel = updateReservation(resUser1.getId(), newEndTime, resUser1.getBookedFacilityIds().size());
        assertEquals(resModel.getWorkplaceCount(), resModel.getWorkplaceCount());
        assertEquals(resModel.getStartTime(), resUser1.getStartTime());
    }

    /**
	 * Tests the updatedReservationTest updating both workplace count and the end time
	 */
    @Test
    public void testUpdateBoth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(resUser1.getEndTime());
        calendar.add(Calendar.MINUTE, -30);
        Date newEndTime = calendar.getTime();
        int wpCountBefore = resUser1.getBookedFacilityIds().size();
        ReservationModel resModel = updateReservation(resUser1.getId(), newEndTime, wpCountBefore - 1);
        assertEquals(resModel.getStartTime(), resUser1.getStartTime());
        assertEquals(wpCountBefore - 1, resModel.getWorkplaceCount());
    }

    /**
	 * Tests the updatedReservationTest updating the workplace count
	 */
    @Test
    public void testUpdateRoomReservation() {
        Reservation reservation = createRoomReservation();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservation.getEndTime());
        calendar.add(Calendar.MINUTE, 30);
        Date newEndTime = calendar.getTime();
        int wpCountBefore = room.getContainedFacilities().size();
        ReservationModel resModel = updateReservation(reservation.getId(), newEndTime, 0);
        assertEquals(wpCountBefore, resModel.getWorkplaceCount());
        assertEquals(resModel.getStartTime(), reservation.getStartTime());
    }

    /**
	 * creates a reservation containing a room and returns its ID
	 * 
	 * @return the ID of the reservation
	 */
    private Reservation createRoomReservation() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 2);
        Date start = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date end = calendar.getTime();
        ArrayList<String> facilities = new ArrayList<String>();
        facilities.add(room.getId());
        Reservation reservation = dataHelper.createPersistedReservation(USERID1, facilities, start, end);
        return reservation;
    }

    /**
	 * Tests the updatedReservationTest using wrong values in the model
	 */
    @Test
    public void testUpdateWrongValues() {
        Model model = new BindingAwareModelMap();
        int wpCountBefore = resUser1.getBookedFacilityIds().size();
        ReservationModel updatedReservation = new ReservationModel();
        updatedReservation.setEndTime(resUser1.getStartTime());
        updatedReservation.setWorkplaceCount(wpCountBefore + 1);
        BindingResult updatedReservationResult = new BeanPropertyBindingResult(updatedReservation, "updatedReservation");
        String view = reservationController.updateReservation(model, resUser1.getId(), updatedReservation, updatedReservationResult);
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("formErrors"));
        List<ObjectError> errors = updatedReservationResult.getAllErrors();
        ArrayList<String> errorFields = new ArrayList<String>();
        for (ObjectError error : errors) {
            String field = error.getCode().split("\\.")[0];
            errorFields.add(field);
        }
        assertEquals(2, errors.size());
        assertTrue("wrong end date value was not detected", errorFields.contains("endTime"));
        assertTrue("wrong workplace value count was not detected", errorFields.contains("workplaceCount"));
        Map<String, Object> modelAttributes = model.asMap();
        assertTrue(modelAttributes.get("reservation") instanceof ReservationModel);
        ReservationModel resModel = (ReservationModel) modelAttributes.get("reservation");
        assertEquals(resModel.getId(), resUser1.getId());
        assertEquals(resModel.getRoomName(), room.getName());
        assertEquals(resModel.getBuildingName(), room.getParentFacility().getName());
        assertEquals(resModel.getRoom().getId(), room.getId());
        assertEquals(resModel.getStartTime(), resUser1.getStartTime());
        assertEquals(resModel.getEndTime(), resUser1.getEndTime());
        assertEquals(resModel.getWorkplaceCount(), wpCountBefore);
    }

    /**
	 * Tests the updatedReservationTest trying to use an empty id
	 */
    @Test
    public void testUpdateReservationEmptyID() {
        testUpdateReservation("");
    }

    /**
	 * Tests the updatedReservationTest trying to use an wrong id
	 */
    @Test
    public void testUpdateReservationWrongID() {
        testUpdateReservation("wrong-id");
    }

    /**
	 * Tests the updatedReservationTest trying to use another user's reservation id
	 */
    @Test
    public void testUpdateReservationWrongUser() {
        testUpdateReservation(resUser2.getId());
    }

    /**
	 * tests for the updateReservation method, using a reservation id which is expected to fail the update
	 */
    private void testUpdateReservation(String wrongReservationID) {
        Model model = new BindingAwareModelMap();
        ReservationModel updatedReservation = new ReservationModel();
        BindingResult updatedReservationResult = new BeanPropertyBindingResult(updatedReservation, "updatedReservation");
        String view = reservationController.updateReservation(model, wrongReservationID, updatedReservation, updatedReservationResult);
        assertEquals("reservation/details", view);
        assertTrue(model.containsAttribute("errorReservationNotFound"));
    }
}
