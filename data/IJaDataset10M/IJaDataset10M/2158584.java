package co.edu.unal.ungrid.image.dicom.display.event;

import co.edu.unal.ungrid.image.dicom.event.Event;

/**
 * 
 */
public class StatusChangeEvent extends Event {

    /**
	 * @uml.property name="statusMessage"
	 */
    private String statusMessage;

    /**
	 * @param statusMessage
	 */
    public StatusChangeEvent(String statusMessage) {
        super();
        this.statusMessage = statusMessage;
    }

    /**
	 * @uml.property name="statusMessage"
	 */
    public String getStatusMessage() {
        return statusMessage;
    }
}
