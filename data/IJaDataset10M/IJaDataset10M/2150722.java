package com.volantis.mcs.application;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RequestHeaders;

/**
 * Provides methods to retrieve devices for application contexts.
 *
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in PrivateAPI
 */
public interface DeviceReader {

    /**
     * Get the device with the specified name.
     * @param deviceName The device name.
     * @return The device, or null if it could not be found.
     */
    InternalDevice getDevice(String deviceName) throws RepositoryException;

    /**
     * Get the device with the specified headers.
     * @param headers The headers that identify the device.
     * @return The device, or null if it could not be found.
     */
    InternalDevice getDevice(RequestHeaders headers) throws RepositoryException;
}
