package org.light.portal.core.service;

import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Jianmin Liu
 **/
public class ServiceContext {

    public static ServiceContext getInstance() {
        return _instance;
    }

    private ServiceContext() {
    }

    private static ServiceContext _instance = new ServiceContext();

    private ApplicationContext context;

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
