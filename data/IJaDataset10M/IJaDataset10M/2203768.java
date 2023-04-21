package org.apache.felix.upnp.basedriver.export;

import java.util.Dictionary;
import java.util.Enumeration;
import org.apache.felix.upnp.basedriver.Activator;
import org.apache.felix.upnp.basedriver.util.Converter;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.StateVariable;
import org.osgi.service.upnp.UPnPEventListener;

public class ExporterUPnPEventListener implements UPnPEventListener {

    private Device d;

    public ExporterUPnPEventListener(Device d) {
        this.d = d;
    }

    /**
	 * @see org.osgi.service.upnp.UPnPEventListener#notifyUPnPEvent(java.lang.String, java.lang.String, java.util.Dictionary)
	 */
    public void notifyUPnPEvent(String deviceId, String serviceId, Dictionary events) {
        Device dAux = null;
        if (d.getUDN().equals(deviceId)) {
            dAux = d;
        } else {
            dAux = d.getDevice(deviceId);
        }
        Service s = dAux.getService(serviceId);
        Enumeration e = events.keys();
        while (e.hasMoreElements()) {
            StateVariable sv;
            String dataType;
            String name;
            Object key = e.nextElement();
            if (key instanceof String) {
                name = (String) key;
                sv = s.getStateVariable(name);
                dataType = sv.getDataType();
            } else {
                Activator.logger.ERROR(deviceId + " notified the change in the StateVariable of " + serviceId + " but the key Java type contained in the Dictiories was " + key.getClass().getName() + " instead of " + String.class.getName() + " as specified by Javadoc");
                continue;
            }
            try {
                sv.setValue(Converter.toString(events.get(key), dataType));
            } catch (Exception ignored) {
                Activator.logger.ERROR("UPnP Base Driver Exporter: error converting datatype while sending event, exception message follows:" + ignored.getMessage());
            }
        }
    }
}
