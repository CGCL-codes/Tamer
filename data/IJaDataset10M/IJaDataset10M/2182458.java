package com.bluemarsh.jswat.core.breakpoint;

import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.session.SessionEvent;
import com.bluemarsh.jswat.core.session.SessionListener;
import com.bluemarsh.jswat.core.util.Threads;
import com.sun.jdi.Location;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import org.openide.util.NbBundle;

/**
 * Class DefaultLocationBreakpoint is a default implementation of a
 * LocationBreakpoint.
 *
 * @author Nathan Fiedler
 */
public class DefaultLocationBreakpoint extends AbstractBreakpoint implements LocationBreakpoint, SessionListener {

    /** The Location that we are set to stop upon. */
    private Location location;

    /** Resolved event request, if breakpoint has resolved. */
    private EventRequest eventRequest;

    /**
     * Creates a new instance of DefaultLineBreakpoint.
     */
    public DefaultLocationBreakpoint() {
    }

    @Override
    public boolean canFilterClass() {
        return false;
    }

    @Override
    public boolean canFilterThread() {
        return true;
    }

    @Override
    public void closing(SessionEvent sevt) {
    }

    @Override
    public void connected(SessionEvent sevt) {
    }

    /**
     * Create the breakpoint request.
     */
    private void createRequests() {
        VirtualMachine vm = location.virtualMachine();
        EventRequestManager erm = vm.eventRequestManager();
        eventRequest = erm.createBreakpointRequest(location);
        register(eventRequest);
        propSupport.firePropertyChange(PROP_RESOLVED, false, true);
    }

    @Override
    protected void deleteRequests() {
        if (eventRequest != null) {
            try {
                VirtualMachine vm = eventRequest.virtualMachine();
                EventRequestManager erm = vm.eventRequestManager();
                erm.deleteEventRequest(eventRequest);
            } catch (VMDisconnectedException vmde) {
            } finally {
                eventRequest = null;
            }
        }
    }

    @Override
    public String describe(Event e) {
        LocatableEvent le = (LocatableEvent) e;
        String[] params = new String[] { location.declaringType().name(), location.method().name(), location.method().signature(), String.valueOf(location.codeIndex()), Threads.getIdentifier(le.thread()) };
        return NbBundle.getMessage(DefaultLocationBreakpoint.class, "Location.description.stop", params);
    }

    @Override
    public void destroy() {
        deleteRequests();
        super.destroy();
    }

    @Override
    public void disconnected(SessionEvent sevt) {
        deleteRequests();
        fireEvent(new BreakpointEvent(this, BreakpointEventType.REMOVED, null));
    }

    @Override
    public String getDescription() {
        String[] params = new String[] { location.declaringType().name(), location.method().name(), location.method().signature(), String.valueOf(location.codeIndex()) };
        return NbBundle.getMessage(DefaultLocationBreakpoint.class, "Location.description", params);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public void opened(Session session) {
    }

    @Override
    public void resuming(SessionEvent sevt) {
    }

    @Override
    public void setEnabled(boolean enabled) {
        deleteRequests();
        super.setEnabled(enabled);
        if (isEnabled()) {
            createRequests();
        }
    }

    @Override
    public void setLocation(Location location) {
        Location old = this.location;
        this.location = location;
        deleteRequests();
        propSupport.firePropertyChange(PROP_LOCATION, old, location);
        if (isEnabled()) {
            createRequests();
        }
    }

    @Override
    public void setSuspendPolicy(int policy) {
        super.setSuspendPolicy(policy);
        if (eventRequest != null) {
            boolean enabled = eventRequest.isEnabled();
            eventRequest.setEnabled(false);
            applySuspendPolicy(eventRequest);
            eventRequest.setEnabled(enabled);
        }
    }

    @Override
    public void suspended(SessionEvent sevt) {
    }
}
