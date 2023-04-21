package org.personalsmartspace.pss_sm_synchroniser.impl;

import java.util.Collection;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtConstants;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.log.impl.PersistPerformanceMessage;
import org.personalsmartspace.onm.api.platform.IAdvertisementMgr;
import org.personalsmartspace.onm.api.platform.IPeerGroupMgr;
import org.personalsmartspace.onm.api.platform.PeerInfo;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.PSSAdvertisement;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtEventConstants;
import org.personalsmartspace.pss_sm_dbc.impl.Dbc;
import org.personalsmartspace.pss_sm_registry.api.IServiceRegistry;
import org.personalsmartspace.pss_sm_registry.impl.ServiceRegistry;
import org.personalsmartspace.sre.ems.api.pss3p.EventListener;
import org.personalsmartspace.sre.ems.api.pss3p.IEventMgr;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEvent;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEventTypes;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEvent;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEventTypes;

public class RegistrySynchroniserEventListener extends EventListener {

    public static final String PMI_TESTCONTEXT = "IntraPSSServiceDiscovery.ReceiveEvent.dynamic";

    public static final String PMI_SOURCE_COMPONENT = "RegistrySynchroniser";

    private static final boolean ENABLE_PERFORMANCE_LOGGING = false;

    private PSSLog logger = new PSSLog(this);

    private PersistPerformanceMessage pssPMI;

    private IEventMgr eventMgr;

    private IPeerGroupMgr peerGrpMgr;

    private IAdvertisementMgr advertMgr;

    public RegistrySynchroniserEventListener(IEventMgr eventMgr, IPeerGroupMgr peerGroupMgr, IAdvertisementMgr advertMgr) {
        Dbc.require(null != eventMgr);
        logger.debug("Registry Synchroniser Listener created");
        this.configurePerformanceMessage();
        this.eventMgr = eventMgr;
        this.peerGrpMgr = peerGroupMgr;
        this.advertMgr = advertMgr;
    }

    /**
     * Listen for PSS left events in order to remove services advertised previously by the PSS.
     */
    public void handlePSSEvent(PSSEvent pssevent) {
        Dbc.require(null != pssevent);
        logger.debug("Event Listener : PSS event source " + pssevent.geteventSource());
        logger.debug("Event Listener : PSS event name " + pssevent.geteventName());
        logger.debug("Event Listener : PSS event type " + pssevent.geteventType());
        if (pssevent.geteventSource().contains(ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE)) {
            this.handleRegistrySynchroniserPSSEvent(pssevent);
        } else if (pssevent.geteventType().equals(PSSEventTypes.PSS_LEFT_EVENT)) {
            this.handleONMPSSEvent(pssevent);
        }
    }

    public void handlePeerEvent(PeerEvent peerEvent) {
        Dbc.require(null != peerEvent);
        logger.debug("Registry Synchroniser Events Listener : Peer event source " + peerEvent.geteventSource());
        logger.debug("Registry Synchroniser Events Listener : Peer event name " + peerEvent.geteventName());
        logger.debug("Registry Synchroniser Events Listener : Peer event type " + peerEvent.geteventType());
        logger.debug("Registry Synchroniser Events Listener : Peer event object " + peerEvent.geteventInfo().getClass().getName());
        if (ServiceMgmtEventConstants.OVERLAY_NETWORK_MANAGEMENT_SOURCE.equals(peerEvent.geteventSource())) {
            this.handleONMPeerEvent(peerEvent);
        }
    }

    private void handleRegistrySynchroniserPSSEvent(PSSEvent pssevent) {
        logger.debug("Registry Synchroniser Event Listener : REGISTRY_SYNCHRONISER_SOURCE " + pssevent.geteventType());
        if (pssevent.geteventName().contains(ServiceMgmtEventConstants.RS_NEW_SERVICE_EVENT)) {
            this.handleRSNewService(pssevent);
        } else if (pssevent.geteventName().contains(ServiceMgmtEventConstants.RS_REMOVED_SERVICE_EVENT)) {
            this.handleRSRemovedService(pssevent);
        } else if (ServiceMgmtEventConstants.RS_NOTIFY_EVENT.equals(pssevent.geteventName())) {
            this.handleRSNotifiedService(pssevent);
        }
    }

    /**
     * Add a new service from a peer
     * 
     * @param pssevent
     */
    private void handleRSNewService(PSSEvent pssevent) {
        SerializedPssService serial = null;
        PssService service = null;
        if (!isPssEventFromLocalPeer(pssevent)) {
            try {
                serial = (SerializedPssService) LocalXMLConverter.xmlToObject(pssevent.geteventInfoAsXML(), SerializedPssService.class);
                service = PreparePSSEvent.recreatePssService(serial);
                logger.debug("Registry Synchroniser Event Listener : REGISTRY_SYNCHRONISER_SOURCE object created");
                PeerServices m_PeerServices = new PeerServices(this.eventMgr);
                logger.debug("Registry Synchroniser Event Listener : REGISTRY_SYNCHRONISER_SOURCE add new service");
                m_PeerServices.addPeerService(service);
                logger.debug("Event Listener (PSS) called PeerServices.addPeerService with " + service);
            } catch (Exception e) {
                logger.error("New service exception", e);
            } finally {
                if (ENABLE_PERFORMANCE_LOGGING) {
                    long pMIDurationTime = System.currentTimeMillis() - extractEventSourceInfo(pssevent.geteventSource());
                    this.pssPMI.setPerformanceNameValue(ServiceMgmtConstants.PMI_TEST_1_OPERATION_TYPE + "=" + pMIDurationTime);
                    this.logger.trace(this.pssPMI);
                }
            }
        }
    }

