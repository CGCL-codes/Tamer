package org.nakedobjects.plugins.headless.embedded.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.commons.components.ApplicationScopedComponent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification.CreationMode;
import org.nakedobjects.metamodel.spec.identifier.Identified;
import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAbstract;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.services.ServicesInjectorDefault;
import org.nakedobjects.plugins.headless.embedded.EmbeddedContext;
import org.nakedobjects.plugins.headless.embedded.internal.PersistenceState;
import org.nakedobjects.plugins.headless.embedded.internal.ServiceAdapter;
import org.nakedobjects.plugins.headless.embedded.internal.StandaloneAdapter;

/**
 * Acts as a bridge between the {@link RuntimeContext} (as used internally
 * within the meta-model) and the {@link EmbeddedContext} 
 * provided by the embedder (which deals only with pojos).
 */
public class RuntimeContextForEmbeddedMetaModel extends RuntimeContextAbstract implements ApplicationScopedComponent {

    private final EmbeddedContext context;

    private final List<Object> services;

    private List<NakedObject> serviceAdapters;

    private ServicesInjector servicesInjector;

    public RuntimeContextForEmbeddedMetaModel(final EmbeddedContext context, final List<Object> services) {
        this.context = context;
        this.services = services;
    }

    public void init() {
        this.serviceAdapters = adaptersFor(services);
        servicesInjector = new ServicesInjectorDefault();
        servicesInjector.setContainer(getContainer());
        servicesInjector.setServices(services);
    }

    public void shutdown() {
    }

    private List<NakedObject> adaptersFor(List<Object> services) {
        List<NakedObject> serviceAdapters = new ArrayList<NakedObject>();
        for (Object service : services) {
            NakedObjectSpecification spec = getSpecificationLoader().loadSpecification(service.getClass());
            serviceAdapters.add(new ServiceAdapter(spec, service));
        }
        return Collections.unmodifiableList(serviceAdapters);
    }

    public AuthenticationSession getAuthenticationSession() {
        return context.getAuthenticationSession();
    }

    public NakedObject adapterFor(Object domainObject) {
        NakedObjectSpecification domainObjectSpec = getSpecificationLoader().loadSpecification(domainObject.getClass());
        PersistenceState persistenceState = context.getPersistenceState(domainObject);
        return new StandaloneAdapter(domainObjectSpec, domainObject, persistenceState);
    }

    public NakedObject adapterFor(Object domainObject, NakedObject ownerAdapter, Identified identified) {
        return adapterFor(domainObject);
    }

    public NakedObject getAdapterFor(Object domainObject) {
        return adapterFor(domainObject);
    }

    public NakedObject getAdapterFor(Oid oid) {
        throw new UnsupportedOperationException("Not supported by this implementation of RuntimeContext");
    }

    public NakedObject createTransientInstance(NakedObjectSpecification spec) {
        Object domainObject = spec.createObject(CreationMode.INITIALIZE);
        return adapterFor(domainObject);
    }

    public Object instantiate(Class<?> type) throws ObjectInstantiationException {
        return context.instantiate(type);
    }

    public void resolve(Object parent) {
        context.resolve(parent);
    }

    public void resolve(Object parent, Object field) {
        context.resolve(parent, field);
    }

    public void objectChanged(NakedObject adapter) {
        context.objectChanged(adapter.getObject());
    }

    public void objectChanged(Object object) {
        context.objectChanged(object);
    }

    public void makePersistent(NakedObject adapter) {
        context.makePersistent(adapter.getObject());
    }

    public void remove(NakedObject adapter) {
        context.remove(adapter.getObject());
    }

    public boolean flush() {
        return context.flush();
    }

    public void commit() {
        context.commit();
    }

    public <T> List<NakedObject> allMatchingQuery(Query<T> query) {
        return wrap(context.allMatchingQuery(query));
    }

    public <T> NakedObject firstMatchingQuery(Query<T> query) {
        return adapterFor(context.firstMatchingQuery(query));
    }

    private List<NakedObject> wrap(List<?> pojos) {
        List<NakedObject> adapters = new ArrayList<NakedObject>();
        for (Object pojo : pojos) {
            adapters.add(adapterFor(pojo));
        }
        return adapters;
    }

    public void informUser(String message) {
        context.informUser(message);
    }

    public void warnUser(String message) {
        context.warnUser(message);
    }

    public void raiseError(String message) {
        context.raiseError(message);
    }

    /**
	 * Unmodifiable. 
	 */
    public List<NakedObject> getServices() {
        return serviceAdapters;
    }

    public void injectDependenciesInto(Object domainObject) {
        if (servicesInjector == null) {
            throw new IllegalStateException("must setContainer before using this method");
        }
        servicesInjector.injectDependencies(domainObject);
    }

    public ServicesInjector getServicesInjector() {
        return servicesInjector;
    }
}
