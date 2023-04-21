package com.google.gwt.inject.client.binder;

import java.lang.annotation.Annotation;

public interface GinAnnotatedBindingBuilder<T> extends GinLinkedBindingBuilder<T> {

    GinLinkedBindingBuilder<T> annotatedWith(Class<? extends Annotation> annotation);

    GinLinkedBindingBuilder<T> annotatedWith(Annotation annotation);
}
