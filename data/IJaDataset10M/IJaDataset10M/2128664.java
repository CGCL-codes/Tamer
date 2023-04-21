package edu.kit.pse.ass.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Class Reservation.
 * 
 * @author Sebastian
 */
@Entity(name = "t_reservation")
public class Reservation {

    /** the unique id of the reservation. */
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Id
    private String id;

    /** the beginning time of the reservation. */
    private Date startTime;

    /** the end time of the reservation. */
    private Date endTime;

    /** the id of the user, who does the reservation. */
    private String bookingUserId;

    /** the collection of facility ids of the facilities, which are booked. */
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Collection<String> bookedFacilityIds;

    /**
	 * Creates a new reservation without values.
	 */
    public Reservation() {
        bookedFacilityIds = new LinkedList<String>();
    }

    /**
	 * Creates a new reservation with the specified values.
	 * 
	 * @param startTime
	 *            the time the reservation starts.
	 * @param endTime
	 *            the time the reservation ends.
	 * @param bookingUserId
	 *            the id of the user, which does the reservation
	 * @throws IllegalArgumentException
	 *             startTime is after endTime or one of them is null or bookingUserId is null or empty.
	 */
    public Reservation(Date startTime, Date endTime, String bookingUserId) throws IllegalArgumentException {
        setStartTime(startTime);
        setEndTime(endTime);
        setBookingUserId(bookingUserId);
        bookedFacilityIds = new LinkedList<String>();
    }

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * Gets the start time.
	 * 
	 * @return the startTime of the reservation
	 */
    public Date getStartTime() {
        return startTime;
    }

    /**
	 * Sets the start time.
	 * 
	 * @param startTime
	 *            the startTime to set
	 * @throws IllegalArgumentException
	 *             startTime is null or startTime is after endTime
	 */
    public void setStartTime(Date startTime) throws IllegalArgumentException {
        if (startTime == null) {
            throw new IllegalArgumentException("startTime must not be null.");
        }
        if (endTime != null && startTime.after(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        this.startTime = startTime;
    }

    /**
	 * Gets the end time.
	 * 
	 * @return the endTime of the reservation
	 */
    public Date getEndTime() {
        return endTime;
    }

    /**
	 * Sets the end time.
	 * 
	 * @param endTime
	 *            the end time of the reservation to set
	 * @throws IllegalArgumentException
	 *             endTime is null or endTime is before startTime
	 */
    public void setEndTime(Date endTime) throws IllegalArgumentException {
        if (endTime == null) {
            throw new IllegalArgumentException("endTime must not be null");
        }
        if (startTime != null && endTime.before(startTime)) {
            throw new IllegalArgumentException("end time must be after start time");
        }
        this.endTime = endTime;
    }

    /**
	 * Gets the booking user id.
	 * 
	 * @return the id of the booking user
	 */
    public String getBookingUserId() {
        return bookingUserId;
    }

    /**
	 * Sets the booking user id.
	 * 
	 * @param bookingUserId
	 *            the id of the user, which does this reservation, to set
	 * @throws IllegalArgumentException
	 *             bookingUserID is empty or null
	 */
    public void setBookingUserId(String bookingUserId) throws IllegalArgumentException {
        if (bookingUserId == null || bookingUserId.isEmpty()) {
            throw new IllegalArgumentException("bookingUserId must not be null or empty");
        }
        this.bookingUserId = bookingUserId;
    }

    /**
	 * Gets the booked facility IDs.
	 * 
	 * @return the IDs of the booked facilities
	 */
    public Collection<String> getBookedFacilityIds() {
        return bookedFacilityIds;
    }

    /**
	 * Sets the booked facility IDs.
	 * 
	 * @param facilityIDs
	 *            the new booked facility IDs
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
    public void setBookedFacilityIDs(Collection<String> facilityIDs) throws IllegalArgumentException {
        if (facilityIDs == null) {
            throw new IllegalArgumentException("facilityIDs must be not null");
        }
        this.bookedFacilityIds = facilityIDs;
    }

    /**
	 * Adds a facility to this reservation.
	 * 
	 * @param facilityID
	 *            the id of the facility to add
	 * @throws IllegalArgumentException
	 *             facility id is null or empty
	 */
    public void addBookedFacilityId(String facilityID) throws IllegalArgumentException {
        if (facilityID == null || facilityID.isEmpty()) {
            throw new IllegalArgumentException("facilityID must not be null or empty");
        }
        if (bookedFacilityIds.contains(facilityID)) {
            throw new IllegalArgumentException("facilityID was added a long time ago ;-)");
        }
        bookedFacilityIds.add(facilityID);
    }

    /**
	 * Removes a facility of this reservation.
	 * 
	 * @param facilityID
	 *            the id of the facility to remove
	 * @throws IllegalArgumentException
	 *             facilityID is null or empty
	 */
    public void removeBookedFacilityId(String facilityID) throws IllegalArgumentException {
        if (facilityID == null || facilityID.isEmpty()) {
            throw new IllegalArgumentException("facilityID must be not null or empty");
        }
        if (!getBookedFacilityIds().contains(facilityID)) {
            throw new IllegalArgumentException("the facility you want to remove is not booked in this reservation");
        }
        ArrayList<String> facilityIDs = new ArrayList<String>(bookedFacilityIds);
        facilityIDs.remove(facilityID);
        setBookedFacilityIDs(facilityIDs);
    }
}
