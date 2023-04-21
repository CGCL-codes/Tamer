package com.volantis.devrep.repository.impl;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;

public class DeviceRepositoryLocationImpl implements DeviceRepositoryLocation {

    private final String deviceRepositoryName;

    public DeviceRepositoryLocationImpl(String deviceRepositoryName) {
        this.deviceRepositoryName = deviceRepositoryName;
    }

    public String getDeviceRepositoryName() {
        return deviceRepositoryName;
    }
}
