package com.google.inject;

import com.google.inject.spi.BindingVisitor;

class InvalidBindingImpl<T> extends BindingImpl<T> {

    InvalidBindingImpl(InjectorImpl injector, Key<T> key, Object source) {
        super(injector, key, source, new InternalFactory<T>() {

            public T get(InternalContext context, InjectionPoint<?> injectionPoint) {
                throw new AssertionError();
            }
        }, Scopes.NO_SCOPE);
    }

    public void accept(BindingVisitor<? super T> bindingVisitor) {
        throw new AssertionError();
    }

    public String toString() {
        return "InvalidBinding";
    }
}
