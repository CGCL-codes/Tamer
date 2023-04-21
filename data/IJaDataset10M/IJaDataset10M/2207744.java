package org.torweg.pulse.service.event;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.request.CacheMode;
import org.torweg.pulse.service.request.ServiceRequest;

/**
 * sends a {@code 404 HTTP error}.
 * 
 * @author Thomas Weber
 * @version $Revision: 1393 $
 */
public class NotFoundEvent implements Event {

    /**
	 * the cache mode.
	 */
    private CacheMode cache = CacheMode.NONE;

    /**
	 * the missing resource.
	 */
    private String resource;

    /**
	 * flag, indicating whether the current event is an output event.
	 */
    private final boolean outputEvent;

    /**
	 * flag, indicating whether the current event is an output event.
	 */
    private final boolean stopEvent;

    /**
	 * builds a new {@code NotFoundEvent} which will be processed by the
	 * standard output.
	 * <p>
	 * This {@code NotFoundEvent} will neither be an output nor a stop
	 * event.
	 * </p>
	 */
    public NotFoundEvent() {
        super();
        this.outputEvent = false;
        this.stopEvent = false;
    }

    /**
	 * builds a new {@code NotFoundEvent} resulting in a raw error page
	 * with the given URI to the missing resource.
	 * <p>
	 * This makes the {@code NotFoundEvent} both an output and a stop
	 * event.
	 * </p>
	 * 
	 * @param res
	 *            the URI
	 */
    public NotFoundEvent(final String res) {
        super();
        this.resource = res;
        this.outputEvent = true;
        this.stopEvent = true;
    }

    /**
	 * @param req
	 *            the current request
	 * @see org.torweg.pulse.service.event.Event#run(org.torweg.pulse.service.request.ServiceRequest)
	 */
    public final void run(final ServiceRequest req) {
        HttpServletResponse response = req.getHttpServletResponse();
        try {
            if (this.outputEvent) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, this.resource);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new PulseException("Error running Event " + this.getClass().getCanonicalName() + ": " + e.getLocalizedMessage(), e);
        }
    }

    /**
	 * @return {@code true}, if and only if the {@code NotFoundEvent}
	 *         is an output event. Otherwise {@code false}.
	 * @see org.torweg.pulse.service.event.Event#isOutputEvent()
	 */
    public final boolean isOutputEvent() {
        return this.outputEvent;
    }

    /**
	 * @return {@code true}
	 * @see org.torweg.pulse.service.event.Event#isStopEvent()
	 */
    public final boolean isStopEvent() {
        return this.stopEvent;
    }

    /**
	 * @see org.torweg.pulse.service.event.Event#isSingularEvent()
	 * @return {@code true}
	 */
    public final boolean isSingularEvent() {
        return true;
    }

    /**
	 * returns the {@code CacheMode}.
	 * 
	 * @return the cache mode
	 * @see org.torweg.pulse.service.event.Event#getCacheMode()
	 */
    public final CacheMode getCacheMode() {
        return this.cache;
    }

    /**
	 * sets the {@code CacheMode}.
	 * 
	 * @param c
	 *            the cache mode to set
	 * @see org.torweg.pulse.service.event.Event#setCacheMode(org.torweg.pulse.service.request.CacheMode)
	 */
    public final void setCacheMode(final CacheMode c) {
        this.cache = c;
    }
}
