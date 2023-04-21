package org.tolven.mobile.client.view.list;

import java.util.Vector;
import org.tolven.mobile.client.control.Controller;
import org.tolven.mobile.client.data.Appointment;
import org.tolven.mobile.client.data.MenuData;
import org.tolven.mobile.client.data.Patient;

public class AppointmentsPage extends ListPage {

    Patient patient;

    public AppointmentsPage(Patient patient, Controller controller) {
        super("Appointments", controller);
        this.patient = patient;
    }

    /**
	 * Provide the query string
	 * @return
	 */
    public String getQuery() {
        return getController().getApplication().getAccountType() + ":patient-" + patient.getId() + ":appointments:future";
    }

    public MenuData createItem(String line) {
        return new Appointment(line);
    }

    public Vector getList() {
        return patient.getAppointments();
    }
}
