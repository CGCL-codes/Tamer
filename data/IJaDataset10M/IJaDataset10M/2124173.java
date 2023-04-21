package gilgamesh.guice;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public final class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

    private static final Log logger = LogFactory.getLog(EntityManagerFactoryProvider.class);

    @Inject
    @Named("persistenceUnitName")
    private String persistenceUnitName;

    public EntityManagerFactory get() {
        logger.info("create EntityManagerFactory:" + persistenceUnitName);
        return Persistence.createEntityManagerFactory(persistenceUnitName);
    }
}
