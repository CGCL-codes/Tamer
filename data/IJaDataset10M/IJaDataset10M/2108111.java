package org.shalma.persistence;

public interface PersistenceService {

    Environment getEnvironment();

    EntityStore getEntityStore();
}
