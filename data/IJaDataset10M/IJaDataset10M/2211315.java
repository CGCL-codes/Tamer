package org.marcont.services.definitions.timeEntry;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.listeners.StatementListener;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.adtech.jastor.*;
import com.ibm.adtech.jastor.util.*;

/**
 * Implementation of {@link org.marcont.services.definitions.timeEntry.TimeZone}
 * Use the org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory to create instances of this class.
 * <p>(URI: http://www.isi.edu/~pan/damltime/timezone-ont.owl#TimeZone)</p>
 * <br>
 */
public class TimeZoneImpl extends com.ibm.adtech.jastor.ThingImpl implements org.marcont.services.definitions.timeEntry.TimeZone {

    private static com.hp.hpl.jena.rdf.model.Property intFinishedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intFinishedBy");

    private java.util.ArrayList intFinishedBy;

    private static com.hp.hpl.jena.rdf.model.Property intOverlappedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intOverlappedBy");

    private java.util.ArrayList intOverlappedBy;

    private static com.hp.hpl.jena.rdf.model.Property intStartedByProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intStartedBy");

    private java.util.ArrayList intStartedBy;

    private static com.hp.hpl.jena.rdf.model.Property afterProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#after");

    private java.util.ArrayList after;

    private static com.hp.hpl.jena.rdf.model.Property intContainsProperty = ResourceFactory.createProperty("http://www.isi.edu/~pan/damltime/time-entry.owl#intContains");

    private java.util.ArrayList intContains;

    TimeZoneImpl(Resource resource, Model model) throws JastorException {
        super(resource, model);
        setupModelListener();
    }

    static TimeZoneImpl getTimeZone(Resource resource, Model model) throws JastorException {
        return new TimeZoneImpl(resource, model);
    }

    static TimeZoneImpl createTimeZone(Resource resource, Model model) throws JastorException {
        TimeZoneImpl impl = new TimeZoneImpl(resource, model);
        if (!impl._model.contains(new com.hp.hpl.jena.rdf.model.impl.StatementImpl(impl._resource, RDF.type, TimeZone.TYPE))) impl._model.add(impl._resource, RDF.type, TimeZone.TYPE);
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
        org.marcont.services.definitions.timeEntry.time_DASH_entry_DOT_owlFactory.registerThing(this);
    }

