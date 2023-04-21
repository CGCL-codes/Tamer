package dryven.persistence.provider;

import javax.persistence.EntityManager;
import dryven.config.DatabaseConfiguration;

public interface PersistenceProvider {

    public EntityManager createEntityManager(DatabaseConfiguration config);

    public void cleanup(DatabaseConfiguration config);
}
