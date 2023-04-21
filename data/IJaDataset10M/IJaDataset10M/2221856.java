package org.nextframework.core.standard;

import org.nextframework.authorization.AuthorizationManager;
import org.nextframework.bean.BeanDescriptor;
import org.nextframework.core.config.Config;
import org.nextframework.report.ReportGenerator;
import org.nextframework.rtf.RTFGenerator;

/**
 * @author rogelgarcia
 * @since 21/01/2006
 * @version 1.1
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected Config config;

    public <E> BeanDescriptor<E> getBeanDescriptor(E bean, Class<E> clazz) {
        return config.getBeanDescriptorFactory().createBeanDescriptor(bean, clazz);
    }

    public <E> BeanDescriptor<E> getBeanDescriptor(E bean) {
        return config.getBeanDescriptorFactory().createBeanDescriptor(bean);
    }

    public AuthorizationManager getAuthorizationManager() {
        return config.getAuthorizationManager();
    }

    public AbstractApplicationContext(Config config) {
        this.config = config;
    }

    public ReportGenerator getReportGenerator() {
        return config.getReportGenerator();
    }

    public RTFGenerator getRTFGenerator() {
        return config.getRTFGenerator();
    }

    public Config getConfig() {
        return config;
    }

    public Object getBean(String string) {
        return config.getDefaultListableBeanFactory().getBean(string);
    }
}
