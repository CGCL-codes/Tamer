package org.apache.tapestry5.ioc.internal.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.PreventServiceDecoration;
import org.apache.tapestry5.ioc.services.ClassFabUtils;
import org.apache.tapestry5.ioc.services.MasterObjectProvider;
import java.util.List;

@PreventServiceDecoration
public class MasterObjectProviderImpl implements MasterObjectProvider {

    private final List<ObjectProvider> configuration;

    private final OperationTracker tracker;

    public MasterObjectProviderImpl(List<ObjectProvider> configuration, OperationTracker tracker) {
        this.configuration = configuration;
        this.tracker = tracker;
    }

    public <T> T provide(final Class<T> objectType, final AnnotationProvider annotationProvider, final ObjectLocator locator, final boolean required) {
        return tracker.invoke(String.format("Resolving object of type %s using MasterObjectProvider", ClassFabUtils.toJavaClassName(objectType)), new Invokable<T>() {

            public T invoke() {
                for (ObjectProvider provider : configuration) {
                    T result = provider.provide(objectType, annotationProvider, locator);
                    if (result != null) return result;
                }
                if (required) return locator.getService(objectType);
                return null;
            }
        });
    }
}
