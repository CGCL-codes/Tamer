package fr.esrf.TangoApi.events;

import fr.esrf.Tango.AttDataReady;
import fr.esrf.Tango.DevError;
import fr.esrf.TangoApi.AttributeInfoEx;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceProxy;

/**
 * @author pascal_verdier
 */
public class EventData implements java.io.Serializable {

    public DeviceProxy device;

    public String name;

    public String event;

    public int event_type;

    public int event_source;

    public DeviceAttribute attr_value;

    public AttributeInfoEx attr_config;

    public AttDataReady data_ready;

    public DevError[] errors;

    public boolean err;

    public long date;

    public static final int ZMQ_EVENT = 0;

    public static final int NOTIFD_EVENT = 1;

    public EventData(DeviceProxy device, String name, String event, int event_type, int event_source, DeviceAttribute attr_value, AttributeInfoEx attr_config, AttDataReady data_ready, DevError[] errors) {
        this.device = device;
        this.name = name;
        this.event = event;
        this.event_type = event_type;
        this.event_source = event_source;
        this.attr_value = attr_value;
        this.attr_config = attr_config;
        this.data_ready = data_ready;
        this.errors = errors;
        err = (errors != null);
        date = System.currentTimeMillis();
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public boolean isAttrValue() {
        return (attr_value != null);
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public boolean isAttrConfig() {
        return (attr_config != null);
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public boolean isAttrDataReady() {
        return (data_ready != null);
    }
}
