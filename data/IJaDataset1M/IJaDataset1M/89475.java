package org.dozer.functional_tests.proxied;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import javassist.util.proxy.ProxyFactory;
import org.dozer.MappingException;
import org.dozer.functional_tests.DataObjectInstantiator;

public class JavassistDataObjectInstantiator implements DataObjectInstantiator {

    public static final DataObjectInstantiator INSTANCE = new JavassistDataObjectInstantiator();

    private JavassistDataObjectInstantiator() {
    }

    public <T> T newInstance(Class<T> classToInstantiate) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(classToInstantiate);
        Class newClass = proxyFactory.createClass();
        Object instance;
        try {
            instance = newClass.newInstance();
        } catch (Exception e) {
            throw new MappingException(e);
        }
        return (T) instance;
    }

    public <T> T newInstance(Class<T> classToInstantiate, Object[] args) {
        return null;
    }

    public Object newInstance(Class<?>[] interfacesToProxy, Object target) {
        return null;
    }
}
