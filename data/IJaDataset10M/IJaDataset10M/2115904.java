package fr.esrf.TangoApi.events;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceProxy;
import javax.swing.*;

/**
 * @author pascal_verdier
 */
public class TangoDataReady extends EventDispatcher implements java.io.Serializable {

    public TangoDataReady(DeviceProxy device_proxy, String attr_name, String[] filters) {
        super(device_proxy);
        this.attr_name = attr_name;
        this.filters = filters;
        event_identifier = -1;
    }

    public void addTangoDataReadyListener(ITangoDataReadyListener listener, boolean stateless) throws DevFailed {
        event_listeners.add(ITangoDataReadyListener.class, listener);
        event_identifier = subscribe_data_ready_event(attr_name, filters, stateless);
    }

    public void removeTangoDataReadyListener(ITangoDataReadyListener listener) throws DevFailed {
        event_listeners.remove(ITangoDataReadyListener.class, listener);
        if (event_listeners.getListenerCount() == 0) unsubscribe_event(event_identifier);
    }

    public void dispatch_event(final EventData event_data) {
        final TangoDataReady tg = this;
        Runnable do_work_later = new Runnable() {

            public void run() {
                TangoDataReadyEvent data_ready_event = new TangoDataReadyEvent(tg, event_data);
                fireTangoDataReadyEvent(data_ready_event);
            }
        };
        SwingUtilities.invokeLater(do_work_later);
    }

    private void fireTangoDataReadyEvent(TangoDataReadyEvent data_ready_event) {
        Object[] listeners = event_listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ITangoDataReadyListener.class) {
                ((ITangoDataReadyListener) listeners[i + 1]).data_ready(data_ready_event);
            }
        }
    }

    String attr_name;

    int event_identifier;

    String[] filters;
}
