package eu.ict.persist.examples.ExampleContentProviderService.proxy;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.IMessageQueue;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.ServiceMessage;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.api.pss3p.callback.IProxy;
import eu.ict.persist.examples.ExampleContentProviderService.api.ExampleContentProviderService;

/**
 * @author Kajetan Dolinar
 *
 */
public class ExampleContentProviderServiceProxy implements IProxy, ExampleContentProviderService {

    private PSSLog log = new PSSLog(this);

    private boolean localPSSProxy;

    private String peerId;

    private IServiceIdentifier serviceId;

    private ServiceTracker srtMessageQueue;

    public ExampleContentProviderServiceProxy(BundleContext bc) {
        srtMessageQueue = new ServiceTracker(bc, IMessageQueue.class.getName(), null);
        srtMessageQueue.open();
    }

    public void setEndpoint(IServiceIdentifier sid) {
        localPSSProxy = false;
        serviceId = sid;
    }

    public void setEndpoint(IServiceIdentifier sid, String pid) {
        localPSSProxy = true;
        serviceId = sid;
        peerId = pid;
    }

    public void ProvideContent(String dpi, String blockSize, ICallbackListener listener) {
        ServiceMessage localPSSmessage, remotePSSmessage;
        IMessageQueue msgQue = (IMessageQueue) srtMessageQueue.getService();
        if (msgQue == null) {
            log.error(IMessageQueue.class.getName() + " not found in local OSGi");
            return;
        }
        localPSSmessage = new ServiceMessage(ExampleContentProviderServiceProxy.class.getName(), serviceId.toUriString(), peerId, false, "ProvideContent", true, new String[] { dpi, blockSize }, new String[] { dpi.getClass().getName(), blockSize.getClass().getName() });
        remotePSSmessage = new ServiceMessage(ExampleContentProviderServiceProxy.class.getName(), serviceId.toUriString(), serviceId.getOperatorId(), true, "ProvideContent", true, new String[] { dpi, blockSize }, new String[] { dpi.getClass().getName(), blockSize.getClass().getName() });
        try {
            msgQue.addServiceMessage(localPSSProxy ? localPSSmessage : remotePSSmessage, listener);
        } catch (ONMException ex) {
            log.error("RPC failed when calling service from proxy " + ExampleContentProviderServiceProxy.class.getName() + "to service" + serviceId, ex);
            ex.printStackTrace();
        }
    }

    public void SayGoodbye(String dpi, ICallbackListener listener) {
        ServiceMessage localPSSmessage, remotePSSmessage;
        IMessageQueue msgQue = (IMessageQueue) srtMessageQueue.getService();
        if (msgQue == null) {
            log.error(IMessageQueue.class.getName() + " not found in local OSGi");
            return;
        }
        localPSSmessage = new ServiceMessage(ExampleContentProviderServiceProxy.class.getName(), serviceId.toUriString(), peerId, false, "SayGoodbye", true, new String[] { dpi }, new String[] { dpi.getClass().getName() });
        remotePSSmessage = new ServiceMessage(ExampleContentProviderServiceProxy.class.getName(), serviceId.toUriString(), serviceId.getOperatorId(), true, "SayGoodbye", true, new String[] { dpi }, new String[] { dpi.getClass().getName() });
        try {
            msgQue.addServiceMessage(localPSSProxy ? localPSSmessage : remotePSSmessage, listener);
        } catch (ONMException ex) {
            log.error("RPC failed when calling service from proxy " + ExampleContentProviderServiceProxy.class.getName() + "to service" + serviceId, ex);
            ex.printStackTrace();
        }
    }

    public void SayHello(String dpi, ICallbackListener listener) {
        ServiceMessage localPSSmessage, remotePSSmessage;
        IMessageQueue msgQue = (IMessageQueue) srtMessageQueue.getService();
        if (msgQue == null) {
            log.error(IMessageQueue.class.getName() + " not found in local OSGi");
            return;
        }
        localPSSmessage = new ServiceMessage(ExampleContentProviderServiceProxy.class.getName(), serviceId.toUriString(), peerId, false, "SayHello", true, new String[] { dpi }, new String[] { dpi.getClass().getName() });
        remotePSSmessage = new ServiceMessage(ExampleContentProviderServiceProxy.class.getName(), serviceId.toUriString(), serviceId.getOperatorId(), true, "SayHello", true, new String[] { dpi }, new String[] { dpi.getClass().getName() });
        try {
            msgQue.addServiceMessage(localPSSProxy ? localPSSmessage : remotePSSmessage, listener);
        } catch (ONMException ex) {
            log.error("RPC failed when calling service from proxy " + ExampleContentProviderServiceProxy.class.getName() + "to service" + serviceId, ex);
            ex.printStackTrace();
        }
    }

    protected void finalize() {
        srtMessageQueue.close();
    }
}
