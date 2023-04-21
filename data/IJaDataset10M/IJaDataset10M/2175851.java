package org.garbagecollected.util;

import java.lang.reflect.Method;

public class SimpleBuilderSpecification implements BuilderSpecification {

    private final Class<?> spec;

    SimpleBuilderSpecification(Class<?> spec) {
        this.spec = spec;
    }

    public boolean isWriter(Method method, Object[] args) {
        return (args != null && args.length == 1 && spec.isAssignableFrom(method.getReturnType()));
    }

    public boolean isReader(Method method, Object[] args) {
        return args == null && !Void.TYPE.isAssignableFrom(method.getReturnType());
    }

    public String readerIdentity(Method reader) {
        return reader.getName();
    }

    public String writerIdentity(Method writer) {
        return writer.getName();
    }
}
