package com.pbxworkbench.commons;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

public class ProxyFactoryBean implements FactoryBean {

    private static final Log LOG = LogFactory.getLog(ProxyFactoryBean.class);

    private boolean registered = false;

    private Object obj;

    private String registrationId = null;

    public ProxyFactoryBean() {
    }

    public ProxyFactoryBean(String registrationId) {
        this.registrationId = registrationId;
    }

    public boolean isSingleton() {
        return true;
    }

    public Class getObjectType() {
        return obj.getClass();
    }

    public Object getObject() {
        if ((registrationId != null) && (!registered)) {
            PbxWorkbenchEnvironment.getInstance().register(new RegisteredObjectId(registrationId), new MyRegisteredObject(registrationId));
            registered = true;
        }
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new MyInvocationHandler());
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public void setTarget(Object obj) {
        this.obj = obj;
    }

    public Object getTarget() {
        return obj;
    }

    private class MyInvocationHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            Object result;
            try {
                result = m.invoke(obj, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
            }
            return result;
        }
    }

    private class MyRegisteredObject implements IRegisteredObject {

        private String id;

        public MyRegisteredObject(String id) {
            this.id = id;
        }

        public void setPersistentData(Object object) {
            LOG.debug("restoring peristent data for object " + id);
            setTarget(object);
        }

        public Object getPersistentData() {
            LOG.debug("saving peristent data for object " + id);
            return getTarget();
        }
    }
}
