package service_manager;

import java.io.*;
import java.util.*;

/**
 * This is a generic service manager class, responsible for managing startup services across the 
 * supported platforms, currently Windows and Linux.
 * 
 */
public class ServiceManager {

    PlatformHandler platformHandler;

    private ServiceConfiguration serviceConfiguration;

    /**
     * Constructor
     */
    public ServiceManager(ServiceConfiguration serviceConfiguration) throws ServiceManagerException {
        this.serviceConfiguration = serviceConfiguration;
        Vector<PlatformHandler> platformHandlers = new Vector<PlatformHandler>();
        platformHandlers.add(new WindowsPlatformHandler(serviceConfiguration));
        platformHandlers.add(new LinuxPlatformHandler(serviceConfiguration));
        platformHandler = null;
        for (PlatformHandler platformHandler : platformHandlers) {
            if (platformHandler.isPlatformSupported()) {
                this.platformHandler = platformHandler;
            }
        }
        if (platformHandler == null) {
            throw new ServiceManagerException(PlatformHandler.GetOSName() + " is currently an unsupported platform in ServiceManager");
        }
    }

    /**
     * Install a service 
     * @throws ServiceManagerException
     */
    public void install() throws ServiceManagerException, IOException, InterruptedException {
        if (serviceConfiguration.getSpecialServiceManager() != null) {
            serviceConfiguration.getSpecialServiceManager().install();
        } else {
            platformHandler.install();
        }
    }

    /**
     * Uninistall a service
     * @throws ServiceManagerException
     */
    public void uninstall() throws ServiceManagerException, IOException, InterruptedException {
        if (serviceConfiguration.getSpecialServiceManager() != null) {
            serviceConfiguration.getSpecialServiceManager().uninstall();
        } else {
            platformHandler.uninstall();
        }
    }

    /**
     * Start an installed service
     * @throws ServiceManagerException
     */
    public void start() throws ServiceManagerException, IOException, InterruptedException {
        if (serviceConfiguration.getSpecialServiceManager() != null) {
            serviceConfiguration.getSpecialServiceManager().start();
        } else {
            platformHandler.start();
        }
    }

    /**
     * Stop an installed service
     * @throws ServiceManagerException
     */
    public void stop() throws ServiceManagerException, IOException, InterruptedException {
        if (serviceConfiguration.getSpecialServiceManager() != null) {
            serviceConfiguration.getSpecialServiceManager().stop();
        } else {
            platformHandler.stop();
        }
    }

    /**
     * Check if a service is installed
     * @returns True if the service is installed
     * @throws ServiceManagerException
     */
    public boolean isInstalled() throws ServiceManagerException, IOException, InterruptedException {
        if (serviceConfiguration.getSpecialServiceManager() != null) {
            return serviceConfiguration.getSpecialServiceManager().isInstalled();
        } else {
            return platformHandler.isInstalled();
        }
    }

    /**
     * Check if a service is running
     * @returns True if the service is running
     * @throws ServiceManagerException
     */
    public boolean isRunning() throws ServiceManagerException, IOException, InterruptedException {
        if (serviceConfiguration.getSpecialServiceManager() != null) {
            return serviceConfiguration.getSpecialServiceManager().isRunning();
        } else {
            return platformHandler.isRunning();
        }
    }

    /**
     * Run the configured command as administrator/super user.
     * args[0] = The program to execute
     * args[1].. The program arguments
     * 
     * @returns The programs error code.
     */
    public int administratorRun() throws ServiceManagerException, IOException, InterruptedException {
        if (serviceConfiguration.getSpecialServiceManager() != null) {
            return serviceConfiguration.getSpecialServiceManager().administratorRun();
        } else {
            return platformHandler.administratorRun();
        }
    }
}
