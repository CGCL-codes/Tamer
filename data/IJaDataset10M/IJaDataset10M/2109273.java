package com.safi.asterisk.handler.mbean;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import com.safi.asterisk.handler.SafletEngine;

public class SafiWorkshopAppender extends AppenderSkeleton {

    /**
   * We remember host name as String in addition to the resolved InetAddress so that it
   * can be returned via getOption().
   */
    boolean locationInfo = false;

    public SafiWorkshopAppender() {
    }

    /**
   * Connect to the specified <b>RemoteHost</b> and <b>Port</b>.
   */
    public void activateOptions() {
    }

    /**
   * Close this appender.
   * 
   * <p>
   * This will mark the appender as closed and call then {@link #cleanUp} method.
   */
    public synchronized void close() {
        if (closed) return;
        this.closed = true;
        cleanUp();
    }

    public void cleanUp() {
    }

    public void append(LoggingEvent event) {
        if (event == null) return;
        if (locationInfo) {
            event.getLocationInformation();
        }
        try {
            SafletEngine.getInstance().getServerMonitor().postLogEvent(event);
        } catch (Exception e) {
            LogLog.error("Couldn't send notifcation to SafiWorkshop", e);
        }
    }

    /**
   * The <b>LocationInfo</b> option takes a boolean value. If true, the information sent
   * to the remote host will include location information. By default no location
   * information is sent to the server.
   */
    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    /**
   * Returns value of the <b>LocationInfo</b> option.
   */
    public boolean getLocationInfo() {
        return locationInfo;
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
