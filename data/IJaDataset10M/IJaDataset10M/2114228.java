package org.marcont.services.definitions.service;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.listeners.StatementListener;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.adtech.jastor.*;
import com.ibm.adtech.jastor.util.*;

/**
 * Implementation of {@link org.marcont.services.definitions.service.ServiceProfile}
 * Use the org.marcont.services.definitions.service.Service_DOT_owlFactory to create instances of this class.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Service.owl#ServiceProfile)</p>
 * <br>
 */
public class ServiceProfileImpl extends com.ibm.adtech.jastor.ThingImpl implements org.marcont.services.definitions.service.ServiceProfile {

    private static com.hp.hpl.jena.rdf.model.Property presentedByProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/Service.owl#presentedBy");

    private java.util.ArrayList presentedBy;

    private static com.hp.hpl.jena.rdf.model.Property providesProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/Service.owl#provides");

    private java.util.ArrayList provides;

    ServiceProfileImpl(Resource resource, Model model) throws JastorException {
        super(resource, model);
        setupModelListener();
    }

    static ServiceProfileImpl getServiceProfile(Resource resource, Model model) throws JastorException {
        return new ServiceProfileImpl(resource, model);
    }

    static ServiceProfileImpl createServiceProfile(Resource resource, Model model) throws JastorException {
        ServiceProfileImpl impl = new ServiceProfileImpl(resource, model);
        if (!impl._model.contains(new com.hp.hpl.jena.rdf.model.impl.StatementImpl(impl._resource, RDF.type, ServiceProfile.TYPE))) impl._model.add(impl._resource, RDF.type, ServiceProfile.TYPE);
        impl.addSuperTypes();
        impl.addHasValueValues();
        return impl;
    }

    void addSuperTypes() {
    }

    void addHasValueValues() {
    }

    private void setupModelListener() {
        listeners = new java.util.ArrayList();
        org.marcont.services.definitions.service.Service_DOT_owlFactory.registerThing(this);
    }

