package com.google.inject;

public class GuiceUtil {

    public static Injector getInjector(Binder impl) {
        return ((BinderImpl) impl).injector;
    }

    @SuppressWarnings("unchecked")
    public static void bindInternalFactory(Binder impl, Key<?> key, InternalFactoryPublic factory) {
        ((BinderImpl) impl).bind(key).to(factory);
    }
}
