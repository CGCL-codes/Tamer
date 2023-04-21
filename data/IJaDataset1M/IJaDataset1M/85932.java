package com.liferay.portlet.calendar;

import com.liferay.portal.PortalException;

/**
 * <a href="EventEndDateException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class EventEndDateException extends PortalException {

    public EventEndDateException() {
        super();
    }

    public EventEndDateException(String msg) {
        super(msg);
    }

    public EventEndDateException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EventEndDateException(Throwable cause) {
        super(cause);
    }
}
