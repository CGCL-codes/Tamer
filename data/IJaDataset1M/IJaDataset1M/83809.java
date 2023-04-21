package org.tamacat.di.impl;

import groovy.lang.GroovyClassLoader;
import org.tamacat.di.define.BeanDefineMap;
import org.tamacat.groovy.ClasspathGroovyLoader;

public class GroovyDIContainer extends TamaCatDIContainer {

    public GroovyDIContainer(String xml) {
        super(xml, null);
    }

    public GroovyDIContainer(String xml, ClassLoader loader) {
        super(xml, loader);
    }

    public GroovyDIContainer(BeanDefineMap defines, ClassLoader loader) {
        super(defines, loader);
    }

    BeanDefineHandler loadBeanDefineHandler() {
        beanDefineHandler = new GroovySpringBeanDefineHandler(loader);
        return beanDefineHandler;
    }

    protected ClassLoader getClassLoader() {
        if (loader == null) {
            loader = new ClasspathGroovyLoader(new GroovyClassLoader());
        }
        return loader;
    }

    protected ClassLoader getClassLoader(ClassLoader parent) {
        if (loader == null) {
            loader = new ClasspathGroovyLoader(new GroovyClassLoader(parent));
        }
        return loader;
    }
}
