package org.jitr.core;

import java.lang.annotation.Annotation;
import javax.servlet.ServletContext;

public interface Container<T> {

    String getName();

    T getWrappedContainer();

    ServletContext initialize(ConfigurationModel configuration, Annotation[] testClassAnnotations);

    void start() throws ContainerOperationException;

    void stop() throws ContainerOperationException;
}
