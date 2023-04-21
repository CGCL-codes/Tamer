package org.nakedobjects.runtime.testsystem;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.commons.ensure.Ensure.ensureThatArg;
import java.util.Hashtable;
import java.util.Vector;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.services.ServicesInjectorDefault;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerDefault;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionAbstract;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactoryAbstract;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerDefault;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryBasic;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryAbstract.Mode;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionDefault;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManagerAbstract;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;

/**
 * Static mock implementation of {@link PersistenceSession} that provides some partial implementation but also
 * has methods to spy on interactions.
 * 
 * <p>
 * Is an alternative is to using the JMock mocking library.
 * 
 * <p>
 * Previously called <tt>TestProxyPersistor</tt>.
 */
public class TestProxyPersistenceSession extends PersistenceSessionAbstract {

    protected static class AdapterFactoryTestProxyNakedObject extends AdapterFactoryAbstract {

        public TestProxyNakedObject createAdapter(Object pojo, Oid oid) {
            final TestProxyNakedObject testProxyNakedObject = new TestProxyNakedObject();
            testProxyNakedObject.setupObject(pojo);
            testProxyNakedObject.setupOid(oid);
            testProxyNakedObject.setupResolveState(oid == null ? ResolveState.VALUE : oid.isTransient() ? ResolveState.TRANSIENT : ResolveState.GHOST);
            testProxyNakedObject.setupSpecification(NakedObjectsContext.getSpecificationLoader().loadSpecification(pojo.getClass()));
            return testProxyNakedObject;
        }
    }

    private final NakedObjectTransactionManager transactionManager = new NakedObjectTransactionManagerAbstract<NakedObjectTransactionDefault>() {

        public void startTransaction() {
            actions.addElement("start transaction");
            createTransaction();
        }

        @Override
        protected NakedObjectTransactionDefault createTransaction(final MessageBroker messageBroker, final UpdateNotifier updateNotifier) {
            return new NakedObjectTransactionDefault(this, messageBroker, updateNotifier);
        }

        public boolean flushTransaction() {
            actions.addElement("flush transaction");
            return false;
        }

        public void abortTransaction() {
            getTransaction().abort();
        }

        public void endTransaction() {
            actions.addElement("end transaction");
            getTransaction().commit();
        }
    };

    private final Vector<String> actions = new Vector<String>();

    /**
     * Playing the role of the object store.
     */
    private final Hashtable<Oid, NakedObject> persistedObjects = new Hashtable<Oid, NakedObject>();

    public TestProxyPersistenceSession(final PersistenceSessionFactory persistenceSessionFactory) {
        super(persistenceSessionFactory, new AdapterFactoryTestProxyNakedObject(), new ObjectFactoryBasic(Mode.RELAXED) {
        }, new ServicesInjectorDefault(), new TestProxyOidGenerator(), new AdapterManagerDefault());
        RuntimeContextFromSession runtimeContext = new RuntimeContextFromSession();
        DomainObjectContainerDefault container = new DomainObjectContainerDefault();
        runtimeContext.injectInto(container);
        runtimeContext.setContainer(container);
        getServicesInjector().setContainer(container);
        setTransactionManager(transactionManager);
    }

    public void doOpen() {
        getAdapterFactory().injectInto(getAdapterManager());
        getSpecificationLoader().injectInto(getAdapterManager());
        getOidGenerator().injectInto(getAdapterManager());
    }

    public NakedObject loadObject(Oid oid, NakedObjectSpecification spec) {
        ensureThatArg(oid, is(notNullValue()));
        ensureThatArg(spec, is(notNullValue()));
        NakedObject adapter = getAdapterManager().getAdapterFor(oid);
        if (adapter == null) {
            adapter = persistedObjects.get(oid);
        }
        if (adapter == null) {
            throw new TestProxyException("No persisted object to get for " + oid);
        }
        return adapter;
    }

    public void makePersistent(NakedObject object) {
        getAdapterManager().remapAsPersistent(object);
        object.setOptimisticLock(new TestProxyVersion(1));
    }

    public void objectChanged(NakedObject object) {
        actions.addElement("object changed " + object.getOid());
        object.setOptimisticLock(((TestProxyVersion) object.getVersion()).next());
    }

    public void destroyObject(NakedObject object) {
        actions.addElement("object deleted " + object.getOid());
    }

    public void testReset() {
        getAdapterManager().reset();
    }

    public void resolveImmediately(NakedObject object) {
        throw new NotYetImplementedException();
    }

    public void resolveField(NakedObject object, NakedObjectAssociation association) {
        actions.addElement("object deleted " + object.getOid());
    }

    @Override
    protected NakedObject[] getInstances(PersistenceQuery criteria) {
        throw new NotYetImplementedException();
    }

    @Override
    protected Oid getOidForService(String name) {
        throw new NotYetImplementedException();
    }

    @Override
    protected void registerService(String name, Oid oid) {
        throw new NotYetImplementedException();
    }

    @Override
    public void reload(NakedObject adapter) {
        throw new NotYetImplementedException();
    }

    public boolean isFixturesInstalled() {
        throw new NotYetImplementedException();
    }

    public boolean hasInstances(NakedObjectSpecification specification) {
        throw new NotYetImplementedException();
    }

    public String debugTitle() {
        return null;
    }
}
