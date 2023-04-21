package es.ehrflex.client.main.event;

import com.extjs.gxt.ui.client.mvc.AppEvent;

/**
 * An object of this class represents an happened event in the UI.
 * 
 * @author Anton Brass
 * @version 1.0, 11.05.2009
 */
public class EHRflexEvent extends AppEvent {

    public EHRflexEventType mEventType;

    /**
	 * Setting the event type
	 * 
	 * @param pEventType
	 *            an event type
	 */
    public EHRflexEvent(EHRflexEventType pEventType) {
        super(pEventType.getEventType());
        mEventType = pEventType;
    }

    /**
	 * Setting the event type and data
	 * 
	 * @param pEventType
	 *            an event type and data
	 */
    public EHRflexEvent(EHRflexEventType pEventType, Object data) {
        super(pEventType.getEventType(), data);
        mEventType = pEventType;
    }

    /**
	 * "Event Type: " + mEventType;
	 */
    public String toString() {
        return "Event Type: " + mEventType;
    }
}
