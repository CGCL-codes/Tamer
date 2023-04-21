package com.beimin.evedata.hsqldb;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public abstract class AnnotatedHibConfigTest extends AbstractHSQLDBTest {

    @Override
    protected Configuration createConfiguration() {
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        configuration.configure(getHibernateConfig());
        return configuration;
    }

    protected abstract String getHibernateConfig();
}
