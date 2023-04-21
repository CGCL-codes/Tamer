package org.personalsmartspace.spm.trust.impl.pss2pss;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.ReferenceStrategy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.pss3p.IMessageQueue;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.PSSAdvertisement;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.spm.identity.api.platform.DigitalPersonalIdentifier;
import org.personalsmartspace.spm.identity.api.platform.MalformedDigitialPersonalIdentifierException;
import org.personalsmartspace.spm.trust.api.repo.ITrustRepo;
import org.personalsmartspace.spm.trust.api.repo.TrustRepoException;
import org.personalsmartspace.spm.trust.impl.model.IndirectTrust;
import org.personalsmartspace.spm.trust.impl.model.Provider;
import org.personalsmartspace.spm.trust.impl.model.Reputation;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.ems.api.pss3p.EventListener;
import org.personalsmartspace.sre.ems.api.pss3p.IEventMgr;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEvent;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEventTypes;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEvent;

/**
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas
 *         Liampotis</a> (ICCS)
 * @since 0.5.0
 */
@Component(name = "ServiceReputationAdvertiser", immediate = true)
@Service(ServiceReputationAdvertiser.class)
@References({ @Reference(name = "trustRepo", referenceInterface = ITrustRepo.class, cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC, strategy = ReferenceStrategy.EVENT, bind = "setTrustRepo", unbind = "unsetTrustRepo"), @Reference(name = "eventMgr", referenceInterface = IEventMgr.class, cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC, strategy = ReferenceStrategy.EVENT, bind = "setEventMgr", unbind = "unsetEventMgr"), @Reference(name = "messageQueue", referenceInterface = IMessageQueue.class, cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC, strategy = ReferenceStrategy.EVENT, bind = "setMessageQueue", unbind = "unsetMessageQueue") })
public class ServiceReputationAdvertiser extends EventListener {

    /** The Trust repository. */
    private ITrustRepo trustRepo;

    /** The PSS Event Mgmt service. */
    private IEventMgr eventMgr;

    /** The PSS Message Queue */
    private IMessageQueue messageQueue;

    /** The services whose reputation to advertise. */
    private final Set<PssService> services = new CopyOnWriteArraySet<PssService>();

    /** The PSS logging facility. */
    private final PSSLog log = new PSSLog(this);

    public ServiceReputationAdvertiser() {
        if (this.log.isDebugEnabled()) this.log.debug("Initialising");
    }

    @Override
    public void handlePSSEvent(PSSEvent event) {
        if (this.log.isDebugEnabled()) this.log.debug("Received PSS event " + event.geteventType() + ": " + event.geteventName());
        final IDigitalPersonalIdentifier discoveredDpi;
        final PSSAdvertisement pssAd = (PSSAdvertisement) XMLConverter.xmlToObject(event.geteventInfoAsXML(), PSSAdvertisement.class);
        try {
            discoveredDpi = DigitalPersonalIdentifier.fromString(pssAd.getDpi());
            this.doAdvertise(discoveredDpi);
        } catch (MalformedDigitialPersonalIdentifierException mdpie) {
            this.log.error("Could not advertise trust opinion to discovered PSS: " + mdpie.getLocalizedMessage());
        }
    }

    @Override
    public void handlePeerEvent(PeerEvent event) {
        this.log.warn("Received unexpected peer event: " + event.geteventType() + ": " + event.geteventName());
    }

    /**
     * Adds the specified service to the set of services whose reputation to
     * advertise.
     * 
     * @param service
     *            the service to add.
     */
    public void addService(final PssService service) {
        if (this.log.isDebugEnabled()) this.log.debug("Adding service " + service);
        this.services.add(service);
    }

    /**
     * Removes the specified service from the set of services whose reputation
     * to advertise.
     * 
     * @param service
     *            the service to remove.
     */
    public void removeService(final PssService service) {
        if (this.log.isDebugEnabled()) this.log.debug("Removing service " + service);
        this.services.remove(service);
    }

