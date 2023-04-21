package com.volantis.mcs.migrate.impl.framework;

import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.impl.framework.identification.ResourceIdentifier;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;

/**
 * Default implementation of {@link CoreFactory}.
 */
public class DefaultCoreFactory implements CoreFactory {

    public ResourceMigrator createResourceMigrator(StreamBufferFactory factory, ResourceIdentifier recogniser, NotificationReporter reporter) {
        return new DefaultResourceMigrator(factory, recogniser, reporter);
    }
}
