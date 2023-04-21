package com.incendiaryblue.tracking;

import com.incendiaryblue.logging.LogEvent;
import java.util.*;

/**
 * Title:       Tracking Log Event
 * Description: Represents a site tracking event generated during site usage.
 * Copyright:   Copyright (c) 2002
 * Company:     Syzygy
 * @author      Nick Sidhu
 * @version     0.1
 */
public class TrackingLogEvent implements LogEvent {

    /** Event code of all Graham log events. */
    private static final String EVENT_CODE = "SYZYGY_TRACKING_EVENT";

    /** Event type recorded when a user begins a session on the site. */
    public static final int EVENT_INIT_USER = 0;

    /**	Event type recorded when a user has successfully logged in. */
    public static final int EVENT_LOGIN = -1;

    /** The tracking ID of the user on the site at the time this event was
	 *  generated. */
    private String trackingID;

    /** Parameters supporting the event. */
    private String[] params;

    /** Specifies the type of log event that occurred. */
    private int logEventType;

    /** Time at which the event occurred. */
    private Date eventTime;

    /**
	 *  Constructs a new tracking log event of the given type.
	 *
	 *  @param  logEventType    The type of Graham log event that occurred.
	 *                          This should be one of the EVENT constants
	 *                          defined by this class.
	 *  @param  params          An array of Strings which are any parameters
	 *                          associated with the given event. The array need
	 *                          only be as long as the number of parameters.
	 *  @param  trackingID      Tracking ID (typically found in the session
	 *                          TrackingInfo object) at the time the event
	 *                          occurred.
	 */
    public TrackingLogEvent(int logEventType, String[] params, String trackingID) {
        this.logEventType = logEventType;
        this.params = params;
        this.trackingID = trackingID;
    }

    /**
	 *  @return The log event code, which is EVENT_CODE.
	 */
    public String getEventCode() {
        return EVENT_CODE;
    }

    /**
	 *  @return The type code of this event. This equates to one of the
	 *          EVENT constants declared by this class.
	 */
    public int getEventType() {
        return this.logEventType;
    }

    /**
	 *  @return The parameters associated with this event.
	 */
    public String[] getParams() {
        return this.params;
    }

    /**
	 *  @return The tracking ID of this event.
	 */
    public String getTrackingID() {
        return this.trackingID;
    }
}
