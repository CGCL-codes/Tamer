package org.oors;

import java.util.LinkedList;
import java.util.List;

public abstract class OorsEventGenerator {

    protected List<OorsListener> listeners = new LinkedList<OorsListener>();

    public void addOorsListener(OorsListener l) {
        listeners.add(l);
    }

    public void removeOorsListener(OorsListener l) {
        listeners.remove(l);
    }

    protected void fireCreatedEvent(OorsEventGenerator b) {
        if (listeners.isEmpty()) return;
        OorsEvent ev = new OorsEvent(b);
        for (OorsListener l : listeners) l.createdUpdate(ev);
    }

    protected void fireModifiedEvent(OorsEventGenerator b) {
        if (listeners.isEmpty()) return;
        OorsEvent ev = new OorsEvent(b);
        for (OorsListener l : listeners) l.modifiedUpdate(ev);
    }

    protected void fireDeletedEvent(OorsEventGenerator b) {
        if (listeners.isEmpty()) return;
        OorsEvent ev = new OorsEvent(b);
        for (OorsListener l : listeners) l.deletedUpdate(ev);
    }
}
