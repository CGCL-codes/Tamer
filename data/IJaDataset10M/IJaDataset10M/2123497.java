package org.jfeature.feature.impl.rte;

import java.lang.reflect.Method;
import org.jfeature.feature.GetCollection;
import org.jfeature.feature.impl.AbstractGetCollectionHandler;
import org.jfeature.fpi.FPElement;
import org.jfeature.fpi.FPProducible;
import org.jfeature.fpi.rte.RTTElement;
import org.jfeature.fpi.rte.RTTMethodHandler;
import org.jfeature.fpi.rte.RTTProducible;
import org.jtools.meta.meta_inf.services.ServiceProvider;

@ServiceProvider(value = GetCollection.class, args = "RTE")
public final class GetCollectionRTHandler<T> extends AbstractGetCollectionHandler<Class<?>, Method, Class<?>, GetCollection, RTTMethodHandler<?>> implements RTTMethodHandler<T> {

    public Object invoke(RTTProducible container, Method method, RTTElement<T> feature, Object[] args) {
        return feature.getValue();
    }

    @Override
    protected RTTMethodHandler<?> newMethodHandler(FPProducible<Class<?>, Method, Class<?>, GetCollection, RTTMethodHandler<?>> producible, GetCollection anno, Method method, FPElement<Class<?>, Method, Class<?>, GetCollection, RTTMethodHandler<?>> element) {
        return this;
    }
}
