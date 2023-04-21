package org.jrest4guice.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class RemoteServiceProvider<T> implements Provider<T> {

    @Inject
    private RemoteServiceDynamicProxy proxy;

    private Class<T> clazz;

    public RemoteServiceProvider(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get() {
        return (T) proxy.createRemoteService(this.clazz);
    }

    public static <T> Provider<T> create(Class<T> providerType) {
        return new RemoteServiceProvider<T>(providerType);
    }
}
