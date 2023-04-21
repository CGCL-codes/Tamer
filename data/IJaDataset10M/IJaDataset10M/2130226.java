package org.datanucleus.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.OMFContext;
import org.datanucleus.ObjectManagerFactoryImpl;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.MetaDataManager;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * Manager to control the replication of objects from one datastore to another.
 * Allow replication of specified objects, or all objects of particular types.
 * Supports a series of properties defining the replication behaviour.
 */
public class JDOReplicationManager {

    /** Localisation utility for output messages from jdo. */
    protected static final Localiser LOCALISER_JDO = Localiser.getInstance("org.datanucleus.jdo.Localisation", ObjectManagerFactoryImpl.class.getClassLoader());

    /** PMF for the source datastore. */
    final PersistenceManagerFactory pmfSource;

    /** PMF for the target datastore. */
    final PersistenceManagerFactory pmfTarget;

    /** Properties defining the replication process. */
    protected Properties properties = new Properties();

    /**
     * Constructor for replicating between source and target PMF.
     * @param pmf1 PMF source
     * @param pmf2 PMF target
     */
    public JDOReplicationManager(PersistenceManagerFactory pmf1, PersistenceManagerFactory pmf2) {
        if (pmf1 == null || pmf1.isClosed()) {
            throw new JDOUserException(LOCALISER_JDO.msg("012050"));
        } else if (pmf2 == null || pmf2.isClosed()) {
            throw new JDOUserException(LOCALISER_JDO.msg("012050"));
        }
        pmfSource = pmf1;
        pmfTarget = pmf2;
        properties.setProperty("datanucleus.replicateObjectGraph", "true");
        properties.setProperty("datanucleus.deleteUnknownObjects", "false");
    }

    /**
     * Method to set a property for replication.
     * @param key Property key
     * @param value Property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Accessor for the replication properties.
     * Supported properties include
     * <ul>
     * <li>datanucleus.replicateObjectGraph - whether we replicate the object graph from an object.
     *     if this is set we attempt to replicate the graph from this object. Otherwise just the object
     *     and its near neighbours.</li>
     * </ul>
     * @return Replication properties
     */
    public Properties getProperties() {
        return properties;
    }

    protected boolean getBooleanProperty(String key) {
        String val = properties.getProperty(key);
        if (val == null) {
            return false;
        }
        return val.equalsIgnoreCase("true");
    }

