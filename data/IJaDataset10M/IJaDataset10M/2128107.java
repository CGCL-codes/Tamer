package br.net.woodstock.rockframework.web.types;

abstract class AbstractType<T> implements Type<T> {

    private static final long serialVersionUID = -1244509539056506655L;

    private T value;

    public AbstractType() {
        super();
    }

    public AbstractType(final T value) {
        super();
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(final T value) {
        this.value = value;
    }
}
