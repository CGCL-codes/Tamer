package de.simplydevelop.mexs.domain;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class MTFMessageTypeCRUDBean {

    @PersistenceContext
    private EntityManager em;

    public void add(MTFMessageType newType) {
        em.persist(newType);
    }
}
