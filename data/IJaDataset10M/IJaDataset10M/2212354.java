package name.emu.webapp.kos.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import name.emu.webapp.kos.dao.AccessKeyDao;
import name.emu.webapp.kos.domain.AccessKey;
import name.emu.webapp.kos.domain.SecuredObject;
import name.emu.webapp.kos.domain.SecurityEntity;

public class AccessKeyDaoImpl extends AbstractJpaDao implements AccessKeyDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AccessKey findById(long id) {
        return entityManager.find(AccessKey.class, id);
    }

    @Override
    public void save(AccessKey accessKey) {
        entityManager.persist(accessKey);
    }

    @Override
    public boolean existsFor(SecurityEntity owner, SecuredObject securedObject) {
        Long count;
        Query query = entityManager.createQuery("SELECT COUNT(accessKey) FROM AccessKey accessKey WHERE accessKey.owner=? AND accessKey.target=?");
        query.setParameter(1, owner);
        query.setParameter(2, securedObject);
        count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    public void delete(AccessKey key) {
        key.getOwner().getAccessKeys().remove(key);
        key.getTarget().getAccessors().remove(key);
        entityManager.remove(key);
    }
}
