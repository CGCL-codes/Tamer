package org.marcont.services.definitions.process;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.listeners.StatementListener;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.adtech.jastor.*;
import com.ibm.adtech.jastor.util.*;

/**
 * Implementation of {@link org.marcont.services.definitions.process.ControlConstructBag}
 * Use the org.marcont.services.definitions.process.Process_DOT_owlFactory to create instances of this class.
 * <p>(URI: http://www.daml.org/services/owl-s/1.2/Process.owl#ControlConstructBag)</p>
 * <br>
 */
public class ControlConstructBagImpl extends com.ibm.adtech.jastor.ThingImpl implements org.marcont.services.definitions.process.ControlConstructBag {

    private static com.hp.hpl.jena.rdf.model.Property firstProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/generic/ObjectList.owl#first");

    private java.util.ArrayList first;

    private java.util.ArrayList first_asControlConstruct;

    private static com.hp.hpl.jena.rdf.model.Property restProperty = ResourceFactory.createProperty("http://www.daml.org/services/owl-s/1.2/generic/ObjectList.owl#rest");

    private java.util.ArrayList rest;

    private java.util.ArrayList rest_asControlConstructBag;

    ControlConstructBagImpl(Resource resource, Model model) throws JastorException {
        super(resource, model);
        setupModelListener();
    }

    static ControlConstructBagImpl getControlConstructBag(Resource resource, Model model) throws JastorException {
        return new ControlConstructBagImpl(resource, model);
    }

    static ControlConstructBagImpl createControlConstructBag(Resource resource, Model model) throws JastorException {
        ControlConstructBagImpl impl = new ControlConstructBagImpl(resource, model);
        if (!impl._model.contains(new com.hp.hpl.jena.rdf.model.impl.StatementImpl(impl._resource, RDF.type, ControlConstructBag.TYPE))) impl._model.add(impl._resource, RDF.type, ControlConstructBag.TYPE);
        impl.addSuperTypes();
        impl.addHasValueValues();
        return impl;
    }

    void addSuperTypes() {
        if (!_model.contains(_resource, RDF.type, org.marcont.services.definitions.objectList.List.TYPE)) _model.add(new com.hp.hpl.jena.rdf.model.impl.StatementImpl(_resource, RDF.type, org.marcont.services.definitions.objectList.List.TYPE));
    }

    void addHasValueValues() {
    }

    private void setupModelListener() {
        listeners = new java.util.ArrayList();
        org.marcont.services.definitions.process.Process_DOT_owlFactory.registerThing(this);
    }

    public java.util.List listStatements() {
        java.util.List list = new java.util.ArrayList();
        StmtIterator it = null;
        it = _model.listStatements(_resource, firstProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, restProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, RDF.type, org.marcont.services.definitions.process.ControlConstructBag.TYPE);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, RDF.type, org.marcont.services.definitions.objectList.List.TYPE);
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public void clearCache() {
        first = null;
        first_asControlConstruct = null;
        rest = null;
        rest_asControlConstructBag = null;
    }

    private com.hp.hpl.jena.rdf.model.Literal createLiteral(Object obj) {
        return _model.createTypedLiteral(obj);
    }

