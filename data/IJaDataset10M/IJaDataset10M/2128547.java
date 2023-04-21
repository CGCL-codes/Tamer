package es.unizar.tecnodiscap.osgi4ami.gateway.xml.osgi;

import es.unizar.tecnodiscap.osgi4ami.device.Device;
import es.unizar.tecnodiscap.osgi4ami.service.Service;
import java.util.ArrayList;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * 
 */
public class Osgi4amiManager {

    public static Device getOSGi4amiDeviceFromOSGi(String id, BundleContext bc) {
        Device result = null;
        if (bc != null) {
            ServiceTracker deviceTracker = new ServiceTracker(bc, Device.class.getName(), null);
            deviceTracker.open();
            Object[] decives = deviceTracker.getServices();
            if (decives != null) {
                for (int i = 0; i < decives.length; i++) {
                    Device object = (Device) decives[i];
                    if (object.getDeviceId().equals(id)) {
                        result = object;
                    }
                }
                deviceTracker.close();
                deviceTracker = null;
            }
        }
        return result;
    }

    public static ArrayList<Device> getOSGi4amiDevicesFromOSGi(BundleContext bc) {
        ArrayList<Device> result = new ArrayList<Device>();
        if (bc != null) {
            ServiceTracker deviceTracker = new ServiceTracker(bc, Device.class.getName(), null);
            deviceTracker.open();
            Object[] decives = deviceTracker.getServices();
            if (decives != null) {
                for (int i = 0; i < decives.length; i++) {
                    Device object = (Device) decives[i];
                    result.add(object);
                }
                deviceTracker.close();
                deviceTracker = null;
            }
        }
        return result;
    }

    public static ArrayList<Service> getServicesFromOSGi(BundleContext bc) {
        ArrayList<Service> result = new ArrayList<Service>();
        if (bc != null) {
            ServiceTracker serviceTracker = new ServiceTracker(bc, Service.class.getName(), null);
            serviceTracker.open();
            Object[] services = serviceTracker.getServices();
            if (services != null) {
                for (int i = 0; i < services.length; i++) {
                    Service object = (Service) services[i];
                    result.add(object);
                }
                serviceTracker.close();
                serviceTracker = null;
            }
        }
        return result;
    }
}
