package org.td4j.core.tk;

public interface IFilter<T> {

    public boolean accept(T element);
}
