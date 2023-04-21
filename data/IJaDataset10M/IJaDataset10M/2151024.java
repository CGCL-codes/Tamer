package org.eclipse.ufacekit.core.internal.databinding.sse.dom.events.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.dom.events.DOMEventConstants;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public abstract class DOMEventAdapter implements INodeAdapter, DOMEventConstants {

    protected List listeners = new ArrayList();

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    public boolean hasEventListener() {
        return listeners.size() > 0;
    }

    protected void doHandleEvent(Event event) {
        for (Iterator iterator = listeners.iterator(); iterator.hasNext(); ) {
            EventListener listener = (EventListener) iterator.next();
            listener.handleEvent(event);
        }
    }
}
