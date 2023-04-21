package ru.autofan.action;

import org.springframework.webflow.action.FormAction;
import ru.autofan.logic.ServiceLocator;

public abstract class AbstractAction extends FormAction {

    private ServiceLocator serviceLocator;

    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public <T> T getService(Class<T> clazz) {
        return serviceLocator.getService(clazz);
    }
}
