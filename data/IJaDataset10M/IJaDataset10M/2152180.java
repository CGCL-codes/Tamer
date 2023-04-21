package net.sf.buildbox.worker.localjobs;

import java.io.File;
import java.util.Set;
import java.util.Collection;

public interface ReservationsDao {

    public void reserveDir(File dir, long duration);

    public Reservation get(File dir);

    public Collection<Reservation> listReservations();

    public Collection<Reservation> listExpiredReservations();

    public void cancel(Reservation reservation);

    public void markUpdated(File dir);
}
