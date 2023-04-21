package org.lopatka.idonc.fetcher;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ApplicationSession {

    private EntityManagerFactory emf;

    private EntityManager em;

    private ApplicationSession() {
    }

    public static ApplicationSession getInstance() {
        return ApplicationSessionHolder.INSTANCE;
    }

    private static class ApplicationSessionHolder {

        private static final ApplicationSession INSTANCE = new ApplicationSession();
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