    public java.util.List listStatements() {
        java.util.List list = new java.util.ArrayList();
        StmtIterator it = null;
        it = _model.listStatements(_resource, intFinishedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, intOverlappedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, intStartedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, afterProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, intContainsProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, RDF.type, org.marcont.services.definitions.timeEntry.TimeZone.TYPE);
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public void clearCache() {
        intFinishedBy = null;
        intOverlappedBy = null;
        intStartedBy = null;
        after = null;
        intContains = null;
    }

    private com.hp.hpl.jena.rdf.model.Literal createLiteral(Object obj) {
        return _model.createTypedLiteral(obj);
    }

    private void initIntFinishedBy() throws JastorException {
        this.intFinishedBy = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intFinishedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intFinishedBy properties in TimeZone model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intFinishedBy.add(intFinishedBy);
            }
        }
    }

    public java.util.Iterator getIntFinishedBy() throws JastorException {
        if (intFinishedBy == null) initIntFinishedBy();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intFinishedBy, _resource, intFinishedByProperty, true);
    }

    public void addIntFinishedBy(com.ibm.adtech.jastor.Thing intFinishedBy) throws JastorException {
        if (this.intFinishedBy == null) initIntFinishedBy();
        if (this.intFinishedBy.contains(intFinishedBy)) {
            this.intFinishedBy.remove(intFinishedBy);
            this.intFinishedBy.add(intFinishedBy);
            return;
        }
        this.intFinishedBy.add(intFinishedBy);
        _model.add(_model.createStatement(_resource, intFinishedByProperty, intFinishedBy.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntFinishedBy() throws JastorException {
        com.ibm.adtech.jastor.Thing intFinishedBy = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intFinishedBy == null) initIntFinishedBy();
        this.intFinishedBy.add(intFinishedBy);
        _model.add(_model.createStatement(_resource, intFinishedByProperty, intFinishedBy.resource()));
        return intFinishedBy;
    }

    public com.ibm.adtech.jastor.Thing addIntFinishedBy(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intFinishedBy == null) initIntFinishedBy();
        if (this.intFinishedBy.contains(intFinishedBy)) return intFinishedBy;
        this.intFinishedBy.add(intFinishedBy);
        _model.add(_model.createStatement(_resource, intFinishedByProperty, intFinishedBy.resource()));
        return intFinishedBy;
    }

    public void removeIntFinishedBy(com.ibm.adtech.jastor.Thing intFinishedBy) throws JastorException {
        if (this.intFinishedBy == null) initIntFinishedBy();
        if (!this.intFinishedBy.contains(intFinishedBy)) return;
        if (!_model.contains(_resource, intFinishedByProperty, intFinishedBy.resource())) return;
        this.intFinishedBy.remove(intFinishedBy);
        _model.removeAll(_resource, intFinishedByProperty, intFinishedBy.resource());
    }

    private void initIntOverlappedBy() throws JastorException {
        this.intOverlappedBy = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intOverlappedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intOverlappedBy properties in TimeZone model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intOverlappedBy.add(intOverlappedBy);
            }
        }
    }

    public java.util.Iterator getIntOverlappedBy() throws JastorException {
        if (intOverlappedBy == null) initIntOverlappedBy();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intOverlappedBy, _resource, intOverlappedByProperty, true);
    }

    public void addIntOverlappedBy(com.ibm.adtech.jastor.Thing intOverlappedBy) throws JastorException {
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        if (this.intOverlappedBy.contains(intOverlappedBy)) {
            this.intOverlappedBy.remove(intOverlappedBy);
            this.intOverlappedBy.add(intOverlappedBy);
            return;
        }
        this.intOverlappedBy.add(intOverlappedBy);
        _model.add(_model.createStatement(_resource, intOverlappedByProperty, intOverlappedBy.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntOverlappedBy() throws JastorException {
        com.ibm.adtech.jastor.Thing intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        this.intOverlappedBy.add(intOverlappedBy);
        _model.add(_model.createStatement(_resource, intOverlappedByProperty, intOverlappedBy.resource()));
        return intOverlappedBy;
    }

    public com.ibm.adtech.jastor.Thing addIntOverlappedBy(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        if (this.intOverlappedBy.contains(intOverlappedBy)) return intOverlappedBy;
        this.intOverlappedBy.add(intOverlappedBy);
        _model.add(_model.createStatement(_resource, intOverlappedByProperty, intOverlappedBy.resource()));
        return intOverlappedBy;
    }

    public void removeIntOverlappedBy(com.ibm.adtech.jastor.Thing intOverlappedBy) throws JastorException {
        if (this.intOverlappedBy == null) initIntOverlappedBy();
        if (!this.intOverlappedBy.contains(intOverlappedBy)) return;
        if (!_model.contains(_resource, intOverlappedByProperty, intOverlappedBy.resource())) return;
        this.intOverlappedBy.remove(intOverlappedBy);
        _model.removeAll(_resource, intOverlappedByProperty, intOverlappedBy.resource());
    }

    private void initIntStartedBy() throws JastorException {
        this.intStartedBy = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intStartedByProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intStartedBy properties in TimeZone model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intStartedBy.add(intStartedBy);
            }
        }
    }

    public java.util.Iterator getIntStartedBy() throws JastorException {
        if (intStartedBy == null) initIntStartedBy();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intStartedBy, _resource, intStartedByProperty, true);
    }

    public void addIntStartedBy(com.ibm.adtech.jastor.Thing intStartedBy) throws JastorException {
        if (this.intStartedBy == null) initIntStartedBy();
        if (this.intStartedBy.contains(intStartedBy)) {
            this.intStartedBy.remove(intStartedBy);
            this.intStartedBy.add(intStartedBy);
            return;
        }
        this.intStartedBy.add(intStartedBy);
        _model.add(_model.createStatement(_resource, intStartedByProperty, intStartedBy.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntStartedBy() throws JastorException {
        com.ibm.adtech.jastor.Thing intStartedBy = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intStartedBy == null) initIntStartedBy();
        this.intStartedBy.add(intStartedBy);
        _model.add(_model.createStatement(_resource, intStartedByProperty, intStartedBy.resource()));
        return intStartedBy;
    }

    public com.ibm.adtech.jastor.Thing addIntStartedBy(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intStartedBy == null) initIntStartedBy();
        if (this.intStartedBy.contains(intStartedBy)) return intStartedBy;
        this.intStartedBy.add(intStartedBy);
        _model.add(_model.createStatement(_resource, intStartedByProperty, intStartedBy.resource()));
        return intStartedBy;
    }

    public void removeIntStartedBy(com.ibm.adtech.jastor.Thing intStartedBy) throws JastorException {
        if (this.intStartedBy == null) initIntStartedBy();
        if (!this.intStartedBy.contains(intStartedBy)) return;
        if (!_model.contains(_resource, intStartedByProperty, intStartedBy.resource())) return;
        this.intStartedBy.remove(intStartedBy);
        _model.removeAll(_resource, intStartedByProperty, intStartedBy.resource());
    }

    private void initAfter() throws JastorException {
        this.after = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, afterProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#after properties in TimeZone model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.after.add(after);
            }
        }
    }

    public java.util.Iterator getAfter() throws JastorException {
        if (after == null) initAfter();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(after, _resource, afterProperty, true);
    }

    public void addAfter(com.ibm.adtech.jastor.Thing after) throws JastorException {
        if (this.after == null) initAfter();
        if (this.after.contains(after)) {
            this.after.remove(after);
            this.after.add(after);
            return;
        }
        this.after.add(after);
        _model.add(_model.createStatement(_resource, afterProperty, after.resource()));
    }

    public com.ibm.adtech.jastor.Thing addAfter() throws JastorException {
        com.ibm.adtech.jastor.Thing after = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.after == null) initAfter();
        this.after.add(after);
        _model.add(_model.createStatement(_resource, afterProperty, after.resource()));
        return after;
    }

    public com.ibm.adtech.jastor.Thing addAfter(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.after == null) initAfter();
        if (this.after.contains(after)) return after;
        this.after.add(after);
        _model.add(_model.createStatement(_resource, afterProperty, after.resource()));
        return after;
    }

    public void removeAfter(com.ibm.adtech.jastor.Thing after) throws JastorException {
        if (this.after == null) initAfter();
        if (!this.after.contains(after)) return;
        if (!_model.contains(_resource, afterProperty, after.resource())) return;
        this.after.remove(after);
        _model.removeAll(_resource, afterProperty, after.resource());
    }

    private void initIntContains() throws JastorException {
        this.intContains = new java.util.ArrayList();
        StmtIterator it = _model.listStatements(_resource, intContainsProperty, (RDFNode) null);
        while (it.hasNext()) {
            com.hp.hpl.jena.rdf.model.Statement stmt = (com.hp.hpl.jena.rdf.model.Statement) it.next();
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) throw new JastorInvalidRDFNodeException(uri() + ": One of the http://www.isi.edu/~pan/damltime/time-entry.owl#intContains properties in TimeZone model not a Resource", stmt.getObject());
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                this.intContains.add(intContains);
            }
        }
    }

    public java.util.Iterator getIntContains() throws JastorException {
        if (intContains == null) initIntContains();
        return new com.ibm.adtech.jastor.util.CachedPropertyIterator(intContains, _resource, intContainsProperty, true);
    }

    public void addIntContains(com.ibm.adtech.jastor.Thing intContains) throws JastorException {
        if (this.intContains == null) initIntContains();
        if (this.intContains.contains(intContains)) {
            this.intContains.remove(intContains);
            this.intContains.add(intContains);
            return;
        }
        this.intContains.add(intContains);
        _model.add(_model.createStatement(_resource, intContainsProperty, intContains.resource()));
    }

    public com.ibm.adtech.jastor.Thing addIntContains() throws JastorException {
        com.ibm.adtech.jastor.Thing intContains = com.ibm.adtech.jastor.ThingFactory.createThing(_model.createResource(), _model);
        if (this.intContains == null) initIntContains();
        this.intContains.add(intContains);
        _model.add(_model.createStatement(_resource, intContainsProperty, intContains.resource()));
        return intContains;
    }

    public com.ibm.adtech.jastor.Thing addIntContains(com.hp.hpl.jena.rdf.model.Resource resource) throws JastorException {
        com.ibm.adtech.jastor.Thing intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
        if (this.intContains == null) initIntContains();
        if (this.intContains.contains(intContains)) return intContains;
        this.intContains.add(intContains);
        _model.add(_model.createStatement(_resource, intContainsProperty, intContains.resource()));
        return intContains;
    }

    public void removeIntContains(com.ibm.adtech.jastor.Thing intContains) throws JastorException {
        if (this.intContains == null) initIntContains();
        if (!this.intContains.contains(intContains)) return;
        if (!_model.contains(_resource, intContainsProperty, intContains.resource())) return;
        this.intContains.remove(intContains);
        _model.removeAll(_resource, intContainsProperty, intContains.resource());
    }

    private java.util.ArrayList listeners;

    public void registerListener(ThingListener listener) {
        if (!(listener instanceof TimeZoneListener)) throw new IllegalArgumentException("ThingListener must be instance of TimeZoneListener");
        if (listeners == null) setupModelListener();
        if (!this.listeners.contains(listener)) {
            this.listeners.add((TimeZoneListener) listener);
        }
    }

    public void unregisterListener(ThingListener listener) {
        if (!(listener instanceof TimeZoneListener)) throw new IllegalArgumentException("ThingListener must be instance of TimeZoneListener");
        if (listeners == null) return;
        if (this.listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void addedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(intFinishedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intFinishedBy = null;
                try {
                    _intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intFinishedBy == null) {
                    try {
                        initIntFinishedBy();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intFinishedBy.contains(_intFinishedBy)) intFinishedBy.add(_intFinishedBy);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntFinishedBy;
                    synchronized (listeners) {
                        consumersForIntFinishedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntFinishedBy.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intFinishedByAdded(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intFinishedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intOverlappedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intOverlappedBy = null;
                try {
                    _intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intOverlappedBy == null) {
                    try {
                        initIntOverlappedBy();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intOverlappedBy.contains(_intOverlappedBy)) intOverlappedBy.add(_intOverlappedBy);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntOverlappedBy;
                    synchronized (listeners) {
                        consumersForIntOverlappedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntOverlappedBy.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intOverlappedByAdded(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intOverlappedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intStartedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intStartedBy = null;
                try {
                    _intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intStartedBy == null) {
                    try {
                        initIntStartedBy();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intStartedBy.contains(_intStartedBy)) intStartedBy.add(_intStartedBy);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntStartedBy;
                    synchronized (listeners) {
                        consumersForIntStartedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntStartedBy.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intStartedByAdded(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intStartedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(afterProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _after = null;
                try {
                    _after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (after == null) {
                    try {
                        initAfter();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!after.contains(_after)) after.add(_after);
                if (listeners != null) {
                    java.util.ArrayList consumersForAfter;
                    synchronized (listeners) {
                        consumersForAfter = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForAfter.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.afterAdded(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _after);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intContainsProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intContains = null;
                try {
                    _intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                } catch (JastorException e) {
                }
                if (intContains == null) {
                    try {
                        initIntContains();
                    } catch (JastorException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (!intContains.contains(_intContains)) intContains.add(_intContains);
                if (listeners != null) {
                    java.util.ArrayList consumersForIntContains;
                    synchronized (listeners) {
                        consumersForIntContains = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntContains.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intContainsAdded(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intContains);
                    }
                }
            }
            return;
        }
    }

    public void removedStatement(com.hp.hpl.jena.rdf.model.Statement stmt) {
        if (stmt.getPredicate().equals(intFinishedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intFinishedBy = null;
                if (intFinishedBy != null) {
                    boolean found = false;
                    for (int i = 0; i < intFinishedBy.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intFinishedBy.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intFinishedBy = __item;
                            break;
                        }
                    }
                    if (found) intFinishedBy.remove(_intFinishedBy); else {
                        try {
                            _intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intFinishedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntFinishedBy;
                    synchronized (listeners) {
                        consumersForIntFinishedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntFinishedBy.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intFinishedByRemoved(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intFinishedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intOverlappedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intOverlappedBy = null;
                if (intOverlappedBy != null) {
                    boolean found = false;
                    for (int i = 0; i < intOverlappedBy.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intOverlappedBy.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intOverlappedBy = __item;
                            break;
                        }
                    }
                    if (found) intOverlappedBy.remove(_intOverlappedBy); else {
                        try {
                            _intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intOverlappedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntOverlappedBy;
                    synchronized (listeners) {
                        consumersForIntOverlappedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntOverlappedBy.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intOverlappedByRemoved(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intOverlappedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intStartedByProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intStartedBy = null;
                if (intStartedBy != null) {
                    boolean found = false;
                    for (int i = 0; i < intStartedBy.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intStartedBy.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intStartedBy = __item;
                            break;
                        }
                    }
                    if (found) intStartedBy.remove(_intStartedBy); else {
                        try {
                            _intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intStartedBy = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntStartedBy;
                    synchronized (listeners) {
                        consumersForIntStartedBy = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntStartedBy.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intStartedByRemoved(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intStartedBy);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(afterProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _after = null;
                if (after != null) {
                    boolean found = false;
                    for (int i = 0; i < after.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) after.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _after = __item;
                            break;
                        }
                    }
                    if (found) after.remove(_after); else {
                        try {
                            _after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _after = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForAfter;
                    synchronized (listeners) {
                        consumersForAfter = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForAfter.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.afterRemoved(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _after);
                    }
                }
            }
            return;
        }
        if (stmt.getPredicate().equals(intContainsProperty)) {
            if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Resource.class)) return;
            com.hp.hpl.jena.rdf.model.Resource resource = (com.hp.hpl.jena.rdf.model.Resource) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Resource.class);
            if (true) {
                com.ibm.adtech.jastor.Thing _intContains = null;
                if (intContains != null) {
                    boolean found = false;
                    for (int i = 0; i < intContains.size(); i++) {
                        com.ibm.adtech.jastor.Thing __item = (com.ibm.adtech.jastor.Thing) intContains.get(i);
                        if (__item.resource().equals(resource)) {
                            found = true;
                            _intContains = __item;
                            break;
                        }
                    }
                    if (found) intContains.remove(_intContains); else {
                        try {
                            _intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                        } catch (JastorException e) {
                        }
                    }
                } else {
                    try {
                        _intContains = com.ibm.adtech.jastor.ThingFactory.getThing(resource, _model);
                    } catch (JastorException e) {
                    }
                }
                if (listeners != null) {
                    java.util.ArrayList consumersForIntContains;
                    synchronized (listeners) {
                        consumersForIntContains = (java.util.ArrayList) listeners.clone();
                    }
                    for (java.util.Iterator iter = consumersForIntContains.iterator(); iter.hasNext(); ) {
                        TimeZoneListener listener = (TimeZoneListener) iter.next();
                        listener.intContainsRemoved(org.marcont.services.definitions.timeEntry.TimeZoneImpl.this, _intContains);
                    }
                }
            }
            return;
        }
    }
}
