package org.granite.hibernate;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.granite.messaging.amf.io.util.instantiator.AbstractInstantiator;
import org.hibernate.proxy.HibernateProxy;

/**
 * @author Franck WOLFF
 */
public class HibernateProxyInstantiator extends AbstractInstantiator<HibernateProxy> {

    private static final long serialVersionUID = 1L;

    private final ConcurrentHashMap<String, ProxyFactory> proxyFactories;

    private final String detachedState;

    private Serializable id;

    public HibernateProxyInstantiator(ConcurrentHashMap<String, ProxyFactory> proxyFactories, String detachedState) {
        this.proxyFactories = proxyFactories;
        this.detachedState = detachedState;
    }

    public void readId(ObjectInput in) throws IOException, ClassNotFoundException {
        this.id = (Serializable) in.readObject();
    }

    protected Serializable getId() {
        return id;
    }

    @Override
    public List<String> getOrderedFieldNames() {
        return Collections.emptyList();
    }

    @Override
    public HibernateProxy newInstance() throws IOException, ClassNotFoundException, InstantiationException {
        final String[] tokens = detachedState.split("\\Q:\\E", -1);
        ProxyFactory factory = proxyFactories.get(tokens[0]);
        if (factory == null) {
            factory = newProxyFactory(tokens[0]);
            ProxyFactory previousFactory = proxyFactories.putIfAbsent(tokens[0], factory);
            if (previousFactory != null) factory = previousFactory;
        }
        return factory.getProxyInstance(tokens[1], tokens[2], id);
    }

    protected ProxyFactory newProxyFactory(String initializerClassName) {
        return new ProxyFactory(initializerClassName);
    }
}
