package com.googlecode.proxymatic.ioc.guice.autoboundary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.googlecode.proxymatic.apps.autoboundary.AutoboundaryHelper;

public class AutoboundaryWrapperProvider<T> implements Provider<T> {

    @Inject
    AutoboundaryHelper autoboundaryHelper;

    @Inject
    Injector injector;

    private final Key<T> autoboundaryKey;

    public AutoboundaryWrapperProvider(Key<T> aClass) {
        this.autoboundaryKey = aClass;
    }

    public T get() {
        Class<T> autoboundaryClass = (Class<T>) autoboundaryKey.getTypeLiteral().getType();
        Class<?> realClass = autoboundaryHelper.getRealClass(autoboundaryClass);
        Object o = injector.getInstance(Key.get(realClass, autoboundaryKey.getAnnotation()));
        return autoboundaryHelper.wrap(autoboundaryClass, o);
    }
}