    /**
     * Add a new service from a peer
     * 
     * @param pssevent
     */
    private void handleRSRemovedService(PSSEvent pssevent) {
        SerializedPssService serial = null;
        PssService service = null;
        if (!this.isPssEventFromLocalPeer(pssevent)) {
            try {
                serial = (SerializedPssService) LocalXMLConverter.xmlToObject(pssevent.geteventInfoAsXML(), SerializedPssService.class);
                service = PreparePSSEvent.recreatePssService(serial);
                logger.debug("Registry Synchroniser Event Listener : REGISTRY_SYNCHRONISER_SOURCE object created");
                PeerServices m_PeerServices = new PeerServices(this.eventMgr);
                logger.debug("Registry Synchroniser Event Listener : REGISTRY_SYNCHRONISER_SOURCE remove service");
                m_PeerServices.removePeerService(service);
                logger.debug("Event Listener (PSS) called PeerServices.removePeerService with " + service);
            } catch (Exception e) {
                logger.error("Removed service exception", e);
            } finally {
                if (ENABLE_PERFORMANCE_LOGGING) {
                    long pMIDurationTime = System.currentTimeMillis() - extractEventSourceInfo(pssevent.geteventSource());
                    this.pssPMI.setPerformanceNameValue(ServiceMgmtConstants.PMI_TEST_1_OPERATION_TYPE + "=" + pMIDurationTime);
                    this.logger.trace(this.pssPMI);
                }
            }
        }
    }

    /**
     * Add a new service from a peer
     * 
     * @param pssevent
     */
    private void handleRSNotifiedService(PSSEvent pssevent) {
        SerializedPssService serial = null;
        PssService service = null;
        try {
            serial = (SerializedPssService) LocalXMLConverter.xmlToObject(pssevent.geteventInfoAsXML(), SerializedPssService.class);
            service = PreparePSSEvent.recreatePssService(serial);
            logger.debug("Registry Synchroniser Event Listener : REGISTRY_SYNCHRONISER_SOURCE object created");
            PeerServices m_PeerServices = new PeerServices(this.eventMgr);
            logger.debug("Registry Synchroniser Event Listener : REGISTRY_SYNCHRONISER_SOURCE peer notified service");
            m_PeerServices.addPeerService(service);
            logger.debug("Event Listener (PSS) called PeerServices.addPeerService with " + service);
        } catch (Exception e) {
            logger.error("Notified service exception", e);
        } finally {
            long pMIDurationTime = System.currentTimeMillis() - extractEventSourceInfo(pssevent.geteventSource());
            this.pssPMI.setPerformanceNameValue(ServiceMgmtConstants.PMI_TEST_1_OPERATION_TYPE + "=" + pMIDurationTime);
            this.logger.trace(this.pssPMI);
        }
    }

    private void handleONMPSSEvent(PSSEvent pssevent) {
        logger.debug("PSS discovered event");
        PSSAdvertisement pssAd = (PSSAdvertisement) XMLConverter.xmlToObject(pssevent.geteventInfoAsXML(), PSSAdvertisement.class);
        logger.debug("PSS event : type : " + pssevent.geteventType());
        logger.debug("PSS event : PSS name : " + pssAd.getPssName());
        logger.debug("PSS event : PSS DPI : " + pssAd.getDpi());
        logger.debug("PSS event : Ad size : " + pssAd.getServices().size());
        logger.debug("PSS discovered event from : " + pssAd.getPssName());
        if (pssevent.geteventType().equals(PSSEventTypes.PSS_LEFT_EVENT)) {
            logger.debug("PSS Left event discovered");
            this.removeForeignPSSServices(pssAd);
        }
    }

    /**
     * Handle ONM peer events such as a new peer joining
     * When this event is received on another peer it should only reply with 
     * its local and foreign services.
     * 
     * @param peerEvent
     */
    private void handleONMPeerEvent(PeerEvent peerEvent) {
        logger.debug("ONM Peer event : " + peerEvent.geteventType() + " from " + ((PeerInfo) peerEvent.geteventInfo()).getPeerID());
        if (peerEvent.geteventType().equals(PeerEventTypes.PEER_JOINED_EVENT)) {
            try {
                this.notifyPeersOfServices();
            } catch (Exception e) {
                logger.error("ONM Peer exception", e);
            }
        } else if (peerEvent.geteventType().equals(PeerEventTypes.PEER_LEFT_EVENT)) {
            try {
                this.removePeerServices(peerEvent);
            } catch (Exception e) {
                logger.error("ONM Peer exception", e);
            }
        }
    }

