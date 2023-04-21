package org.opensaas.jaudit.service;

import java.util.UUID;
import org.opensaas.jaudit.service.ObjectFactory;

/**
 * A default implementation of {@link ObjectFactory} which return String
 * representations of UUID suitable to be used by
 * {@link AuditServiceImpl#setGuidFactory(ObjectFactory)}.
 */
public class UUIDStringFactory implements ObjectFactory<String> {

    /**
     * Default required constructor.
     */
    public UUIDStringFactory() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public String getObject() {
        return UUID.randomUUID().toString();
    }
}
