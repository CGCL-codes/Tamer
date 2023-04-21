package org.jadapter.adapters;

import org.jadapter.Adapter;
import org.jadapter.Transformer;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

/**
 * Wrapper around {@link org.jadapter.Transformer}.
 * @author Vidar Svansson
 * @since 0.4
 */
public class TransformerAdapter<T, F> implements Adapter<T, F> {

    private Transformer<T, F> transformer;

    private Type from;

    private Type to;

    public TransformerAdapter(Transformer<T, F> transformer, Class<T> to, Class<F> from) {
        this.transformer = transformer;
        this.from = from;
        this.to = to;
    }

    public TransformerAdapter(Transformer<T, F> transformer) {
        Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        this.to = types[0];
        this.from = types[1];
        this.transformer = transformer;
    }

    public Type from() {
        return from;
    }

    public Type to() {
        return to;
    }

    public T transform(F context) {
        return transformer.transform(context);
    }
}
