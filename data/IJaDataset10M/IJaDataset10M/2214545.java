package dryven.model.di;

import java.lang.annotation.Annotation;

public class ValueDependencyInjector<T> implements DependencyInjector {

    private T _value;

    public ValueDependencyInjector(T value) {
        super();
        _value = value;
    }

    @Override
    public boolean appliesToType(Class<?> type, Annotation[] annotations) {
        return type.isAssignableFrom(_value.getClass());
    }

    @Override
    public Object load(LocalThreadStorage storage, Class<?> type, Annotation[] annotations) {
        return _value;
    }
}
