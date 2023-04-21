package org.opennms.spring.xmlrpc;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 */
public class XmlRpcProxyFactoryBean extends XmlRpcClientInterceptor implements FactoryBean, InitializingBean {

    private Object serviceProxy;

    public void afterPropertiesSet() {
        if (getServiceInterface() == null) {
            throw new IllegalArgumentException("serviceInterface is required");
        }
        this.serviceProxy = ProxyFactory.getProxy(getServiceInterface(), this);
    }

    public Object getObject() throws Exception {
        return this.serviceProxy;
    }

    public Class getObjectType() {
        return (this.serviceProxy != null) ? this.serviceProxy.getClass() : getServiceInterface();
    }

    public boolean isSingleton() {
        return true;
    }
}
