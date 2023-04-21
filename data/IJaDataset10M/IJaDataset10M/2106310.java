package net.sourceforge.eclipservices.services;

import java.util.Properties;
import net.sourceforge.eclipservices.exceptions.ServiceException;
import net.sourceforge.eclipservices.services.windows.WinServiceManager;

public class ServiceManagerFactory {

    /**
     * @param serverName
     *            name of managed server
     * @return ServiceManager for the current Operating System
     * @throws ServiceException
     */
    public static IServiceManager getServiceManager(String serverName) throws ServiceException {
        Properties systemProps = System.getProperties();
        String osName = systemProps.getProperty("os.name");
        if (osName.startsWith("Win")) {
            return new WinServiceManager(serverName);
        }
        throw new ServiceException("Unknown 'os.name' System property");
    }
}
