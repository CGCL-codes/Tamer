package net.stickycode.reflector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public abstract class AnnotatedMethodProcessor implements MethodProcessor {

    private Class<? extends Annotation>[] annotation;

    public AnnotatedMethodProcessor(Class<? extends Annotation>... annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean canProcess(Method method) {
        for (Class<? extends Annotation> a : annotation) if (method.isAnnotationPresent(a)) return true;
        return false;
    }

    @Override
    public void sort(List<Method> methods) {
    }
}
