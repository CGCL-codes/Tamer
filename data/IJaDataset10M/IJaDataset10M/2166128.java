package com.sun.midp.services;

import com.sun.midp.main.MIDletSuiteUtils;
import com.sun.midp.security.SecurityToken;
import com.sun.midp.security.Permissions;

/**
 * Keeps track of available services.
 */
public abstract class SystemServiceManager {

    private static SystemServiceManager instance = null;

    /**
     * Gets class instance.
     *
     * @return SystemServiceManage class instance
     */
    public static synchronized SystemServiceManager getInstance(SecurityToken token) {
        token.checkIfPermissionAllowed(Permissions.MIDP);
        if (instance == null) {
            if (MIDletSuiteUtils.isAmsIsolate()) {
                instance = new SystemServiceManagerImpl();
            }
        }
        return instance;
    }

    /**
     * Registers service.
     *
     * @param service service to register
     */
    public abstract void registerService(SystemService service);

    /**
     * Gets registered service.
     *
     * @param serviceID unique service ID
     * @return service corresponding to specified ID
     */
    public abstract SystemService getService(String serviceID);

    /**
     * Shutdowns service manager
     */
    public abstract void shutdown();
}