    /**
     * Binds the <code>ITrustRepo</code> service from PSS-to-PSS ITrust service
     * implementation.
     * 
     * @param trustRepo
     *            the ITrustRepo service to bind.
     */
    protected synchronized void setTrustRepo(ITrustRepo trustRepo) {
        if (this.log.isDebugEnabled()) this.log.debug("Binding " + trustRepo);
        this.trustRepo = trustRepo;
    }

    /**
     * Unbinds the <code>ITrustRepo</code> service from PSS-to-PSS ITrust
     * service implementation.
     * 
     * @param trustRepo
     *            the ITrustRepo service to unbind.
     */
    protected synchronized void unsetTrustRepo(ITrustRepo trustRepo) {
        if (this.log.isDebugEnabled()) this.log.debug("Unbinding " + trustRepo);
        this.trustRepo = null;
    }

    protected synchronized void setEventMgr(IEventMgr eventMgr) {
        if (this.log.isDebugEnabled()) this.log.debug("Binding " + eventMgr);
        this.eventMgr = eventMgr;
    }

    protected synchronized void unsetEventMgr(IEventMgr eventMgr) {
        if (this.log.isDebugEnabled()) this.log.debug("Unbinding " + eventMgr);
        this.eventMgr = null;
    }

    protected synchronized void setMessageQueue(IMessageQueue messageQueue) {
        if (this.log.isDebugEnabled()) this.log.debug("Binding " + messageQueue);
        this.messageQueue = messageQueue;
    }

    protected synchronized void unsetMessageQueue(IMessageQueue messageQueue) {
        if (this.log.isDebugEnabled()) this.log.debug("Unbinding " + messageQueue);
        this.messageQueue = null;
    }

    protected synchronized void activate(ComponentContext compContext) throws Exception {
        this.log.info("Activating " + this);
        if (this.log.isDebugEnabled()) this.log.debug("Registering listener for events of type " + PSSEventTypes.DISCOVERED_PSS_EVENT);
        if (this.eventMgr != null) {
            this.eventMgr.registerListener(this, new String[] { PSSEventTypes.DISCOVERED_PSS_EVENT }, null);
        } else {
            this.log.fatal("!! Could not activate ServiceReputationAdvertiser: EMS service reference is NULL");
        }
    }

    protected synchronized void deactivate(ComponentContext compContext) throws Exception {
        this.log.info("Deactivating " + this);
        if (this.log.isDebugEnabled()) this.log.debug("TODO Unregistering listener for events of type " + PSSEventTypes.DISCOVERED_PSS_EVENT);
    }

    private void doAdvertise(final IDigitalPersonalIdentifier discoveredDpi) {
        for (final PssService service : this.services) {
            try {
                final IDigitalPersonalIdentifier myDpi = DigitalPersonalIdentifier.fromString(service.getServiceId().getOperatorId());
                final String type = service.getServiceType();
                Provider myPss = this.trustRepo.getProvider(myDpi, type);
                if (myPss == null) {
                    myPss = new Provider(myDpi, type);
                    this.trustRepo.storeProvider(myPss);
                }
                final Reputation reputation = myPss.getReputation();
                Provider discoveredPss = this.trustRepo.getProvider(discoveredDpi, type);
                if (discoveredPss == null) {
                    discoveredPss = new Provider(discoveredDpi, type);
                    this.trustRepo.storeProvider(discoveredPss);
                }
                final IndirectTrust opinion = new IndirectTrust();
                opinion.setValue(discoveredPss.getDirectTrust().getNormalisedValue());
                new ProxyTrustManagerP2P(discoveredDpi, this.messageQueue).sendOpinion(type, myDpi, discoveredDpi, reputation, opinion);
            } catch (MalformedDigitialPersonalIdentifierException mdpie) {
                this.log.error("Could not advertise trust opinion to discovered PSS: " + mdpie.getLocalizedMessage());
            } catch (ONMException onme) {
                this.log.error("Could not advertise trust opinion to discovered PSS: " + onme.getLocalizedMessage());
            } catch (TrustRepoException tre) {
                this.log.error("Could not advertise trust opinion to discovered PSS: " + tre.getLocalizedMessage());
            }
        }
    }
}
