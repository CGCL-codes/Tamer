package com.sin.client.uuid;

import com.google.inject.Inject;

public class UuidManagerImpl implements UuidManager {

    private UuidProvider uuidProvider;

    @Inject
    public UuidManagerImpl(UuidProvider uuidProvider) {
        this.uuidProvider = uuidProvider;
    }

    public String getUuid() {
        return uuidProvider.createUuid();
    }
}
