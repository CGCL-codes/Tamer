package org.ccsoft.mc.support.spring;

import org.ccsoft.mc.core.ServiceContext;
import org.ccsoft.mc.core.impl.MiniContainer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author Chao Cai
 *
 * introduce the component to spring framework
 */
public class MiniContainerBridge implements FactoryBean, InitializingBean {

    private MiniContainer container;

    private ServiceContext context;

    private String configFile;

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public MiniContainer getContainer() {
        return container;
    }

    public void setContainer(MiniContainer container) {
        this.container = container;
    }

    public ServiceContext getContext() {
        return context;
    }

    public void setContext(ServiceContext context) {
        this.context = context;
    }

    public Object getObject() throws Exception {
        return context;
    }

    public Class getObjectType() {
        return ServiceContext.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        container = new MiniContainer();
        container.loadBundlesFromClassPath(configFile);
        context = container.getServiceContext();
    }
}