    /**
     * Notify other peers of local and foreign services
     */
    private void notifyPeersOfServices() {
        try {
            logger.debug("Notifying PSS peers of local services in response to request");
            RegistryPublisher registryPublisher = new RegistryPublisher(eventMgr);
            IServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
            Collection<PssService> localServices = serviceRegistry.findAllLocalServices();
            Collection<PssService> foreignServices = serviceRegistry.findAllExternalServices();
            if (null != localServices && localServices.size() > 0) {
                logger.debug("Notifying PSS peers of " + localServices.size() + " local services in response to request");
                for (PssService localService : localServices) {
                    try {
                        PSSEvent pssRegistryEvent = new PSSEvent(PSSEventTypes.NOTIFY_SERVICE_EVENT, ServiceMgmtEventConstants.RS_NOTIFY_EVENT, ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE + System.currentTimeMillis(), LocalXMLConverter.objectToXml(PreparePSSEvent.createEventInfoClass(localService)));
                        registryPublisher.sendPSSEvent(pssRegistryEvent);
                        logger.debug("Notifying PSS peers of local service : " + localService.getServiceName() + " in response to request");
                    } catch (Exception e) {
                        logger.error("Event sending exception", e);
                    }
                }
            }
            if (null != foreignServices && foreignServices.size() > 0) {
                logger.debug("Notifying PSS peers of " + foreignServices.size() + " foreign services in response to request");
                for (PssService foreignService : foreignServices) {
                    try {
                        PSSEvent pssRegistryEvent = new PSSEvent(PSSEventTypes.NOTIFY_SERVICE_EVENT, ServiceMgmtEventConstants.RS_NOTIFY_EVENT, ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE + System.currentTimeMillis(), LocalXMLConverter.objectToXml(PreparePSSEvent.createEventInfoClass(foreignService)));
                        registryPublisher.sendPSSEvent(pssRegistryEvent);
                        logger.debug("Notifying PSS peers of foreign service : " + foreignService.getServiceName() + " in response to request");
                    } catch (Exception e) {
                        logger.error("Event sending exception", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Event sending exception", e);
        }
    }

    /**
     * Remove the services discovered previously from the peer that has 
     * left the PSS 
     * 
     * @param peerEvent
     */
    private void removePeerServices(PeerEvent peerEvent) {
        logger.debug("Removing services for peer : " + ((PeerInfo) peerEvent.geteventInfo()).getPeerID());
        PeerServices peerServices = new PeerServices(this.eventMgr);
        String peerId = ((PeerInfo) peerEvent.geteventInfo()).getPeerID();
        peerServices.peerLost(peerId);
    }

    /**
     * Remove Foreign PSS Services
     * 
     * @param pssAd
     */
    private void removeForeignPSSServices(PSSAdvertisement pssAd) {
        if (null != pssAd && pssAd.getDpi() != null) {
            logger.debug("Removing " + pssAd.getServices().size() + " foreign PSS services from SR with DPI : " + pssAd.getDpi());
            ForeignPSSServices foreignPSSService = new ForeignPSSServices(this.peerGrpMgr, eventMgr);
            try {
                foreignPSSService.removeForeignServices(pssAd.getDpi());
            } catch (ServiceMgmtException e) {
                logger.error("Service removal exception", e);
            }
        }
    }

    /**
     * Configure the Persist Performance Message object
     */
    private void configurePerformanceMessage() {
        this.pssPMI = new PersistPerformanceMessage();
        this.pssPMI.setD55TestTableIndex(ServiceMgmtConstants.PMI_SERVICE_MGMT_TEST_1);
        this.pssPMI.setOperationType(ServiceMgmtConstants.PMI_TEST_1_OPERATION_TYPE);
        this.pssPMI.setSourceComponent(PMI_SOURCE_COMPONENT);
        this.pssPMI.setPerformanceType(PersistPerformanceMessage.Delay);
        this.pssPMI.setTestContext(PMI_TESTCONTEXT);
    }

    /**
     * Extract out the time of the sending of the event
     * 
     * @param eventSource
     * @return
     */
    private long extractEventSourceInfo(String eventSource) {
        long retValue = 0;
        this.logger.debug("Event source : " + eventSource);
        if (eventSource.contains(ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE) && eventSource.length() > ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE.length()) {
            int start = eventSource.indexOf(ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE) + ServiceMgmtEventConstants.REGISTRY_SYNCHRONISER_SOURCE.length();
            this.logger.debug("Event start time : " + eventSource.substring(start));
            retValue = Long.parseLong(eventSource.substring(start));
            this.logger.debug("Event start time : " + retValue);
        }
        return retValue;
    }

    /**
     * Check if the PSS event originated from the local peer
     * @param event
     * @return
     */
    private boolean isPssEventFromLocalPeer(PSSEvent event) {
        boolean retValue = false;
        try {
            retValue = (event.geteventName().contains(this.peerGrpMgr.getMyPeerInfo().getPeerID()));
        } catch (ONMException e) {
            this.logger.error("Unable to retrieve local peer identifier", e);
        }
        return retValue;
    }
}
