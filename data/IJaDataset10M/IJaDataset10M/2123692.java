package net.sf.wtk.common.collections;

public class ConstantValue<T> implements LazyValue<T> {

    private final T value;

    public ConstantValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
