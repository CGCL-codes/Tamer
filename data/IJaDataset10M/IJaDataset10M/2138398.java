package org.plazmaforge.bsolution.base;

import java.util.HashMap;
import java.util.Map;
import org.plazmaforge.bsolution.base.common.services.ReminderService;
import org.plazmaforge.framework.platform.service.ServiceFactory;

public class ReminderEnvironment {

    private static ReminderEnvironment instance;

    private Map<String, Class> reminderServices = new HashMap<String, Class>();

    private ReminderEnvironment() {
    }

    public static ReminderEnvironment getInstance() {
        if (instance == null) {
            instance = new ReminderEnvironment();
        }
        return instance;
    }

    public void registerReminderService(String type, Class serviceClass) {
        getInstance().reminderServices.put(type, serviceClass);
    }

    public Class getReminderServiceClass(String type) {
        return getInstance().reminderServices.get(type);
    }

    public ReminderService getReminderService(String type) {
        Class serviceClass = getReminderServiceClass(type);
        if (serviceClass == null) {
            return null;
        }
        return (ReminderService) ServiceFactory.getService(serviceClass);
    }
}
