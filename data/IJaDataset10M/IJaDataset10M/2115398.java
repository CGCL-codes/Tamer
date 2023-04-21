package com.javaforge.honeycomb.hivemind.hibernate;

import org.hibernate.cfg.AnnotationConfiguration;

public interface HibernateConfigElement {

    void applyTo(AnnotationConfiguration config);
}