    private void initFirst() throws JastorException {
        this.first = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, firstProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.daml.org/services/owl-s/1.2/generic/ObjectList.owl#first properties in ControlConstructBag model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing first = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.first.add(first);
            }
        }
    }

    public java.util.Iterator getFirst() throws JastorException {
        if (first == null) initFirst();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(first, _resource, firstProperty, true);
    }

    public void addFirst(com.ibm.adtech.jastor.Thing first) throws JastorException {
        if (this.first == null) initFirst();
        if (this.first.contains(first)) {
            this.first.remove(first);
            this.first.add(first);
            return;
        }
        this.first.add(first);
        _model.add(_model.createStatement(_resource, firstProperty, first.resource()));
    }

    public com.ibm.adtech.jastor.Thing addFirst() throws JastorException {
        com.ibm.adtech.jastor.Thing first = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.first == null) initFirst();
        this.first.add(first);
        _model.add(_model.createStatement(_resource, firstProperty, first.resource()));
        return first;
    }

    public com.ibm.adtech.jastor.Thing addFirst(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing first = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.first == null) initFirst();
        if (this.first.contains(first)) return first;
        this.first.add(first);
        _model.add(_model.createStatement(_resource, firstProperty, first.resource()));
        return first;
    }

    public void removeFirst(com.ibm.adtech.jastor.Thing first) throws JastorException {
        if (this.first == null) initFirst();
        if (!this.first.contains(first)) return;
        if (!_model.contains(_resource, firstProperty, first.resource())) return;
        this.first.remove(first);
        _model.removeAll(_resource, firstProperty, first.resource());
    }

    private void initFirst_asControlConstruct() throws JastorException {
        this.first_asControlConstruct = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, firstProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.daml.org/services/owl-s/1.2/generic/ObjectList.owl#first properties in ControlConstructBag model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (_model.contains(resource, RDF.type, org.marcont.services.definitions.process.ControlConstruct.TYPE)) {
                org.marcont.services.definitions.process.ControlConstruct first = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstruct(resource, _model);
                this.first_asControlConstruct.add(first);
            }
        }
    }

    public java.util.Iterator getFirst_asControlConstruct() throws JastorException {
        if (first_asControlConstruct == null) initFirst_asControlConstruct();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(first_asControlConstruct, _resource, firstProperty, true);
    }

    public void addFirst(org.marcont.services.definitions.process.ControlConstruct first) throws JastorException {
        if (this.first_asControlConstruct == null) initFirst_asControlConstruct();
        if (this.first_asControlConstruct.contains(first)) {
            this.first_asControlConstruct.remove(first);
            this.first_asControlConstruct.add(first);
            return;
        }
        this.first_asControlConstruct.add(first);
        _model.add(_model.createStatement(_resource, firstProperty, first.resource()));
    }

    public org.marcont.services.definitions.process.ControlConstruct addFirst_asControlConstruct() throws JastorException {
        org.marcont.services.definitions.process.ControlConstruct first = org.marcont.services.definitions.process.Process_DOT_owlFactory.createControlConstruct(_model.createResource(), _model);
        if (this.first_asControlConstruct == null) initFirst_asControlConstruct();
        this.first_asControlConstruct.add(first);
        _model.add(_model.createStatement(_resource, firstProperty, first.resource()));
        return first;
    }

    public org.marcont.services.definitions.process.ControlConstruct addFirst_asControlConstruct(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        org.marcont.services.definitions.process.ControlConstruct first = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstruct(resource, _model);
        if (this.first_asControlConstruct == null) initFirst_asControlConstruct();
        if (this.first_asControlConstruct.contains(first)) return first;
        this.first_asControlConstruct.add(first);
        _model.add(_model.createStatement(_resource, firstProperty, first.resource()));
        return first;
    }

    public void removeFirst(org.marcont.services.definitions.process.ControlConstruct first) throws JastorException {
        if (this.first_asControlConstruct == null) initFirst_asControlConstruct();
        if (!this.first_asControlConstruct.contains(first)) return;
        if (!_model.contains(_resource, firstProperty, first.resource())) return;
        this.first_asControlConstruct.remove(first);
        _model.removeAll(_resource, firstProperty, first.resource());
    }

    private void initRest() throws JastorException {
        this.rest = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, restProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.daml.org/services/owl-s/1.2/generic/ObjectList.owl#rest properties in ControlConstructBag model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing rest = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.rest.add(rest);
            }
        }
    }

    public java.util.Iterator getRest() throws JastorException {
        if (rest == null) initRest();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(rest, _resource, restProperty, true);
    }

    public void addRest(com.ibm.adtech.jastor.Thing rest) throws JastorException {
        if (this.rest == null) initRest();
        if (this.rest.contains(rest)) {
            this.rest.remove(rest);
            this.rest.add(rest);
            return;
        }
        this.rest.add(rest);
        _model.add(_model.createStatement(_resource, restProperty, rest.resource()));
    }

    public com.ibm.adtech.jastor.Thing addRest() throws JastorException {
        com.ibm.adtech.jastor.Thing rest = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.rest == null) initRest();
        this.rest.add(rest);
        _model.add(_model.createStatement(_resource, restProperty, rest.resource()));
        return rest;
    }

    public com.ibm.adtech.jastor.Thing addRest(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing rest = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.rest == null) initRest();
        if (this.rest.contains(rest)) return rest;
        this.rest.add(rest);
        _model.add(_model.createStatement(_resource, restProperty, rest.resource()));
        return rest;
    }

    public void removeRest(com.ibm.adtech.jastor.Thing rest) throws JastorException {
        if (this.rest == null) initRest();
        if (!this.rest.contains(rest)) return;
        if (!_model.contains(_resource, restProperty, rest.resource())) return;
        this.rest.remove(rest);
        _model.removeAll(_resource, restProperty, rest.resource());
    }

    private void initRest_asControlConstructBag() throws JastorException {
        this.rest_asControlConstructBag = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, restProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.daml.org/services/owl-s/1.2/generic/ObjectList.owl#rest properties in ControlConstructBag model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (_model.contains(resource, RDF.type, org.marcont.services.definitions.process.ControlConstructBag.TYPE)) {
                org.marcont.services.definitions.process.ControlConstructBag rest = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstructBag(resource, _model);
                this.rest_asControlConstructBag.add(rest);
            }
        }
    }

    public java.util.Iterator getRest_asControlConstructBag() throws JastorException {
        if (rest_asControlConstructBag == null) initRest_asControlConstructBag();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(rest_asControlConstructBag, _resource, restProperty, true);
    }

    public void addRest(org.marcont.services.definitions.process.ControlConstructBag rest) throws JastorException {
        if (this.rest_asControlConstructBag == null) initRest_asControlConstructBag();
        if (this.rest_asControlConstructBag.contains(rest)) {
            this.rest_asControlConstructBag.remove(rest);
            this.rest_asControlConstructBag.add(rest);
            return;
        }
        this.rest_asControlConstructBag.add(rest);
        _model.add(_model.createStatement(_resource, restProperty, rest.resource()));
    }

    public org.marcont.services.definitions.process.ControlConstructBag addRest_asControlConstructBag() throws JastorException {
        org.marcont.services.definitions.process.ControlConstructBag rest = org.marcont.services.definitions.process.Process_DOT_owlFactory.createControlConstructBag(_model.createResource(), _model);
        if (this.rest_asControlConstructBag == null) initRest_asControlConstructBag();
        this.rest_asControlConstructBag.add(rest);
        _model.add(_model.createStatement(_resource, restProperty, rest.resource()));
        return rest;
    }

    public org.marcont.services.definitions.process.ControlConstructBag addRest_asControlConstructBag(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        org.marcont.services.definitions.process.ControlConstructBag rest = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstructBag(resource, _model);
        if (this.rest_asControlConstructBag == null) initRest_asControlConstructBag();
        if (this.rest_asControlConstructBag.contains(rest)) return rest;
        this.rest_asControlConstructBag.add(rest);
        _model.add(_model.createStatement(_resource, restProperty, rest.resource()));
        return rest;
    }

    public void removeRest(org.marcont.services.definitions.process.ControlConstructBag rest) throws JastorException {
        if (this.rest_asControlConstructBag == null) initRest_asControlConstructBag();
        if (!this.rest_asControlConstructBag.contains(rest)) return;
        if (!_model.contains(_resource, restProperty, rest.resource())) return;
        this.rest_asControlConstructBag.remove(rest);
        _model.removeAll(_resource, restProperty, rest.resource());
    }

    private java.util.ArrayList listeners;

    public void registerListener(ThingListener listener) {
        if (!(listener instanceof ControlConstructBagListener)) throw new IllegalArgumentException("ThingListener must be instance of ControlConstructBagListener");
        if (listeners == null) setupModelListener();
        if (!this.listeners.contains(listener)) {
            this.listeners.add((ControlConstructBagListener) listener);
        }
    }

    public void unregisterListener(ThingListener listener) {
        if (!(listener instanceof ControlConstructBagListener)) throw new IllegalArgumentException("ThingListener must be instance of ControlConstructBagListener");
        if (listeners == null) return;
        if (this.listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void addedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(firstProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _first = null;
                try {
                    _first = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (first == null) {
                    try {
                        initFirst();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!first.contains(_first)) first.add(_first);
                if (listeners != null) {
                    java.util.ArrayList consumersForFirst;
                    synchronized (listeners) {
                        consumersForFirst = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForFirst.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.firstAdded(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _first);
                    }
                }
            }
            if (_model.contains(resource, RDF.type, org.marcont.services.definitions.process.ControlConstruct.TYPE)) {
                org.marcont.services.definitions.process.ControlConstruct _first_asControlConstruct = null;
                try {
                    _first_asControlConstruct = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstruct(resource, _model);
                } catch (JastorException e) {
                }
                if (first_asControlConstruct == null) {
                    try {
                        initFirst_asControlConstruct();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!first_asControlConstruct.contains(_first_asControlConstruct)) first_asControlConstruct.add(_first_asControlConstruct);
                if (listeners != null) {
                    java.util.ArrayList consumersForFirst_asControlConstruct;
                    synchronized (listeners) {
                        consumersForFirst_asControlConstruct = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForFirst_asControlConstruct.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.firstAdded(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _first_asControlConstruct);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(restProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _rest = null;
                try {
                    _rest = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (rest == null) {
                    try {
                        initRest();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!rest.contains(_rest)) rest.add(_rest);
                if (listeners != null) {
                    java.util.ArrayList consumersForRest;
                    synchronized (listeners) {
                        consumersForRest = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForRest.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.restAdded(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _rest);
                    }
                }
            }
            if (_model.contains(resource, RDF.type, org.marcont.services.definitions.process.ControlConstructBag.TYPE)) {
                org.marcont.services.definitions.process.ControlConstructBag _rest_asControlConstructBag = null;
                try {
                    _rest_asControlConstructBag = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstructBag(resource, _model);
                } catch (JastorException e) {
                }
                if (rest_asControlConstructBag == null) {
                    try {
                        initRest_asControlConstructBag();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!rest_asControlConstructBag.contains(_rest_asControlConstructBag)) rest_asControlConstructBag.add(_rest_asControlConstructBag);
                if (listeners != null) {
                    java.util.ArrayList consumersForRest_asControlConstructBag;
                    synchronized (listeners) {
                        consumersForRest_asControlConstructBag = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForRest_asControlConstructBag.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.restAdded(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _rest_asControlConstructBag);
                    }
                }
            }
            return;
        }
    }

    public void removedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(firstProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _first = null;
                if (first != null) {
                    boolean found = false;
                    for (int i = 0; i < first.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) first.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _first = __item;
                            break;
                        }
                    }
                    if (found) first.remove(_first); else {
                        try {
                            _first = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _first = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForFirst;
                    synchronized (listeners) {
                        consumersForFirst = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForFirst.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.firstRemoved(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _first);
                    }
                }
            }
            if (_model.contains(resource, RDF.type, org.marcont.services.definitions.process.ControlConstruct.TYPE)) {
                org.marcont.services.definitions.process.ControlConstruct _first_asControlConstruct = null;
                if (first_asControlConstruct != null) {
                    boolean found = false;
                    for (int i = 0; i < first_asControlConstruct.size(); i++) {
                        org.marcont.services.definitions.process.ControlConstruct __item = (org.marcont.services.definitions.process.ControlConstruct) first_asControlConstruct.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _first_asControlConstruct = __item;
                            break;
                        }
                    }
                    if (found) first_asControlConstruct.remove(_first_asControlConstruct); else {
                        try {
                            _first_asControlConstruct = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstruct(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _first_asControlConstruct = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstruct(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForFirst_asControlConstruct;
                    synchronized (listeners) {
                        consumersForFirst_asControlConstruct = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForFirst_asControlConstruct.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.firstRemoved(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _first_asControlConstruct);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(restProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _rest = null;
                if (rest != null) {
                    boolean found = false;
                    for (int i = 0; i < rest.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) rest.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _rest = __item;
                            break;
                        }
                    }
                    if (found) rest.remove(_rest); else {
                        try {
                            _rest = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _rest = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForRest;
                    synchronized (listeners) {
                        consumersForRest = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForRest.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.restRemoved(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _rest);
                    }
                }
            }
            if (_model.contains(resource, RDF.type, org.marcont.services.definitions.process.ControlConstructBag.TYPE)) {
                org.marcont.services.definitions.process.ControlConstructBag _rest_asControlConstructBag = null;
                if (rest_asControlConstructBag != null) {
                    boolean found = false;
                    for (int i = 0; i < rest_asControlConstructBag.size(); i++) {
                        org.marcont.services.definitions.process.ControlConstructBag __item = (org.marcont.services.definitions.process.ControlConstructBag) rest_asControlConstructBag.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _rest_asControlConstructBag = __item;
                            break;
                        }
                    }
                    if (found) rest_asControlConstructBag.remove(_rest_asControlConstructBag); else {
                        try {
                            _rest_asControlConstructBag = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstructBag(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _rest_asControlConstructBag = org.marcont.services.definitions.process.Process_DOT_owlFactory.getControlConstructBag(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForRest_asControlConstructBag;
                    synchronized (listeners) {
                        consumersForRest_asControlConstructBag = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForRest_asControlConstructBag.iterator(); iter.hasNext(); ) {
                        ControlConstructBagListener listener = (ControlConstructBagListener) iter.next();
                        listener.restRemoved(org.marcont.services.definitions.process.ControlConstructBagImpl.this, _rest_asControlConstructBag);
                    }
                }
            }
            return;
        }
    }
}
