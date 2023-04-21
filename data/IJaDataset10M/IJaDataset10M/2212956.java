package net.krecan.spring.ip;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.MethodParameter;

/**
 * Wraps {@link DependencyDescriptor} and exposes it as {@link InjectionPoint}.
 * @author lukas
 *
 */
public class DependecyDescriptorInjectionPoint implements InjectionPoint {

    private final DependencyDescriptor dependencyDescriptor;

    public DependecyDescriptorInjectionPoint(DependencyDescriptor dependencyDescriptor) {
        this.dependencyDescriptor = dependencyDescriptor;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        for (Annotation annotation : getAnnotations()) {
            if (annotation.getClass().equals(annotationType)) {
                return (T) annotation;
            }
        }
        return null;
    }

    public Annotation[] getAnnotations() {
        return (Annotation[]) dependencyDescriptor.getAnnotations();
    }

    public Field getField() {
        return dependencyDescriptor.getField();
    }

    public MethodParameter getMethodParameter() {
        return dependencyDescriptor.getMethodParameter();
    }

    public Member getMember() {
        if (getField() != null) {
            return getField();
        } else if (getMethodParameter() != null) {
            return getMethodParameter().getMethod();
        } else {
            return null;
        }
    }

    public boolean isEager() {
        return dependencyDescriptor.isEager();
    }

    public boolean isRequired() {
        return dependencyDescriptor.isRequired();
    }
}
