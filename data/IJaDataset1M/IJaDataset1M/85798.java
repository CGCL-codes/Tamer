package org.beanopen.f.unused.prototype.define;

import java.util.ArrayList;
import java.util.List;

public class FieldMultipleDefinition<T> implements FieldDefinition<T, FieldMultipleDefinition<T>> {

    protected List<T> optionals;

    private T value;

    public FieldMultipleDefinition(T... v) {
        optionals = new ArrayList<T>();
        for (T t : v) optionals.add(t);
    }

    public List<T> getOptionals() {
        return optionals;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    public String toString() {
        return String.format("%s%s", FieldMultipleDefinition.class, optionals);
    }

    @Override
    public void merge(FieldMultipleDefinition<T> superFieldDefinition) {
        for (int i = optionals.size() - 1; i >= 0; i--) if (!isExists(superFieldDefinition.optionals, optionals.get(i))) getOptionals().remove(i);
        if (getOptionals().size() == 0) optionals = superFieldDefinition.getOptionals();
    }

    private boolean isExists(List<T> list, T entity) {
        for (T s : list) if (s.equals(entity)) return true;
        return false;
    }
}
