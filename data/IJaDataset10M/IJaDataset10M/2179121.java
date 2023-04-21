package ca.parit;

import java.util.List;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.apache.felix.dependencymanager.DependencyActivatorBase;
import org.apache.felix.dependencymanager.DependencyManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

public class Activator extends DependencyActivatorBase implements BundleActivator {

    private EntityManager entityManager = null;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void init(BundleContext context, DependencyManager manager) throws Exception {
        System.out.println("init for openjpa-osgi-maven");
        manager.add(createService().setImplementation(EntityManagerListener.class).add(createServiceDependency().setService(PersistenceProvider.class, (String) null).setCallbacks("added", "changed", "removed").setRequired(false)));
        System.out.println("openjpa-osgi-maven init completed");
    }

    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
        System.out.println("Destroy fun for openjpa-osgi-maven");
    }
}
