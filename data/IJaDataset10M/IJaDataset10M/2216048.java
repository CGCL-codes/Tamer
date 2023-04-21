package org.hrodberaht.inject.internal;

import java.lang.annotation.Annotation;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-03 19:57:23
 * @version 1.0
 * @since 1.0
 */
public class InjectionKey {

    private Class<? extends Annotation> annotation;

    private String name;

    private Class serviceDefinition;

    private Boolean provider = false;

    public InjectionKey(String name, Class serviceDefinition, boolean provider) {
        this.name = name;
        this.serviceDefinition = serviceDefinition;
        this.provider = provider;
    }

    public InjectionKey(Class<? extends Annotation> annotation, Class serviceDefinition, boolean provider) {
        this.annotation = annotation;
        this.serviceDefinition = serviceDefinition;
        this.provider = provider;
    }

    public InjectionKey(Class serviceDefinition, boolean provider) {
        this.serviceDefinition = serviceDefinition;
        this.provider = provider;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public String getName() {
        return name;
    }

    public Class getServiceDefinition() {
        return serviceDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InjectionKey that = (InjectionKey) o;
        if (this.provider != that.provider) {
            return false;
        }
        if (annotation != null ? !annotation.equals(that.annotation) : that.annotation != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (serviceDefinition != null ? !serviceDefinition.equals(that.serviceDefinition) : that.serviceDefinition != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = annotation != null ? annotation.hashCode() : 0;
        result = 31 * result + provider.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (serviceDefinition != null ? serviceDefinition.hashCode() : 0);
        return result;
    }

    public String getQualifier() {
        if (name != null) {
            return name;
        }
        if (annotation != null) {
            return annotation.getName();
        }
        return null;
    }

    public boolean isProvider() {
        return provider;
    }

    /**
     * Makes a clone that is not marked as a Provider for implementation search 
     * @param key
     * @return
     */
    public static InjectionKey purify(InjectionKey key) {
        InjectionKey injectionKey = new InjectionKey(key.getServiceDefinition(), false);
        injectionKey.annotation = key.annotation;
        injectionKey.name = key.name;
        return injectionKey;
    }
}
