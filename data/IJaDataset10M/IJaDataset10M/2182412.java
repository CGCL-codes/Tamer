package org.p2pws.jxta;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.p2pws.ServiceDescriptor;
import org.p2pws.platform.discovery.Finder;
import org.p2pws.platform.discovery.ServiceFoundListener;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Element;
import net.jxta.document.StructuredDocument;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.ModuleSpecAdvertisement;

/**
 * @author Administrador
 *
 */
public class JxtaFinder implements Finder {

    private static final Log log = LogFactory.getLog(JxtaFinder.class);

    private PeerGroup peerGroup;

    private Map<String, ServiceDescriptor> descriptorsCache = new HashMap<String, ServiceDescriptor>();

    private Object discoveryLock = new Object();

    public JxtaFinder(PeerGroup peerGroup) {
        this.peerGroup = peerGroup;
    }

    private Map<String, ModuleSpecAdvertisement> msadvs = new HashMap<String, ModuleSpecAdvertisement>();

    public void findServices(String serviceName, ServiceFoundListener listener) {
        int found = 0;
        int tries = 0;
        int timeout = 2000;
        Object tempAdv;
        log.debug("Searching for '" + serviceName + "'");
        DiscoveryService discoverySvc = peerGroup.getDiscoveryService();
        do {
            try {
                Enumeration advs = discoverySvc.getLocalAdvertisements(DiscoveryService.ADV, "Name", serviceName);
                if (advs != null && advs.hasMoreElements()) {
                    while (advs.hasMoreElements()) {
                        if ((tempAdv = advs.nextElement()) instanceof ModuleSpecAdvertisement) {
                            ModuleSpecAdvertisement adv = (ModuleSpecAdvertisement) tempAdv;
                            msadvs.put(adv.getName(), adv);
                            ServiceDescriptor descriptor = new ServiceDescriptor(adv.getName(), adv.getDescription());
                            descriptor.addAttribute("PipeAdvertisement", adv.getPipeAdvertisement());
                            String epr = ((Element) adv.getParam().getChildren("epr").nextElement()).getValue().toString();
                            descriptor.setEpr(epr);
                            Enumeration wsdlChildren = adv.getParam().getChildren("WSDL");
                            if (wsdlChildren.hasMoreElements()) {
                                Element wsdlelement = (Element) wsdlChildren.nextElement();
                                if (wsdlelement != null && wsdlelement.getValue() != null) {
                                    String wsdl = wsdlelement.getValue().toString();
                                    descriptor.setWsdl(wsdl);
                                } else {
                                    log.error("WSDL document not found for service " + descriptor.getName());
                                }
                            } else {
                                log.error("WSDL document not found for service " + descriptor.getName());
                            }
                            descriptorsCache.put(descriptor.getName(), descriptor);
                            listener.serviceFound(descriptor);
                            ++found;
                        }
                    }
                }
                discoverySvc.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", serviceName, 5, new ServiceDiscoveryListener(listener));
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                }
            } catch (IOException e) {
            }
        } while (found == 0 && tries++ <= 5);
        log.debug("Found " + found + " advertisements(s).");
    }

    public ServiceDescriptor findService(String serviceName) {
        ServiceDescriptor descriptor = descriptorsCache.get(serviceName);
        if (descriptor != null) {
            return descriptor;
        }
        int found = 0;
        int timeout = 1000;
        Object tempAdv;
        log.debug("Searching for '" + serviceName + "'");
        DiscoveryService discoverySvc = peerGroup.getDiscoveryService();
        while (true) {
            try {
                Enumeration advs = discoverySvc.getLocalAdvertisements(DiscoveryService.ADV, "Name", serviceName);
                if (advs != null && advs.hasMoreElements()) {
                    while (advs.hasMoreElements()) {
                        if ((tempAdv = advs.nextElement()) instanceof ModuleSpecAdvertisement) {
                            ModuleSpecAdvertisement adv = (ModuleSpecAdvertisement) tempAdv;
                            msadvs.put(adv.getName(), adv);
                            descriptor = new ServiceDescriptor(adv.getName(), adv.getDescription());
                            descriptor.addAttribute("PipeAdvertisement", adv.getPipeAdvertisement());
                            StructuredDocument param = adv.getParam();
                            String epr = ((Element) param.getChildren("epr").nextElement()).getValue().toString();
                            descriptor.setEpr(epr);
                            Element elem = (Element) param.getChildren("WSDL").nextElement();
                            if (elem != null && elem.getValue() != null) {
                                String wsdl = elem.getValue().toString();
                                descriptor.setWsdl(wsdl);
                            } else {
                                log.warn("WSDL element not found");
                            }
                            ++found;
                        }
                    }
                    if (found > 0) break;
                }
                discoverySvc.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", serviceName, 5, new InternalDiscoveryListener());
                synchronized (discoveryLock) {
                    try {
                        discoveryLock.wait(timeout);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (IOException e) {
            }
        }
        log.debug("Found " + found + " advertisements(s).");
        descriptorsCache.put(serviceName, descriptor);
        return descriptor;
    }

    private class InternalDiscoveryListener implements DiscoveryListener {

        public void discoveryEvent(DiscoveryEvent event) {
            synchronized (discoveryLock) {
                discoveryLock.notifyAll();
            }
        }
    }
}
