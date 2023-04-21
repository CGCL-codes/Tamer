package de.javagimmicks.collections.transformer;

import java.util.Comparator;

class TransformingComparator<F, T> implements Comparator<T> {

    protected final Comparator<? super F> _internalComparator;

    private final Transformer<T, F> _transformer;

    /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
    @Deprecated
    public TransformingComparator(Comparator<? super F> comparator, Transformer<T, F> transformer) {
        _internalComparator = comparator;
        _transformer = transformer;
    }

    public int compare(T o1, T o2) {
        return _internalComparator.compare(_transformer.transform(o1), _transformer.transform(o2));
    }
}
