package org.mobicents.servlet.sip.ctf.core.environment.mssjboss5;

import java.lang.reflect.InvocationTargetException;
import javax.naming.NamingException;
import org.jboss.weld.environment.servlet.inject.AbstractInjector;
import org.jboss.weld.manager.api.WeldManager;

public class SipWeldJbossInstanceManager extends AbstractInjector implements org.apache.InstanceManager {

    public SipWeldJbossInstanceManager(WeldManager manager) {
        super(manager);
    }

    @Override
    public Object newInstance(String className) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
        return null;
    }

    @Override
    public Object newInstance(String fqcn, ClassLoader classLoader) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
        return null;
    }

    @Override
    public void newInstance(Object o) throws IllegalAccessException, InvocationTargetException, NamingException {
        inject(o);
    }

    @Override
    public void destroyInstance(Object o) throws IllegalAccessException, InvocationTargetException {
    }
}
