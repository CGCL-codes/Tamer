package org.isurf.gdssu.globalregistry.db.entities;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author nodari
 */
@Stateless
public class ItemFacade implements ItemFacadeLocal {

    @PersistenceContext
    private EntityManager em;

    public void create(Item item) {
        em.persist(item);
    }

    public void edit(Item item) {
        em.merge(item);
    }

    public void remove(Item item) {
        em.remove(em.merge(item));
    }

    public Item find(Object id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select object(o) from Item as o").getResultList();
    }
}
