package utilities.events;

import javax.swing.event.EventListenerList;

public class dispatcher {

    protected EventListenerList listenerList;

    public dispatcher() {
        listenerList = new EventListenerList();
    }

    public final void addListener(listener lit) {
        listenerList.add(listener.class, lit);
    }

    public final void removeListener(listener lit) {
        listenerList.remove(listener.class, lit);
    }

    protected final void fireEvent(event evt) {
        Object[] listeners = listenerList.getListeners(listener.class);
        for (int i = 0; i < listeners.length; i++) ((listener) listeners[i]).eventOccurred(evt);
    }
}
