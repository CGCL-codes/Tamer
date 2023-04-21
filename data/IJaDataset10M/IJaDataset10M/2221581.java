package OfficeServer.office;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import OfficeServer.log_error.AppointmentException;
import OfficeServer.report.Appointment;
import OfficeServer.users.Doctor;

/**
 * @author mramsey3
 * 
 */
public class Day implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<Boolean[]> schedule = new ArrayList<Boolean[]>();

    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();

    public Day() {
        discoverNewDoctor();
    }

    public void discoverNewDoctor() {
        int numDoctors = Doctor.getCounter();
        for (int i = schedule.size(); i < numDoctors; i++) {
            schedule.add(new Boolean[8]);
            for (int j = 0; j < 8; j++) {
                schedule.get(schedule.size() - 1)[j] = true;
            }
        }
    }

    public void addAppointment(Appointment appointment) throws AppointmentException {
        boolean flag = false;
        if (!appointments.contains(appointment)) {
            appointments.add(appointment);
            if (schedule.get(appointment.getDoctorID())[appointment.getTimeSlot()] == true) {
                schedule.get(appointment.getDoctorID())[appointment.getTimeSlot()] = false;
            } else {
                throw (new AppointmentException(Level.INFO, AppointmentException.REASON.NO_AVALIABLE, ""));
            }
        }
    }

    public void removeAppointment(Appointment appointment) throws AppointmentException {
        boolean flag = false;
        if (appointments.contains(appointment)) {
            appointments.remove(appointment);
            schedule.get(appointment.getDoctorID())[appointment.getTimeSlot()] = true;
        } else {
            throw (new AppointmentException(Level.INFO, AppointmentException.REASON.NULL_APPOINTMENT, ""));
        }
    }

    /**
     * Returns the ArrayList of Appointments
     * 
     * @return result which the ArrayList of all the appointments on the day
     */
    public ArrayList<Appointment> getAppointments() {
        ArrayList<Appointment> result = new ArrayList<Appointment>();
        for (Appointment i : this.appointments) {
            result.add(i.clone());
        }
        return result;
    }

    public boolean[][] getAvaliability() {
        boolean result[][] = new boolean[8][schedule.size()];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < schedule.size(); j++) {
                result[i][j] = schedule.get(j)[i];
            }
        }
        return result;
    }

    public void clear() {
        try {
            schedule.clear();
            appointments.clear();
        } catch (Exception e) {
            schedule = new ArrayList<Boolean[]>();
            appointments = new ArrayList<Appointment>();
        }
    }
}
