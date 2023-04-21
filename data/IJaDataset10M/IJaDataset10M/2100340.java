package hu.sztaki.lpds.APP_SPEC.services;

import java.util.HashMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gridlab.gridsphere.portlet.service.spi.impl.SportletServiceFactory;

public class APP_SPECServiceFactory {

    private static HashMap<Class<? extends APP_SPECService>, Object> instances = new HashMap<Class<? extends APP_SPECService>, Object>();

    private APP_SPECServiceFactory() {
    }

    public static final APP_SPECService getInstance(Class<? extends APP_SPECService> cls, String root, String instanceClass, String userPreferencesClassName, String role) throws InstantiationException, IllegalAccessException {
        Logger log = null;
        log = LogManager.getLogger(APP_SPECServiceFactory.class);
        APP_SPECService result = (APP_SPECService) instances.get(cls);
        if (result == null) {
            try {
                result = (APP_SPECService) SportletServiceFactory.getInstance().createPortletService(cls, true);
                result.initService(root, instanceClass, userPreferencesClassName, role);
                instances.put(cls, result);
                log.info("New service for role (" + role + ") was initialized");
            } catch (Exception e) {
                log.fatal("Service for role (" + role + ") cannot be initialized");
            }
        }
        log.info("New service for role (" + role + ") called");
        return result;
    }
}
