package util;

import java.util.*;

public class ReadOnlyArrayModel<E> extends AbstractList<E> {

    private final E[] data;

    private ArrayList<ModifiedListener> listeners;

    public ReadOnlyArrayModel(E[] data) {
        this.data = data;
    }

    @Override
    public E get(int index) {
        return data[index];
    }

    @Override
    public int size() {
        return data.length;
    }

    public void addModifiedListener(ModifiedListener l) {
        listeners.add(l);
    }

    public void removeModifiedListener(ModifiedListener l) {
        listeners.remove(l);
    }

    public void fireIndexModified(int index) {
        for (ModifiedListener l : listeners) {
            l.modified(new ModifiedEvent(this, index));
        }
    }
}
