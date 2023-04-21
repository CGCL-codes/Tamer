package bom;

import org.nakedobjects.applib.annotation.Disabled;

public interface Scheduler {

    @Disabled
    void checkAvailability(Booking booking);

    @Disabled
    void makeReservation(Booking booking);
}
