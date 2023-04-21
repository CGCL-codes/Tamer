package com.gwtent.client.reflection.impl;

import java.lang.annotation.Annotation;

/**
 * 
 * @author JamesLuo.au@gmail.com
 *
 */
public class AnnotationImpl implements java.lang.annotation.Annotation {

    private final Class<? extends Annotation> clazz;

    public AnnotationImpl(Class<? extends Annotation> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Annotation> annotationType() {
        return clazz;
    }
}
