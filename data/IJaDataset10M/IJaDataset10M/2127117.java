package com.mycila.plugin.spi.invoke;

import com.mycila.plugin.InvokeException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class InvokableCompositeImpl<T> implements InvokableComposite<T> {

    private final List<Invokable<T>> invokables = new LinkedList<Invokable<T>>();

    InvokableCompositeImpl() {
    }

    @Override
    public void add(Invokable<T> invokable) {
        this.invokables.add(invokable);
    }

    @Override
    public void addAll(Iterable<Invokable<T>> invokables) {
        for (Invokable<T> invokable : invokables) this.invokables.add(invokable);
    }

    @Override
    public Iterator<Invokable<T>> iterator() {
        return invokables.iterator();
    }

    @Override
    public T invoke(Object... args) throws InvokeException {
        T res = null;
        for (Invokable<? extends T> invokable : invokables) res = invokable.invoke(args);
        return res;
    }
}
