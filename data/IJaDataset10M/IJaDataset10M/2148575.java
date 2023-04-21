package es.unizar.tecnodiscap.osgi;

import es.unizar.tecnodiscap.osgi4ami.device.Device;
import es.unizar.tecnodiscap.osgi4ami.service.Service;
import es.unizar.tecnodiscap.osgi4ami.service.monitoring.MonitoringServiceListener;
import es.unizar.tecnodiscap.tcp.TcpGateway;
import es.unizar.tecnodiscap.tcp.TcpGatewayListener;
import es.unizar.tecnodiscap.util.sockets.tcp.TcpClientThread;
import es.unizar.tecnodiscap.xml.XMLProcessor;
import java.util.ArrayList;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.w3c.dom.Document;

/**
 *
 * @author ciru
 */
public class Xml2osgiActivator extends XMLProcessor implements BundleActivator, TcpGatewayListener {

    private BundleContext bc = null;

    private TcpGateway gateway;

    @Override
    public void start(BundleContext bc) throws Exception {
        this.bc = bc;
        gateway = new TcpGateway(9760);
        gateway.registerListener(this);
        gateway.start();
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        gateway.close();
        gateway.removeListener(this);
        gateway = null;
    }

    @Override
    public void newCommand(Document document, TcpClientThread tct) {
        super.document = document;
        String result = processDocument();
        tct.sendMessage(result);
    }

    @Override
    protected Device getDevice(String id) {
        Device result = null;
        ServiceTracker srvTrkr = new ServiceTracker(bc, Device.class.getName(), null);
        srvTrkr.open();
        Object[] svcs = srvTrkr.getServices();
        if (svcs != null) {
            for (int i = 0; i < svcs.length; i++) {
                Device object = (Device) svcs[i];
                if (object.getDeviceId().equals(id)) {
                    result = object;
                    break;
                }
            }
        }
        srvTrkr.close();
        return result;
    }

    @Override
    public ArrayList<Device> getDevices() {
        ArrayList<Device> result = new ArrayList<Device>();
        if (bc != null) {
            ServiceTracker serviceTracker = new ServiceTracker(bc, Device.class.getName(), null);
            serviceTracker.open();
            Object[] services = serviceTracker.getServices();
            if (services != null) {
                for (int i = 0; i < services.length; i++) {
                    Device object = (Device) services[i];
                    result.add(object);
                }
                serviceTracker.close();
                serviceTracker = null;
            }
        }
        return result;
    }

    @Override
    public ArrayList<Service> getServices() {
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

    @Override
    protected Service getService(String id) {
        Service result = null;
        ServiceTracker srvTrkr = new ServiceTracker(bc, Service.class.getName(), null);
        srvTrkr.open();
        Object[] svcs = srvTrkr.getServices();
        if (svcs != null) {
            for (int i = 0; i < svcs.length; i++) {
                Service object = (Service) svcs[i];
                if (object.getServiceId().equals(id)) {
                    result = object;
                    break;
                }
            }
        }
        srvTrkr.close();
        return result;
    }
}
