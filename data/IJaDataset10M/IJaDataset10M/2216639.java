package fr.esrf.TangoApi.events;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.CallBack;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.TangoConst;
import javax.swing.event.EventListenerList;

/**
 * @author pascal_verdier
 */
public abstract class EventDispatcher extends CallBack implements TangoConst, java.io.Serializable {

    /**
     * Creates a new instance of EventDispatcher
     * @param device_proxy device object
     */
    public EventDispatcher(DeviceProxy device_proxy) {
        event_supplier = device_proxy;
        event_listeners = new EventListenerList();
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public DeviceProxy getEventSupplier() {
        return event_supplier;
    }

    protected int subscribe_periodic_event(String attr_name, String[] filters, boolean stateless) throws DevFailed {
        return event_supplier.subscribe_event(attr_name, PERIODIC_EVENT, this, filters, stateless);
    }

    protected int subscribe_change_event(String attr_name, String[] filters, boolean stateless) throws DevFailed {
        return event_supplier.subscribe_event(attr_name, CHANGE_EVENT, this, filters, stateless);
    }

    protected int subscribe_quality_change_event(String attr_name, String[] filters, boolean stateless) throws DevFailed {
        return event_supplier.subscribe_event(attr_name, QUALITY_EVENT, this, filters, stateless);
    }

    protected int subscribe_archive_event(String attr_name, String[] filters, boolean stateless) throws DevFailed {
        return event_supplier.subscribe_event(attr_name, ARCHIVE_EVENT, this, filters, stateless);
    }

    protected int subscribe_user_event(String attr_name, String[] filters, boolean stateless) throws DevFailed {
        return event_supplier.subscribe_event(attr_name, USER_EVENT, this, filters, stateless);
    }

    protected int subscribe_att_config_event(String attr_name, String[] filters, boolean stateless) throws DevFailed {
        return event_supplier.subscribe_event(attr_name, ATT_CONF_EVENT, this, filters, stateless);
    }

    protected int subscribe_data_ready_event(String attr_name, String[] filters, boolean stateless) throws DevFailed {
        return event_supplier.subscribe_event(attr_name, DATA_READY_EVENT, this, filters, stateless);
    }

    public void unsubscribe_event(int event_id) throws DevFailed {
        event_supplier.unsubscribe_event(event_id);
    }

    public void push_event(EventData event_data) {
        dispatch_event(event_data);
    }

    public abstract void dispatch_event(EventData event_data);

    protected EventListenerList event_listeners = null;

    protected DeviceProxy event_supplier = null;
}
