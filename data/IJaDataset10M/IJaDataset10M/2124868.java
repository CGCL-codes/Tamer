package org.jfeature.fpi.feature.impl.rte;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.jfeature.fpi.FPElement;
import org.jfeature.fpi.FPProducible;
import org.jfeature.fpi.feature.RetrieveGeneric;
import org.jfeature.fpi.feature.impl.AbstractRetrieveGenericHandler;
import org.jfeature.fpi.rte.RTTElement;
import org.jfeature.fpi.rte.RTTMethodHandler;
import org.jfeature.fpi.rte.RTTProducible;
import org.jtools.meta.meta_inf.services.ServiceProvider;

@ServiceProvider(value = RetrieveGeneric.class, args = "RTE")
public class RetrieveGenericRTHandler extends AbstractRetrieveGenericHandler<Class<?>, Method, Class<?>, RetrieveGeneric, RTTMethodHandler<?>> implements RTTMethodHandler<Void> {

    private <T> T retrieve(RTTElement<T> element) {
        return element.getValue();
    }

    private <T> T retrieve(RTTProducible container, Object elementKey) {
        FPElement<Class<?>, Method, Class<?>, Annotation, RTTMethodHandler<?>> ec = container.__jfeature__getFPElement(elementKey);
        if (ec == null) throw new NullPointerException("element " + elementKey);
        return retrieve((RTTElement<T>) container.__jfeature__getFPTElement(ec));
    }

    public Object invoke(RTTProducible container, Method method, RTTElement<Void> element, Object[] args) throws Throwable {
        return retrieve(container, args[0]);
    }

    @Override
    protected RTTMethodHandler<?> newMethodHandler(FPProducible<Class<?>, Method, Class<?>, RetrieveGeneric, RTTMethodHandler<?>> producible, RetrieveGeneric anno, Method method, FPElement<Class<?>, Method, Class<?>, RetrieveGeneric, RTTMethodHandler<?>> element) {
        return this;
    }
}
