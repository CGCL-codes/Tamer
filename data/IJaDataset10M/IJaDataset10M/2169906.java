package com.patientis.client.state;

import com.patientis.model.patient.PatientModel;

/**
 * One line class description
 *
 * 
 *   
 */
public class ControllerState {

    /**
	 * Patient selected or openeed for controller
	 */
    private PatientModel patient = null;

    /**
	 * @return the patient
	 */
    public PatientModel getPatient() {
        return patient;
    }

    /**
	 * @param patient the patient to set
	 */
    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    /**
	 * Controller has a patient
	 * 
	 * @return
	 */
    public boolean hasPatient() {
        return patient != null;
    }

    /**
	 * Clear selected patient
	 * 
	 * @return
	 */
    public void clearPatient() {
        patient = null;
    }
}
