package net.sf.easyweb4j.repository.orm.hibernate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.sf.easyweb4j.exceptions.RepositoryException;
import net.sf.easyweb4j.repository.PersistenceManager;
import net.sf.easyweb4j.repository.Repository;
import net.sf.easyweb4j.util.ReflectionUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernatePersistenceManager implements PersistenceManager {

    private Map<String, HibernateSessionProvider> sessionProviders;

    public HibernatePersistenceManager(Properties config) {
        if (config != null) {
            createProviders(config);
        }
    }

    public void initializeRepository(Repository<?> repository) {
        try {
            if (sessionProviders == null) {
                throw new RepositoryException("HibernateRepository cannot be used without a valid hibernate configuration (a 'hibernate' entry in /repositories.properties).", null);
            }
            HibernateSessionProvider chosenProvider = getProvider(repository);
            ReflectionUtil.setField(HibernateRepository.SESSION_PROVIDER_FIELD, repository, chosenProvider);
        } catch (Exception e) {
            throw new RepositoryException("Failed to initialize hibernate repository.", e);
        }
    }

    public void closePersistence() {
        if (sessionProviders != null) {
            for (HibernateSessionProvider provider : sessionProviders.values()) {
                provider.closeFactory();
            }
        }
    }

    private void createProviders(Properties config) {
        if (((String) config.get("hibernate")) != null) {
            String[] sessionFactories = ((String) config.get("hibernate")).split(",");
            sessionProviders = new HashMap<String, HibernateSessionProvider>();
            for (String factoryName : sessionFactories) {
                SessionFactory sessionFactory = new AnnotationConfiguration().configure("/" + factoryName + ".xml").buildSessionFactory();
                sessionProviders.put(factoryName, new HibernateSessionProvider(sessionFactory));
            }
        }
    }

    private HibernateSessionProvider getProvider(Repository<?> repository) {
        HibernateSessionProvider chosenProvider = null;
        if (sessionProviders != null) {
            UseSessionFactory annotation = repository.getClass().getAnnotation(UseSessionFactory.class);
            if (annotation == null) {
                if (sessionProviders.size() > 1) throw new RepositoryException("UseSessionFactory annotation is required when multiple session factories are available.", null);
                chosenProvider = sessionProviders.values().iterator().next();
            } else {
                String providerKey = annotation.value();
                chosenProvider = sessionProviders.get(providerKey);
            }
        }
        return chosenProvider;
    }
}
