package com.slychief.clubmixer.server.library.management;

import com.slychief.clubmixer.server.library.DBHelper;
import com.slychief.clubmixer.server.library.entities.ClubmixerUser;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Alexander Schindler
 */
public class UserManager {

    private DBHelper db;

    public UserManager() {
        db = DBHelper.getInstance();
    }

    public void createUser(ClubmixerUser _user) {
        db.singlePersist(_user);
    }

    public List<ClubmixerUser> loadUsers(String userIDs) {
        return getUsers("FROM ClubmixerUser AS c WHERE c.id IN (" + userIDs + ")");
    }

    public ClubmixerUser getStandardUser() {
        return executeNamedQuerySingleResult("GetStandardUser");
    }

    private List<ClubmixerUser> getUsers(String querystring) {
        List<ClubmixerUser> result = null;
        EntityManager em = db.getNewEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery(querystring);
        result = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return result;
    }

    public List<ClubmixerUser> getAllUsers() {
        return getUsers("FROM ClubmixerUser AS c");
    }

    public void update(ClubmixerUser user) {
        EntityManager em = db.getNewEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void persist(ClubmixerUser user) {
        EntityManager em = db.getNewEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(ClubmixerUser user) {
        EntityManager em = db.getNewEntityManager();
        try {
            em.getTransaction().begin();
            ClubmixerUser delUser = em.getReference(ClubmixerUser.class, user.getId());
            em.remove(delUser);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public ClubmixerUser getByID(int userid) {
        EntityManager em = db.getNewEntityManager();
        return em.find(ClubmixerUser.class, userid);
    }

    public ClubmixerUser getByName(String username) {
        EntityManager em = db.getNewEntityManager();
        Query query = em.createNamedQuery("GetByName");
        query.setParameter("login", username);
        ClubmixerUser user = null;
        try {
            user = (ClubmixerUser) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return user;
    }

    public ClubmixerUser login(String username, String password) {
        EntityManager em = db.getNewEntityManager();
        ClubmixerUser user = null;
        try {
            em.getTransaction().begin();
            Query query = em.createNamedQuery("GetByName");
            query.setParameter("login", username);
            user = (ClubmixerUser) query.getSingleResult();
            user.setLoggedIn(true);
            GregorianCalendar cal = new GregorianCalendar();
            user.setLoginTime(cal.getTime());
            em.getTransaction().commit();
        } catch (NoResultException e) {
        } finally {
            em.close();
        }
        return user;
    }

    public ClubmixerUser logout(String username) {
        EntityManager em = db.getNewEntityManager();
        ClubmixerUser user = null;
        try {
            em.getTransaction().begin();
            Query query = em.createNamedQuery("GetByName");
            query.setParameter("login", username);
            user = (ClubmixerUser) query.getSingleResult();
            user.setLoggedIn(false);
            user.setLoginTime(null);
            em.getTransaction().commit();
        } catch (NoResultException e) {
        } finally {
            em.close();
        }
        return user;
    }

    private ClubmixerUser executeNamedQuerySingleResult(String string) {
        ClubmixerUser result = null;
        EntityManager em = db.getNewEntityManager();
        try {
            Query query = em.createNamedQuery(string);
            result = (ClubmixerUser) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return result;
    }
}
