package net.sourceforge.jfunctions.functions.basic;

import net.sourceforge.jfunctions.filters.Filter;
import net.sourceforge.jfunctions.functions.Transformation;

public class IfThenElseFunction<K, V> extends Transformation<K, V> {

    private Filter<K> filter;

    private Transformation<K, V> accept;

    private Transformation<K, V> reject;

    public IfThenElseFunction(Filter<K> filter, Transformation<K, V> accept, Transformation<K, V> reject) {
        super();
        this.filter = filter;
        this.accept = accept;
        this.reject = reject;
    }

    @Override
    public V apply(K argument) {
        return filter.accept(argument) ? accept.apply(argument) : reject.apply(argument);
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(filter) + ") ? (" + accept + ") : (" + reject + ")";
    }
}
