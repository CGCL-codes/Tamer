package com.mycila.inject.injector;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Lists.reverse;
import static com.mycila.inject.internal.Reflect.annotatedBy;
import static com.mycila.inject.internal.Reflect.findMethods;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MethodHandlerTypeListener<A extends Annotation> implements TypeListener {

    private final Class<A> annotationType;

    private final Class<? extends MethodHandler<A>> handlerClass;

    public MethodHandlerTypeListener(Class<A> annotationType, Class<? extends MethodHandler<A>> handlerClass) {
        this.annotationType = annotationType;
        this.handlerClass = handlerClass;
    }

    @Override
    public <I> void hear(final TypeLiteral<I> type, TypeEncounter<I> encounter) {
        final Provider<? extends MethodHandler<A>> provider = encounter.getProvider(handlerClass);
        encounter.register(new InjectionListener<I>() {

            @Override
            public void afterInjection(I injectee) {
                MethodHandler<A> handler = provider.get();
                for (Method method : reverse(newLinkedList(filter(findMethods(type.getRawType()), annotatedBy(annotationType))))) handler.handle(type, injectee, method, method.getAnnotation(annotationType));
            }
        });
    }
}
