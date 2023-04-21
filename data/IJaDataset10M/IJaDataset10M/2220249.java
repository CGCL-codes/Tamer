package net.sourceforge.freejava.util;

public abstract class PretypedFactory<T> extends _Factory<T> {

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public PretypedFactory() {
        this((Class<T>) Object.class);
    }

    public PretypedFactory(Class<T> type) {
        this.type = type;
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
