package org.tripcom.security.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import net.jini.core.entry.Entry;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base stub implementation for a passive component.
 * <p>
 * This class implements the basic behavior of a passive component which listens
 * for incoming requests and process them as they are received.
 * </p>
 * <p>
 * Subclasses should implement the {@link #processRequest(JavaSpace, Entry)}
 * method, which may write response entries to the space as a consequence of the
 * request received.
 * </p>
 * <p>
 * Response time may be configured using the {@link #setResponseTime(long)}
 * method. By default it is zero, implying that requests are immediately
 * processed. This feature is useful in order to test proper handling of timeout
 * conditions.
 * </p>
 * <p>
 * Received requests are logged and they can be retrieved by invoking the
 * {@link #getReceivedRequests()} method.
 * </p>
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class PassiveComponentStub {

    /** The shared log object. */
    private static Log log = LogFactory.getLog(PassiveComponentStub.class);

    /** The space connected to the component, for input/output. */
    private JavaSpace space;

    /** The response time of the component. */
    private long responseTime;

    /** Flag stating whether the component should not respond to requests. */
    private boolean notResponding;

    /** Flag stating whether the component should simulate mulfunctionings. */
    private boolean malfunctioning;

    /** The ordered list of received request (starting from oldest one). */
    private List<Entry> receivedRequests;

    /**
	 * Create the stub given the space and templates provided. The templates
	 * parameter specifies which requests the component listens for.
	 * 
	 * @param space the space connected to the component (not null).
	 * @param templates the list of templates for the requests handled by the
	 *            component (not null).
	 */
    public PassiveComponentStub(JavaSpace space, Entry... templates) {
        this.responseTime = 0L;
        this.space = space;
        this.notResponding = false;
        this.malfunctioning = false;
        this.receivedRequests = new ArrayList<Entry>();
        try {
            for (Entry template : templates) {
                space.notify(template, null, new Listener(template), Lease.FOREVER, null);
            }
        } catch (Exception ex) {
            log.error("Exception caught while registering listeners", ex);
            throw new Error(ex);
        }
    }

    /**
	 * Return the response time of the component.
	 * 
	 * @return the response time.
	 */
    public long getResponseTime() {
        return responseTime;
    }

    /**
	 * Set the response time of the component.
	 * 
	 * @param responseTime the response time (not negative).
	 */
    public void setResponseTime(long responseTime) {
        if (responseTime < 0L) {
            throw new IllegalArgumentException("Invalid response time: " + responseTime);
        }
        this.responseTime = responseTime;
    }

    /**
	 * Check whether the component is configured to not respond to incoming
	 * requests.
	 * 
	 * @return true if the component is not responding to client requests.
	 */
    public boolean isNotResponding() {
        return notResponding;
    }

    /**
	 * Set whether the component is configured to not respond to incoming
	 * requests.
	 * 
	 * @param notResponding true if the component is not responding to client
	 *            requests.
	 */
    public void setNotResponding(boolean notResponding) {
        this.notResponding = notResponding;
    }

    /**
	 * Check whether the component is configured to simulate malfunctionings.
	 * 
	 * @return true if the component is malfunctioning.
	 */
    public boolean isMalfunctioning() {
        return malfunctioning;
    }

    /**
	 * Set whether the component is configured to simulate malfunctionings.
	 * 
	 * @param malfunctioning true if the component should simulate
	 *            mulfunctionings.
	 */
    public void setMalfunctioning(boolean malfunctioning) {
        this.malfunctioning = malfunctioning;
    }

    /**
	 * Return the list of received requests. The first request of the list
	 * correspond to the oldest one.
	 * 
	 * @return the list of received requests.
	 */
    public List<Entry> getReceivedRequests() {
        return receivedRequests;
    }

    /**
	 * Process an incoming request. This method is invoked for each request
	 * received through the connected space which satisfies one of the
	 * configured templates. This method should be overridden by subclasses.
	 * 
	 * @param space the space connected to the component (not null).
	 * @param request the request received (not null).
	 * @param malfunctioning true if the method should simulate a
	 *            malfunctioning.
	 * @throws Exception in case of error.
	 */
    protected void processRequest(JavaSpace space, Entry request, boolean malfunctioning) throws Exception {
    }

    /**
	 * Listener implementation.
	 * 
	 * @author Francesco Corcoglioniti
	 *         &lt;francesco.corcoglioniti@cefriel.it&gt;
	 */
    private class Listener implements RemoteEventListener {

        /** The template related to this listener. */
        private Entry template;

        /**
		 * Create a new listener instance based on the template specified. The
		 * template will be used in order to retrieve the entry after the
		 * notification.
		 * 
		 * @param template the template for this listener (not null).
		 */
        public Listener(Entry template) {
            this.template = template;
        }

        /**
		 * {@inheritDoc} This method retrieves the request, waits for the
		 * response time configured and then invokes
		 * {@link PassiveComponentStub#processRequest(JavaSpace, Entry)}.
		 */
        public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
            assert (event != null);
            Entry request = null;
            try {
                request = space.takeIfExists(template, null, 0L);
                if (request == null) {
                    return;
                }
                receivedRequests.add(request);
                log.info("Received request " + request.getClass().getSimpleName());
            } catch (Exception ex) {
                log.error("Unable to read request entry", ex);
                throw new Error();
            }
            if (notResponding) {
                return;
            }
            Util.sleep(responseTime);
            try {
                processRequest(space, request, malfunctioning);
            } catch (Exception ex) {
                log.error("Error while processing incoming request", ex);
                throw new Error(ex);
            }
        }
    }
}
