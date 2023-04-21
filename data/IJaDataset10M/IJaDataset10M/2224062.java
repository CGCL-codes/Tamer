package org.rapla.facade;

import java.util.HashMap;
import java.util.Map;
import org.rapla.entities.User;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;

public class AllocationChangeEvent {

    static Map TYPES = new HashMap();

    public static class Type {

        private String type;

        Type(String type) {
            TYPES.put(type, this);
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }

    Type m_type;

    public static Type CHANGE = new Type("change");

    public static Type ADD = new Type("add");

    public static Type REMOVE = new Type("remove");

    User m_user;

    Reservation m_newReservation;

    Allocatable m_allocatable;

    Appointment m_newAppointment;

    Appointment m_oldAppointment;

    public AllocationChangeEvent(Type type, User user, Reservation newReservation, Allocatable allocatable, Appointment appointment) {
        m_user = user;
        m_type = type;
        m_allocatable = allocatable;
        if (type.equals(REMOVE)) m_oldAppointment = appointment;
        m_newAppointment = appointment;
        m_newReservation = newReservation;
    }

    public AllocationChangeEvent(User user, Reservation newReservation, Allocatable allocatable, Appointment newAppointment, Appointment oldApp) {
        this(CHANGE, user, newReservation, allocatable, newAppointment);
        m_oldAppointment = oldApp;
    }

    /** either Type.CHANE,Type.REMOVE or Type.ADD */
    public Type getType() {
        return m_type;
    }

    /** returns the user-object, of the user that made the change.
     *  <strong>Warning can be null</strong>
     */
    public User getUser() {
        return m_user;
    }

    public Allocatable getAllocatable() {
        return m_allocatable;
    }

    public Appointment getNewAppointment() {
        return m_newAppointment;
    }

    public Reservation getNewReservation() {
        return m_newReservation;
    }

    /** only available if type is "change" */
    public Appointment getOldAppointment() {
        return m_oldAppointment;
    }
}
