package com.andre.conferenceroom.model.impl;

import com.andre.conferenceroom.util.Utils;
import com.andre.conferenceroom.model.Reservation;

/**
 * The model implementation for the Reservation service. Represents a row in the &quot;RESERVATION&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.andre.conferenceroom.model.Reservation} interface.
 * </p>
 *
 * <p>
 * Never reference this class directly. All methods that expect a reservation model instance should use the {@link Reservation} interface instead.
 * </p>
 */
public class ReservationImpl extends ReservationModelImpl implements Reservation {

    public ReservationImpl() {
    }

    public String getDateFormatted() {
        return Utils.formatDate(getDate());
    }

    public String getStartFormatted() {
        return Utils.formatTime(getStart());
    }

    public String getEndFormatted() {
        return Utils.formatTime(getEnd());
    }
}
