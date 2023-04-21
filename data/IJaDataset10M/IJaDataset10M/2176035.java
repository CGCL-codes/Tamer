package org.middleheaven.core.wiring.service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.services.ServiceEvent;
import org.middleheaven.core.services.ServiceListener;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.util.Hash;
import org.middleheaven.util.collections.CollectionUtils;

public class ServiceProxy<T> implements ServiceListener, ProxyHandler {

    Class<T> serviceClass;

    private Map<String, Object> params;

    BlockingQueue<T> queue;

    public ServiceProxy(Class<T> serviceClass, Map<String, Object> params) {
        this(serviceClass, null, null, params);
    }

    protected ServiceProxy(Class<T> serviceClass, Object lateBinderObject, Method lateBinder, Map<String, Object> params) {
        this.serviceClass = serviceClass;
        this.params = params;
    }

    public void setImplementation(T implementation) {
        queue = new ArrayBlockingQueue<T>(1);
        queue.offer(implementation);
    }

    @Override
    public void onEvent(ServiceEvent event) {
        if (event.getServiceClass().equals(serviceClass) && CollectionUtils.equalContents(this.params, event.getParams())) {
            if (ServiceEvent.ServiceEventType.ADDED.equals(event.getType())) {
                setImplementation(serviceClass.cast(event.getImplementation()));
            } else if (ServiceEvent.ServiceEventType.TEMPORARY_REMOVED.equals(event.getType())) {
                queue.clear();
            } else if (ServiceEvent.ServiceEventType.REMOVED.equals(event.getType())) {
                queue.clear();
                queue = null;
            }
        }
    }

    public boolean equals(Object other) {
        return other instanceof ServiceProxy && this.serviceClass.getName().equals(((ServiceProxy) other).serviceClass.getName()) && CollectionUtils.equalContents(this.params, ((ServiceProxy) other).params);
    }

    public int hashCode() {
        return Hash.hash(this.serviceClass.getName()).hash(this.params).hashCode();
    }

    @Override
    public Object invoke(Object proxy, Object[] params, MethodDelegator delegator) throws Throwable {
        if (queue == null) {
            throw new ServiceNotAvailableException(serviceClass.getName());
        }
        T implementation = queue.peek();
        if (implementation == null) {
            implementation = queue.take();
        }
        return delegator.invoke(implementation, params);
    }
}
