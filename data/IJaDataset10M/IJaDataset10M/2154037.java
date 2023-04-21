package org.nomadpim.core.util.filter;

public final class EverythingFilter<T> implements IFilter<T> {

    public boolean evaluate(T o) {
        return false;
    }
}
