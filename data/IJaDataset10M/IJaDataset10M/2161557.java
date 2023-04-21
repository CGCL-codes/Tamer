package org.ultracalendar.appointment_system;

import java.util.List;
import javax.ejb.Remote;
import org.ultracalendar.appointment_management.Appointment;
import org.ultracalendar.login_system.LoginException;
import org.ultracalendar.login_system.UserDoesNotExistException;
import org.ultracalendar.user_management.User;

@Remote
public interface ITodaysAppointments {

    public List<Appointment> getTodaysAppointments(User user) throws UserDoesNotExistException, LoginException;
}
