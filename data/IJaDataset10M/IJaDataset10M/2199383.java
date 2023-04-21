package org.openconcerto.utils.cc;

public class ConstantFactory<E> implements IFactory<E> {

    private static final ConstantFactory<Object> nullFactory = new ConstantFactory<Object>(null);

    @SuppressWarnings("unchecked")
    public static final <F> ConstantFactory<F> nullFactory() {
        return (ConstantFactory<F>) nullFactory;
    }

    public static final <E, F> ITransformer<E, F> createTransformer(final F obj) {
        return new ITransformer<E, F>() {

            @Override
            public F transformChecked(E input) {
                return obj;
            }
        };
    }

    private final E obj;

    public ConstantFactory(E obj) {
        super();
        this.obj = obj;
    }

    @Override
    public E createChecked() {
        return this.obj;
    }
}
