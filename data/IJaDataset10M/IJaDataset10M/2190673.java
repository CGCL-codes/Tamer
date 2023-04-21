package net.sf.javagimmicks.collections.transformer;

import java.util.List;
import java.util.ListIterator;

class BidiTransformingList<F, T> extends TransformingList<F, T> implements BidiTransforming<F, T> {

    /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
    @Deprecated
    public BidiTransformingList(List<F> list, BidiTransformer<F, T> transformer) {
        super(list, transformer);
    }

    public BidiTransformer<F, T> getBidiTransformer() {
        return (BidiTransformer<F, T>) getTransformer();
    }

    @Override
    public void add(int index, T element) {
        _internalList.add(index, transformBack(element));
    }

    @Override
    public T set(int index, T element) {
        return transform(_internalList.set(index, transformBack(element)));
    }

    @Override
    public ListIterator<T> listIterator() {
        return TransformerUtils.decorate(_internalList.listIterator(), getBidiTransformer());
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return TransformerUtils.decorate(_internalList.listIterator(index), getBidiTransformer());
    }

    protected F transformBack(T element) {
        return getBidiTransformer().transformBack(element);
    }
}
