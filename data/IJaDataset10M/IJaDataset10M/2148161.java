package org.granite.gravity.tomcat;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometEvent.EventSubType;
import org.apache.catalina.CometEvent.EventType;

/**
 * @author Franck WOLFF
 */
public class EventUtil {

    public static boolean isTimeout(CometEvent event) {
        return (event.getEventType() == EventType.ERROR && event.getEventSubType() == EventSubType.TIMEOUT);
    }

    public static boolean isError(CometEvent event) {
        return (event.getEventType() == EventType.ERROR);
    }

    public static boolean isErrorButNotTimeout(CometEvent event) {
        return (event.getEventType() == EventType.ERROR && event.getEventSubType() != EventSubType.TIMEOUT);
    }

    public static boolean isValid(CometEvent event) {
        if (event != null) {
            try {
                return event.getHttpServletRequest() != null && event.getHttpServletResponse() != null;
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static String toString(CometEvent event) {
        if (event == null) return "null";
        try {
            return event.getClass().getName() + " [" + event.getEventType() + '.' + event.getEventSubType() + ']';
        } catch (Exception e) {
            return e.toString();
        }
    }
}
