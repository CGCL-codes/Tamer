package net.sourceforge.advantag.event.dispatcher;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.advantag.event.OpenEvent;
import net.sourceforge.advantag.listener.OpenEventListener;

public class OpenEventDispatcher {

    private List<OpenEventListener> listeners;

    private static class OpenEventDispatcherHolder {

        private static final OpenEventDispatcher INSTANCE = new OpenEventDispatcher();
    }

    private OpenEventDispatcher() {
        listeners = new ArrayList<OpenEventListener>();
    }

    public static OpenEventDispatcher getInstance() {
        return OpenEventDispatcherHolder.INSTANCE;
    }

    public void addOpenEventListener(OpenEventListener listener) {
        listeners.add(listener);
    }

    public void removeOpenEventListener(OpenEventListener listener) {
        listeners.remove(listener);
    }

    public void fireOpenEvent(OpenEvent event) {
        for (OpenEventListener openEventListener : listeners) {
            openEventListener.openEventOccurred(event);
        }
    }
}
