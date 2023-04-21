package org.personalsmartspace.pm.prefmgr.impl.evaluation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.osgi.framework.BundleContext;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttributeIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmgr.impl.ServiceRetriever;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

public class PrivateContextCache {

    private Hashtable<ICtxIdentifier, String> cache;

    private Hashtable<String, ICtxIdentifier> mapping;

    private final PSSLog logging = new PSSLog(this);

    private ICtxBroker broker;

    private ContextCacheUpdater updater;

    public PrivateContextCache(ICtxBroker broker) {
        this.broker = broker;
        this.mapping = new Hashtable<String, ICtxIdentifier>();
        this.cache = new Hashtable<ICtxIdentifier, String>();
        this.updater = new ContextCacheUpdater(broker, this);
    }

    public String getContextValue(ICtxIdentifier id) {
        this.logging.debug("looking for value of context id: " + id.toUriString());
        this.printCache();
        if (this.cache.containsKey(id)) {
            this.logging.debug("cache contains context value of id: " + id.toUriString());
            return this.cache.get(id);
        }
        this.logging.debug("cache doesn't have id:" + id.toUriString());
        this.retrieveContext(id);
        if (this.cache.containsKey(id)) {
            return this.cache.get(id);
        }
        return "";
    }

    public void printCache() {
        this.logging.debug("********* CONTEXT CACHE CONTENTS START ********************");
        Enumeration<String> e = mapping.keys();
        while (e.hasMoreElements()) {
            String type = e.nextElement();
            ICtxIdentifier id = mapping.get(type);
            this.logging.debug("Type: " + type + " :: ID: " + mapping.get(type).toUriString() + "  VALUE: " + this.cache.get(id));
        }
        this.logging.debug("********* CONTEXT CACHE CONTENTS END ********************");
    }

    private void retrieveContext(String type) {
        this.printCache();
        try {
            List<ICtxIdentifier> attrList = broker.lookup(CtxModelType.ATTRIBUTE, type);
            if (attrList.size() > 0) {
                ICtxIdentifier id = (ICtxIdentifier) attrList.get(0);
                ICtxAttribute attr = (ICtxAttribute) broker.retrieve(id);
                String val = attr.getStringValue();
                this.mapping.put(type, id);
                this.cache.put(id, val);
            }
        } catch (ContextException e) {
            e.printStackTrace();
        }
    }

    private void retrieveContext(ICtxIdentifier id) {
        this.updater.registerForContextEvent((ICtxAttributeIdentifier) id);
        this.printCache();
        this.logging.debug("contacting context DB for retrieving id" + id.toUriString());
        try {
            ICtxAttribute attr = (ICtxAttribute) broker.retrieve(id);
            if (null != attr) {
                this.logging.debug("found id: " + id.toUriString() + " in context DB");
                String val = attr.getStringValue();
                String type = attr.getType();
                if (type == null) {
                    this.logging.debug("context attribute type is null!");
                }
                if (id == null) {
                    this.logging.debug("Context ID is null!");
                }
                if (val == null) {
                    this.logging.debug("String value of attribute is null!");
                }
                this.mapping.put(type, id);
                this.cache.put(id, val);
                this.logging.debug("updated Context Cache for context type: " + type + " with id: " + id.toUriString() + " with value: " + this.cache.get(id));
            } else {
                this.logging.debug("id :" + id.toUriString() + " not found in context DB");
            }
        } catch (ContextException e) {
            e.printStackTrace();
        }
    }

    public void updateCache(ICtxAttribute ctxAttr) {
        if (ctxAttr == null) {
            this.logging.debug("Attempt to update Preference Manager context cache with null CtxAttribute, ignoring ");
            return;
        }
        String type = ctxAttr.getType();
        String value = ctxAttr.getStringValue();
        ICtxIdentifier id = ctxAttr.getCtxIdentifier();
        this.cache.put(id, value);
        this.mapping.put(type, id);
        this.logging.debug("updated Context Cache for context type: " + type + " with id: " + id.toUriString() + " with value: " + this.cache.get(id));
        this.printCache();
    }

    private class Pair {

        private String type;

        private IServiceIdentifier id;

        Pair(String serviceType, IServiceIdentifier serviceID) {
            this.setServiceType(serviceType);
            this.setServiceId(serviceID);
        }

        public void setServiceType(String type) {
            this.type = type;
        }

        public String getServiceType() {
            return type;
        }

        public void setServiceId(IServiceIdentifier id) {
            this.id = id;
        }

        public IServiceIdentifier getServiceId() {
            return this.id;
        }

        public boolean equals(Pair p) {
            if (!(this.type.equalsIgnoreCase(p.getServiceType()))) {
                return false;
            }
            if (!(this.id.toString().equalsIgnoreCase(p.getServiceId().toString()))) {
                return false;
            }
            return true;
        }
    }
}
