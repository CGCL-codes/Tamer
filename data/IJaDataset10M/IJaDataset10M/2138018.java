package org.kumenya.psi.tree;

import org.kumenya.annotations.NotNull;
import org.kumenya.annotations.Nullable;
import org.kumenya.openapi.util.Ref;

/**
 * @author max
 */
public interface FlyweightCapableTreeStructure<T> {

    @NotNull
    T getRoot();

    @Nullable
    T getParent(T node);

    @NotNull
    T prepareForGetChildren(T node);

    int getChildren(T parent, Ref<T[]> into);

    void disposeChildren(T[] nodes, int count);
}
