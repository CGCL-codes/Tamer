package org.jenmo.core.persistence;

/**
 * Application lifecycle helper class. If in a web environment, use
 * {@link PersistenceWebAppListener} and {@link PersistenceWebAppRequestListener} classes.
 * 
 * @author Nicolas Ocquidant
 * @since 1.0
 */
public class PersistenceAppSupport {

    public void contextDestroyed(PersistenceManager pm) {
        pm.closeEntityManagerFactory();
    }

    public void sessionDestroyed(PersistenceManager pm) {
        if (pm.getEntityManagerFactory() instanceof ScopedEntityManagerFactory) {
            LazyCloseEntityManager em = ((ScopedEntityManagerFactory) pm.getEntityManagerFactory()).getEntityManager();
            if (em != null) {
                em.lazyClose();
            }
        }
    }
}
