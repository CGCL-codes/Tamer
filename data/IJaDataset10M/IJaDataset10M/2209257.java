package de.akquinet.jbosscc.needle.db;

import javax.persistence.EntityManager;

public interface PersistenceConfiguration {

    EntityManager getEntityManager();
}
