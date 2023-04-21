package org.sucri.dao;

import org.sucri.floxs.servlet.User;
import org.sucri.floxs.Tools;
import org.sucri.servlet.controller.ListRange;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Dec 23, 2007
 * Time: 12:10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class JPA_Util {

    public static boolean persist(EntityManagerFactory emf, Object news) {
        if (emf == null) throw new RuntimeException("EntityManagerFactory is null!");
        boolean outcome = false;
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(news);
            em.getTransaction().commit();
            outcome = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return outcome;
    }

    public static Object merge(EntityManagerFactory emf, Object news) {
        if (emf == null) throw new RuntimeException("EntityManagerFactory is null!");
        Object outcome = null;
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            outcome = em.merge(news);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return outcome;
    }

    /**
   * "select e from UsersEntity e where e.uname = '" + username + "' and e.password = '" + password + "'"
   * @param emf
   * @param query
   * @return
   */
    public static List get(EntityManagerFactory emf, String query) {
        EntityManager em = null;
        List outcomes = null;
        try {
            em = emf.createEntityManager();
            Query Q = em.createQuery(query);
            outcomes = Q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return outcomes;
    }

    public static void remove(EntityManagerFactory emf, Object news) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.remove(em.merge(news));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
