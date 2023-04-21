package org.granite.tide.seam.lazy;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.jboss.seam.Component;
import org.jboss.seam.Entity;

/**
 * Manager responsible for looking up the EntityManager from Seam for the
 * passed in SessionManagerName
 * @author CIngram
 */
public class SeamEntityManager extends PersistenceContextManager {

    private String emName = null;

    public SeamEntityManager(String emName) {
        this.emName = emName;
    }

    /**
	 * Attach the passed in entity with the EntityManager retrieved
	 * from Seam.
	 * @param entity
	 * @return The attached entity
	 */
    @Override
    public Object findEntity(Object entity, String[] fetch) {
        EntityManager em = (EntityManager) Component.getInstance(emName);
        if (em == null) throw new RuntimeException("Could not find entity, EntityManager [" + emName + "] not found");
        Serializable id = (Serializable) Entity.forClass(entity.getClass()).getIdentifier(entity);
        if (id == null) return null;
        if (fetch == null || em.getDelegate().getClass().getName().indexOf(".hibernate.") < 0) return em.find(entity.getClass(), id);
        for (String f : fetch) {
            Query q = em.createQuery("select e from " + entity.getClass().getName() + " e left join fetch e." + f + " where e = :entity");
            q.setParameter("entity", entity);
            entity = q.getSingleResult();
        }
        return entity;
    }
}
