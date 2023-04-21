package com.volantis.mcs.runtime.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Holds configuration information about the overall devices configuration.
 * <p>
 * This corresponds to the mcs-config/devices element.
 */
public class DevicesConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The standard repository to use in the current instance of MCS.
     */
    private RepositoryDeviceConfiguration standardRepository;

    /**
     * Any custom repositories to be used in this instance of MCS.  This list
     * will contain instances of DeviceConfiguration which represent the
     * paths (either absolute or relative) to the given repository.
     */
    private List customRepositories = new ArrayList();

    /**
     * The configuration object for logging abstract and unknown devices and
     * sending e-mail notifications with the logged entries.
     */
    private UnknownDevicesLoggingConfiguration unknownDevicesLogging;

    /**
     * Name of device to use in case of a request from unknown device
     */
    private String defaultDeviceName;

    /**
     * Returns the standard device repository.
     */
    public RepositoryDeviceConfiguration getStandardDeviceRepository() {
        return standardRepository;
    }

    /**
     * Sets the default device repository.
     */
    public void setStandardDeviceRepository(RepositoryDeviceConfiguration deviceRepository) {
        this.standardRepository = deviceRepository;
    }

    /**
     * Returns the custom repositories.
     */
    public Iterator getCustomDeviceRepositoriesListIterator() {
        return customRepositories.iterator();
    }

    /**
     * Adds a custom device repository.
     */
    public void addDeviceRepository(RepositoryDeviceConfiguration deviceRepository) {
        customRepositories.add(deviceRepository);
    }

    /**
     * Returns the configuration object for abstract and unknown devices.
     * @return the stored configuration
     */
    public UnknownDevicesLoggingConfiguration getUnknownDevicesLogging() {
        return unknownDevicesLogging;
    }

    /**
     * Sets the configuration object for abstract or unknown devices.
     * @param unknownDevicesLogging the configuration to store
     */
    public void setUnknownDevicesLogging(final UnknownDevicesLoggingConfiguration unknownDevicesLogging) {
        this.unknownDevicesLogging = unknownDevicesLogging;
    }

    /**
     * Returns the configured name of default device to use in case of a request from unknown device
     * @return the name of default device
     */
    public String getDefaultDeviceName() {
        return defaultDeviceName;
    }

    /**
     * Sets the name of default device to use in case of a request from unknown device
     * @param defaultDeviceName the name of default device
     */
    public void setDefaultDeviceName(String defaultDeviceName) {
        this.defaultDeviceName = defaultDeviceName;
    }
}
