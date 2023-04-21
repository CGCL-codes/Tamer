package org.rapla.plugin.extendedtableview;

import java.util.ArrayList;
import java.util.List;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentBlock;
import org.rapla.entities.domain.Reservation;

/**
 * This class provides methods to calculate the total duration in minutes of an
 * appointment, an array of appointments, a reservation, or an array of
 * reservations. The return values are long values to assure that they can be
 * used by the DateTools class.
 * 
 * @author Alexandre Acker
 * 
 */
public abstract class DurationCalculator {

    /**
     * This method calculates the total duration of an array of reservations in
     * minutes.
     * 
     * @param reservations
     *            , reservations whose duration will be calculated
     * @return duration of the reservations as a long value representing minutes
     */
    public static long calculateDuration(Reservation[] reservations) {
        long duration = 0;
        for (Reservation reservation : reservations) {
            duration += calculateDuration(reservation);
        }
        return duration;
    }

    /**
     * This method calculates the total duration of a reservation in minutes.
     * 
     * @param reservation
     *            , reservation whose duration will be calculated
     * @return duration of the reservation as a long value representing minutes
     */
    public static long calculateDuration(Reservation reservation) {
        return calculateDuration(reservation.getAppointments());
    }

    /**
     * This method calculates the total duration of an array of appointments in
     * minutes.
     * 
     * @param appointments
     *            , appointments whose duration will be calculated
     * @return duration of the appointments as a long value representing minutes
     */
    public static long calculateDuration(Appointment[] appointments) {
        long duration = 0;
        for (Appointment appointment : appointments) {
            duration += calculateDuration(appointment);
        }
        return duration;
    }

    /**
     * This method calculates the total duration of an appointment in minutes.
     * 
     * @param appointment, appointment whose duration will be calculated
     * @return duration of the appointment as a long value representing minutes
     */
    public static long calculateDuration(Appointment appointment) {
        long duration = 0;
        if (appointment.getRepeating() != null && appointment.getRepeating().getEnd() == null) {
            return 999999;
        }
        if (appointment.getRepeating() == null) {
            duration = DateTools.countMinutes(appointment.getStart(), appointment.getEnd());
        } else {
            List<AppointmentBlock> splits = new ArrayList<AppointmentBlock>();
            appointment.createBlocks(appointment.getStart(), DateTools.fillDate(appointment.getMaxEnd()), splits);
            for (AppointmentBlock block : splits) {
                duration += DateTools.countMinutes(block.getStart(), block.getEnd());
            }
        }
        return duration;
    }
}
