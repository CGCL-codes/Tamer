package org.simcom.hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.eclipse.core.runtime.Platform;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEventListener;
import org.osgi.framework.Bundle;
import org.simcom.commons.util.EnvironmentUtil;
import org.simcom.commons.util.FileFinder;
import org.simcom.commons.util.SimComUtil;

public class HibernateUtil {

    private static AnnotationConfiguration config;

    public static AnnotationConfiguration createHibernateConfiguration() {
        if (config != null) return config;
        config = new AnnotationConfiguration();
        setHibernateConfigurationProperties(config);
        loadEntites(config);
        return config;
    }

    public static List<Class<?>> loadEntites(AnnotationConfiguration configuration) {
        Bundle _bundle = Platform.getBundle("org.simcom.hibernate");
        Bundle[] bundles = _bundle.getBundleContext().getBundles();
        List<Class<?>> entityClasses = new ArrayList<Class<?>>();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().startsWith("org.eclipse")) continue;
            entityClasses.addAll(loadEntitiesFromBundle(bundle, configuration));
        }
        return entityClasses;
    }

    public static void setHibernateConfigurationProperties(AnnotationConfiguration config) {
        String hibernatePropertiesFileName = "hibernate-" + EnvironmentUtil.getEnvironmentPostfix() + ".properties";
        Properties properties = new Properties();
        try {
            properties.load(FileFinder.findFileAsInputStream(hibernatePropertiesFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.addProperties(properties);
        addTimestampListeners(config);
    }

    private static void addTimestampListeners(AnnotationConfiguration config) {
        TimestampAutoListener timestampListener = new TimestampAutoListener();
        addPreInsertEventListener(config, timestampListener);
        addPreUpdateEventListener(config, timestampListener);
    }

    private static void addPreInsertEventListener(AnnotationConfiguration config, TimestampAutoListener timestampListener) {
        PreInsertEventListener[] PreInsertEventListeners = config.getEventListeners().getPreInsertEventListeners();
        if (PreInsertEventListeners == null) {
            config.getEventListeners().setPreInsertEventListeners(new PreInsertEventListener[] { timestampListener });
        } else {
            int length = PreInsertEventListeners.length;
            PreInsertEventListener[] newPreInsertEventListeners = new PreInsertEventListener[length + 1];
            System.arraycopy(PreInsertEventListeners, 0, newPreInsertEventListeners, 0, length);
            newPreInsertEventListeners[length] = timestampListener;
            config.getEventListeners().setPreInsertEventListeners(newPreInsertEventListeners);
        }
    }

    private static void addPreUpdateEventListener(AnnotationConfiguration config, TimestampAutoListener timestampListener) {
        PreUpdateEventListener[] PreUpdateEventListeners = config.getEventListeners().getPreUpdateEventListeners();
        if (PreUpdateEventListeners == null) {
            config.getEventListeners().setPreUpdateEventListeners(new PreUpdateEventListener[] { timestampListener });
        } else {
            int length = PreUpdateEventListeners.length;
            PreUpdateEventListener[] newPreUpdateEventListeners = new PreUpdateEventListener[length + 1];
            System.arraycopy(PreUpdateEventListeners, 0, newPreUpdateEventListeners, 0, length);
            newPreUpdateEventListeners[length] = timestampListener;
            config.getEventListeners().setPreUpdateEventListeners(newPreUpdateEventListeners);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Class<?>> loadEntitiesFromBundle(Bundle bundle, AnnotationConfiguration configuration) {
        List<Class<?>> findClassInBundle = SimComUtil.findClassInBundle(bundle, new EntityClassFilter());
        for (Class clz : findClassInBundle) {
            configuration.addAnnotatedClass(clz);
        }
        return findClassInBundle;
    }

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) sessionFactory = createHibernateConfiguration().buildSessionFactory();
        return sessionFactory;
    }
}
