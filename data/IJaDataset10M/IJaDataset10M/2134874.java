package org.lightrpc.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringSupport implements ApplicationContextAware {

    private ApplicationContext currentContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        currentContext = applicationContext;
    }

    public void setWebService(Object webService) {
    }
}
