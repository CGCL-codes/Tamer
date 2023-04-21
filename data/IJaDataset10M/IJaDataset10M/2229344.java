package org.personalsmartspace.pm.prefmgr.impl.evaluation;

import java.util.ArrayList;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttributeIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.sre.ems.api.pss3p.EventListener;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEvent;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEventTypes;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEvent;

public class ContextCacheUpdater extends EventListener {

    private ICtxBroker broker;

    private final PSSLog logging = new PSSLog(this);

    private PrivateContextCache contextCache;

    private ArrayList<ICtxAttributeIdentifier> attrList;

    public ContextCacheUpdater(ICtxBroker broker, PrivateContextCache cache) {
        this.broker = broker;
        this.contextCache = cache;
        this.attrList = new ArrayList<ICtxAttributeIdentifier>();
    }

    public void registerForContextEvent(ICtxAttributeIdentifier id) {
        if (this.attrList.contains(id)) {
            this.logging.debug("Already Registered for context events for : " + id.getType() + " ID: " + id.toUriString());
            return;
        }
        try {
            this.broker.registerUpdateNotification(this, id);
            this.logging.debug("Registered for context events for : " + id.getType() + " ID: " + id.toUriString());
        } catch (ContextException e) {
            this.logging.debug("Unable to register for context events for : " + id.getType() + " ID: " + id.toUriString());
            e.printStackTrace();
        }
    }

    @Override
    public void handlePSSEvent(PSSEvent arg0) {
    }

    @Override
    public void handlePeerEvent(PeerEvent event) {
        try {
            this.logging.debug("Peer Event Received " + event.geteventType());
            if (event.geteventType().equals(PSSEventTypes.CONTEXT_UPDATE_EVENT)) {
                ICtxAttribute ctxAttr = (ICtxAttribute) event.geteventInfo();
                String type = ctxAttr.getType();
                String value = ctxAttr.getStringValue();
                ICtxIdentifier id = ctxAttr.getCtxIdentifier();
                this.logging.debug("Event received: type: " + type + " value: " + value);
                this.contextCache.updateCache(ctxAttr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
