package org.nakedobjects.nos.store.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.event.LoadEvent;
import org.hibernate.event.LoadEventListener;
import org.hibernate.event.def.DefaultLoadEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedObjectLoader;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.util.Assert;

public class NakedLoadEventListener extends DefaultLoadEventListener {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(NakedLoadEventListener.class);

    public void onLoad(final LoadEvent event, final LoadType loadType) throws HibernateException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("LoadEvent - pre onLoad type=" + event.getEntityClassName() + ", id=" + event.getEntityId() + ", result=" + (event.getResult() == null ? "null" : event.getResult().getClass().getName()) + ", instancetoload=" + event.getInstanceToLoad());
        }
        super.onLoad(event, loadType);
    }

    protected Object proxyOrLoad(final LoadEvent event, final EntityPersister persister, final EntityKey keyToLoad, final LoadEventListener.LoadType options) throws HibernateException {
        if (!persister.hasProxy()) {
            return load(event, persister, keyToLoad, options);
        }
        final PersistenceContext persistenceContext = event.getSession().getPersistenceContext();
        Assert.assertNull(persistenceContext.getProxy(keyToLoad));
        if (options.isAllowProxyCreation()) {
            return loadUnresolvedObject(event, persister, keyToLoad, options, persistenceContext);
        } else {
            return load(event, persister, keyToLoad, options);
        }
    }

    private Object loadUnresolvedObject(final LoadEvent event, final EntityPersister persister, final EntityKey keyToLoad, final LoadEventListener.LoadType options, final PersistenceContext persistenceContext) {
        final HibernateOid oid = new HibernateOid(event.getEntityClassName(), event.getEntityId());
        final NakedObjectSpecification spec = NakedObjectsContext.getReflector().loadSpecification(event.getEntityClassName());
        final NakedObjectLoader loader = NakedObjectsContext.getObjectLoader();
        NakedObject nakedObject = loader.getAdapterFor(oid);
        if (nakedObject == null) {
            nakedObject = loader.recreateAdapterForPersistent(oid, spec);
            Assert.assertFalse(persistenceContext.isEntryFor(nakedObject.getObject()));
            return nakedObject.getObject();
        } else {
            return load(event, persister, keyToLoad, options);
        }
    }
}
