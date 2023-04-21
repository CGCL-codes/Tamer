package com.mysolution.persistence.impl;

import com.mysolution.persistence.DomainID;
import com.mysolution.persistence.DomainIDGenerator;
import java.util.UUID;

/**
 * Generates a version 4 UUID.
 * Based on {@link java.util.UUID}
 */
public class GUIDGenerator implements DomainIDGenerator {

    /**
     * Generates UUID.
     * @return new ID;
     */
    public DomainID generate() {
        return new GUID(UUID.randomUUID().toString());
    }
}
