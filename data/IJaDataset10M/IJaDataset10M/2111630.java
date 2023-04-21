package au.edu.uq.itee.maenad.restlet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.restlet.resource.Representation;
import org.restlet.service.ConnectorService;
import au.edu.uq.itee.maenad.dataaccess.jpa.EntityManagerSource;

/**
 * The JPA connector service manages an entity manager within the scope of a request.
 *
 * This class acts as an EntityManagerSource that provides access to an EntityManager
 * instance that is scoped per thread and request. The EntityManager is initialised
 * lazily and then closed once the request cycle is finished.
 */
public class JpaConnectorService extends ConnectorService implements EntityManagerSource {

    private final EntityManagerFactory emf;

    private final ThreadLocal<EntityManager> entityManagerTL = new ThreadLocal<EntityManager>();

    public JpaConnectorService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void afterSend(Representation entity) {
        EntityManager entityManager = entityManagerTL.get();
        if (entityManager != null) {
            entityManagerTL.remove();
            assert entityManager.isOpen() : "Entity manager should only be closed here but must have been closed elsewhere";
            entityManager.close();
        }
        super.afterSend(entity);
    }

    @Override
    public EntityManager getEntityManager() {
        EntityManager entityManager = entityManagerTL.get();
        if (entityManager == null) {
            entityManager = emf.createEntityManager();
            entityManagerTL.set(entityManager);
        }
        return entityManager;
    }
}