    public java.util.List listStatements() {
        java.util.List list = new java.util.ArrayList();
        StmtIterator it = null;
        it = _model.listStatements(_resource, presentedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, providesProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, RDF.type, org.marcont.services.definitions.service.ServiceProfile.TYPE);
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public void clearCache() {
        presentedBy = null;
        provides = null;
    }

    private com.hp.hpl.jena.rdf.model.Literal createLiteral(Object obj) {
        return _model.createTypedLiteral(obj);
    }

    private void initPresentedBy() throws JastorException {
        this.presentedBy = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, presentedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.daml.org/services/owl-s/1.2/Service.owl#presentedBy properties in ServiceProfile model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                org.marcont.services.definitions.service.Service presentedBy = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                this.presentedBy.add(presentedBy);
            }
        }
    }

    public java.util.Iterator getPresentedBy() throws JastorException {
        if (presentedBy == null) initPresentedBy();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(presentedBy, _resource, presentedByProperty, true);
    }

    public void addPresentedBy(org.marcont.services.definitions.service.Service presentedBy) throws JastorException {
        if (this.presentedBy == null) initPresentedBy();
        if (this.presentedBy.contains(presentedBy)) {
            this.presentedBy.remove(presentedBy);
            this.presentedBy.add(presentedBy);
            return;
        }
        this.presentedBy.add(presentedBy);
        _model.add(_model.createStatement(_resource, presentedByProperty, presentedBy.resource()));
    }

    public org.marcont.services.definitions.service.Service addPresentedBy() throws JastorException {
        org.marcont.services.definitions.service.Service presentedBy = org.marcont.services.definitions.service.Service_DOT_owlFactory.createService(_model.createResource(), _model);
        if (this.presentedBy == null) initPresentedBy();
        this.presentedBy.add(presentedBy);
        _model.add(_model.createStatement(_resource, presentedByProperty, presentedBy.resource()));
        return presentedBy;
    }

    public org.marcont.services.definitions.service.Service addPresentedBy(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        org.marcont.services.definitions.service.Service presentedBy = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
        if (this.presentedBy == null) initPresentedBy();
        if (this.presentedBy.contains(presentedBy)) return presentedBy;
        this.presentedBy.add(presentedBy);
        _model.add(_model.createStatement(_resource, presentedByProperty, presentedBy.resource()));
        return presentedBy;
    }

    public void removePresentedBy(org.marcont.services.definitions.service.Service presentedBy) throws JastorException {
        if (this.presentedBy == null) initPresentedBy();
        if (!this.presentedBy.contains(presentedBy)) return;
        if (!_model.contains(_resource, presentedByProperty, presentedBy.resource())) return;
        this.presentedBy.remove(presentedBy);
        _model.removeAll(_resource, presentedByProperty, presentedBy.resource());
    }

    private void initProvides() throws JastorException {
        this.provides = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, providesProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.daml.org/services/owl-s/1.2/Service.owl#provides properties in ServiceProfile model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                org.marcont.services.definitions.service.Service provides = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                this.provides.add(provides);
            }
        }
    }

    public java.util.Iterator getProvides() throws JastorException {
        if (provides == null) initProvides();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(provides, _resource, providesProperty, true);
    }

    public void addProvides(org.marcont.services.definitions.service.Service provides) throws JastorException {
        if (this.provides == null) initProvides();
        if (this.provides.contains(provides)) {
            this.provides.remove(provides);
            this.provides.add(provides);
            return;
        }
        this.provides.add(provides);
        _model.add(_model.createStatement(_resource, providesProperty, provides.resource()));
    }

    public org.marcont.services.definitions.service.Service addProvides() throws JastorException {
        org.marcont.services.definitions.service.Service provides = org.marcont.services.definitions.service.Service_DOT_owlFactory.createService(_model.createResource(), _model);
        if (this.provides == null) initProvides();
        this.provides.add(provides);
        _model.add(_model.createStatement(_resource, providesProperty, provides.resource()));
        return provides;
    }

    public org.marcont.services.definitions.service.Service addProvides(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        org.marcont.services.definitions.service.Service provides = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
        if (this.provides == null) initProvides();
        if (this.provides.contains(provides)) return provides;
        this.provides.add(provides);
        _model.add(_model.createStatement(_resource, providesProperty, provides.resource()));
        return provides;
    }

    public void removeProvides(org.marcont.services.definitions.service.Service provides) throws JastorException {
        if (this.provides == null) initProvides();
        if (!this.provides.contains(provides)) return;
        if (!_model.contains(_resource, providesProperty, provides.resource())) return;
        this.provides.remove(provides);
        _model.removeAll(_resource, providesProperty, provides.resource());
    }

    private java.util.ArrayList listeners;

    public void registerListener(ThingListener listener) {
        if (!(listener instanceof ServiceProfileListener)) throw new IllegalArgumentException("ThingListener must be instance of ServiceProfileListener");
        if (listeners == null) setupModelListener();
        if (!this.listeners.contains(listener)) {
            this.listeners.add((ServiceProfileListener) listener);
        }
    }

    public void unregisterListener(ThingListener listener) {
        if (!(listener instanceof ServiceProfileListener)) throw new IllegalArgumentException("ThingListener must be instance of ServiceProfileListener");
        if (listeners == null) return;
        if (this.listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void addedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(presentedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                org.marcont.services.definitions.service.Service _presentedBy = null;
                try {
                    _presentedBy = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                } catch (JastorException e) {
                }
                if (presentedBy == null) {
                    try {
                        initPresentedBy();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!presentedBy.contains(_presentedBy)) presentedBy.add(_presentedBy);
                if (listeners != null) {
                    java.util.ArrayList consumersForPresentedBy;
                    synchronized (listeners) {
                        consumersForPresentedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForPresentedBy.iterator(); iter.hasNext(); ) {
                        ServiceProfileListener listener = (ServiceProfileListener) iter.next();
                        listener.presentedByAdded(org.marcont.services.definitions.service.ServiceProfileImpl.this, _presentedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(providesProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                org.marcont.services.definitions.service.Service _provides = null;
                try {
                    _provides = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                } catch (JastorException e) {
                }
                if (provides == null) {
                    try {
                        initProvides();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!provides.contains(_provides)) provides.add(_provides);
                if (listeners != null) {
                    java.util.ArrayList consumersForProvides;
                    synchronized (listeners) {
                        consumersForProvides = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForProvides.iterator(); iter.hasNext(); ) {
                        ServiceProfileListener listener = (ServiceProfileListener) iter.next();
                        listener.providesAdded(org.marcont.services.definitions.service.ServiceProfileImpl.this, _provides);
                    }
                }
            }
            return;
        }
    }

    public void removedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(presentedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                org.marcont.services.definitions.service.Service _presentedBy = null;
                if (presentedBy != null) {
                    boolean found = false;
                    for (int i = 0; i < presentedBy.size(); i++) {
                        org.marcont.services.definitions.service.Service __item = (org.marcont.services.definitions.service.Service) presentedBy.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _presentedBy = __item;
                            break;
                        }
                    }
                    if (found) presentedBy.remove(_presentedBy); else {
                        try {
                            _presentedBy = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _presentedBy = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForPresentedBy;
                    synchronized (listeners) {
                        consumersForPresentedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForPresentedBy.iterator(); iter.hasNext(); ) {
                        ServiceProfileListener listener = (ServiceProfileListener) iter.next();
                        listener.presentedByRemoved(org.marcont.services.definitions.service.ServiceProfileImpl.this, _presentedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(providesProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                org.marcont.services.definitions.service.Service _provides = null;
                if (provides != null) {
                    boolean found = false;
                    for (int i = 0; i < provides.size(); i++) {
                        org.marcont.services.definitions.service.Service __item = (org.marcont.services.definitions.service.Service) provides.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _provides = __item;
                            break;
                        }
                    }
                    if (found) provides.remove(_provides); else {
                        try {
                            _provides = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _provides = org.marcont.services.definitions.service.Service_DOT_owlFactory.getService(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForProvides;
                    synchronized (listeners) {
                        consumersForProvides = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForProvides.iterator(); iter.hasNext(); ) {
                        ServiceProfileListener listener = (ServiceProfileListener) iter.next();
                        listener.providesRemoved(org.marcont.services.definitions.service.ServiceProfileImpl.this, _provides);
                    }
                }
            }
            return;
        }
    }
}
