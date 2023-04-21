package com.gae.tutorial.buisines;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import com.gae.tutorial.pojo.SimplePojo;

public class SimplePojoManager {

    public SimplePojoManager() {
    }

    public void put(String value) {
        EntityManager em = null;
        EntityTransaction etrx = null;
        try {
            System.out.println("put");
            em = EMF.get().createEntityManager();
            etrx = em.getTransaction();
            etrx.begin();
            System.out.println("trx begin");
            SimplePojo sp = new SimplePojo();
            sp.setValor(value);
            em.persist(sp);
            System.out.println("pre commit");
            etrx.commit();
            System.out.println("pre flush");
            em.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            try {
                if (etrx.isActive()) {
                    etrx.rollback();
                }
            } finally {
            }
            try {
                em.close();
            } finally {
            }
        }
    }

    public SimplePojo getLast() {
        EntityManager em = null;
        EntityTransaction etrx = null;
        SimplePojo p = null;
        try {
            em = EMF.get().createEntityManager();
            etrx = em.getTransaction();
            System.out.println("pre begin");
            etrx.begin();
            Query q = em.createNamedQuery("getLast");
            p = (SimplePojo) q.getSingleResult();
            System.out.println("pre commit");
            etrx.commit();
            System.out.println("pre flush");
            em.flush();
        } catch (Exception ex) {
            try {
                etrx.rollback();
            } finally {
            }
            try {
                em.close();
            } finally {
            }
        }
        return p;
    }
}
