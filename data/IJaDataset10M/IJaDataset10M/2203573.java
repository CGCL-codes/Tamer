package com.patientis.model.scheduling;

/**
 * AppointmentStatus
 * 
 */
public class AppointmentStatusModel extends AppointmentStatusDataModel {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public AppointmentStatusModel() {
    }

    public boolean hasForegroundColor() {
        return getForegroundColor() != 0;
    }

    public boolean hasBackgroundColor() {
        return getBackgroundColor() != 0;
    }

    public boolean hasBorderColor() {
        return getBorderColor() != 0;
    }
}
