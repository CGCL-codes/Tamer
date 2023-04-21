package org.apache.isis.extensions.jpa.runtime.persistence;

import org.apache.isis.extensions.jpa.runtime.persistence.maps.JpaAdapterManager;
import org.apache.isis.extensions.jpa.runtime.persistence.objectstore.JpaObjectStore;
import org.apache.isis.extensions.jpa.runtime.persistence.oid.JpaOidGenerator;
import org.apache.isis.extensions.jpa.runtime.persistence.storealgorithm.JpaSimplePersistAlgorithm;
import org.apache.isis.metamodel.config.IsisConfiguration;
import org.apache.isis.metamodel.services.ServicesInjector;
import org.apache.isis.runtime.persistence.PersistenceSessionFactory;
import org.apache.isis.runtime.persistence.adapterfactory.AdapterFactory;
import org.apache.isis.runtime.persistence.adaptermanager.AdapterManager;
import org.apache.isis.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.apache.isis.runtime.persistence.objectfactory.ObjectFactory;
import org.apache.isis.runtime.persistence.objectstore.ObjectStore;
import org.apache.isis.runtime.persistence.objectstore.ObjectStorePersistence;
import org.apache.isis.runtime.persistence.objectstore.ObjectStorePersistenceMechanismInstallerAbstract;
import org.apache.isis.runtime.persistence.objectstore.PersistenceSessionObjectStore;
import org.apache.isis.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.apache.isis.runtime.persistence.oidgenerator.OidGenerator;
import org.apache.isis.runtime.system.DeploymentType;
import org.apache.log4j.Logger;

/**
 * Installs the JPA object store.
 */
public class JpaPersistenceMechanismInstaller extends ObjectStorePersistenceMechanismInstallerAbstract {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(JpaPersistenceMechanismInstaller.class);

    public JpaPersistenceMechanismInstaller() {
        super("jpa");
    }

    public JpaPersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new JpaPersistenceSessionFactory(deploymentType, this);
    }

    /**
     * Just downcasts.
     * <p>
     * Simplifies tests by removing downcasts.
     */
    @Override
    public JpaPersistenceSession createPersistenceSession(final PersistenceSessionFactory persistenceSessionFactory) {
        return (JpaPersistenceSession) super.createPersistenceSession(persistenceSessionFactory);
    }

    @Override
    protected PersistenceSessionObjectStore createObjectStorePersistor(final PersistenceSessionFactory persistenceSessionFactory, final AdapterFactory adapterFactory, final ObjectFactory objectFactory, final ServicesInjector containerInjector, final OidGenerator oidGenerator, final AdapterManagerExtended identityMap, final PersistAlgorithm persistAlgorithm, final ObjectStorePersistence objectStore) {
        return new JpaPersistenceSession(persistenceSessionFactory, adapterFactory, objectFactory, containerInjector, oidGenerator, identityMap, persistAlgorithm, objectStore);
    }

    @Override
    protected AdapterManagerExtended createAdapterManager(final IsisConfiguration configuration) {
        return new JpaAdapterManager();
    }

    @Override
    protected OidGenerator createOidGenerator(final IsisConfiguration configuration) {
        return new JpaOidGenerator();
    }

    @Override
    protected PersistAlgorithm createPersistAlgorithm(final IsisConfiguration configuration) {
        return new JpaSimplePersistAlgorithm();
    }

    @Override
    protected ObjectStore createObjectStore(final IsisConfiguration configuration, final AdapterFactory adapterFactory, final AdapterManager adapterManager) {
        return new JpaObjectStore(configuration, adapterFactory);
    }
}
