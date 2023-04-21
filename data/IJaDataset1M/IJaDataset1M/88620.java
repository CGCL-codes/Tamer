package edu.infosys.FreeFoodFinder.client;

import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @see edu.infosys.FreeFoodFinder.server.EventsServiceImpl
 * @author John McCarthy jhm9235@rit.edu
 *
 */
@RemoteServiceRelativePath("events")
public interface EventsService extends RemoteService {

    /**
	 * Create a new Event
	 * 
	 * @param title
	 *            the title of the event
	 * @param locationDesc
	 *            A text description of where the event will take place
	 * @param latitude
	 *            The latitude of the event location
	 * @param longitude
	 *            The longitutude of the event location
	 * @param foodDescription
	 *            A description of the food
	 * @param startTime
	 *            The start time of the event
	 * @param endTime
	 *            The end time of the event
	 * @param date
	 *            The day the event will take place
	 * @param creatorEmail
	 *            The e-mail address of the person who created it
	 * @param sponsor
	 *            The person or organization sponsoring the event
	 * @param privateEvent
	 *            Whether the event is private or not
	 */
    public void createNewEvent(String title, String locationDesc, double latitude, double longitude, String foodDescription, Date startTime, Date endTime, Date date, String creatorEmail, String sponsor, boolean privateEvent);

    /**
	 * Get all of the Events
	 * 
	 * @return A list of Events
	 */
    public List<Event> getAllEvents();

    /**
	 * Subscribe to the provided event
	 * 
	 * @param toSubscribeTo
	 *            the event to subscribe to
	 * @param the
	 *            e-mail address of the user
	 */
    public void subscribe(Event toSubscribeTo, String email);

    /**
	 * Unsubscribe to the provided event
	 * 
	 * @param toUnsubscribeFrom
	 *            the event to unsubscribe from. If the user is not currently
	 *            subscribed, no error occurs
	 * @param the
	 *            e-mail address of the user
	 */
    public void unsubscribe(Event toUnsubscribeFrom, String email);

    public Event addReviewToEvent(int rating, String comment, String creatorEmail, String eventID);

    public Event thumbsUpReview(String reviewID, String eventID);

    public Event thumbsDownReview(String reviewID, String eventID);
}
