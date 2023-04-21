package org.obe.engine.repository;

import org.obe.client.api.repository.AbstractMetaData;

/**
 * @author Adrian Price
 */
public class RepositoryEntries {

    public AbstractMetaData[] type;

    public RepositoryEntry[] entry;

    public RepositoryEntries() {
    }

    public RepositoryEntries(AbstractMetaData[] types, RepositoryEntry[] entries) {
        type = types;
        entry = entries;
    }
}
