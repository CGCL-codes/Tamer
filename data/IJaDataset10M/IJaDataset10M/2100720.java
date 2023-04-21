package edu.kit.pse.ass.gui.controller;

import java.util.Collection;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import edu.kit.pse.ass.facility.management.FacilityNotFoundException;
import edu.kit.pse.ass.gui.model.BookingFormModel;
import edu.kit.pse.ass.gui.model.CalendarParamModel;
import edu.kit.pse.ass.gui.model.Event;
import edu.kit.pse.ass.gui.model.SearchFilterModel;
import edu.kit.pse.ass.gui.model.SearchFormModel;

/**
 * The RoomDetailController sets up the room details page and handles book requests.
 * 
 * @author Oliver Schneider
 */
public interface RoomDetailController {

    /**
	 * This method is called to display the RoomDetailsPage.
	 * 
	 * @param roomId
	 *            the room id to display details of
	 * @param model
	 *            the spring model
	 * @param searchFormModel
	 *            the SearchFormModel filled at SearchPage
	 * @param searchFilterModel
	 *            the SearchFilterModel filled at SearchPage
	 * @param bookingFormModel
	 *            the BookingFormModel if filled at RoomDetailsPage
	 * @return the view path
	 */
    @RequestMapping(value = "room/{roomId}/details.html", method = { RequestMethod.GET })
    String setUpRoomDetails(@PathVariable("roomId") String roomId, Model model, @ModelAttribute SearchFormModel searchFormModel, @ModelAttribute SearchFilterModel searchFilterModel, @ModelAttribute BookingFormModel bookingFormModel);

    /**
	 * This method is called when the BookingForm at the RoomDetailsPage was filled and submitted to the server.
	 * 
	 * @param roomId
	 *            the room id to display details of
	 * @param model
	 *            the spring model
	 * @param bookingFormModel
	 *            the BookingFormModel if filled at RoomDetailsPage
	 * @param bookingFormModelResult
	 *            result for error binding while booking
	 * @param searchFormModel
	 *            the SearchFormModel filled at AdvanceSearchPage
	 * @param searchFilterModel
	 *            the SearchFilterModel filled at AdvanceSearchPage
	 * @return the view path
	 * @throws FacilityNotFoundException
	 *             to be handled by general error page
	 * @throws IllegalStateException
	 *             to be handled by general error page
	 * @throws IllegalArgumentException
	 *             to be handled by general error page
	 */
    @RequestMapping(value = "room/{roomId}/details.html", method = { RequestMethod.POST })
    String book(@PathVariable("roomId") String roomId, Model model, @ModelAttribute BookingFormModel bookingFormModel, BindingResult bookingFormModelResult, @ModelAttribute SearchFormModel searchFormModel, @ModelAttribute SearchFilterModel searchFilterModel) throws IllegalArgumentException, IllegalStateException, FacilityNotFoundException;

    /**
	 * JSON return for the occupancy of the room
	 * 
	 * @param roomId
	 *            the room id to show occupancy of
	 * @param calendarParamModel
	 *            the Parameters of the jQuery calendar
	 * @return JSON response: a list of events to display
	 */
    @RequestMapping(value = "room/{roomId}/calendar.html")
    @ResponseBody
    Collection<Event> showBookableOccupancy(@PathVariable("roomId") String roomId, @ModelAttribute CalendarParamModel calendarParamModel);
}
