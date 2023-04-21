package org.fao.fenix.persistence.communication;

import org.fao.fenix.persistence.map.*;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.fao.fenix.domain.communication.CommunicationResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author etj
 */
@Repository
@Transactional
public class CommunicationResourceDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CommunicationResource> findAll() {
        return entityManager.createQuery("FROM CommunicationResource cr").getResultList();
    }

    public CommunicationResource findById(long id) {
        return entityManager.find(CommunicationResource.class, id);
    }

    public void save(CommunicationResource communicationResource) {
        entityManager.persist(communicationResource);
    }

    public CommunicationResource update(CommunicationResource communicationResource) {
        return entityManager.merge(communicationResource);
    }

    public void delete(CommunicationResource communicationResource) {
        entityManager.remove(communicationResource);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
