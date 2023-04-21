package net.kodveus.kumanifest.persistence;

import java.util.ArrayList;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import net.kodveus.kumanifest.utility.LogHelper;
import net.kodveus.kumanifest.utility.RefreshUtility;

@SuppressWarnings("unchecked")
public class PersistenceManager {

    private static PersistenceManager instance;

    public static PersistenceManager getInstance() {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        return instance;
    }

    @PersistenceUnit
    protected EntityManager entityManager;

    protected EntityManagerFactory entityManagerFactory;

    private PersistenceManager() {
        Locale.setDefault(Locale.US);
        entityManagerFactory = Persistence.createEntityManagerFactory("PersistentUnit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitTransaction() {
        entityManager.getTransaction().commit();
        RefreshUtility.getInstance().refresh();
    }

    public boolean delete(final Object sinif) {
        try {
            beginTransaction();
            entityManager.remove(entityManager.merge(sinif));
            commitTransaction();
            return true;
        } catch (final Exception e) {
            printStackTrace(e);
            failTransaction();
            return false;
        }
    }

    public ArrayList executeNamedQuery(final String queryName) {
        return executeNamedQuery(queryName, null);
    }

    public ArrayList executeNamedQuery(final String queryName, final Object[] parameters) {
        try {
            final Query query = entityManager.createNamedQuery(queryName);
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter("param" + i, parameters[i]);
                }
            }
            return new ArrayList(query.getResultList());
        } catch (final Exception e) {
            printStackTrace(e);
            return null;
        }
    }

    public ArrayList executeQuery(final String nativeQuery) {
        try {
            final Query query = entityManager.createNativeQuery(nativeQuery);
            return new ArrayList(query.getResultList());
        } catch (final Exception e) {
            printStackTrace(e);
            return null;
        }
    }

    private void failTransaction() {
        entityManager.getTransaction().rollback();
    }

    public Object find(final Class sinif, final Object primKey) {
        return entityManager.find(sinif, primKey);
    }

    public ArrayList findAll(final String pre) {
        return executeNamedQuery(pre + ".findAll");
    }

    public EntityManager getEM() {
        return entityManager;
    }

    private void printStackTrace(final Exception e) {
        e.printStackTrace();
        LogHelper.getInstance().exception(e);
    }

    public void save(final Object sinif) {
        try {
            beginTransaction();
            entityManager.persist(sinif);
            commitTransaction();
        } catch (final Exception e) {
            printStackTrace(e);
            failTransaction();
        }
    }

    public boolean update(final Object sinif) {
        try {
            beginTransaction();
            entityManager.merge(sinif);
            commitTransaction();
            return true;
        } catch (final Exception e) {
            printStackTrace(e);
            failTransaction();
            return false;
        }
    }
}