    /**
     * Method to perform the replication for all objects of the specified types.
     * @param types Classes to replicate
     */
    public void replicate(Class... types) {
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012052", pmfSource, pmfTarget, StringUtils.objectArrayToString(types)));
        }
        OMFContext omfCtxSource = ((JDOPersistenceManagerFactory) pmfSource).getOMFContext();
        MetaDataManager mmgr = omfCtxSource.getMetaDataManager();
        ClassLoaderResolver clr = omfCtxSource.getClassLoaderResolver(null);
        for (int i = 0; i < types.length; i++) {
            AbstractClassMetaData cmd = mmgr.getMetaDataForClass(types[i], clr);
            if (!cmd.isDetachable()) {
                throw new JDOUserException("Class " + types[i] + " is not detachable so cannot replicate");
            }
        }
        Object[] detachedObjects = null;
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012053"));
        }
        PersistenceManager pm1 = pmfSource.getPersistenceManager();
        Transaction tx1 = pm1.currentTransaction();
        if (getBooleanProperty("datanucleus.replicateObjectGraph")) {
            pm1.getFetchPlan().setGroup(javax.jdo.FetchPlan.ALL);
            pm1.getFetchPlan().setMaxFetchDepth(-1);
        }
        try {
            tx1.begin();
            ArrayList objects = new ArrayList();
            for (int i = 0; i < types.length; i++) {
                AbstractClassMetaData cmd = mmgr.getMetaDataForClass(types[i], clr);
                if (!cmd.isEmbeddedOnly()) {
                    Extent ex = pm1.getExtent(types[i]);
                    Iterator iter = ex.iterator();
                    while (iter.hasNext()) {
                        objects.add(iter.next());
                    }
                }
            }
            Collection detachedColl = pm1.detachCopyAll(objects);
            detachedObjects = detachedColl.toArray();
            tx1.commit();
        } finally {
            if (tx1.isActive()) {
                tx1.rollback();
            }
            pm1.close();
        }
        replicateInTarget(detachedObjects);
    }

    /**
     * Method to perform the replication for all objects of the specified class names.
     * @param classNames Classes to replicate
     */
    public void replicate(String... classNames) {
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012052", pmfSource, pmfTarget, StringUtils.objectArrayToString(classNames)));
        }
        OMFContext omfCtxSource = ((JDOPersistenceManagerFactory) pmfSource).getOMFContext();
        MetaDataManager mmgr = omfCtxSource.getMetaDataManager();
        ClassLoaderResolver clr = omfCtxSource.getClassLoaderResolver(null);
        for (int i = 0; i < classNames.length; i++) {
            AbstractClassMetaData cmd = mmgr.getMetaDataForClass(classNames[i], clr);
            if (!cmd.isDetachable()) {
                throw new JDOUserException("Class " + classNames[i] + " is not detachable so cannot replicate");
            }
        }
        Object[] detachedObjects = null;
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012053"));
        }
        PersistenceManager pm1 = pmfSource.getPersistenceManager();
        Transaction tx1 = pm1.currentTransaction();
        if (getBooleanProperty("datanucleus.replicateObjectGraph")) {
            pm1.getFetchPlan().setGroup(javax.jdo.FetchPlan.ALL);
            pm1.getFetchPlan().setMaxFetchDepth(-1);
        }
        try {
            tx1.begin();
            clr = ((JDOPersistenceManager) pm1).getObjectManager().getClassLoaderResolver();
            ArrayList objects = new ArrayList();
            for (int i = 0; i < classNames.length; i++) {
                Class cls = clr.classForName(classNames[i]);
                AbstractClassMetaData cmd = mmgr.getMetaDataForClass(cls, clr);
                if (!cmd.isEmbeddedOnly()) {
                    Extent ex = pm1.getExtent(cls);
                    Iterator iter = ex.iterator();
                    while (iter.hasNext()) {
                        objects.add(iter.next());
                    }
                }
            }
            Collection detachedColl = pm1.detachCopyAll(objects);
            detachedObjects = detachedColl.toArray();
            tx1.commit();
        } finally {
            if (tx1.isActive()) {
                tx1.rollback();
            }
            pm1.close();
        }
        replicateInTarget(detachedObjects);
    }

    /**
     * Method to perform the replication of the objects defined by the supplied identities.
     * @param oids Identities of the objects to replicate
     */
    public void replicate(Object... oids) {
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012051", pmfSource, pmfTarget, StringUtils.objectArrayToString(oids)));
        }
        Object[] detachedObjects = null;
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012053"));
        }
        PersistenceManager pm1 = pmfSource.getPersistenceManager();
        Transaction tx1 = pm1.currentTransaction();
        if (getBooleanProperty("datanucleus.replicateObjectGraph")) {
            pm1.getFetchPlan().setGroup(javax.jdo.FetchPlan.ALL);
            pm1.getFetchPlan().setMaxFetchDepth(-1);
        }
        try {
            tx1.begin();
            Object[] objs = pm1.getObjectsById(oids);
            detachedObjects = pm1.detachCopyAll(objs);
            tx1.commit();
        } finally {
            if (tx1.isActive()) {
                tx1.rollback();
            }
            pm1.close();
        }
        replicateInTarget(detachedObjects);
    }

    /**
     * Method to perform the replication for all objects registered in the pmf source.
     */
    public void replicateRegisteredClasses() {
        ClassLoaderResolver clr = ((JDOPersistenceManager) pmfSource.getPersistenceManager()).getObjectManager().getClassLoaderResolver();
        MetaDataManager mmgr = ((JDOPersistenceManagerFactory) pmfSource).getOMFContext().getMetaDataManager();
        Collection classNames = mmgr.getClassesWithMetaData();
        ArrayList arrayTypes = new ArrayList();
        Iterator iterator = classNames.iterator();
        while (iterator.hasNext()) {
            String className = (String) iterator.next();
            AbstractClassMetaData cmd = mmgr.getMetaDataForClass(className, clr);
            if (!cmd.isEmbeddedOnly()) {
                arrayTypes.add(clr.classForName(className));
            }
        }
        replicate((Class[]) arrayTypes.toArray(new Class[arrayTypes.size()]));
    }

    /**
     * Method to replicate the provided detached objects in the target datastore.
     * @param detachedObjects The detached objects (from the source datastore)
     */
    protected void replicateInTarget(Object... detachedObjects) {
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012054"));
        }
        JDOPersistenceManager pm2 = (JDOPersistenceManager) pmfTarget.getPersistenceManager();
        Transaction tx2 = pm2.currentTransaction();
        try {
            tx2.begin();
            pm2.makePersistentAll(detachedObjects);
            tx2.commit();
        } finally {
            if (tx2.isActive()) {
                tx2.rollback();
            }
            pm2.close();
        }
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER_JDO.msg("012055"));
        }
    }
}
