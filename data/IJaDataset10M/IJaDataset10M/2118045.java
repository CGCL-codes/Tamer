package net.sf.borg.model.db.remote;

import java.util.Collection;
import net.sf.borg.model.beans.Appointment;
import net.sf.borg.model.db.AppointmentDB;

/**
 * @author Mohan Embar
 */
public class ApptRemoteBeanDB extends RemoteBeanDB implements AppointmentDB {

    public ApptRemoteBeanDB() {
        super(Appointment.class, "Appointment");
    }

    public Collection getTodoKeys() throws Exception {
        return (Collection) call("getTodoKeys", null);
    }

    public Collection getRepeatKeys() throws Exception {
        return (Collection) call("getRepeatKeys", null);
    }
}
