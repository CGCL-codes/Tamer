package org.mobicents.servlet.sip.ctf.core.environment.msstomcat7;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import org.jboss.weld.environment.servlet.inject.AbstractInjector;
import org.jboss.weld.manager.api.WeldManager;
import org.mobicents.servlet.sip.catalina.annotations.SipInstanceManager;

public class SipWeldInstanceManager extends AbstractInjector implements SipInstanceManager {

    public SipWeldInstanceManager(WeldManager manager) {
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

    @Override
    public void processAnnotations(Object instance, Map<String, String> injections) throws IllegalAccessException, InvocationTargetException, NamingException {
    }

    @Override
    public Map<String, String> getInjectionMap(String name) {
        return null;
    }

    @Override
    public void setContext(Context context) {
    }

    @Override
    public Context getContext() {
        return null;
    }
}
