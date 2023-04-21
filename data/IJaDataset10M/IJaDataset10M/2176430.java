package org.middleheaven.aas;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CallbacksSet implements Iterable<Callback> {

    private final Set<Callback> callbacks = new HashSet<Callback>();

    public void add(Callback callback) {
        callbacks.add(callback);
    }

    public void remove(Callback callback) {
        callbacks.remove(callback);
    }

    @Override
    public Iterator<Callback> iterator() {
        return callbacks.iterator();
    }

    public boolean isBlank() {
        boolean isBlank = false;
        for (Callback c : this) {
            isBlank = isBlank | c.isBlank();
        }
        return isBlank;
    }

    public <T extends Callback> T getCallback(Class<T> type) {
        for (Callback c : this) {
            if (type.isInstance(c)) {
                return type.cast(c);
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this.callbacks.isEmpty();
    }
}
