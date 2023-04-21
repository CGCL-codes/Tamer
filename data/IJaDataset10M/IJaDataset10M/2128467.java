package org.ochan.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * 
 * @author David Seymore Jul 27, 2008
 */
@ManagedResource(objectName = "System:type=statistics,name=UserCounter", description = "Keeps tabs on users on this host", log = true, logFile = "jmx.log")
public class UserCounter implements HttpSessionListener {

    private static int sessions = 0;

    /**
	 * 
	 */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        sessions++;
    }

    /**
	 * 
	 */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (sessions > 0) {
            sessions--;
        }
    }

    /**
	 * The number of sessions on the server may not be the actual number of
	 * current users.. session generally have a keep-alive time... so.. this is
	 * just a rough guess.
	 * 
	 * @return
	 */
    @ManagedAttribute(description = "retrieves the current number of sessions the server is holding on to.")
    public int getSessionCount() {
        return sessions;
    }
}
