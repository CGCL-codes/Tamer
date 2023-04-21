package be.kuleuven.cs.mop.domain.model;

import java.util.Calendar;

/**
 * Interface representing a reservation
 */
public interface Reservation {

    /**
	 * Returns the (start) date of the {@link Reservation}
	 */
    public Calendar getDate();

    /**
	 * Returns the duration of the {@link Reservation}
	 */
    public long getDuration();

    /**
	 * Returns the end date of the {@link Reservation}
	 */
    public Calendar getEnd();
}
