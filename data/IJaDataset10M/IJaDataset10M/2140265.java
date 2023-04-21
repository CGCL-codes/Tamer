package org.apache.isis.extensions.jpa.metamodel.hibernate;

import org.hibernate.cfg.AnnotationConfiguration;

public class HibListenerManagerNoop implements HibListenerManager {

    public void addListeners(final AnnotationConfiguration hibConfiguration) {
    }

    public void init() {
    }

    public void shutdown() {
    }

    public void resumeListeners() {
    }

    public void suspendListeners() {
    }
}
