package net.sf.extcos.selector;

import java.util.Set;
import net.sf.extcos.util.Assert;

public class TypedStoreBindingBuilder<T> {

    private final TypeFilter filter;

    public TypedStoreBindingBuilder(final TypeFilter filter) {
        Assert.notNull(filter, IllegalArgumentException.class);
        this.filter = filter;
    }

    public StoreBinding into(final Set<Class<? extends T>> store) {
        return new StoreBinding(filter, cast(store));
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> cast(final Set<Class<? extends T>> store) {
        return (Set<Class<?>>) (Set<? extends Class<?>>) store;
    }
}
